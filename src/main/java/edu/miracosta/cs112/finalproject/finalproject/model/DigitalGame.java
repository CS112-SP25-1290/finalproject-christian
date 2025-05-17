/** package edu.miracosta.cs112.finalproject.finalproject.model;

import java.time.LocalDate;
import java.util.List;
import javafx.scene.image.Image;

public  class DigitalGame extends Game{
    private String storeUrl;

    public DigitalGame(String title,
                       String console,
                       List<String> genres,
                       LocalDate releaseDate,
                       boolean completeCopy,   // now here
                       String coverArtPath,    // and this
                       double pricePaid,
                       double marketValue,
                       String storeUrl) {
        super(title,
                console,
                genres,
                releaseDate,
                completeCopy,
                coverArtPath,
                pricePaid,
                marketValue);
        this.storeUrl = storeUrl;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    @Override
    public Image loadCoverArt() {
        return new Image(getCoverArtPath());
    }



} */
