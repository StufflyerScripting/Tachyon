import java.io.*;
import java.util.*;

public class Phase1Solver {
    static final int N_CO = 2187;
    static final int N_EO = 2048;
    static final int N_SLICE = 495;
    static final int N_MOVES = 18;

    static int[][] coMove, eoMove, sliceMove;
    static byte[] prunePhase1;
    static String[] moveNames = {"U","U2","U'","R","R2","R'","F","F2","F'","D","D2","D'","L","L2","L'","B","B2","B'"};
    static CubieCube[] ALL_MOVES;
    static int[] solution;
    static int length;
    static boolean found;
    static CubieCube startCube;

    public static void init() throws IOException {
        CubeCoords.init();
        coMove = loadTable("coMove.bin", N_CO);
        eoMove = loadTable("eoMove.bin", N_EO);
        sliceMove = loadTable("sliceMove.bin", N_SLICE);

        ALL_MOVES = new CubieCube[]{
                MoveTables.U, MoveTables.U2, MoveTables.UP,
                MoveTables.R, MoveTables.R2, MoveTables.RP,
                MoveTables.F, MoveTables.F2, MoveTables.FP,
                MoveTables.D, MoveTables.D2, MoveTables.DP,
                MoveTables.L, MoveTables.L2, MoveTables.LP,
                MoveTables.B, MoveTables.B2, MoveTables.BP
        };

        prunePhase1 = new byte[N_CO * N_EO];
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream("prunePhase1.bin")))) {
            in.readFully(prunePhase1);
        }
    }

    private static int[][] loadTable(String name, int rows) throws IOException {
        int[][] table = new int[rows][N_MOVES];
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(name)))) {
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < N_MOVES; j++) table[i][j] = in.readInt();
        }
        return table;
    }

    public static List<String> solve(CubieCube cube, int max) {
        if (phase1(cube)) return null;

        startCube = cube;
        int co = CubeCoords.GetCoordCorner(cube);
        int eo = CubeCoords.GetCoordEdge(cube);
        int slice = CubeCoords.GetCoordSlice(cube);
        int goalSlice = CubeCoords.getSlice();

        solution = new int[max];
        for (int depth = 0; depth <= max; depth++) {
            found = false;
            length = 0;
            search(co, eo, slice, goalSlice, 0, depth, -1);
            if (found) {
                List<String> result = new ArrayList<>();
                for (int i = 0; i < length; i++) result.add(moveNames[solution[i]]);
                return result;
            }
        }
        return null;
    }

    public static boolean phase1(CubieCube cube) {
        for (int co : cube.co) if (co != 0) return false;
        for (int eo : cube.eo) if (eo != 0) return false;
        for (int pos = 8; pos <= 11; pos++) if (cube.ep[pos] != pos) return false;
        return true;
    }

    private static void search(int co, int eo, int slice, int goalSlice, int depth, int max, int last) {
        if (found) return;

        if (co == 0 && eo == 0 && slice == goalSlice) {
            CubieCube verify = new CubieCube();
            verify.cp = startCube.cp.clone();
            verify.co = startCube.co.clone();
            verify.ep = startCube.ep.clone();
            verify.eo = startCube.eo.clone();

            for (int i = 0; i < depth; i++) {
                verify = verify.multiply(ALL_MOVES[solution[i]]);
            }

            if (phase1(verify)) {
                found = true;
                length = depth;
                return;
            }
        }

        if (depth >= max) return;

        int estimate = prunePhase1[co * N_EO + eo];
        if (depth + estimate > max) return;

        for (int move = 0; move < N_MOVES; move++) {
            if (last != -1 && last / 3 == move / 3) continue;

            solution[depth] = move;
            search(coMove[co][move], eoMove[eo][move], sliceMove[slice][move],
                    goalSlice, depth + 1, max, move);

            if (found) return;
        }
    }

    public static CubieCube applyScramble(String scrambleStr) {
        CubieCube cube = new CubieCube();
        if (scrambleStr.isEmpty()) return cube;
        for (String token : scrambleStr.trim().split("\\s+")) {
            for(int i=0; i<moveNames.length; i++) {
                if(moveNames[i].equals(token)) {
                    cube = cube.multiply(ALL_MOVES[i]);
                    break;
                }
            }
        }
        return cube;
    }
}
