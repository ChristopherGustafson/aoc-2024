package com.github.christophergustafson.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtils {

    public static String readInputFile(String fileName) {
        try {
            return Files.readString(Path.of(ClassLoader.getSystemResource(fileName).toURI()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("Could not read file: " + e);
            return "";
        }
    }

    public static List<List<Integer>> parseLinesOfIntegers(String input) {
        return Arrays.stream(input.split("\n"))
            .map(line -> Arrays.stream(line.split(" "))
                .map(Integer::parseInt)
                .toList())
            .toList();
    }

    public static int[][] parseLinesOfIntegers(String input, String divider) {
        return Arrays.stream(input.split("\n"))
            .map(line -> Arrays.stream(line.split(divider))
                .mapToInt(Integer::parseInt)
                .toArray())
            .toArray(int[][]::new);
    }

    public static List<String> parseLinesOfStrings(String input) {
        return Arrays.stream(input.split("\n")).toList();
    }

    public static char[][] parse2DGrid(String input) {
        return Arrays.stream(input.split("\n"))
            .map(String::toCharArray)
            .toArray(char[][]::new);
    }

    public static int[][] parseInteger2DGrid(String input) {
        return Arrays.stream(input.split("\n"))
            .map(line -> line.chars().map(Character::getNumericValue).toArray())
            .toArray(int[][]::new);
    }

    public static List<Long> findLongs(String stringToSearch) {
        Pattern numberPattern = Pattern.compile("-?\\d+");
        Matcher matcher = numberPattern.matcher(stringToSearch);

        List<Long> longList = new ArrayList<>();
        while (matcher.find()) {
            longList.add(Long.parseLong(matcher.group()));
        }

        return longList;
    }

    public static List<Integer> findIntegers(String stringToSearch) {
        Pattern numberPattern = Pattern.compile("-?\\d+");
        Matcher matcher = numberPattern.matcher(stringToSearch);

        List<Integer> longList = new ArrayList<>();
        while (matcher.find()) {
            longList.add(Integer.parseInt(matcher.group()));
        }

        return longList;
    }

    public static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }
}
