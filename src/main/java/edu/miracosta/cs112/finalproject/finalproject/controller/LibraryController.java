package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.model.DigitalGame;
import edu.miracosta.cs112.finalproject.finalproject.model.PhysicalGame;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Arrays;

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

    private final ObservableList<Game> masterData   = FXCollections.observableArrayList();
    private       FilteredList<Game> filteredData;

    @FXML
    public void initialize() {
        // 1) wire up columns
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

        // 2) sample data
        masterData.addAll(
                new DigitalGame(
                        "Hades",
                        "PC",
                        Arrays.asList("Action", "Rogue-like"),
                        LocalDate.of(2020, 9, 17),
                        false,
                        "https://example.com/hades.jpg",
                        25.00,
                        30.00,
                        "https://store.steampowered.com/app/1145360/Hades/"
                ),
                new PhysicalGame(
                        "Super Mario Bros",
                        "NES",
                        Arrays.asList("Platformer"),
                        LocalDate.of(1985, 9, 13),
                        true,
                        "/images/mario.jpg",
                        20.00,
                        75.00
                )
        );

        // 3) wrap in a FilteredList and bind to the TableView
        filteredData = new FilteredList<>(masterData, p -> true);
        gameTable.setItems(filteredData);

        // 4) enable/disable edit+delete based on selection
        gameTable.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean has = sel != null;
            editButton  .setDisable(!has);
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
    private void onAddGame() {
        // TODO: open your Add/Edit dialog
    }

    @FXML
    private void onEditGame() {
        // TODO: load selected Game into form
    }

    @FXML
    private void onDeleteGame() {
        Game sel = gameTable.getSelectionModel().getSelectedItem();
        if (sel != null) masterData.remove(sel);
    }
}

