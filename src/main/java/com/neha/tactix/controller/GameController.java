package com.neha.tactix.controller;

import com.neha.tactix.ai.AIPlayer;
import com.neha.tactix.model.Board;
import com.neha.tactix.model.Cell;
import com.neha.tactix.model.Difficulty;
import com.neha.tactix.model.Game;
import com.neha.tactix.model.GameStatus;
import com.neha.tactix.model.Move;
import com.neha.tactix.service.GameSession;
import com.neha.tactix.service.SceneManager;
import com.neha.tactix.service.Scoreboard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

public class GameController {

    @FXML private GridPane boardGrid;
    @FXML private Label turnLabel;
    @FXML private Label xScoreLabel;
    @FXML private Label oScoreLabel;
    @FXML private Label drawScoreLabel;
    @FXML private Label timerLabel;
    @FXML private ListView<String> historyList;
    @FXML private Button undoButton;
    @FXML private Button restartButton;
    @FXML private Button homeButton;

    private final Button[] cells = new Button[Board.CELL_COUNT];
    private final GameSession session = GameSession.get();
    private final Scoreboard scoreboard = Scoreboard.get();

    private AIPlayer aiPlayer;
    private boolean aiThinking = false;
    private Timeline timer;
    private long secondsElapsed;
    private boolean scoreRecorded;

    @FXML
    public void initialize() {
        buildBoardButtons();
        aiPlayer = session.isHumanVsComputer()
                ? new AIPlayer(Cell.O, session.getDifficulty())
                : null;
        scoreRecorded = false;
        startTimer();
        refreshAll();
        if (session.isHumanVsComputer() && session.getGame().getCurrentPlayer() == Cell.O) {
            triggerAiMove();
        }
    }

