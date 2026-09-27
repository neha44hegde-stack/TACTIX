package com.neha.tactix.service;

public class Scoreboard {
    private static final Scoreboard INSTANCE = new Scoreboard();
    private int xWins, oWins, draws;
    private Scoreboard() { }
    public static Scoreboard get() { return INSTANCE; }
    public int getXWins() { return xWins; }
    public int getOWins() { return oWins; }
    public int getDraws() { return draws; }
    public void recordXWin() { xWins++; }
    public void recordOWin() { oWins++; }
    public void recordDraw() { draws++; }
    public void reset() { xWins = oWins = draws = 0; }
}