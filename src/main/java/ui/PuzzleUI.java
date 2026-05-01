package ui;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

/**
 * Builds and owns every UI node.
 * PuzzleApp only calls build(stage) and then reads the public fields.
 */
public class PuzzleUI {

    public static final String BG="#0d0d14";
    static final String CARD="#1a1a2e";
    public static final String TILE="#252540";
    public static final String GOAL_T="#1e3a5f";
    public static final String FG="#e8e8f0";
    public static final String CYAN="#4fc3f7";
    static final String PURPLE="#7c4dff";
    public static final String GREEN="#69f0ae";
    public static final String YELLOW="#ffd740";
    public static final String GREY="#6b6b8a";

    // ── Public nodes read by PuzzleApp ─────────────────────────────────────
    public final Label[][]  tiles  = new Label[3][3];
    public Label   status, steps, nodes, time;
    public Button  shuffleBtn, solveBtn, stepBtn, autoBtn, resetBtn;
    public ToggleGroup algo;
    public ProgressBar bar;

    // ── Build
    public void build(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG);
        root.setPadding(new Insets(22));

        root.setTop(buildTitle());
        BorderPane.setMargin(root.getTop(), new Insets(0, 0, 16, 0));
        root.setCenter(buildGrid());
        root.setRight(buildPanel());

        stage.setScene(new Scene(root, 780, 560));
        stage.setTitle("8-Puzzle Solver · BFS & A*");
        stage.setResizable(false);
        stage.show();
    }

    // ── Sections
    private VBox buildTitle() {
        Label title = lbl("8-PUZZLE SOLVER", CYAN, 22);
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 22));
        return new VBox(4, title, lbl("BFS  ·  A*  SEARCH ALGORITHMS", GREY, 11));
    }

    private VBox buildGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(6); grid.setVgap(6); grid.setAlignment(Pos.CENTER);
        for (int r = 0; r < 3; r++) for (int c = 0; c < 3; c++) {
            Label l = new Label(); l.setPrefSize(110, 110); l.setAlignment(Pos.CENTER);
            l.setFont(Font.font("Courier New", FontWeight.BOLD, 36));
            tiles[r][c] = l; grid.add(l, c, r);
        }
        steps = lbl("Steps: –", FG, 13);
        bar   = new ProgressBar(0); bar.setPrefWidth(340);
        bar.setStyle("-fx-accent:" + CYAN + "; -fx-background-color:" + TILE);

        VBox inner = new VBox(14, grid, steps, bar);
        inner.setAlignment(Pos.CENTER); inner.setPadding(new Insets(20));
        StackPane card = new StackPane(inner);
        card.setStyle("-fx-background-color:" + CARD + "; -fx-background-radius:14; " +
                "-fx-border-color:" + PURPLE + "33; -fx-border-radius:14; -fx-border-width:1");
        card.setPadding(new Insets(20));
        VBox wrapper = new VBox(card); wrapper.setAlignment(Pos.CENTER);
        return wrapper;
    }

    private VBox buildPanel() {
        algo = new ToggleGroup();
        RadioButton bfs   = radio("BFS  (Breadth-First)");
        RadioButton astar = radio("A*   (Manhattan Dist.)");
        bfs.setToggleGroup(algo); astar.setToggleGroup(algo); astar.setSelected(true);

        shuffleBtn = btn("⟳  SHUFFLE",    YELLOW);
        solveBtn   = btn("▶  SOLVE",      CYAN);
        stepBtn    = btn("→  STEP",       PURPLE);
        autoBtn    = btn("⏩  AUTO PLAY", GREEN);
        resetBtn   = btn("↺  RESET",      GREY);
        stepBtn.setDisable(true); autoBtn.setDisable(true);

        nodes  = lbl("Nodes: –", FG, 12);
        time   = lbl("Time:  –", FG, 12);
        status = lbl("Ready",    CYAN, 12);

        VBox panel = new VBox(14,
                section("ALGORITHM",  bfs, astar),
                section("CONTROLS",   shuffleBtn, solveBtn, stepBtn, autoBtn, resetBtn),
                section("STATISTICS", nodes, time, status));
        panel.setMinWidth(220); panel.setMaxWidth(220);
        return panel;
    }

    // ── Widget factories
    public static Label lbl(String t, String col, int sz) {
        Label l = new Label(t);
        l.setStyle("-fx-font-family:'Courier New'; -fx-font-size:" + sz + "px; -fx-text-fill:" + col);
        return l;
    }

    private static RadioButton radio(String t) {
        RadioButton r = new RadioButton(t);
        r.setStyle("-fx-font-family:'Courier New'; -fx-font-size:12px; -fx-text-fill:" + FG);
        return r;
    }

    private static Button btn(String t, String col) {
        Button b = new Button(t); b.setPrefWidth(196);
        String base = "-fx-font-family:'Courier New'; -fx-font-size:12px; -fx-font-weight:bold; " +
                "-fx-border-radius:6; -fx-background-radius:6; -fx-cursor:hand; -fx-border-width:1";
        b.setStyle(base + "; -fx-background-color:" + col + "22; -fx-text-fill:" + col + "; -fx-border-color:" + col + "88");
        b.setOnMouseEntered(e -> b.setStyle(base + "; -fx-background-color:" + col + "44; -fx-text-fill:" + col + "; -fx-border-color:" + col));
        b.setOnMouseExited (e -> b.setStyle(base + "; -fx-background-color:" + col + "22; -fx-text-fill:" + col + "; -fx-border-color:" + col + "88"));
        return b;
    }

    private static VBox section(String title, javafx.scene.Node... kids) {
        Label h = new Label(title);
        h.setStyle("-fx-font-family:'Courier New'; -fx-font-size:10px; -fx-text-fill:" + GREY);
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color:" + PURPLE + "55");
        VBox box = new VBox(6, h, sep); box.getChildren().addAll(kids);
        box.setStyle("-fx-background-color:" + CARD + "; -fx-background-radius:10; " +
                "-fx-border-color:#ffffff11; -fx-border-radius:10; -fx-border-width:1");
        box.setPadding(new Insets(12)); return box;
    }
}