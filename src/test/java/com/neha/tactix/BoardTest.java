package com.neha.tactix;

import com.neha.tactix.model.Board;
import com.neha.tactix.model.Cell;
import com.neha.tactix.model.GameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test void newBoardIsEmpty() {
        Board b = new Board();
        for (int i = 0; i < Board.CELL_COUNT; i++) {
            assertEquals(Cell.EMPTY, b.getCell(i));
            assertTrue(b.isValidMove(i));
        }
        assertFalse(b.isBoardFull());
        assertEquals(GameStatus.IN_PROGRESS, b.checkWinner());
    }

    @Test void validAndInvalidMoves() {
        Board b = new Board();
        assertTrue(b.makeMove(0, Cell.X));
        assertFalse(b.makeMove(0, Cell.O));
        assertFalse(b.isValidMove(0));
        assertTrue(b.isValidMove(1));
    }

    @Test void rejectsEmptyPlayer() {
        Board b = new Board();
        assertFalse(b.makeMove(0, Cell.EMPTY));
    }

    @Test void detectsRowWin() {
        Board b = new Board();
        b.makeMove(0, Cell.X); b.makeMove(1, Cell.X); b.makeMove(2, Cell.X);
        assertEquals(GameStatus.X_WINS, b.checkWinner());
    }

    @Test void detectsColumnWin() {
        Board b = new Board();
        b.makeMove(0, Cell.O); b.makeMove(3, Cell.O); b.makeMove(6, Cell.O);
        assertEquals(GameStatus.O_WINS, b.checkWinner());
    }

    @Test void detectsDiagonalWin() {
        Board b = new Board();
        b.makeMove(0, Cell.X); b.makeMove(4, Cell.X); b.makeMove(8, Cell.X);
        assertEquals(GameStatus.X_WINS, b.checkWinner());
    }

    @Test void detectsAntiDiagonalWin() {
        Board b = new Board();
        b.makeMove(2, Cell.O); b.makeMove(4, Cell.O); b.makeMove(6, Cell.O);
        assertEquals(GameStatus.O_WINS, b.checkWinner());
    }

    @Test void detectsDraw() {
        Board b = new Board();
        int[]  mv = {0,1,2,4,3,5,7,6,8};
        Cell[] sym = {Cell.X,Cell.O,Cell.X,Cell.O,Cell.X,Cell.O,Cell.X,Cell.O,Cell.X};
        for (int i = 0; i < mv.length; i++) b.makeMove(mv[i], sym[i]);
        assertTrue(b.isBoardFull());
        assertEquals(GameStatus.DRAW, b.checkWinner());
    }

    @Test void availableMovesReflectState() {
        Board b = new Board();
        b.makeMove(0, Cell.X); b.makeMove(4, Cell.O);
        var moves = b.getAvailableMoves();
        assertEquals(7, moves.size());
        assertFalse(moves.contains(0));
        assertFalse(moves.contains(4));
    }

    @Test void resetClears() {
        Board b = new Board();
        b.makeMove(0, Cell.X); b.makeMove(1, Cell.O);
        b.resetBoard();
        for (int i = 0; i < Board.CELL_COUNT; i++) assertTrue(b.getCell(i).isEmpty());
    }

    @Test void copyIsIndependent() {
        Board b = new Board();
        b.makeMove(0, Cell.X);
        Board c = b.copy();
        c.makeMove(1, Cell.O);
        assertEquals(Cell.EMPTY, b.getCell(1));
        assertEquals(Cell.O,     c.getCell(1));
    }
}