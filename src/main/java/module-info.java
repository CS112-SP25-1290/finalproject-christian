module edu.miracosta.cs112.finalproject.finalproject {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    // Jackson modules
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;

    // Allow FXMLLoader to reflectively instantiate your controllers
    opens edu.miracosta.cs112.finalproject.finalproject.controller to javafx.fxml;

    // Allow Jackson (and JavaFX properties if you ever bind them) to reflectively
    // access your model classes
    opens edu.miracosta.cs112.finalproject.finalproject.model
            to com.fasterxml.jackson.databind,
            com.fasterxml.jackson.annotation,
            com.fasterxml.jackson.datatype.jsr310,
            javafx.base;

    // Expose your top‐level package to the outside if you ever need it
    exports edu.miracosta.cs112.finalproject.finalproject;
}

