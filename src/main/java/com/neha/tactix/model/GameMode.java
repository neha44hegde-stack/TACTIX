package com.neha.tactix.model;

public enum GameMode {
    HUMAN_VS_HUMAN("Human vs Human"),
    HUMAN_VS_COMPUTER("Human vs Computer");
    private final String label;
    GameMode(String label) { this.label = label; }
    public String getLabel() { return label; }
}