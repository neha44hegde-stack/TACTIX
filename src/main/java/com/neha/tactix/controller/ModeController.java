package com.neha.tactix.controller;

import com.neha.tactix.model.Difficulty;
import com.neha.tactix.model.GameMode;
import com.neha.tactix.service.GameSession;
import com.neha.tactix.service.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

public class ModeController {

    @FXML private RadioButton hvhRadio;
    @FXML private RadioButton hvcRadio;
    @FXML private RadioButton easyRadio;
    @FXML private RadioButton mediumRadio;
    @FXML private RadioButton hardRadio;

    @FXML
    private void initialize() {
        hvcRadio.setSelected(true);
        mediumRadio.setSelected(true);
        updateDifficultyDisabled();
        hvcRadio.selectedProperty().addListener((o, a, b) -> updateDifficultyDisabled());
    }

    private void updateDifficultyDisabled() {
        boolean enable = hvcRadio.isSelected();
        easyRadio.setDisable(!enable);
        mediumRadio.setDisable(!enable);
        hardRadio.setDisable(!enable);
    }

    @FXML
    private void onStart() {
        GameMode mode = hvhRadio.isSelected() ? GameMode.HUMAN_VS_HUMAN : GameMode.HUMAN_VS_COMPUTER;
        Difficulty diff = easyRadio.isSelected() ? Difficulty.EASY
                        : hardRadio.isSelected() ? Difficulty.HARD : Difficulty.MEDIUM;
        GameSession.get().startNewGame(mode, diff);
        SceneManager.showGame();
    }

    @FXML private void onBack() { SceneManager.showMainMenu(); }
}