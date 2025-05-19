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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class LibraryController {
    // your existing table/search fields…
    @FXML private TextField   searchField;
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

    // NEW: these two must match fx:id’s in main.fxml
    @FXML private FlowPane sidebar;
    @FXML private FlowPane coverPane;

    private final ObservableList<Game> masterData = FXCollections.observableArrayList();
    private FilteredList<Game> filteredData;

    // Persist to ~/games.json
    private static final File DATA_FILE =
            new File(System.getProperty("user.home"), "games.json");

    @FXML
    public void initialize() {
        // 1) wire up your TableView columns (exactly as before)…
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

        // 2) load or seed…
        List<Game> loaded = FileHelper.load(DATA_FILE);
        if (loaded.isEmpty()) {
            masterData.addAll(
                    new Game("Hades","PC",Arrays.asList("Action","Rogue-like"),
                            LocalDate.of(2020,9,17),0,false,false,
                            "https://example.com/hades.jpg",25,30),
                    new Game("Super Mario Bros","NES",Arrays.asList("Platformer"),
                            LocalDate.of(1985,9,13),0,false,true,
                            "/images/mario.jpg",20,75)
            );
        } else {
            masterData.addAll(loaded);
        }

        // 3) wrap & bind to the TableView
        filteredData = new FilteredList<>(masterData, g -> true);
        gameTable.setItems(filteredData);

        // 4) enable/disable edit+delete
        gameTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, sel) -> {
                    boolean has = sel != null;
                    editButton.setDisable(!has);
                    deleteButton.setDisable(!has);
                });

        // 5) search → table AND cover‐grid
        searchField.textProperty().addListener((obs, old, text) -> {
            String lc = text == null ? "" : text.toLowerCase();
            filteredData.setPredicate(game -> {
                if (lc.isEmpty()) return true;
                return game.getTitle().toLowerCase().contains(lc)
                        || game.getConsole().toLowerCase().contains(lc);
            });
            renderCovers();
        });

        // finally, initial render of your cover‐art grid:
        renderCovers();
    }


    // build your little cover‐art “cards” and stick them in the FlowPane
    private void renderCovers() {
        coverPane.getChildren().clear();
        for (Game game : filteredData) {
            ImageView iv = new ImageView();
            iv.setPreserveRatio(true);
            iv.setFitWidth(100);

            try {
                iv.setImage(new Image(game.getCoverArtPath(), true));
            } catch (Exception e) {
                // ignore or set a placeholder
            }

            Label title = new Label(game.getTitle());
            VBox card = new VBox(iv, title);
            card.setSpacing(5);
            card.setOnMouseClicked(evt -> {
                try {
                    showGameDetails(game);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            coverPane.getChildren().add(card);
        }
    }

    // pop‐up your existing form in “edit” mode
    private void showGameDetails(Game game) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-form.fxml"));
        Parent pane = loader.load();
        FormController fc = loader.getController();

        Stage dialog = new Stage();
        dialog.initOwner(coverPane.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(pane));
        fc.setDialogStage(dialog);
        fc.setGame(game);
        dialog.showAndWait();

        // after editing, re‐draw & re‐save
        renderCovers();
        FileHelper.save(masterData, DATA_FILE);
    }


    // ─── your sidebar button handlers ────────────────────

    @FXML public void onShowAll() {
        filteredData.setPredicate(g -> true);
        renderCovers();
    }

    @FXML public void onFilterPC() {
        filteredData.setPredicate(g -> "PC".equalsIgnoreCase(g.getConsole()));
        renderCovers();
    }

    @FXML public void onFilterPS5() {
        filteredData.setPredicate(g -> "PS5".equalsIgnoreCase(g.getConsole()));
        renderCovers();
    }


    @FXML
    private void onAddGame() throws IOException { /* same as before */ }

    @FXML
    private void onEditGame() throws IOException { /* same as before */ }

    @FXML
    private void onDeleteGame() throws IOException { /* same as before */ }

    public ObservableList<Game> getMasterData() {
        return masterData;
    }
}

