package edu.miracosta.cs112.finalproject.finalproject;

import edu.miracosta.cs112.finalproject.finalproject.controller.LibraryController;
import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.persistence.DataStore;
import edu.miracosta.cs112.finalproject.finalproject.util.FileHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    private LibraryController controller;
    private File dataFile;
    private final DataStore dataStore = new FileHelper();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/library-view.fxml")
        );
        Parent root = loader.load();

        controller = loader.getController();
        dataFile = new File(System.getProperty("user.home"), "games.json");
        System.out.println("Loading data from: " + dataFile.getAbsolutePath());
        // now load via the DataStore interface
        controller.loadData(dataFile, dataStore);

        stage.setTitle("My Video Game Collection");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Saving data to: " + dataFile.getAbsolutePath());
        List<Game> games = controller.getGames();
        // save via the DataStore interface
        dataStore.save(games, dataFile);
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

