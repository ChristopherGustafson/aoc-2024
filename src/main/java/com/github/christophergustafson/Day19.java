package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class Day19 {

    public static void main(String[] args) {

        String testInput = """
            r, wr, b, g, bwu, rb, gb, br
            
            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb""";
        String realInput = readInputFile("day19.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        long part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static ParsedInput parseInput(String input) {
        String[] split = input.split("\n\n");
        String[] patterns = split[0].split(", ");
        String[] designs = split[1].split("\n");
        return new ParsedInput(patterns, designs);
    }

    record ParsedInput(String[] patterns, String[] designs) {

    }

    static int part1(ParsedInput input) {
        Map<Integer, List<String>> patterns = patternsByLength(input.patterns);
        Set<String> isPossible = new HashSet<>();
        Set<String> isImpossible = new HashSet<>();

        int result = 0;
        for (String design : input.designs) {
            if (solve(design, patterns, isPossible, isImpossible)) {
                result++;
            }
        }

        return result;
    }

    static Map<Integer, List<String>> patternsByLength(String[] patterns) {
        return Arrays.stream(patterns).collect(Collectors.groupingBy(String::length));
    }

    static boolean solve(
        String design,
        Map<Integer, List<String>> patterns,
        Set<String> isPossible,
        Set<String> isImpossible
    ) {

        if (design.isEmpty()) {
            return true;
        }

        if (isPossible.contains(design)) {
            return true;
        }
        if (isImpossible.contains(design)) {
            return false;
        }

        for (int i = design.length(); i > 0; i--) {
            for (String pattern : patterns.getOrDefault(i, Collections.emptyList())) {
                if (design.contains(pattern)) {
                    String[] subDesigns = design.split(pattern, 2);
                    if (solve(subDesigns[0], patterns, isPossible, isImpossible) && solve(
                        subDesigns[1],
                        patterns,
                        isPossible,
                        isImpossible
                    )) {
                        isPossible.add(design);
                        return true;
                    }
                }
            }
        }
        isImpossible.add(design);
        return false;
    }

    static long part2(ParsedInput input) {
        Map<Integer, List<String>> patterns = patternsByLength(input.patterns);
        Map<String, Long> counts = new HashMap<>();

        long result = 0;
        for (String design : input.designs) {
            result += solveCount(design, patterns, counts);
        }

        return result;
    }

    static long solveCount(
        String design,
        Map<Integer, List<String>> patterns,
        Map<String, Long> counts
    ) {

        if (design.isEmpty()) {
            return 1;
        }

        if (counts.containsKey(design)) {
            return counts.get(design);
        }

        long count = 0;
        for (int i = design.length(); i > 0; i--) {
            for (String pattern : patterns.getOrDefault(i, Collections.emptyList())) {
                if (design.startsWith(pattern)) {
                    String rest = design.replaceFirst(pattern, "");
                    long restCount = solveCount(rest, patterns, counts);
                    count += restCount;
                }
            }
        }
        counts.put(design, count);
        return count;
    }

}
