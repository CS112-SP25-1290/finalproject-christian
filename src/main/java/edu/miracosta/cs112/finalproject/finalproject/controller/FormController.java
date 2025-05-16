package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FormController {
    @FXML private TextField   titleField;
    @FXML private TextField   consoleField;
    @FXML private TextField   genresField;
    @FXML private DatePicker  releaseDatePicker;
    @FXML private Spinner<Double> hoursSpinner;
    @FXML private CheckBox    completedCheck;
    @FXML private CheckBox    copyCheck;
    @FXML private TextField   coverPathField;
    @FXML private TextField   priceField;
    @FXML private TextField   valueField;

    private Stage dialogStage;
    private Game  gameResult;   // will hold the new/edited game

    /** Called by LibraryController to set up this dialog. */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    /** If editing, pre-fill fields from an existing Game. */
    public void setGame(Game game) {
        this.gameResult = game;
        titleField.setText(game.getTitle());
        consoleField.setText(game.getConsole());
        genresField.setText(String.join(", ", game.getGenres()));
        releaseDatePicker.setValue(game.getReleaseDate());
        hoursSpinner.getValueFactory().setValue(game.getHoursPlayed());
        completedCheck.setSelected(game.isCompleted());
        copyCheck.setSelected(game.isCompleteCopy());
        coverPathField.setText(game.getCoverArtPath());
        priceField.setText(Double.toString(game.getPricePaid()));
        valueField.setText(Double.toString(game.getMarketValue()));
    }

    /** Returns the new or updated Game after Save was clicked. */
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
        // collect and validate inputs
        String title    = titleField.getText().trim();
        String console  = consoleField.getText().trim();
        List<String> genres = Arrays.asList(
                genresField.getText().split("\\s*,\\s*")
        );
        LocalDate date = releaseDatePicker.getValue();
        double hours   = hoursSpinner.getValue();
        boolean done   = completedCheck.isSelected();
        boolean copy   = copyCheck.isSelected();
        String cover   = coverPathField.getText().trim();
        double paid    = Double.parseDouble(priceField.getText().trim());
        double value   = Double.parseDouble(valueField.getText().trim());

        // if editing existing, update it else create new Physical or Digital
        if (gameResult != null) {

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
            // storeUrl remains unchanged
        } else {
            // default to PhysicalGame if gameResult was null or Physical
            PhysicalGame pg = new PhysicalGame(
                    title, console, genres, date,
                    copy,    // completeCopy
                    cover,   // coverArtPath
                    paid,    // pricePaid
                    value    // marketValue
            );
            // now apply the hours & completed flags
            pg.setHoursPlayed(hours);
            pg.setCompleted(done);
            gameResult = pg;
        }

        dialogStage.close();

    }


    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}

