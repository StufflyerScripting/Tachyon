import java.io.*;
import java.util.*;

public class Phase1Solver {

    static final int N_CO = 2187;
    static final int N_EO = 2048;
    static final int N_SLICE = 495;
    static final int N_MOVES = 18;

    static int[][] coMove;
    static int[][] eoMove;
    static int[][] sliceMove;
    static byte[] prunePhase1;

    static String[] moveNames = {
            "U", "U2", "U'",
            "R", "R2", "R'",
            "F", "F2", "F'",
            "D", "D2", "D'",
            "L", "L2", "L'",
            "B", "B2", "B'"
    };

    static int[] solution;
    static int solutionLength;
    static boolean found;
    static long nodesVisited;

    public static void init() throws IOException {
        CubeCoords.init();

        coMove = new int[N_CO][N_MOVES];
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream("coMove.bin")))) {
            for (int i = 0; i < N_CO; i++) {
                for (int j = 0; j < N_MOVES; j++) {
                    coMove[i][j] = in.readInt();
                }
            }
        }

        eoMove = new int[N_EO][N_MOVES];
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream("eoMove.bin")))) {
            for (int i = 0; i < N_EO; i++) {
                for (int j = 0; j < N_MOVES; j++) {
                    eoMove[i][j] = in.readInt();
                }
            }
        }

        sliceMove = new int[N_SLICE][N_MOVES];
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream("sliceMove.bin")))) {
            for (int i = 0; i < N_SLICE; i++) {
                for (int j = 0; j < N_MOVES; j++) {
                    sliceMove[i][j] = in.readInt();
                }
            }
        }

        prunePhase1 = new byte[N_CO * N_EO];
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream("prunePhase1.bin")))) {
            in.readFully(prunePhase1);
        }
    }

    public static List<String> solve(CubieCube cube, int max) {
        int co = CubeCoords.GetCoordCorner(cube);
        int eo = CubeCoords.GetCoordEdge(cube);
        int slice = CubeCoords.GetCoordSlice(cube);

        boolean solved = phase1(cube);

        long total_time = 0;

        if (solved) {
            System.out.println("Cube is already in Phase 1, will return null solution.");
            return null;
        }

        int goalSlice = CubeCoords.getSlice();
        solution = new int[max];

        for (int depth = 0; depth <= max; depth++) {
            found = false;
            solutionLength = 0;
            nodesVisited = 0;

            long startTime = System.currentTimeMillis();
            search(co, eo, slice, goalSlice, 0, depth, -1);
            long elapsed = System.currentTimeMillis() - startTime;
            total_time += elapsed;

            if (found) {
                List<String> result = new ArrayList<>();
                for (int i = 0; i < solutionLength; i++) {
                    result.add(moveNames[solution[i]]);
                }
                System.out.println("Found solution in " + total_time + "ms.");
                return result;
            }
        }

        return null;
    }


    public static boolean phase1(CubieCube cube) {
        for (int co : cube.co) if (co != 0) return false;
        for (int eo : cube.eo) if (eo != 0) return false;
        boolean[] sliceEdgeInSlice = new boolean[4];
        for (int pos = 8; pos <= 11; pos++) {
            int edge = cube.ep[pos];
            if (edge >= 8 && edge <= 11) {
                sliceEdgeInSlice[edge - 8] = true;
            }
        }
        for (boolean b : sliceEdgeInSlice) if (!b) return false;
        return true;
    }

    private static void search(int co, int eo, int slice, int goal_slice, int depth, int max, int last) {
        if (found) return;

        nodesVisited++;

        if (co == 0 && eo == 0 && slice == goal_slice) {
            found = true;
            solutionLength = depth;
            return;
        }

        if (depth >= max) return;

        // Pruning using CO+EO only (slice is not in pruning table)
        int estimate = prunePhase1[co * N_EO + eo];
        if (depth + estimate > max) {
            return;
        }

        for (int move = 0; move < N_MOVES; move++) {
            if (last != -1 && last / 3 == move / 3) {
                continue;
            }

            solution[depth] = move;

            int newCO = coMove[co][move];
            int newEO = eoMove[eo][move];
            int newSlice = sliceMove[slice][move];

            search(newCO, newEO, newSlice, goal_slice, depth + 1, max, move);

            if (found) return;
        }
    }

    public static CubieCube applyScramble(String scrambleStr) {
        CubieCube cube = new CubieCube();
        String[] tokens = scrambleStr.trim().split("\\s+");

        for (String token : tokens) {
            CubieCube move = switch (token) {
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
                default -> throw new IllegalArgumentException("Unknown move: " + token);
            };
            cube = cube.multiply(move);
        }

        return cube;
    }
}
