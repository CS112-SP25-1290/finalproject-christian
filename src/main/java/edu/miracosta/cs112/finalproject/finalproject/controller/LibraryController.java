package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.util.FileHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
        // 1) wire up columns
        titleColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitle()));
        consoleColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getConsole()));
        genresColumn.setCellValueFactory(cd -> new SimpleStringProperty(
                String.join(", ", cd.getValue().getGenres())));
        releaseDateColumn.setCellValueFactory(cd -> new SimpleObjectProperty<>(
                cd.getValue().getReleaseDate()));
        hoursPlayedColumn.setCellValueFactory(cd -> new SimpleDoubleProperty(
                cd.getValue().getHoursPlayed()).asObject());
        completedColumn.setCellValueFactory(cd -> new SimpleBooleanProperty(
                cd.getValue().isCompleted()));
        completeCopyColumn.setCellValueFactory(cd -> new SimpleBooleanProperty(
                cd.getValue().isCompleteCopy()));
        pricePaidColumn.setCellValueFactory(cd -> new SimpleDoubleProperty(
                cd.getValue().getPricePaid()).asObject());
        marketValueColumn.setCellValueFactory(cd -> new SimpleDoubleProperty(
                cd.getValue().getMarketValue()).asObject());

        // 2) load or seed data
        List<Game> loaded = FileHelper.load(DATA_FILE);
        if (loaded.isEmpty()) {
            masterData.addAll(
                    new Game("Hades","PC", Arrays.asList("Action","Rogue-like"),
                            LocalDate.of(2020,9,17),0,false,false,
                            "https://example.com/hades.jpg",25,30),
                    new Game("Super Mario Bros","NES", Arrays.asList("Platformer"),
                            LocalDate.of(1985,9,13),0,false,true,
                            "/images/mario.jpg",20,75)
            );
        } else {
            masterData.addAll(loaded);
        }

        // 3) bind to TableView
        filteredData = new FilteredList<>(masterData, g -> true);
        gameTable.setItems(filteredData);

        // 4) enable edit/delete buttons
        gameTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, sel) -> {
                    boolean has = sel != null;
                    editButton.setDisable(!has);
                    deleteButton.setDisable(!has);
                });

        // 5) search filters the table
        searchField.textProperty().addListener((obs, old, text) -> {
            String lc = text == null ? "" : text.toLowerCase();
            filteredData.setPredicate(game ->
                    lc.isEmpty()
                            || game.getTitle().toLowerCase().contains(lc)
                            || game.getConsole().toLowerCase().contains(lc)
            );
        });
    }

    // ─── sidebar filters ────────────────────────────────────
    @FXML public void onShowAll()                    { filteredData.setPredicate(g -> true); }
    @FXML public void onFilterPC()    { filteredData.setPredicate(g -> "PC".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS5()   { filteredData.setPredicate(g -> "PS5".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS4()   { filteredData.setPredicate(g -> "PS4".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS3()   { filteredData.setPredicate(g -> "PS3".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS2()   { filteredData.setPredicate(g -> "PS2".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS1()   { filteredData.setPredicate(g -> "PS1".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterNintendoSwitch() { filteredData.setPredicate(g -> "Nintendo Switch".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterXbox360()        { filteredData.setPredicate(g -> "Xbox 360".equalsIgnoreCase(g.getConsole())); }

    /**
     * Opens the “Add Game” dialog, then if OK is clicked,
     * adds the new Game and persists it.
     */
    @FXML
    private void onAddGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/game-form.fxml")
        );
        Parent pane = loader.load();
        FormController fc = loader.getController();

        // prepare an empty Game for the form
        Game newGame = new Game();
        fc.setGame(newGame);

        // show dialog
        Stage dialog = new Stage();
        dialog.initOwner(gameTable.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(pane));
        fc.setDialogStage(dialog);
        dialog.showAndWait();

        // if user hit OK, add & save
        if (fc.isOkClicked()) {
            masterData.add(newGame);
            FileHelper.save(masterData, DATA_FILE);
        }
    }

    @FXML private void onEditGame()   throws IOException { /* unchanged */ }
    @FXML private void onDeleteGame() throws IOException { /* unchanged */ }

    // keep load/save hooks for HelloApplication
    public void loadData(File file) throws IOException {
        List<Game> games = FileHelper.load(file);
        masterData.setAll(games);
    }
    public List<Game> getGames() {
        return masterData;
    }
}
