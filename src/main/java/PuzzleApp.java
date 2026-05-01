import algorithms.AStarSolver;
import algorithms.BFSSolver;
import javafx.animation.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import puzzle.*;
import ui.PuzzleUI;

import java.util.*;

public class PuzzleApp extends Application {

    private final PuzzleUI ui = new PuzzleUI();

    // ── State
    private int[]             board    = {1,2,3,4,5,6,7,8,0};
    private List<PuzzleState> path     = null;
    private int               step     = 0;
    private Timeline          timeline = null;

    // ── Init
    @Override
    public void start(Stage stage) {
        ui.build(stage);
        ui.shuffleBtn.setOnAction(e -> doShuffle());
        ui.solveBtn.setOnAction(e -> doSolve());
        ui.stepBtn.setOnAction(e -> doStep());
        ui.autoBtn.setOnAction(e -> doAuto());
        ui.resetBtn.setOnAction(e -> doReset());
        draw(board);
    }

    // ── Actions
    private void doShuffle() {
        stopAuto(); path = null; step = 0;
        ui.stepBtn.setDisable(true); ui.autoBtn.setDisable(true); ui.bar.setProgress(0);
        ui.steps.setText("Steps: -"); ui.nodes.setText("Nodes: -"); ui.time.setText("Time:  -");
        Random rnd = new Random(); int[] b;
        do { b = new int[]{0,1,2,3,4,5,6,7,8};
            for (int i=8; i>0; i--) { int j=rnd.nextInt(i+1), x=b[i]; b[i]=b[j]; b[j]=x; }
        } while (!PuzzleState.isSolvable(b));
        board = b; draw(board); status("Shuffled - ready to solve", PuzzleUI.CYAN);
    }

    private void doSolve() {
        stopAuto(); path = null; step = 0;
        ui.stepBtn.setDisable(true); ui.autoBtn.setDisable(true); ui.bar.setProgress(0);
        String a = ((RadioButton) ui.algo.getSelectedToggle()).getText().split(" ")[0];
        status("Solving with " + a + "...", PuzzleUI.YELLOW);
        ui.solveBtn.setDisable(true); ui.shuffleBtn.setDisable(true);
        int[] snap = board.clone();
        Task<SolverResult> t = new Task<>() {
            protected SolverResult call() {
                return a.equals("BFS") ? BFSSolver.solve(snap) : AStarSolver.solve(snap);
            }
        };
        t.setOnSucceeded(e -> {
            SolverResult r = t.getValue();
            ui.solveBtn.setDisable(false); ui.shuffleBtn.setDisable(false);
            if (r.solved) {
                path = r.path; ui.stepBtn.setDisable(false); ui.autoBtn.setDisable(false);
                ui.nodes.setText("Nodes: " + r.nodesExplored);
                ui.time.setText("Time:  " + r.timeMs + " ms");
                ui.steps.setText("Steps: " + r.steps() + "  (step 0 / " + r.steps() + ")");
                status("Solved in " + r.steps() + " steps!", PuzzleUI.GREEN);
            } else status("No solution found!", "red");
        });
        t.setOnFailed(e -> { ui.solveBtn.setDisable(false); ui.shuffleBtn.setDisable(false); status("Error!", "red"); });
        new Thread(t).start();
    }

    private void doStep() {
        if (path == null || step >= path.size() - 1) return;
        board = path.get(++step).getBoard(); draw(board);
        int tot = path.size() - 1;
        ui.steps.setText("Steps: " + tot + "  (step " + step + " / " + tot + ")");
        ui.bar.setProgress((double) step / tot);
        if (step >= tot) { status("Goal reached!", PuzzleUI.GREEN); ui.autoBtn.setDisable(true); ui.stepBtn.setDisable(true); }
    }

    private void doAuto() {
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) { stopAuto(); return; }
        ui.autoBtn.setText("PAUSE");
        timeline = new Timeline(new KeyFrame(Duration.millis(400), e -> { doStep(); if (step >= path.size()-1) stopAuto(); }));
        timeline.setCycleCount(Animation.INDEFINITE); timeline.play();
    }

    private void stopAuto() { if (timeline != null) timeline.stop(); ui.autoBtn.setText("AUTO PLAY"); }

    private void doReset() {
        stopAuto(); board = new int[]{1,2,3,4,5,6,7,8,0}; path = null; step = 0;
        ui.stepBtn.setDisable(true); ui.autoBtn.setDisable(true); ui.bar.setProgress(0);
        ui.steps.setText("Steps: -"); ui.nodes.setText("Nodes: -"); ui.time.setText("Time:  -");
        draw(board); status("Reset to goal state", PuzzleUI.CYAN);
    }

    // ── Render
    private void draw(int[] b) {
        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) {
            int v = b[r*3+c]; Label l = ui.tiles[r][c];
            if (v == 0) {
                l.setText(""); l.setStyle("-fx-background-color:" + PuzzleUI.BG + "cc; -fx-border-color:" + PuzzleUI.GREY +
                        "44; -fx-border-width:2; -fx-border-radius:10; -fx-background-radius:10; -fx-border-style:dashed");
            } else {
                boolean goal = v == PuzzleState.GOAL[r*3+c];
                l.setText(String.valueOf(v));
                l.setStyle("-fx-background-color:" + (goal ? PuzzleUI.GOAL_T : PuzzleUI.TILE) + "; -fx-text-fill:" + PuzzleUI.FG +
                        "; -fx-border-color:" + (goal ? PuzzleUI.GREEN : PuzzleUI.CYAN) + "99; -fx-border-width:2; -fx-border-radius:10; -fx-background-radius:10");
                ScaleTransition st = new ScaleTransition(Duration.millis(120), l);
                st.setFromX(0.85); st.setFromY(0.85); st.setToX(1); st.setToY(1); st.play();
            }
        }
    }

    // ── Helpers
    private void status(String t, String col) {
        ui.status.setText(t);
        ui.status.setStyle("-fx-font-family:'Courier New'; -fx-font-size:12px; -fx-text-fill:" + col);
    }

    public static void main(String[] args) { launch(args); }
}