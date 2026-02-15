import java.io.*;
import java.util.*;

public class Phase2Solver {

    // Phase 2 Coordinate Sizes
    static final int N_CP = 40320;      // 8! Corner Permutations
    static final int N_UDEP = 40320;    // 8! UD-Edge Permutations
    static final int N_SLICEP = 24;     // 4! Slice Edge Permutations
    static final int N_MOVES_P2 = 10;

    // Move Tables
    static int[][] cpMove;
    static int[][] udepMove;
    static int[][] slicePMove;

    // Pruning Tables
    static byte[] pruneCP;   // CP + SliceP
    static byte[] pruneUDEP; // UDEP + SliceP

    static String[] moveNames = {
            "U", "U2", "U'",
            "D", "D2", "D'",
            "R2", "L2", "F2", "B2"
    };

    static int[] solution;
    static int solutionLength;
    static boolean found;

    public static void init() throws IOException {
        cpMove = loadMoveTable("cpMove.bin", N_CP);
        udepMove = loadMoveTable("udepMove.bin", N_UDEP);
        slicePMove = loadMoveTable("slicePMove.bin", N_SLICEP);
        pruneCP = loadPruningTable("pruneCPSlice.bin");
        pruneUDEP = loadPruningTable("pruneUDEPSlice.bin");
    }

    public static List<String> solve(CubieCube cube, int maxDepth) {
        found = false; // MUST RESET
        solutionLength = 0; // MUST RESET
        int cp = CubeCoords.GetCoordCP(cube);
        int udep = CubeCoords.GetCoordUDEP(cube);
        int sliceP = CubeCoords.GetCoordSliceP(cube);

        if (cp == 0 && udep == 0 && sliceP == 0) return new ArrayList<>();

        solution = new int[maxDepth + 1];

        for (int depth = 0; depth <= maxDepth; depth++) {
            search(cp, udep, sliceP, 0, depth, -1);

            if (found) {
                List<String> result = new ArrayList<>();
                for (int i = 0; i < solutionLength; i++) {
                    result.add(moveNames[solution[i]]);
                }
                return result;
            }
        }
        return null;
    }

    private static void search(int cp, int udep, int sliceP, int depth, int max, int last) {
        if (found) return;

        if (cp == 0 && udep == 0 && sliceP == 0) {
            found = true;
            solutionLength = depth;
            return;
        }

        if (depth >= max) return;

        int idxCP = cp * 24 + sliceP;
        int idxUDEP = udep * 24 + sliceP;
        int distCP = pruneCP[idxCP] & 0xFF;
        int distUDEP = pruneUDEP[idxUDEP] & 0xFF;
        int estimate = Math.max(distCP, distUDEP);

        if (depth + estimate > max) return;

        for (int m = 0; m < N_MOVES_P2; m++) {
            if (last != -1 && (last / 3 == m / 3) && (m < 6)) continue;

            solution[depth] = m;
            search(cpMove[cp][m], udepMove[udep][m], slicePMove[sliceP][m],
                    depth + 1, max, m);
            if (found) return;
        }
    }

    private static int[][] loadMoveTable(String fileName, int rows) throws IOException {
        int[][] table = new int[rows][N_MOVES_P2];
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < N_MOVES_P2; j++) {
                    table[i][j] = in.readInt();
                }
            }
        }
        return table;
    }

    private static byte[] loadPruningTable(String fileName) throws IOException {
        byte[] table = new byte[967680];
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName))) {
            int read = in.read(table);
            if (read != 967680) throw new IOException("Pruning table " + fileName + " is truncated!");
        }
        return table;
    }

}
