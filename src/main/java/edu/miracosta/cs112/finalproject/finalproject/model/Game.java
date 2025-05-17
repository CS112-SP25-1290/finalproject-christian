package edu.miracosta.cs112.finalproject.finalproject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Game {
    private String title;
    private String console;
    private List<String> genres;
    private LocalDate releaseDate;
    private double hoursPlayed;
    private boolean completed;
    private boolean completeCopy;
    private String coverArtPath;
    private double pricePaid;
    private double marketValue;

    /** For Jackson / FXML */
    public Game() { }

    public Game(String title,
                String console,
                List<String> genres,
                LocalDate releaseDate,
                double hoursPlayed,
                boolean completed,
                boolean completeCopy,
                String coverArtPath,
                double pricePaid,
                double marketValue) {
        this.title        = title;
        this.console      = console;
        this.genres       = genres;
        this.releaseDate  = releaseDate;
        this.hoursPlayed  = hoursPlayed;
        this.completed    = completed;
        this.completeCopy = completeCopy;
        this.coverArtPath = coverArtPath;
        this.pricePaid    = pricePaid;
        this.marketValue  = marketValue;
    }

    // ─── Getters & Setters ─────────────────────────────────

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getConsole() { return console; }
    public void setConsole(String console) { this.console = console; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public double getHoursPlayed() { return hoursPlayed; }
    public void setHoursPlayed(double hoursPlayed) { this.hoursPlayed = hoursPlayed; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isCompleteCopy() { return completeCopy; }
    public void setCompleteCopy(boolean completeCopy) { this.completeCopy = completeCopy; }

    public String getCoverArtPath() { return coverArtPath; }
    public void setCoverArtPath(String coverArtPath) { this.coverArtPath = coverArtPath; }

    public double getPricePaid() { return pricePaid; }
    public void setPricePaid(double pricePaid) { this.pricePaid = pricePaid; }

    public double getMarketValue() { return marketValue; }
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }

    // ─── Convenience ────────────────────────────────────────

    /** Profit (or loss) = marketValue − pricePaid */
    public double getProfit() {
        return marketValue - pricePaid;
    }
}











