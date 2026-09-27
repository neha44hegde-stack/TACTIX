package com.neha.tactix.ai;

import com.neha.tactix.model.Board;
import com.neha.tactix.model.Cell;
import com.neha.tactix.model.GameStatus;

import java.util.List;

/**
 * Minimax with Alpha-Beta pruning for 3x3 Tic-Tac-Toe.
 *
 * <p>Alpha = best MAX (AI) score guaranteed so far.
 * Beta = best MIN (human) score guaranteed so far.
 * When {@code beta <= alpha} the current branch cannot affect the outcome
 * and is pruned. This drastically reduces the search space while keeping
 * optimal play.</p>
 */
public final class Minimax {

    private static final int WIN_SCORE = 1000;

    private Minimax() { }

    public static int findBestMove(Board board, Cell aiSymbol) {
        return search(board.copy(), aiSymbol, aiSymbol.opponent(),
                0, Integer.MIN_VALUE, Integer.MAX_VALUE, true)[0];
    }

    private static int[] search(Board board, Cell aiSymbol, Cell humanSymbol,
                                int depth, int alpha, int beta, boolean maximizing) {

        GameStatus status = board.checkWinner();
        if (status.isOver()) {
            if (status == GameStatus.DRAW) return new int[]{-1, 0};
            Cell winner = status.winner();
            int score = (winner == aiSymbol) ? WIN_SCORE - depth : -WIN_SCORE + depth;
            return new int[]{-1, score};
        }

        List<Integer> moves = board.getAvailableMoves();
        int bestIndex = moves.get(0);

        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int move : moves) {
                Board next = board.copy();
                next.makeMove(move, aiSymbol);
                int score = search(next, aiSymbol, humanSymbol, depth + 1, alpha, beta, false)[1];
                if (score > bestScore) { bestScore = score; bestIndex = move; }
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) break;
            }
            return new int[]{bestIndex, bestScore};
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int move : moves) {
                Board next = board.copy();
                next.makeMove(move, humanSymbol);
                int score = search(next, aiSymbol, humanSymbol, depth + 1, alpha, beta, true)[1];
                if (score < bestScore) { bestScore = score; bestIndex = move; }
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) break;
            }
            return new int[]{bestIndex, bestScore};
        }
    }
}