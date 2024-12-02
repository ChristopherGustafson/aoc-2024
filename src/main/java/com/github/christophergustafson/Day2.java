package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.parseLinesOfIntegers;
import static com.github.christophergustafson.Utils.readInputFile;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

public class Day2 {

    public static void main(String[] args) {

        String testInput = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9""";
        String realInput = readInputFile("day2.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<List<Integer>> parseInput(String input) {
        return parseLinesOfIntegers(input);
    }

    private static int part1(List<List<Integer>> reports) {
        int unsafeAmount = 0;
        for (List<Integer> report : reports) {
            boolean increasing = report.get(0) < report.get(1);
            int prevLevel = report.get(0);

            for (int j = 1; j < report.size(); j++) {
                if (prevLevel < report.get(j) && !increasing) {
                    unsafeAmount++;
                    break;
                }
                if (prevLevel > report.get(j) && increasing) {
                    unsafeAmount++;
                    break;
                }
                int difference = abs(prevLevel - report.get(j));
                if (difference > 3 || difference < 1) {
                    unsafeAmount++;
                    break;
                }
                prevLevel = report.get(j);
            }
        }

        return reports.size() - unsafeAmount;
    }

    private static int part2(List<List<Integer>> reports) {
        int unsafeAmount = 0;
        for (List<Integer> report : reports) {
            if (isUnsafe(report)) {
                unsafeAmount++;
            }
        }

        return reports.size() - unsafeAmount;
    }

    private static boolean isUnsafe(List<Integer> report) {
        for (int dropIndex = 0; dropIndex < report.size(); dropIndex++) {
            List<Integer> trimmedReport = new ArrayList<>(report);
            trimmedReport.remove(dropIndex);
            boolean increasing = trimmedReport.getFirst() < trimmedReport.get(1);
            int prevLevel = trimmedReport.getFirst();
            boolean safe = true;
            for (int j = 1; j < trimmedReport.size(); j++) {
                int currentLevel = trimmedReport.get(j);
                if (prevLevel < trimmedReport.get(j) && !increasing) {
                    safe = false;
                    break;
                }
                if (prevLevel > currentLevel && increasing) {
                    safe = false;
                    break;
                }
                int difference = abs(prevLevel - currentLevel);
                if (difference > 3 || difference < 1) {
                    safe = false;
                    break;
                }
                prevLevel = currentLevel;
            }
            if (safe) { return false; }
        }
        return true;
    }

}
