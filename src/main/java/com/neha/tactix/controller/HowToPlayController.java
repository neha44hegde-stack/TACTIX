package com.neha.tactix.controller;

import com.neha.tactix.service.SceneManager;
import javafx.fxml.FXML;

public class HowToPlayController {
    @FXML private void onBack() { SceneManager.showMainMenu(); }
}