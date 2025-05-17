package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.util.FileHelper;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class LibraryController {
    @FXML private TextField searchField;
    @FXML private TableView<Game> gameTable;
    @FXML private TableColumn<Game, String> titleColumn;
    @FXML private TableColumn<Game, String> consoleColumn;
    @FXML private TableColumn<Game, String> genresColumn;
    @FXML private TableColumn<Game, LocalDate> releaseDateColumn;
    @FXML private TableColumn<Game, Double> hoursPlayedColumn;
    @FXML private TableColumn<Game, Boolean> completedColumn;
    @FXML private TableColumn<Game, Boolean> completeCopyColumn;
    @FXML private TableColumn<Game, Double> pricePaidColumn;
    @FXML private TableColumn<Game, Double> marketValueColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Game> masterData = FXCollections.observableArrayList();
    private FilteredList<Game> filteredData;

    private static final File DATA_FILE =
            new File(System.getProperty("user.home"), "games.json");

    @FXML
    public void initialize() {
        // 1) Wire up columns
        titleColumn       .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitle()));
        consoleColumn     .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getConsole()));
        genresColumn      .setCellValueFactory(cd -> new SimpleStringProperty(
                String.join(", ", cd.getValue().getGenres())));
        releaseDateColumn .setCellValueFactory(cd -> new SimpleObjectProperty<>(
                cd.getValue().getReleaseDate()));
        hoursPlayedColumn .setCellValueFactory(cd -> new SimpleDoubleProperty(
                cd.getValue().getHoursPlayed()).asObject());
        completedColumn   .setCellValueFactory(cd -> new SimpleBooleanProperty(
                cd.getValue().isCompleted()));
        completeCopyColumn.setCellValueFactory(cd -> new SimpleBooleanProperty(
                cd.getValue().isCompleteCopy()));
        pricePaidColumn   .setCellValueFactory(cd -> new SimpleDoubleProperty(
                cd.getValue().getPricePaid()).asObject());
        marketValueColumn .setCellValueFactory(cd -> new SimpleDoubleProperty(
                cd.getValue().getMarketValue()).asObject());

        // 2) Load from disk (or seed if file is empty)
        masterData.clear();  // prevent duplicates on every launch
        List<Game> loaded = FileHelper.load(DATA_FILE);
        if (loaded.isEmpty()) {
            masterData.addAll(
                    new Game(
                            "Hades", "PS5",
                            Arrays.asList("Action", "Rogue-like"),
                            LocalDate.of(2020, 9, 17),
                            10.0,    // hoursPlayed
                            false,   // completed
                            false,   // completeCopy
                            "https://example.com/hades.jpg",
                            25.00,   // pricePaid
                            30.00    // marketValue
                    ),
                    new Game(
                            "Gran Turismo", "PS5",
                            Arrays.asList("Platformer"),
                            LocalDate.of(2022, 3, 2),
                            207.0,
                            false,
                            true,
                            "/images/gran-turismo.jpg",
                            69.99,
                            75.00
                    )
            );
            // wrap the save in try/catch so initialize() doesn't have to declare IOException
            try {
                FileHelper.save(masterData, DATA_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            masterData.addAll(loaded);
        }

        // 3) Wrap in a FilteredList and bind to the TableView
        filteredData = new FilteredList<>(masterData, p -> true);
        gameTable.setItems(filteredData);

        // 4) Enable/disable edit+delete based on selection
        gameTable.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean has = sel != null;
            editButton .setDisable(!has);
            deleteButton.setDisable(!has);
        });

        // 5) searchField filter logic
        searchField.textProperty().addListener((obs, old, text) -> {
            String lower = text == null ? "" : text.toLowerCase();
            filteredData.setPredicate(game -> {
                if (lower.isEmpty()) return true;
                return game.getTitle().toLowerCase().contains(lower)
                        || game.getConsole().toLowerCase().contains(lower);
            });
        });
    }

    @FXML
    private void onAddGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-form.fxml"));
        Parent pane = loader.load();
        FormController fc = loader.getController();

        Stage dialog = new Stage();
        dialog.initOwner(gameTable.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(pane));
        fc.setDialogStage(dialog);
        dialog.showAndWait();

        Game newGame = fc.getGameResult();
        if (newGame != null) {
            masterData.add(newGame);
            FileHelper.save(masterData, DATA_FILE);
        }
    }

    @FXML
    private void onEditGame() throws IOException {
        Game sel = gameTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-form.fxml"));
        Parent pane = loader.load();
        FormController fc = loader.getController();

        Stage dialog = new Stage();
        dialog.initOwner(gameTable.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(pane));
        fc.setDialogStage(dialog);
        fc.setGame(sel);
        dialog.showAndWait();

        gameTable.refresh();
        FileHelper.save(masterData, DATA_FILE);
    }

    @FXML
    private void onDeleteGame() throws IOException {
        Game sel = gameTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            masterData.remove(sel);
            FileHelper.save(masterData, DATA_FILE);
        }
    }

    /** Allow external code to access the current game list. */
    public ObservableList<Game> getMasterData() {
        return masterData;
    }
}

