package com.neha.tactix;

import com.neha.tactix.ai.AIPlayer;
import com.neha.tactix.ai.Minimax;
import com.neha.tactix.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimaxTest {

    private Board boardFrom(String s) {
        Board b = new Board();
        for (int i = 0; i < 9; i++) {
            char c = s.charAt(i);
            if (c == 'X') b.makeMove(i, Cell.X);
            else if (c == 'O') b.makeMove(i, Cell.O);
        }
        return b;
    }

    @Test void picksWinningMoveImmediately() {
        // X X _      X to move → wins by playing 2
        // O O _
        // _ _ _
        Board b = boardFrom("XX_O_____");
        assertEquals(2, Minimax.findBestMove(b, Cell.X));
    }

    @Test void blocksOpponentWinningMove() {
        // X X _      O to move → must block at 2
        // O _ _
        // _ _ _
        Board b = boardFrom("XX_O_____");
        assertEquals(2, Minimax.findBestMove(b, Cell.O));
    }

    @Test void oWinsWhenBothSidesThreaten() {
        // X X _      X threatens 2 (top row)
        // O O _      O threatens 5 (middle row)
        // _ _ _      O to move → wins by playing 5 (blocking at 2 would lose)
        Board b = boardFrom("XX_OO____");
        assertEquals(5, Minimax.findBestMove(b, Cell.O));
    }

    @Test void returnsLegalMove() {
        Board b = new Board();
        int move = Minimax.findBestMove(b, Cell.X);
        assertTrue(move >= 0 && move < 9);
        assertTrue(b.isValidMove(move));
    }

    @Test void neverOverwritesOccupied() {
        Board b = boardFrom("XO_______");
        int move = Minimax.findBestMove(b, Cell.X);
        assertNotEquals(0, move);
        assertNotEquals(1, move);
    }

    @Test void hardAiChoosesWinningMove() {
        AIPlayer ai = new AIPlayer(Cell.X, Difficulty.HARD);
        assertEquals(2, ai.chooseMove(boardFrom("XX_O_____")));
    }

    @Test void hardAiBlocks() {
        AIPlayer ai = new AIPlayer(Cell.O, Difficulty.HARD);
        assertEquals(2, ai.chooseMove(boardFrom("XX_O_____")));
    }

    @Test void hardAiIsLegal() {
        AIPlayer ai = new AIPlayer(Cell.O, Difficulty.HARD);
        Board b = boardFrom("X________");
        assertTrue(b.isValidMove(ai.chooseMove(b)));
    }

    @Test void easyAiIsAlwaysLegal() {
        AIPlayer ai = new AIPlayer(Cell.O, Difficulty.EASY);
        Board b = boardFrom("X_O_X_O__");
        for (int i = 0; i < 20; i++) assertTrue(b.isValidMove(ai.chooseMove(b)));
    }

    @Test void mediumAiTakesCenter() {
        AIPlayer ai = new AIPlayer(Cell.O, Difficulty.MEDIUM);
        assertEquals(4, ai.chooseMove(boardFrom("X________")));
    }

    @Test void mediumAiBlocksThreat() {
        AIPlayer ai = new AIPlayer(Cell.O, Difficulty.MEDIUM);
        assertEquals(2, ai.chooseMove(boardFrom("XX_O_____")));
    }
}