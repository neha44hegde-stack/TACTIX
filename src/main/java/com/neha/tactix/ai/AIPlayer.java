package com.neha.tactix.ai;

import com.neha.tactix.model.*;

import java.util.List;
import java.util.Random;

/** Computer opponent. Chooses a move based on difficulty. */
public class AIPlayer extends Player {

    private final Difficulty difficulty;
    private final Random random = new Random();

    public AIPlayer(Cell symbol, Difficulty difficulty) {
        super(symbol, "Computer (" + difficulty.getLabel() + ")");
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() { return difficulty; }

    public int chooseMove(Board board) {
        return switch (difficulty) {
            case EASY   -> randomMove(board);
            case MEDIUM -> mediumMove(board);
            case HARD   -> Minimax.findBestMove(board, getSymbol());
        };
    }

    private int randomMove(Board board) {
        List<Integer> moves = board.getAvailableMoves();
        return moves.get(random.nextInt(moves.size()));
    }

    private int mediumMove(Board board) {
        int move = findWinningMove(board, getSymbol());
        if (move != -1) return move;
        move = findWinningMove(board, getSymbol().opponent());
        if (move != -1) return move;
        if (board.isValidMove(4)) return 4;
        int[] corners = {0, 2, 6, 8};
        List<Integer> available = board.getAvailableMoves();
        for (int c : corners) if (available.contains(c)) return c;
        return randomMove(board);
    }

    private int findWinningMove(Board board, Cell symbol) {
        for (int move : board.getAvailableMoves()) {
            Board copy = board.copy();
            copy.makeMove(move, symbol);
            if (copy.checkWinner().winner() == symbol) return move;
        }
        return -1;
    }
}