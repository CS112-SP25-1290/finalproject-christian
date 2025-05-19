package edu.miracosta.cs112.finalproject.finalproject;

import edu.miracosta.cs112.finalproject.finalproject.controller.MainController;
import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.util.FileHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class HelloApplication extends Application {
    private MainController controller;
    private File dataFile;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/main.fxml")
        );
        Parent root = loader.load();

        controller = loader.getController();
        dataFile = new File(System.getProperty("user.home"), "games.json");
        System.out.println("Loading data from: " + dataFile.getAbsolutePath());
        controller.loadData(dataFile);

        stage.setTitle("My Video Game Collection");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Saving data to: " + dataFile.getAbsolutePath());
        List<Game> games = controller.getGames();
        FileHelper.save(games, dataFile);
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
