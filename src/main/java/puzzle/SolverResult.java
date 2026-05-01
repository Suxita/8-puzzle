package puzzle;

import java.util.List;

public class SolverResult {
    public final List<PuzzleState> path;
    public final int nodesExplored;
    public final long timeMs;
    public final String algorithm;
    public final boolean solved;

    public SolverResult(String algorithm, List<PuzzleState> path, int nodesExplored, long timeMs) {
        this.algorithm = algorithm;
        this.path = path;
        this.nodesExplored = nodesExplored;
        this.timeMs = timeMs;
        this.solved = path != null && !path.isEmpty();
    }

    public int steps() {
        return path == null ? 0 : path.size() - 1;
    }
}
