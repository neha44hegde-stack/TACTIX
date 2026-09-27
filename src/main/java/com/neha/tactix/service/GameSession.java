package com.neha.tactix.service;

import com.neha.tactix.model.Cell;
import com.neha.tactix.model.Difficulty;
import com.neha.tactix.model.Game;
import com.neha.tactix.model.GameMode;

public class GameSession {
    private static final GameSession INSTANCE = new GameSession();
    private final Game game = new Game();
    private Difficulty difficulty = Difficulty.MEDIUM;
    private Cell humanSymbol = Cell.X;
    private GameSession() { }
    public static GameSession get() { return INSTANCE; }
    public Game getGame()              { return game; }
    public Difficulty getDifficulty()  { return difficulty; }
    public Cell getHumanSymbol()       { return humanSymbol; }
    public boolean isHumanVsComputer() { return game.getMode() == GameMode.HUMAN_VS_COMPUTER; }
    public void startNewGame(GameMode mode, Difficulty difficulty) {
        game.setMode(mode);
        this.difficulty = difficulty;
        this.humanSymbol = Cell.X;
        game.reset();
    }
}