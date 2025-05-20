package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.persistence.DataStore;
import edu.miracosta.cs112.finalproject.finalproject.util.FileHelper;
import edu.miracosta.cs112.finalproject.finalproject.util.DataLoadException;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
    @FXML private TableColumn<Game, Double> getProfitColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Game> masterData = FXCollections.observableArrayList();
    private FilteredList<Game> filteredData;
    private final DataStore dataStore = new FileHelper();

    private static final File DATA_FILE =
            new File(System.getProperty("user.home"), "games.json");

    @FXML
    public void initialize() {
        // 1) wire up columns
        titleColumn   .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitle()));
        consoleColumn .setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getConsole()));
        genresColumn  .setCellValueFactory(cd -> new SimpleStringProperty(
                String.join(", ", cd.getValue().getGenres())));
        releaseDateColumn.setCellValueFactory(cd ->
                new SimpleObjectProperty<>(cd.getValue().getReleaseDate()));

        // 2) format dates MM/dd/yyyy
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        releaseDateColumn.setCellFactory(col -> new TableCell<Game, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? "" : date.format(df));
            }
        });

        // 3) numeric/value columns
        hoursPlayedColumn .setCellValueFactory(cd ->
                new SimpleDoubleProperty(cd.getValue().getHoursPlayed()).asObject());
        completedColumn   .setCellValueFactory(cd ->
                new SimpleBooleanProperty(cd.getValue().isCompleted()));
        completeCopyColumn.setCellValueFactory(cd ->
                new SimpleBooleanProperty(cd.getValue().isCompleteCopy()));
        pricePaidColumn   .setCellValueFactory(cd ->
                new SimpleDoubleProperty(cd.getValue().getPricePaid()).asObject());
        marketValueColumn .setCellValueFactory(cd ->
                new SimpleDoubleProperty(cd.getValue().getMarketValue()).asObject());
        getProfitColumn   .setCellValueFactory(cd ->
                new SimpleDoubleProperty(
                        cd.getValue().getMarketValue()
                                - cd.getValue().getPricePaid()
                ).asObject());

        // 4) currency formatting for price, value, profit
        NumberFormat cf = NumberFormat.getCurrencyInstance(Locale.US);
        cf.setMinimumFractionDigits(2);
        cf.setMaximumFractionDigits(2);

        pricePaidColumn.setCellFactory(col -> new TableCell<Game, Double>() {
            @Override
            protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : cf.format(v));
            }
        });
        marketValueColumn.setCellFactory(col -> new TableCell<Game, Double>() {
            @Override
            protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : cf.format(v));
            }
        });
        getProfitColumn.setCellFactory(col -> new TableCell<Game, Double>() {
            @Override
            protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : cf.format(v));
            }
        });

        // 5) center all columns
        for (TableColumn<?,?> col : gameTable.getColumns()) {
            col.setStyle("-fx-alignment: CENTER;");
        }

        // 6) load or seed data
        // 6) load or seed data
        List<Game> loaded;
        try {
            loaded = dataStore.load(DATA_FILE);
        } catch (DataLoadException e) {
            e.printStackTrace();
            loaded = List.of();
        }


        if (loaded.isEmpty()) {
            masterData.addAll(
                    new Game("Hades","PS%", Arrays.asList("Action","Rogue-like"),
                            LocalDate.of(2020,9,17),0,false,false,
                            "https://example.com/hades.jpg",25,30),
                    new Game("Super Mario Bros","NES", Arrays.asList("Platformer"),
                            LocalDate.of(1985,9,13),0,false,true,
                            "/images/mario.jpg",20,75)
            );
        } else {
            masterData.addAll(loaded);
        }


        // 7) wrap in FilteredList
        filteredData = new FilteredList<>(masterData, g -> true);

        // 8) wrap in SortedList and bind to TableView
        SortedList<Game> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(gameTable.comparatorProperty());
        gameTable.setItems(sortedData);
        gameTable.getSortOrder().add(titleColumn);

        // 9) enable edit/delete buttons
        gameTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, o, sel) -> {
                    boolean has = sel != null;
                    editButton.setDisable(!has);
                    deleteButton.setDisable(!has);
                });

        // 10) search filters the table
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
    @FXML public void onShowAll()              { filteredData.setPredicate(g -> true); }
    @FXML public void onFilterPS5()            { filteredData.setPredicate(g -> "PS5".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS4()            { filteredData.setPredicate(g -> "PS4".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS3()            { filteredData.setPredicate(g -> "PS3".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS2()            { filteredData.setPredicate(g -> "PS2".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterPS1()            { filteredData.setPredicate(g -> "PS1".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterNintendoSwitch() { filteredData.setPredicate(g -> "Nintendo Switch".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onFilterXbox360()        { filteredData.setPredicate(g -> "Xbox 360".equalsIgnoreCase(g.getConsole())); }
    @FXML public void onNintendoDS()           { filteredData.setPredicate(g -> "Nintendo DS".equalsIgnoreCase(g.getConsole())); }

    @FXML
    private void onAddGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-form.fxml"));
        Parent pane = loader.load();
        FormController fc = loader.getController();

        Game newGame = new Game();
        fc.setGame(newGame);

        Stage dialog = new Stage();
        dialog.initOwner(gameTable.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(pane));
        fc.setDialogStage(dialog);
        dialog.showAndWait();

        if (fc.isOkClicked()) {
            masterData.add(newGame);
            dataStore.save(masterData, DATA_FILE);
        }
    }

    @FXML
    private void onDeleteGame() throws IOException {
        Game selected = gameTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(gameTable.getScene().getWindow());
        alert.setTitle("Delete Game");
        alert.setHeaderText("Delete \"" + selected.getTitle() + "\"?");
        alert.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            masterData.remove(selected);
            dataStore.save(masterData, DATA_FILE);
        }
    }

    @FXML
    private void onEditGame() throws IOException {
        Game selected = gameTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-form.fxml"));
        Parent pane = loader.load();
        FormController fc = loader.getController();

        fc.setGame(selected);

        Stage dialog = new Stage();
        dialog.initOwner(gameTable.getScene().getWindow());
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(pane));
        fc.setDialogStage(dialog);
        dialog.showAndWait();

        if (fc.isOkClicked()) {
            dataStore.save(masterData, DATA_FILE);
            gameTable.refresh();
        }
    }

    /** Used by HelloApplication to load via the DataStore abstraction */
    public void loadData(File file, DataStore ds) throws DataLoadException {
        List<Game> games = ds.load(file);
        masterData.setAll(games);
    }

    public void loadData(File file) throws DataLoadException {
        List<Game> games = dataStore.load(file);
        masterData.setAll(games);
    }

    public List<Game> getGames() {
        return masterData;
    }
}



