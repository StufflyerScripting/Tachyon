import java.io.IOException;
import java.util.List;

public class Phase1Demo {
    public static void main(String[] args) throws IOException {
        Phase1Solver.init();

        CubieCube cube = Phase1Solver.applyScramble("U2");
        List<String> solution = Phase1Solver.solve(cube, 100);
        if (solution != null)
        {
            System.out.println(String.join(" ", solution));
        }
    }
}
