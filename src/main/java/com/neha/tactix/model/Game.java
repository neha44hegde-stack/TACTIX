package com.neha.tactix.model;

import java.util.ArrayList;
import java.util.List;

/** Orchestrates a single match: board, turn order, history, outcome. */
public class Game {

    private final Board board = new Board();
    private final List<Move> history = new ArrayList<>();
    private GameMode mode = GameMode.HUMAN_VS_HUMAN;
    private Cell currentPlayer = Cell.X;
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Board getBoard()            { return board; }
    public GameMode getMode()          { return mode; }
    public void setMode(GameMode mode) { this.mode = mode; }
    public Cell getCurrentPlayer()     { return currentPlayer; }
    public GameStatus getStatus()      { return status; }
    public List<Move> getHistory()     { return List.copyOf(history); }

    public boolean makeMove(int index) {
        if (status.isOver()) return false;
        if (!board.makeMove(index, currentPlayer)) return false;
        history.add(new Move(index, currentPlayer));
        status = board.checkWinner();
        if (!status.isOver()) currentPlayer = currentPlayer.opponent();
        return true;
    }

    public boolean undo() {
        if (history.isEmpty()) return false;
        history.remove(history.size() - 1);
        board.resetBoard();
        Cell turn = Cell.X;
        for (Move m : history) {
            board.makeMove(m.index(), m.player());
            turn = m.player().opponent();
        }
        currentPlayer = turn;
        status = board.checkWinner();
        return true;
    }

    public void reset() {
        board.resetBoard();
        history.clear();
        currentPlayer = Cell.X;
        status = GameStatus.IN_PROGRESS;
    }
}