package com.github.christophergustafson;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Utils {

    static String readInputFile(String fileName) {
        try {
            return Files.readString(Path.of(ClassLoader.getSystemResource(fileName).toURI()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Could not read file: " + e);
            return "";
        }
    }

    static List<List<Integer>> parseLinesOfIntegers(String input) {
        return Arrays.stream(input.split("\n"))
            .map(line -> Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .toList())
            .toList();
    }

    static int[][] parseLinesOfIntegers(String input, String divider) {
        return Arrays.stream(input.split("\n"))
            .map(line -> Arrays.stream(line.split(divider))
                .mapToInt(Integer::parseInt)
                .toArray())
            .toArray(int[][]::new);
    }

    static List<String> parseLinesOfStrings(String input) {
        return Arrays.stream(input.split("\n")).toList();
    }

    static char[][] parse2DGrid(String input) {
        return Arrays.stream(input.split("\n"))
            .map(String::toCharArray)
            .toArray(char[][]::new);
    }
}
