# TACTIX — Intelligent Tic-Tac-Toe

A polished JavaFX desktop Tic-Tac-Toe featuring an unbeatable Hard AI built on
**Minimax with Alpha-Beta pruning**.

## Features
- Human vs Human and Human vs Computer
- Three AI difficulties: Easy / Medium / Hard
- Optimal play on Hard (Minimax + Alpha-Beta pruning)
- Timer, scoreboard, move history, undo, restart, settings
- Dark and light themes
- Fully unit-tested core logic

## Technology Stack
Java 21 · JavaFX 21 · FXML · CSS · Maven · JUnit 5 · jpackage · Git

## How Minimax works
The AI recursively explores every possible continuation, assigning
+1000-depth for a win, -1000+depth for a loss and 0 for a draw.
Depth adjustment makes the AI prefer faster wins and slower losses.

## How Alpha-Beta pruning works
Alpha is the best score the AI (MAX) can guarantee so far; beta is the best
score the human (MIN) can guarantee. Whenever beta <= alpha, the remaining
moves in that branch cannot affect the final decision and are skipped.

## How to run
mvn clean javafx:run

## How to test
mvn clean test

## How to package
mvn -q clean package
jpackage --type app-image --name TACTIX --input target --main-jar tactix-1.0.0.jar --main-class com.neha.tactix.Main

## Future improvements
- Persist settings and high scores
- Online multiplayer
- Larger board variants (4x4, 5x5)