package logs;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LogInfo {

    private static final String PATH = "src/logs/logs.txt";

    public static void add(String scramble, List<String> solution, long time) throws IOException {
        List<String> lines = new ArrayList<>();

        Path path = Paths.get(PATH);
        if (Files.exists(path)) {
            lines = Files.readAllLines(path);
        }

        List<String> solves = new ArrayList<>();
        for (String line : lines) {
            if (line.contains(";")) {
                solves.add(line);
            }
        }
        solves.add(scramble + ";" + solution + ";" + time);

        String header = buildHeader(solves);

        List<String> newFile = new ArrayList<>();
        newFile.add(header);
        newFile.add("--------------------------------\n");

        newFile.addAll(solves);

        Files.write(path, newFile);
    }

    private static String buildHeader(List<String> solves) {

        if (solves.isEmpty()) {
            return "TACHYON SOLVE LOGS & STATS\nNo solves yet.";
        }

        int total = solves.size();

        long bestTime = Long.MAX_VALUE;
        long worstTime = 0;
        long sumTime = 0;

        int shortest = Integer.MAX_VALUE;
        int longest = 0;
        int sumLength = 0;

        double bestRatio = Double.MAX_VALUE;
        double sumRatio = 0;

        for (String s : solves) {

            String[] parts = s.split(";");

            String listPart = parts[1].trim();
            listPart = listPart.substring(1, listPart.length() - 1);
            int length = listPart.isBlank() ? 0 : listPart.split(",").length;

            long time = Long.parseLong(parts[2].trim());

            sumTime += time;
            bestTime = Math.min(bestTime, time);
            worstTime = Math.max(worstTime, time);
            
            sumLength += length;
            shortest = Math.min(shortest, length);
            longest = Math.max(longest, length);
            
            if (length > 0) {
                double ratio = (double) time / length;
                sumRatio += ratio;
                bestRatio = Math.min(bestRatio, ratio);
            }
        }

        long avgTime = sumTime / total;
        double avgLength = (double) sumLength / total;
        double avgRatio = sumRatio / total;

        return "TACHYON SOLVE LOGS & STATS\n"
                + "Total solves: " + total + "\n\n"

                + "---- TIME ----\n"
                + "AVG: " + avgTime + "ms\n"
                + "PB: " + bestTime + "ms\n"
                + "PW: " + worstTime + "ms\n\n"

                + "---- SOLUTION LENGTH ----\n"
                + "AVG: " + String.format("%.2f", avgLength) + " moves\n"
                + "PB: " + shortest + " moves\n"
                + "PW: " + longest + " moves\n\n"

                + "---- EFFICIENCY (ms per move) ----\n"
                + "BR: " + String.format("%.2f", bestRatio) + "\n"
                + "AVGR: " + String.format("%.2f", avgRatio);
    }
}
