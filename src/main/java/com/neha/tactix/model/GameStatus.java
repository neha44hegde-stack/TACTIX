package com.neha.tactix.model;

public enum GameStatus {
    IN_PROGRESS, X_WINS, O_WINS, DRAW;
    public boolean isOver() { return this != IN_PROGRESS; }
    public Cell winner() {
        return switch (this) {
            case X_WINS -> Cell.X;
            case O_WINS -> Cell.O;
            default -> Cell.EMPTY;
        };
    }
}