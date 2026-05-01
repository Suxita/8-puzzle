package algorithms;

import puzzle.PuzzleState;
import puzzle.SolverResult;

import java.util.*;

public class AStarSolver {
    public static SolverResult solve(int[] initial) {
        long start = System.currentTimeMillis();
        PuzzleState root = new PuzzleState(initial, null, null, 0);

        if (root.isGoal()) {
            return new SolverResult("A*", root.getPath(), 1, 0);
        }

        PriorityQueue<PuzzleState> open = new PriorityQueue<>(
            Comparator.comparingInt(PuzzleState::getF)
        );
        Map<String, Integer> bestF = new HashMap<>();

        open.add(root);
        bestF.put(root.toKey(), root.getF());
        int explored = 0;

        while (!open.isEmpty()) {
            PuzzleState cur = open.poll();
            explored++;

            if (cur.isGoal()) {
                long elapsed = System.currentTimeMillis() - start;
                return new SolverResult("A*", cur.getPath(), explored, elapsed);
            }

            String curKey = cur.toKey();
            if (bestF.containsKey(curKey) && bestF.get(curKey) < cur.getF()) continue;

            for (PuzzleState neighbor : cur.getNeighbors()) {
                String key = neighbor.toKey();
                int f = neighbor.getF();
                if (!bestF.containsKey(key) || bestF.get(key) > f) {
                    bestF.put(key, f);
                    open.add(neighbor);
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new SolverResult("A*", null, explored, elapsed);
    }
}
