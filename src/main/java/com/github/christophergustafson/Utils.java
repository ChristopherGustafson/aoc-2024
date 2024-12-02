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
        } catch(Exception e) {
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
}
