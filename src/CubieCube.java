public class CubieCube {
    int[] cp = {0, 1, 2, 3, 4, 5, 6, 7};
    int[] co = {0, 0, 0, 0, 0, 0, 0, 0};
    int[] ep = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    int[] eo = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public CubieCube() {}

    public CubieCube(int[] cp, int[] co, int[] ep, int[] eo) {
        this.cp = cp.clone();
        this.co = co.clone();
        this.ep = ep.clone();
        this.eo = eo.clone();
    }

    public CubieCube multiply(CubieCube b) {
        CubieCube c = new CubieCube();

        for (int i = 0; i < 8; i++) {
            c.cp[i] = this.cp[b.cp[i]];
            c.co[i] = (this.co[b.cp[i]] + b.co[i]) % 3;
        }

        for (int i = 0; i < 12; i++) {
            c.ep[i] = this.ep[b.ep[i]];
            c.eo[i] = (this.eo[b.ep[i]] + b.eo[i]) % 2;
        }

        return c;
    }
}
