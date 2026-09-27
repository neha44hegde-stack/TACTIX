package com.neha.tactix.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure, UI-independent 3x3 Tic-Tac-Toe board.
 * Owns board state and all rule logic (moves, win, draw).
 */
public class Board {

    public static final int SIZE = 3;
    public static final int CELL_COUNT = SIZE * SIZE;

    private static final int[][] WINNING_LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private final Cell[] cells = new Cell[CELL_COUNT];

    public Board() { resetBoard(); }

    public void resetBoard() {
        for (int i = 0; i < CELL_COUNT; i++) cells[i] = Cell.EMPTY;
    }

    public Cell getCell(int index) {
        checkIndex(index);
        return cells[index];
    }

    public boolean isValidMove(int index) {
        return index >= 0 && index < CELL_COUNT && cells[index].isEmpty();
    }

    public boolean makeMove(int index, Cell player) {
        if (player == Cell.EMPTY || !isValidMove(index)) return false;
        cells[index] = player;
        return true;
    }

    public boolean isBoardFull() {
        for (Cell c : cells) if (c.isEmpty()) return false;
        return true;
    }

    public GameStatus checkWinner() {
        for (int[] line : WINNING_LINES) {
            Cell a = cells[line[0]];
            if (a == Cell.EMPTY) continue;
            if (a == cells[line[1]] && a == cells[line[2]]) {
                return a == Cell.X ? GameStatus.X_WINS : GameStatus.O_WINS;
            }
        }
        return isBoardFull() ? GameStatus.DRAW : GameStatus.IN_PROGRESS;
    }

    public int[] getWinningLine() {
        for (int[] line : WINNING_LINES) {
            Cell a = cells[line[0]];
            if (a == Cell.EMPTY) continue;
            if (a == cells[line[1]] && a == cells[line[2]]) return line.clone();
        }
        return null;
    }

    public List<Integer> getAvailableMoves() {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < CELL_COUNT; i++) if (cells[i].isEmpty()) moves.add(i);
        return moves;
    }

    public Board copy() {
        Board b = new Board();
        System.arraycopy(cells, 0, b.cells, 0, CELL_COUNT);
        return b;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= CELL_COUNT)
            throw new IndexOutOfBoundsException("Cell index out of range: " + index);
    }
}