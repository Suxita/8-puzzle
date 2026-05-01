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

        PriorityQueue<PuzzleState> openList   = new PriorityQueue<>(  // discovered, ordered by f = g + h
            Comparator.comparingInt(PuzzleState::getF)
        );
        Map<String, Integer> closedList = new HashMap<>();             // best f-score seen per state

        openList.add(root);
        closedList.put(root.toKey(), root.getF());
        int explored = 0;

        while (!openList.isEmpty()) {
            PuzzleState cur = openList.poll();   // take best state from OPEN LIST
            explored++;

            if (cur.isGoal()) {
                long elapsed = System.currentTimeMillis() - start;
                return new SolverResult("A*", cur.getPath(), explored, elapsed);
            }

            String curKey = cur.toKey();
            if (closedList.containsKey(curKey) && closedList.get(curKey) < cur.getF()) continue;

            for (PuzzleState neighbor : cur.getNeighbors()) {
                String key = neighbor.toKey();
                int f = neighbor.getF();
                if (!closedList.containsKey(key) || closedList.get(key) > f) {
                    closedList.put(key, f);   // update CLOSED LIST with best f
                    openList.add(neighbor);   // → add to OPEN LIST
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new SolverResult("A*", null, explored, elapsed);
    }
}
