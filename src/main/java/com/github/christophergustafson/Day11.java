package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class Day11 {

    public static void main(String[] args) {

        String testInput = """
            125 17""";
        String realInput = readInputFile("day11.txt");

        long part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        long part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        long part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<Long> parseInput(String input) {
        return Arrays.stream(input.split(" "))
            .map(Long::parseLong)
            .toList();
    }


    static long part1(List<Long> input) {
        Map<CacheKey, Long> cache = new HashMap<>();
        long result = 0;
        for (Long stone : input) {
            result += solve(cache, new CacheKey(stone, 0), 25);
        }
        return result;
    }

    static long part2(List<Long> input) {
        Map<CacheKey, Long> cache = new HashMap<>();
        long result = 0;
        for (Long stone : input) {
            result += solve(cache, new CacheKey(stone, 0), 75);
        }
        return result;
    }

    static long solve(Map<CacheKey, Long> cache, CacheKey key, int iterations) {
        if (key.iteration == iterations) {
            return 1;
        }

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        String numberString = Long.toString(key.number);
        long stones;
        if (key.number == 0) {
            stones = solve(cache, new CacheKey(1, key.iteration + 1), iterations);
        } else if (numberString.length() % 2 == 0) {
            int midIndex = numberString.length() / 2;

            long leftNumber = Long.parseLong(numberString.substring(0, midIndex));
            long rightNumber = Long.parseLong(numberString.substring(midIndex));
            stones =
                solve(cache, new CacheKey(leftNumber, key.iteration + 1), iterations) +
                solve(cache, new CacheKey(rightNumber, key.iteration + 1), iterations);
        } else {
            stones = solve(cache, new CacheKey(key.number * 2024, key.iteration + 1), iterations);
        }

        cache.put(key, stones);
        return stones;
    }

    record CacheKey(long number, int iteration) { }
}
