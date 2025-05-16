package edu.miracosta.cs112.finalproject.finalproject;

import edu.miracosta.cs112.finalproject.finalproject.controller.LibraryController;
import edu.miracosta.cs112.finalproject.finalproject.model.Game;
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
    private LibraryController controller;      // ← hold onto it
    private File dataFile;                     // ← hold file path

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/library-view.fxml")
        );
        Parent root = loader.load();

        // Grab and keep the controller
        controller = loader.getController();

        // Determine where we'll save
        dataFile = new File(System.getProperty("user.home"), "games.json");
        System.out.println("Loading data from: " + dataFile.getAbsolutePath());
        // Load any existing games
        List<Game> saved = FileHelper.load(dataFile);
        controller.getMasterData().addAll(saved);

        // Show your UI
        stage.setTitle("My Video Game Collection");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // This always runs on JVM shutdown, even if the window is closed via the traffic-light buttons
        System.out.println("Saving data to: " + dataFile.getAbsolutePath());
        FileHelper.save(controller.getMasterData(), dataFile);
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
