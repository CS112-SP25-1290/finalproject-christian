// src/main/java/edu/miracosta/cs112/finalproject/finalproject/persistence/DataStore.java
package edu.miracosta.cs112.finalproject.finalproject.persistence;

import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.util.DataLoadException;
import java.io.File;
import java.io.IOException;
import java.util.List;

// Defines methods to load and save games.

public interface DataStore {
    List<Game> load(File file) throws DataLoadException;
    void save(List<Game> games, File file) throws IOException;
}

