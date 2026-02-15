import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solver {

    public static void init() throws IOException {
        Phase1Solver.init();
        Phase2Solver.init();
    }

    public static List<String> compute(String scramble) {
        CubieCube cube = Phase1Solver.applyScramble(scramble);
        long startTime = System.currentTimeMillis();
        List<String> phase1 = Phase1Solver.solve(cube, 12);

        if (phase1 == null) {
            return null;
        }

        for (String move : phase1) {
            cube = cube.multiply(getMove(move));
        }
        if (!Phase1Solver.phase1(cube)) {
            System.out.println("ERROR: Phase 1 solution didn't work!");
            return null;
        }
        List<String> phase2 = Phase2Solver.solve(cube, 18);

        if (phase2 == null || phase2.isEmpty()) {
            System.out.println("Already solved!");
            phase2 = new ArrayList<>();
        }

        List<String> complete = new ArrayList<>();
        complete.addAll(phase1);
        complete.addAll(phase2);
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Calculated solution in " + elapsed + "ms.");
        return simplify(complete);
    }

    static List<String> simplify(List<String> moves) {
        List<String> result = new ArrayList<>();

        for (String move : moves) {
            if (result.isEmpty()) {
                result.add(move);
                continue;
            }

            String last = result.get(result.size() - 1);

            // Same face?
            if (last.charAt(0) == move.charAt(0)) {

                int power1 = move_power(last);
                int power2 = move_power(move);

                int combined = (power1 + power2) % 4;

                result.remove(result.size() - 1);

                if (combined != 0) {
                    result.add(face_power(last.charAt(0), combined));
                }

            } else {
                result.add(move);
            }
        }

        return result;
    }

    static int move_power(String move) {
        if (move.endsWith("2")) return 2;
        if (move.endsWith("'")) return 3;
        return 1;
    }

    static String face_power(char face, int power) {
        return switch (power) {
            case 1 -> "" + face;
            case 2 -> face + "2";
            case 3 -> face + "'";
            default -> throw new IllegalStateException();
        };
    }


    static CubieCube getMove(String move) {
        return switch (move) {
            case "U" -> MoveTables.U;
            case "U2" -> MoveTables.U2;
            case "U'" -> MoveTables.UP;
            case "R" -> MoveTables.R;
            case "R2" -> MoveTables.R2;
            case "R'" -> MoveTables.RP;
            case "F" -> MoveTables.F;
            case "F2" -> MoveTables.F2;
            case "F'" -> MoveTables.FP;
            case "D" -> MoveTables.D;
            case "D2" -> MoveTables.D2;
            case "D'" -> MoveTables.DP;
            case "L" -> MoveTables.L;
            case "L2" -> MoveTables.L2;
            case "L'" -> MoveTables.LP;
            case "B" -> MoveTables.B;
            case "B2" -> MoveTables.B2;
            case "B'" -> MoveTables.BP;
            default -> throw new IllegalArgumentException("Unknown move: " + move);
        };
    }
}
