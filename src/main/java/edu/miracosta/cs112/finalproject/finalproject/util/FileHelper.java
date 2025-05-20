package edu.miracosta.cs112.finalproject.finalproject.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.miracosta.cs112.finalproject.finalproject.model.Game;
import edu.miracosta.cs112.finalproject.finalproject.persistence.DataStore;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for loading and saving Game data to/from JSON files.
 */
public class FileHelper implements DataStore {

    private final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT);


    @Override
    public List<Game> load(File file) throws DataLoadException {
        try {
            if (!file.exists()) {
                return List.of();
            }
            Game[] array = mapper.readValue(file, Game[].class);
            return Arrays.asList(array);
        } catch (IOException e) {
            throw new DataLoadException("Failed to load game data from file: " + file.getName(), e);
        }
    }

    @Override
    public void save(List<Game> games, File file) throws IOException {
        mapper.writeValue(file, games);
    }
}

