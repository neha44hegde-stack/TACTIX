package com.neha.tactix;

import com.neha.tactix.service.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/** Application entry point. */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.init(primaryStage);
        SceneManager.showMainMenu();
    }
    public static void main(String[] args) { launch(args); }
}