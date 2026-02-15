import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Solver.init();
        String scramble = "F2 R2 U' R2 B2 D B2 D' L2 R2 U' B2 L B D' U2 B' L D R2 U";

        List<String> computed = Solver.compute(scramble); // returns the solution as a string array
        assert computed != null;
        System.out.println(String.join(" ", computed));
    }
}
