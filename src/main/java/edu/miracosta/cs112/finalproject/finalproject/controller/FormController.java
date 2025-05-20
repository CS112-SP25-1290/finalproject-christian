package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FormController {
    @FXML private TextField      titleField;
    @FXML private TextField      consoleField;
    @FXML private TextField      genresField;
    @FXML private DatePicker     releaseDatePicker;
    @FXML private Spinner<Double> hoursSpinner;
    @FXML private CheckBox       completedCheck;
    @FXML private CheckBox       copyCheck;
    @FXML private TextField      coverPathField;
    @FXML private TextField      priceField;
    @FXML private TextField      valueField;

    private Stage  dialogStage;
    private Game   gameResult;
    private boolean okClicked = false;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setGame(Game game) {
        this.gameResult = game;

        // Title & console
        titleField.setText(game.getTitle() == null ? "" : game.getTitle());
        consoleField.setText(game.getConsole() == null ? "" : game.getConsole());

        // Genres
        List<String> genresList = game.getGenres();
        genresField.setText(
                (genresList == null || genresList.isEmpty())
                        ? ""
                        : String.join(", ", genresList)
        );

        // Release date (default to today if null)
        releaseDatePicker.setValue(
                game.getReleaseDate() == null
                        ? LocalDate.now()
                        : game.getReleaseDate()
        );

        // Hours played
        hoursSpinner.getValueFactory().setValue(game.getHoursPlayed());

        // Completed / copy
        completedCheck.setSelected(game.isCompleted());
        copyCheck.setSelected(game.isCompleteCopy());

        // Cover path
        coverPathField.setText(
                game.getCoverArtPath() == null ? "" : game.getCoverArtPath()
        );

        // Price and market value (empty if zero)
        priceField.setText(
                game.getPricePaid() != 0.0
                        ? Double.toString(game.getPricePaid())
                        : ""
        );
        valueField.setText(
                game.getMarketValue() != 0.0
                        ? Double.toString(game.getMarketValue())
                        : ""
        );
    }

    // Was the Save button clicked? */
    public boolean isOkClicked() {
        return okClicked;
    }

    // After Save, retrieve the updated/created Game */
    public Game getGameResult() {
        return gameResult;
    }

    @FXML
    private void onBrowseCover() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Cover Art");
        File f = chooser.showOpenDialog(dialogStage);
        if (f != null) {
            coverPathField.setText(f.toURI().toString());
        }
    }

    @FXML
    private void onSave() {
        // Read values (all fields are non-null now)
        String title   = titleField.getText().trim();
        String console = consoleField.getText().trim();
        List<String> genres = Arrays.asList(
                genresField.getText().split("\\s*,\\s*")
        );
        LocalDate date     = releaseDatePicker.getValue();
        double    hours    = hoursSpinner.getValue();
        boolean   done     = completedCheck.isSelected();
        boolean   copy     = copyCheck.isSelected();
        String    cover    = coverPathField.getText().trim();
        double    paid     = priceField.getText().isEmpty()
                ? 0.0
                : Double.parseDouble(priceField.getText().trim());
        double    value    = valueField.getText().isEmpty()
                ? 0.0
                : Double.parseDouble(valueField.getText().trim());

        if (gameResult != null) {
            // editing existing
            gameResult.setTitle(title);
            gameResult.setConsole(console);
            gameResult.setGenres(genres);
            gameResult.setReleaseDate(date);
            gameResult.setHoursPlayed(hours);
            gameResult.setCompleted(done);
            gameResult.setCompleteCopy(copy);
            gameResult.setCoverArtPath(cover);
            gameResult.setPricePaid(paid);
            gameResult.setMarketValue(value);
        } else {
            // creating new game
            gameResult = new Game(
                    title, console, genres, date,
                    hours, done, copy,
                    cover, paid, value
            );
        }

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        okClicked = false;
        dialogStage.close();
    }
}

