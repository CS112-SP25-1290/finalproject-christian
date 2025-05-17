package edu.miracosta.cs112.finalproject.finalproject.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.miracosta.cs112.finalproject.finalproject.model.Game;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FileHelper {
    // Plain ObjectMapper—no default-typing at all
    private static final ObjectMapper mapper = new ObjectMapper()
            // Support LocalDate as ISO strings
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /** Save the list of games to the given file (pretty-printed). */
    public static void save(List<Game> games, File file) throws IOException {
        // We can serialize your ObservableList directly; nested lists will be plain arrays
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, games);
    }

    /** Load the list of games, or return an empty list if missing or corrupted. */
    public static List<Game> load(File file) {
        if (!file.exists()) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(
                    file,
                    mapper.getTypeFactory()
                            .constructCollectionType(List.class, Game.class)
            );
        } catch (IOException e) {
            e.printStackTrace();
            // Corrupted file? Delete and start fresh next run
            file.delete();
            return Collections.emptyList();
        }
    }
}
