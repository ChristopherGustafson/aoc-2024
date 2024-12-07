package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.parseLinesOfStrings;
import static com.github.christophergustafson.Utils.readInputFile;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Day7 {

    public static void main(String[] args) {

        String testInput = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20""";
        String realInput = readInputFile("day7.txt");

        long part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        long part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        long part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static Equation[] parseInput(String input) {
        List<String> lines = parseLinesOfStrings(input);

        return lines.stream().map(line -> {
                String[] split = line.split(": ");
                long testValue = Long.parseLong(split[0]);
                List<Long> numbers = Arrays.stream(split[1].split(" ")).mapToLong(Long::parseLong).boxed().toList();
                return new Equation(testValue, numbers);
            }
        ).toArray(Equation[]::new);
    }

    record Equation(long testValue, List<Long> numbers) { }

    static long part1(Equation[] input) {
        long result = 0;
        for (Equation equation : input) {
            if (solve(equation.numbers, equation.testValue)) {
                result += equation.testValue;
            }
        }
        return result;
    }

    static boolean solve(List<Long> numbers, long finalResult) {
        return solve(numbers.subList(1, numbers.size()), numbers.getFirst(), finalResult);
    }

    static boolean solve(List<Long> numbers, long currentResult, long finalResult) {
        if (numbers.isEmpty()) {
            return currentResult == finalResult;
        }

        return solve(numbers.subList(1, numbers.size()), currentResult * numbers.getFirst(), finalResult)
            || solve(numbers.subList(1, numbers.size()), currentResult + numbers.getFirst(), finalResult);
    }

    static long part2(Day7.Equation[] input) {
        long result = 0;
        for (Equation equation : input) {
            if (solve2(equation.numbers, equation.testValue)) {
                result += equation.testValue;
            }
        }
        return result;
    }

    static boolean solve2(List<Long> numbers, long finalResult) {
        return solve2(numbers.subList(1, numbers.size()), numbers.getFirst(), finalResult);
    }

    static boolean solve2(List<Long> numbers, long currentResult, long finalResult) {
        if (numbers.isEmpty()) {
            return currentResult == finalResult;
        }

        return solve2(numbers.subList(1, numbers.size()), currentResult * numbers.getFirst(), finalResult)
            || solve2(numbers.subList(1, numbers.size()), currentResult + numbers.getFirst(), finalResult)
            || solve2(numbers.subList(1, numbers.size()), concatenateNumbers(currentResult, numbers.getFirst()), finalResult);
    }

    private static long concatenateNumbers(long a, long b) {
        return Long.parseLong("" + a + b);
    }
}
