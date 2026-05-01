package algorithms;

import puzzle.PuzzleState;
import puzzle.SolverResult;

import java.util.*;

public class BFSSolver {
    public static SolverResult solve(int[] initial) {
        long start = System.currentTimeMillis();
        PuzzleState root = new PuzzleState(initial, null, null, 0);

        if (root.isGoal()) {
            return new SolverResult("BFS", root.getPath(), 1, 0);
        }

        Queue<PuzzleState> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(root);
        visited.add(root.toKey());
        int explored = 0;

        while (!queue.isEmpty()) {
            PuzzleState cur = queue.poll();
            explored++;

            for (PuzzleState neighbor : cur.getNeighbors()) {
                String key = neighbor.toKey();
                if (!visited.contains(key)) {
                    visited.add(key);
                    if (neighbor.isGoal()) {
                        long elapsed = System.currentTimeMillis() - start;
                        return new SolverResult("BFS", neighbor.getPath(), explored, elapsed);
                    }
                    queue.add(neighbor);
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new SolverResult("BFS", null, explored, elapsed);
    }
}
