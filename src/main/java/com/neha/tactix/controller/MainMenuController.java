package com.neha.tactix.controller;

import com.neha.tactix.service.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainMenuController {
    @FXML private void onPlay()      { SceneManager.showModeSelection(); }
    @FXML private void onHowToPlay() { SceneManager.showHowToPlay(); }
    @FXML private void onSettings()  { SceneManager.showSettings(); }
    @FXML private void onExit()      { Platform.exit(); }
}