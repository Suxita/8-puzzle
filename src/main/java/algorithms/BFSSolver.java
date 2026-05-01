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

        Queue<PuzzleState> openList   = new LinkedList<>();  // discovered, not yet processed
        Set<String>        closedList = new HashSet<>();      // already processed

        openList.add(root);
        closedList.add(root.toKey());
        int explored = 0;

        while (!openList.isEmpty()) {
            PuzzleState cur = openList.poll();   // take from front of OPEN LIST
            explored++;

            for (PuzzleState neighbor : cur.getNeighbors()) {
                String key = neighbor.toKey();
                if (!closedList.contains(key)) {   // NOT in CLOSED LIST?
                    closedList.add(key);            // → add to CLOSED LIST
                    if (neighbor.isGoal()) {
                        long elapsed = System.currentTimeMillis() - start;
                        return new SolverResult("BFS", neighbor.getPath(), explored, elapsed);
                    }
                    openList.add(neighbor);         // → add to OPEN LIST
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new SolverResult("BFS", null, explored, elapsed);
    }
}
