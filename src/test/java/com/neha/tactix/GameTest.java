package com.neha.tactix;

import com.neha.tactix.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test void startsWithX() {
        Game g = new Game();
        assertEquals(Cell.X, g.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, g.getStatus());
        assertTrue(g.getHistory().isEmpty());
    }

    @Test void turnAlternates() {
        Game g = new Game();
        g.makeMove(0); assertEquals(Cell.O, g.getCurrentPlayer());
        g.makeMove(1); assertEquals(Cell.X, g.getCurrentPlayer());
    }

    @Test void rejectsOccupiedCell() {
        Game g = new Game();
        assertTrue(g.makeMove(0));
        assertFalse(g.makeMove(0));
    }

    @Test void rejectsMoveAfterGameEnds() {
        Game g = new Game();
        g.makeMove(0); g.makeMove(3);
        g.makeMove(1); g.makeMove(4);
        g.makeMove(2);
        assertEquals(GameStatus.X_WINS, g.getStatus());
        assertFalse(g.makeMove(5));
    }

    @Test void undoRestoresState() {
        Game g = new Game();
        g.makeMove(0); g.makeMove(4);
        assertEquals(Cell.X, g.getCurrentPlayer());
        assertTrue(g.undo());
        assertEquals(Cell.O, g.getCurrentPlayer());
        assertTrue(g.getBoard().getCell(4).isEmpty());
        assertTrue(g.undo());
        assertFalse(g.undo());
    }

    @Test void undoAfterWinClearsStatus() {
        Game g = new Game();
        g.makeMove(0); g.makeMove(3);
        g.makeMove(1); g.makeMove(4);
        g.makeMove(2);
        assertEquals(GameStatus.X_WINS, g.getStatus());
        g.undo();
        assertEquals(GameStatus.IN_PROGRESS, g.getStatus());
    }

    @Test void resetClearsEverything() {
        Game g = new Game();
        g.makeMove(0);
        g.reset();
        assertTrue(g.getHistory().isEmpty());
        assertEquals(Cell.X, g.getCurrentPlayer());
        assertEquals(GameStatus.IN_PROGRESS, g.getStatus());
    }
}