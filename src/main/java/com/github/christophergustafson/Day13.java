package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.findLongs;
import static com.github.christophergustafson.Utils.readInputFile;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Day13 {

    public static void main(String[] args) {

        String testInput = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
            
            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
            
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
            
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279""";
        String realInput = readInputFile("day13.txt");

        long part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        long part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Real = part2(parseInputPart2(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<ClawMachineProblem> parseInput(String input) {
        return Arrays.stream(input.split("\n\n"))
            .map(problem -> {
                String[] lines = problem.split("\n");
                List<Long> buttonA = findLongs(lines[0]);
                List<Long> buttonB = findLongs(lines[1]);
                List<Long> price = findLongs(lines[2]);
                return new ClawMachineProblem(
                    buttonA.getFirst(),
                    buttonA.getLast(),
                    buttonB.getFirst(),
                    buttonB.getLast(),
                    price.getFirst(),
                    price.getLast()
                );
            }).toList();
    }

    static List<ClawMachineProblem> parseInputPart2(String input) {
        return Arrays.stream(input.split("\n\n"))
            .map(problem -> {
                String[] lines = problem.split("\n");
                List<Long> buttonA = findLongs(lines[0]);
                List<Long> buttonB = findLongs(lines[1]);
                List<Long> price = findLongs(lines[2]);
                return new ClawMachineProblem(
                    buttonA.getFirst(),
                    buttonA.getLast(),
                    buttonB.getFirst(),
                    buttonB.getLast(),
                    price.getFirst() + 10000000000000L,
                    price.getLast() + 10000000000000L
                );
            }).toList();
    }

    static long part1(List<ClawMachineProblem> input) {
        long result = 0;

        // Brute force, works when we can assume at most 100 button presses for each button.
        for (ClawMachineProblem problem : input) {
            int lowestCost = Integer.MAX_VALUE;
            for (int a = 0; a < 101; a++) {
                for (int b = 0; b < 101; b++) {
                    if (a * problem.aXIncrease + b * problem.bXIncrease == problem.priceX
                        && a * problem.aYIncrease + b * problem.bYIncrease == problem.priceY) {
                        if (a * 3 + b < lowestCost) {
                            lowestCost = a * 3 + b;
                        }
                    }
                }
            }
            if (lowestCost != Integer.MAX_VALUE) {
                result += lowestCost;
            }
        }
        return result;
    }

    static long part2(List<ClawMachineProblem> input) {
        long result = 0;

        for (ClawMachineProblem problem : input) {
            // We sometimes get rounding errors here, round the result and verify it is a solution.
            double pq = pq(problem);

            long a = Math.round(pq);
            long b = b(problem, a);
            if (isSolution(problem, a, b)) {
                result += a*3 + b(problem, a);
            }
        }
        return result;
    }

    static double pq(ClawMachineProblem p) {
        double t = p.priceY - (double) (p.bYIncrease * p.priceX) /p.bXIncrease;
        double n = p.aYIncrease - (double) (p.bYIncrease * p.aXIncrease) / p.bXIncrease;
        return t/n;
    }

    static boolean isSolution(ClawMachineProblem p, long a, long b) {
        return a * p.aXIncrease + b * p.bXIncrease == p.priceX && a * p.aYIncrease + b * p.bYIncrease == p.priceY;
    }

    static long b(ClawMachineProblem p, long a) {
        return (p.priceX - a * p.aXIncrease)/p.bXIncrease;
    }

    record ClawMachineProblem(
        long aXIncrease,
        long aYIncrease,
        long bXIncrease,
        long bYIncrease,
        long priceX,
        long priceY
    ) { }
}