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
    @FXML private TextField    titleField;
    @FXML private TextField    consoleField;
    @FXML private TextField    genresField;
    @FXML private DatePicker   releaseDatePicker;
    @FXML private Spinner<Double> hoursSpinner;
    @FXML private CheckBox     completedCheck;
    @FXML private CheckBox     copyCheck;
    @FXML private TextField    coverPathField;
    @FXML private TextField    priceField;
    @FXML private TextField    valueField;
    // no storeUrlField any more

    private Stage dialogStage;
    private Game  gameResult;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

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
        String title   = titleField.getText().trim();
        String console = consoleField.getText().trim();
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

        if (gameResult != null) {
            // editing
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
            // new
            gameResult = new Game(
                    title, console, genres, date,
                    hours, done, copy,
                    cover, paid, value
            );
        }

        dialogStage.close();
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }
}
