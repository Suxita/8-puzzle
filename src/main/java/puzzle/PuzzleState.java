package puzzle;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PuzzleState {
    public static final int SIZE = 3;
    public static final int[] GOAL = {1, 2, 3, 4, 5, 6, 7, 8, 0};

    private final int[] board;
    private final PuzzleState parent;
    private final String move;
    private final int depth;
    private final int heuristic;

    public PuzzleState(int[] board, PuzzleState parent, String move, int depth) {
        this.board = board.clone();
        this.parent = parent;
        this.move = move;
        this.depth = depth;
        this.heuristic = manhattanDistance();
    }

    public int[] getBoard() { return board.clone(); }
    public int getF() { return depth + heuristic; }

    public boolean isGoal() {
        return Arrays.equals(board, GOAL);
    }

    private int manhattanDistance() {
        int dist = 0;
        for (int i = 0; i < 9; i++) {
            int val = board[i];
            if (val != 0) {
                int goalRow = (val - 1) / SIZE;
                int goalCol = (val - 1) % SIZE;
                int curRow = i / SIZE;
                int curCol = i % SIZE;
                dist += Math.abs(goalRow - curRow) + Math.abs(goalCol - curCol);
            }
        }
        return dist;
    }

    public int blankIndex() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) return i;
        }
        return -1;
    }

    public List<PuzzleState> getNeighbors() {
        List<PuzzleState> neighbors = new ArrayList<>();
        int bi = blankIndex();
        int row = bi / SIZE, col = bi % SIZE;

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] names = {"Down", "Up", "Right", "Left"}; // tile moves opposite to blank

        for (int d = 0; d < 4; d++) {
            int nr = row + dirs[d][0];
            int nc = col + dirs[d][1];
            if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                int ni = nr * SIZE + nc;
                int[] newBoard = board.clone();
                newBoard[bi] = newBoard[ni];
                newBoard[ni] = 0;
                neighbors.add(new PuzzleState(newBoard, this, names[d], depth + 1));
            }
        }
        return neighbors;
    }

    public String toKey() {
        return Arrays.toString(board);
    }

    public List<PuzzleState> getPath() {
        List<PuzzleState> path = new ArrayList<>();
        PuzzleState cur = this;
        while (cur != null) {
            path.addFirst(cur);
            cur = cur.parent;
        }
        return path;
    }

    public static boolean isSolvable(int[] board) {
        int inv = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) continue;
            for (int j = i + 1; j < 9; j++) {
                if (board[j] != 0 && board[i] > board[j]) inv++;
            }
        }
        return inv % 2 == 0;
    }
}
