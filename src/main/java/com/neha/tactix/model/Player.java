package com.neha.tactix.model;

public abstract class Player {
    protected final Cell symbol;
    protected final String name;
    protected Player(Cell symbol, String name) { this.symbol = symbol; this.name = name; }
    public Cell getSymbol() { return symbol; }
    public String getName() { return name; }
}