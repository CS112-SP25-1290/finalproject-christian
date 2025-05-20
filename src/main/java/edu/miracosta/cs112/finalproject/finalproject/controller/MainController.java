package edu.miracosta.cs112.finalproject.finalproject.controller;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.persistence.DataStore;
import edu.miracosta.cs112.finalproject.finalproject.util.DataLoadException;
import edu.miracosta.cs112.finalproject.finalproject.util.FileHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TilePane coverTilePane;

    private List<Game> allGames;
    private final DataStore dataStore = new FileHelper();

    /** Called once from HelloApplication.start(...) */
    public void loadData(File dataFile) {
        try {
            FileHelper helper = new FileHelper();
            allGames = helper.load(dataFile);

            populateCovers(allGames);
        } catch (DataLoadException e) {
            e.printStackTrace();
            // Optional: show an error dialog here if you'd like
        }
    }



    /** Expose for saving on exit */
    public List<Game> getGames() {
        return allGames;
    }

    @FXML
    private void onShowAll(ActionEvent e) {
        populateCovers(allGames);
    }

    @FXML
    private void onShowPS5(ActionEvent e) {
        List<Game> ps5Only = allGames.stream()
                .filter(g -> "PS5".equalsIgnoreCase(g.getConsole()))
                .collect(Collectors.toList());
        populateCovers(ps5Only);
    }

    @FXML
    private void onShowSwitch(ActionEvent e) {
        List<Game> switchOnly = allGames.stream()
                .filter(g -> "Switch".equalsIgnoreCase(g.getConsole()))
                .collect(Collectors.toList());
        populateCovers(switchOnly);
    }

    private void populateCovers(List<Game> games) {
        coverTilePane.getChildren().clear();
        for (Game game : games) {
            Image img = loadImage(game.getCoverArtPath());
            ImageView iv = new ImageView(img);
            iv.setFitWidth(120);
            iv.setPreserveRatio(true);
            iv.setOnMouseClicked(evt -> showGameDetails(game));
            coverTilePane.getChildren().add(iv);
        }
    }

    private Image loadImage(String rawPath) {
        try {
            if (rawPath.startsWith("http://") || rawPath.startsWith("https://"))
                return new Image(rawPath, true);

            URL res = getClass().getResource(rawPath);
            if (res != null) return new Image(res.toExternalForm(), true);

            File f = new File(rawPath);
            if (f.exists()) return new Image(f.toURI().toString(), true);

            throw new IllegalArgumentException("Not a valid URL, resource or file: " + rawPath);
        } catch (Exception e) {
            URL placeholder = getClass().getResource("/images/default-cover.png");
            if (placeholder != null)
                return new Image(placeholder.toExternalForm(), true);
            return new Image("https://via.placeholder.com/120x160.png?text=No+Cover");
        }
    }

    private void showGameDetails(Game game) {
        // TODO: detail-popup or side-panel code here
        System.out.println("Clicked on: " + game.getTitle());
    }
}

