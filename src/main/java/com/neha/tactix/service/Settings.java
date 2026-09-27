package com.neha.tactix.service;

import com.neha.tactix.model.Difficulty;

public class Settings {
    private static final Settings INSTANCE = new Settings();
    private boolean soundEnabled = true;
    private boolean lightTheme = false;
    private Difficulty defaultDifficulty = Difficulty.MEDIUM;
    private Settings() { }
    public static Settings get() { return INSTANCE; }
    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean v) { this.soundEnabled = v; }
    public boolean isLightTheme() { return lightTheme; }
    public void setLightTheme(boolean v) { this.lightTheme = v; }
    public Difficulty getDefaultDifficulty() { return defaultDifficulty; }
    public void setDefaultDifficulty(Difficulty d) { this.defaultDifficulty = d; }
}