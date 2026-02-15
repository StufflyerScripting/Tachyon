public final class MoveTables {
    public static final CubieCube U, R, F, D, L, B;
    public static final CubieCube U2, R2, F2, D2, L2, B2;
    public static final CubieCube UP, RP, FP, DP, LP, BP;

    static {
        U = new CubieCube(
                new int[]{3, 0, 1, 2, 4, 5, 6, 7}, new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{3, 0, 1, 2, 4, 5, 6, 7, 8, 9, 10, 11}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        R = new CubieCube(
                new int[]{4, 1, 2, 0, 7, 5, 6, 3}, new int[]{2, 0, 0, 1, 1, 0, 0, 2},
                new int[]{8, 1, 2, 3, 11, 5, 6, 7, 4, 9, 10, 0}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        F = new CubieCube(
                new int[]{1, 5, 2, 3, 0, 4, 6, 7}, new int[]{1, 2, 0, 0, 2, 1, 0, 0},
                new int[]{0, 9, 2, 3, 4, 8, 6, 7, 1, 5, 10, 11}, new int[]{0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0});
        D = new CubieCube(
                new int[]{0, 1, 2, 3, 5, 6, 7, 4}, new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                new int[]{0, 1, 2, 3, 5, 6, 7, 4, 8, 9, 10, 11}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        L = new CubieCube(
                new int[]{0, 2, 6, 3, 4, 1, 5, 7}, new int[]{0, 1, 2, 0, 0, 2, 1, 0},
                new int[]{0, 1, 10, 3, 4, 5, 9, 7, 8, 2, 6, 11}, new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        B = new CubieCube(
                new int[]{0, 1, 3, 7, 4, 5, 2, 6}, new int[]{0, 0, 1, 2, 0, 0, 2, 1},
                new int[]{0, 1, 2, 11, 4, 5, 6, 10, 8, 9, 3, 7}, new int[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1});

        U2 = U.multiply(U); UP = U2.multiply(U);
        R2 = R.multiply(R); RP = R2.multiply(R);
        F2 = F.multiply(F); FP = F2.multiply(F);
        D2 = D.multiply(D); DP = D2.multiply(D);
        L2 = L.multiply(L); LP = L2.multiply(L);
        B2 = B.multiply(B); BP = B2.multiply(B);
    }
}
