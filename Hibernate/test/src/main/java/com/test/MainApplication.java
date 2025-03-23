package com.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/MainView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 640, 480);

        stage.setScene(scene);
        stage.setTitle("Zarzadzaj Pracownikami");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}