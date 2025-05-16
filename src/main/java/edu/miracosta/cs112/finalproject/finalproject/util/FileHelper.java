package edu.miracosta.cs112.finalproject.finalproject.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.miracosta.cs112.finalproject.finalproject.model.Game;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FileHelper {
    private static final ObjectMapper mapper = new ObjectMapper()
            // support java.time types (LocalDate, etc.)
            .registerModule(new JavaTimeModule())
            // write dates as ISO strings, not timestamps
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // include @class for any non-final type under your model package
            .activateDefaultTyping(
                    BasicPolymorphicTypeValidator.builder()
                            .allowIfSubType("edu.miracosta.cs112.finalproject.finalproject.model")
                            .build(),
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
            );

    /** Save the list of games to the given file (pretty-printed). */
    public static void save(List<Game> games, File file) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, games);
    }

    /** Load the list of games, or return an empty list if the file doesn’t exist or is bad. */
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
            // Log the error
            e.printStackTrace();
            // Remove the corrupted file so the next run starts fresh
            file.delete();
            return Collections.emptyList();
        }
    }
}



