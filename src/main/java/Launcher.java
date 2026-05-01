public class Launcher {
    public static void main(String[] args) {
        PuzzleApp.main(args);
    }
}
/*

Loop:
        1. Pick a state from Open List
  2. Move it to Closed List (mark as done)
  3. Check if it's the GOAL
  4. Generate its neighbors
  5. If neighbor NOT in Closed List → add to Open List
  6. Repeat

*/
