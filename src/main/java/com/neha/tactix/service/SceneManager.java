package com.neha.tactix.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class SceneManager {

    private static Stage stage;
    private SceneManager() { }

    public static void init(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("TACTIX — Intelligent Tic-Tac-Toe");
        stage.setMinWidth(560);
        stage.setMinHeight(700);
    }

    public static void showMainMenu()      { load("/fxml/main-menu.fxml",      "TACTIX"); }
    public static void showModeSelection() { load("/fxml/mode-selection.fxml", "Select Mode"); }
    public static void showGame()          { load("/fxml/game.fxml",           "TACTIX — Play"); }
    public static void showHowToPlay()     { load("/fxml/how-to-play.fxml",    "How to Play"); }
    public static void showSettings()      { load("/fxml/settings.fxml",       "Settings"); }

    private static void load(String path, String title) {
        try {
            URL url = SceneManager.class.getResource(path);
            if (url == null) throw new IllegalStateException("FXML not found: " + path);
            Parent root = new FXMLLoader(url).load();
            String css = Settings.get().isLightTheme() ? "/css/light-theme.css" : "/css/styles.css";
            URL cssUrl = SceneManager.class.getResource(css);
            Scene scene = new Scene(root);
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load screen: " + path, e);
        }
    }
}