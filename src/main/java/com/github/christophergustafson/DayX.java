package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.readInputFile;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class DayX {

    public static void main(String[] args) {

        String testInput = """
            add test input""";
        String realInput = readInputFile("dayX.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<Integer> parseInput(String input) {
        return new ArrayList<>();
    }

    static int part1(List<Integer> input) {
        return 0;
    }

    static int part2(List<Integer> input) {
        return 0;
    }
}
