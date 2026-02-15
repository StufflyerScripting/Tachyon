import java.util.*;

public class CubeCoords {

    static int[][] C = new int[13][5];

    public static void init() {
        for (int n = 0; n <= 12; n++) {
            C[n][0] = 1;
            for (int k = 1; k <= Math.min(n, 4); k++) {
                C[n][k] = C[n - 1][k - 1] + C[n - 1][k];
            }
        }
    }

    public static int GetCoordCorner(CubieCube cube) {
        int co = 0;
        for (int i = 0; i < 7; i++) {
            co = 3 * co + cube.co[i];
        }
        return co;
    }

    public static void SetCO(CubieCube cube, int coCoord) {
        int sum = 0;
        for (int i = 6; i >= 0; i--) {
            cube.co[i] = coCoord % 3;
            sum += cube.co[i];
            coCoord /= 3;
        }
        cube.co[7] = (3 - sum % 3) % 3;
    }

    public static int GetCoordEdge(CubieCube cube) {
        int eo = 0;
        for (int i = 0; i < 11; i++) {
            eo = 2 * eo + cube.eo[i];
        }
        return eo;
    }

    public static void SetEO(CubieCube cube, int eoCoord) {
        int sum = 0;
        for (int i = 10; i >= 0; i--) {
            cube.eo[i] = eoCoord % 2;
            sum += cube.eo[i];
            eoCoord /= 2;
        }
        cube.eo[11] = (2 - sum % 2) % 2;
    }

    public static int GetCoordSlice(CubieCube cube) {
        int[] slicePositions = new int[4];
        int idx = 0;
        for (int i = 0; i < 12; i++) {
            if (cube.ep[i] >= 8 && cube.ep[i] <= 11) {
                slicePositions[idx++] = i;
            }
        }
        int coord = 0;
        for (int i = 0; i < 4; i++) {
            coord += C[slicePositions[i]][i + 1];
        }
        return coord;
    }

    public static void SetSlice(CubieCube cube, int coord) {
        int[] positions = new int[4];
        int x = coord;
        for (int i = 3; i >= 0; i--) {
            int p = i;
            while (p < 11 && C[p + 1][i + 1] <= x) p++;
            positions[i] = p;
            x -= C[p][i + 1];
        }

        boolean[] used = new boolean[12];
        int[] sliceEdges = {8, 9, 10, 11};
        for (int i = 0; i < 4; i++) {
            cube.ep[positions[i]] = sliceEdges[i];
            used[positions[i]] = true;
        }

        int[] non_slice = {0, 1, 2, 3, 4, 5, 6, 7};
        int index = 0;
        for (int i = 0; i < 12; i++) {
            if (!used[i]) {
                cube.ep[i] = non_slice[index++];
            }
        }
    }
    public static int getSlice() {
        return GetCoordSlice(new CubieCube());
    }
}
