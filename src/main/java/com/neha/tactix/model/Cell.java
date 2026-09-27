package com.neha.tactix.model;

/** A single square of the Tic-Tac-Toe board. */
public enum Cell {
    EMPTY, X, O;
    public boolean isEmpty() { return this == EMPTY; }
    public Cell opponent() {
        return switch (this) {
            case X -> O;
            case O -> X;
            case EMPTY -> EMPTY;
        };
    }
}