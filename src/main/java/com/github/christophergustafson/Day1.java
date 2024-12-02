package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.readInputFile;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day1 {

    public static void main(String[] args) {

        String testInput = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3""";
        List<String> testInputParsed = parseInput(testInput);

        String realInput = readInputFile("day1.txt");
        List<String> realInputParsed = parseInput(realInput);

        int part1Test = part1(testInputParsed);
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(realInputParsed);
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(testInputParsed);
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(realInputParsed);
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<String> parseInput(String input) {
        return Arrays.stream(input.split("\n")).toList();
    }

    static int part1(List<String> input) {
        List<Integer> list1 = input.stream().map(line -> Integer.parseInt(line.split(" {3}")[0])).sorted().toList();
        List<Integer> list2 = input.stream().map(line -> Integer.parseInt(line.split(" {3}")[1])).sorted().toList();

        int result = 0;
        for (int i = 0; i < list1.size(); i++) {
            result += abs(list1.get(i) - list2.get(i));
        }
        return result;
    }

    static int part2(List<String> input) {
        List<Integer> list1 = input.stream().map(line -> Integer.parseInt(line.split(" {3}")[0])).sorted().toList();
        Map<Integer, List<Integer>> list2 = input.stream()
            .map(line -> Integer.parseInt(line.split(" {3}")[1]))
            .collect(Collectors.groupingBy(e -> e));

        AtomicInteger result = new AtomicInteger();
        list1.forEach(element -> {
                int occurrences = list2.getOrDefault(element, new ArrayList<>()).size();
                result.set(result.get() + occurrences * element);
            }
        );
        return result.get();
    }


}
