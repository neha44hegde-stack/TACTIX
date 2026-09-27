package com.neha.tactix.controller;

import com.neha.tactix.model.Difficulty;
import com.neha.tactix.service.SceneManager;
import com.neha.tactix.service.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

public class SettingsController {

    @FXML private CheckBox soundCheck;
    @FXML private CheckBox lightThemeCheck;
    @FXML private ComboBox<Difficulty> difficultyCombo;

    @FXML
    private void initialize() {
        Settings s = Settings.get();
        soundCheck.setSelected(s.isSoundEnabled());
        lightThemeCheck.setSelected(s.isLightTheme());
        difficultyCombo.getItems().setAll(Difficulty.values());
        difficultyCombo.setValue(s.getDefaultDifficulty());
    }

    @FXML
    private void onSave() {
        Settings s = Settings.get();
        s.setSoundEnabled(soundCheck.isSelected());
        s.setLightTheme(lightThemeCheck.isSelected());
        s.setDefaultDifficulty(difficultyCombo.getValue());
        SceneManager.showMainMenu();
    }

    @FXML private void onBack() { SceneManager.showMainMenu(); }
}