    private void buildBoardButtons() {
        boardGrid.getChildren().clear();
        boardGrid.getColumnConstraints().clear();
        boardGrid.getRowConstraints().clear();

        for (int i = 0; i < Board.SIZE; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / Board.SIZE);
            cc.setHgrow(Priority.ALWAYS);
            boardGrid.getColumnConstraints().add(cc);

            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / Board.SIZE);
            rc.setVgrow(Priority.ALWAYS);
            boardGrid.getRowConstraints().add(rc);
        }

        for (int i = 0; i < Board.CELL_COUNT; i++) {
            final int index = i;
            Button b = new Button();
            b.getStyleClass().add("cell");
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            b.setOnAction(e -> onCellClicked(index));
            cells[i] = b;
            boardGrid.add(b, i % Board.SIZE, i / Board.SIZE);
        }
    }

    private void onCellClicked(int index) {
        Game game = session.getGame();
        if (game.getStatus().isOver()) return;
        if (aiThinking) return;
        Cell turn = game.getCurrentPlayer();
        if (session.isHumanVsComputer() && turn != session.getHumanSymbol()) return;
        if (!game.makeMove(index)) return;
        refreshAll();
        if (game.getStatus().isOver()) { onGameEnded(); return; }
        if (session.isHumanVsComputer()) triggerAiMove();
    }

    private void triggerAiMove() {
        Game game = session.getGame();
        if (game.getStatus().isOver()) return;
        aiThinking = true;
        setControlsDisabled(true);

        Thread t = new Thread(() -> {
            int move = aiPlayer.chooseMove(game.getBoard().copy());
            Platform.runLater(() -> {
                aiThinking = false;
                if (!game.getStatus().isOver() && move >= 0) {
                    game.makeMove(move);
                    refreshAll();
                    if (game.getStatus().isOver()) onGameEnded();
                    else setControlsDisabled(false);
                } else {
                    setControlsDisabled(false);
                }
            });
        }, "tactix-ai");
        t.setDaemon(true);
        t.start();
    }

    private void onGameEnded() {
        stopTimer();
        setControlsDisabled(true);
        undoButton.setDisable(true);

        Game game = session.getGame();
        if (!scoreRecorded) {
            scoreRecorded = true;
            switch (game.getStatus()) {
                case X_WINS -> scoreboard.recordXWin();
                case O_WINS -> scoreboard.recordOWin();
                case DRAW   -> scoreboard.recordDraw();
                default     -> { }
            }
        }
        highlightWinningLine();
        refreshScoreboard();
        showResultDialog();
    }

    private void highlightWinningLine() {
        int[] line = session.getGame().getBoard().getWinningLine();
        if (line == null) return;
        for (int idx : line) cells[idx].getStyleClass().add("winning-cell");
    }

    private void showResultDialog() {
        Game game = session.getGame();
        String message;
        if (session.isHumanVsComputer()) {
            Cell human = session.getHumanSymbol();
            message = switch (game.getStatus()) {
                case DRAW -> "Draw!";
                case X_WINS, O_WINS ->
                        game.getStatus().winner() == human ? "You Win!" : "You Lose!";
                default -> "";
            };
        } else {
            message = switch (game.getStatus()) {
                case X_WINS -> "X Wins!";
                case O_WINS -> "O Wins!";
                case DRAW   -> "Draw!";
                default     -> "";
            };
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        ButtonType again = new ButtonType("Play Again", ButtonBar.ButtonData.OK_DONE);
        ButtonType menu  = new ButtonType("Return to Menu", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(again, menu);
        alert.showAndWait().ifPresent(bt -> {
            if (bt == again) restartGame();
            else SceneManager.showMainMenu();
        });
    }

    @FXML
    private void onRestart() { if (!aiThinking) restartGame(); }

    private void restartGame() {
        session.getGame().reset();
        scoreRecorded = false;
        for (Button b : cells) b.getStyleClass().remove("winning-cell");
        resetTimer();
        refreshAll();
        setControlsDisabled(false);
        if (session.isHumanVsComputer() && session.getGame().getCurrentPlayer() != session.getHumanSymbol()) {
            triggerAiMove();
        }
    }

    @FXML
    private void onUndo() {
        if (aiThinking) return;
        Game game = session.getGame();
        if (game.getHistory().isEmpty()) return;
        game.undo();
        if (session.isHumanVsComputer()
                && !game.getStatus().isOver()
                && game.getCurrentPlayer() != session.getHumanSymbol()
                && !game.getHistory().isEmpty()) {
            game.undo();
        }
        scoreRecorded = false;
        for (Button b : cells) b.getStyleClass().remove("winning-cell");
        refreshAll();
    }

    @FXML private void onHome() { stopTimer(); SceneManager.showMainMenu(); }

    @FXML
    private void onResetScore() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Reset the scoreboard?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) { scoreboard.reset(); refreshScoreboard(); }
        });
    }

    private void setControlsDisabled(boolean disabled) {
        boardGrid.setDisable(disabled);
        undoButton.setDisable(disabled || session.getGame().getHistory().isEmpty());
        restartButton.setDisable(false);
    }

    private void refreshAll() {
        Game game = session.getGame();
        for (int i = 0; i < cells.length; i++) {
            Cell c = game.getBoard().getCell(i);
            cells[i].setText(c == Cell.EMPTY ? "" : c.name());
            cells[i].getStyleClass().removeAll("cell-x", "cell-o");
            if (c == Cell.X) cells[i].getStyleClass().add("cell-x");
            if (c == Cell.O) cells[i].getStyleClass().add("cell-o");
        }
        updateTurnLabel();
        updateHistory();
        refreshScoreboard();
        undoButton.setDisable(aiThinking
                || game.getHistory().isEmpty()
                || game.getStatus().isOver());
    }

    private void updateTurnLabel() {
        Game game = session.getGame();
        if (game.getStatus().isOver()) {
            turnLabel.setText(switch (game.getStatus()) {
                case X_WINS -> "X Wins!";
                case O_WINS -> "O Wins!";
                case DRAW   -> "Draw!";
                default     -> "";
            });
            return;
        }
        Cell turn = game.getCurrentPlayer();
        if (session.isHumanVsComputer()) {
            turnLabel.setText(turn == session.getHumanSymbol()
                    ? "Your turn (" + turn + ")"
                    : "Computer thinking…");
        } else {
            turnLabel.setText("Turn: " + turn);
        }
    }

    private void updateHistory() {
        historyList.getItems().clear();
        int n = 1;
        for (Move m : session.getGame().getHistory()) {
            historyList.getItems().add(n++ + ". " + m.player() + " → " + positionName(m.index()));
        }
    }

    private String positionName(int index) {
        String[] names = {"Top Left","Top Center","Top Right","Middle Left","Center",
                "Middle Right","Bottom Left","Bottom Center","Bottom Right"};
        return names[index];
    }

    private void refreshScoreboard() {
        xScoreLabel.setText("X Wins: " + scoreboard.getXWins());
        oScoreLabel.setText("O Wins: " + scoreboard.getOWins());
        drawScoreLabel.setText("Draws: " + scoreboard.getDraws());
    }

    private void startTimer() {
        secondsElapsed = 0;
        timerLabel.setText("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timerLabel.setText(formatTime(secondsElapsed));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void resetTimer() { if (timer != null) timer.stop(); startTimer(); }
    private void stopTimer()  { if (timer != null) timer.stop(); }

    private String formatTime(long totalSeconds) {
        long m = totalSeconds / 60;
        long s = totalSeconds % 60;
        return String.format("%02d:%02d", m, s);
    }
}