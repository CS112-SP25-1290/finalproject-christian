package edu.miracosta.cs112.finalproject.finalproject.model;

import java.time.LocalDate;
import java.util.List;
import javafx.scene.image.Image;

public class PhysicalGame extends Game {

    public PhysicalGame(String title,
                        String console,
                        List<String> genres,
                        LocalDate releaseDate,
                        boolean completeCopy,
                        String coverArtPath,
                        double pricePaid,
                        double marketValue) {
        // Pass all the common fields up to Game’s constructor
        super(title,
                console,
                genres,
                releaseDate,
                completeCopy,
                coverArtPath,
                pricePaid,
                marketValue);
    }

    @Override
    public Image loadCoverArt() {
        String path = getCoverArtPath();
        if (!path.startsWith("file:")) {
            path = "file:" + path;
        }
        return new Image(path);
    }

}
