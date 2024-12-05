package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.parseLinesOfStrings;
import static com.github.christophergustafson.Utils.readInputFile;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("Duplicates")
public class Day3 {

    public static void main(String[] args) {

        String testInput1 = """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))""";
        String realInput = readInputFile("day3.txt");

        int part1Test = part1(parseInput(testInput1));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        String testInput2 = """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))""";

        int part2Test = part2(parseInput(testInput2));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<String> parseInput(String input) {
        return parseLinesOfStrings(input);
    }

    static int part1(List<String> input) {
        int result = 0;
        for (String line : input) {
            String restOfLine = line;
            while (!restOfLine.isEmpty()) {
                int instructionIndex = restOfLine.indexOf("mul(");
                if (instructionIndex < 0) {
                    break;
                }
                int endOfInstructionIndex = restOfLine.substring(instructionIndex).indexOf(")") + instructionIndex;
                if (endOfInstructionIndex < 0) {
                    break;
                }

                String[] numbers = restOfLine.substring(instructionIndex + 4, endOfInstructionIndex).split(",");

                if (numbers.length == 2 && numbers[0].length() < 4 && numbers[1].length() < 4) {
                    try {
                        int leftNumber = Integer.parseInt(numbers[0]);
                        int rightNumber = Integer.parseInt(numbers[1]);
                        result += leftNumber * rightNumber;
                    } catch (NumberFormatException ignored) {
                    }
                }

                restOfLine = restOfLine.substring(instructionIndex + 4);
            }
        }
        return result;
    }

    static int part2(List<String> input) {
        int result = 0;
        boolean mulEnabled = true;
        for (String line : input) {
            String restOfLine = line;
            while (!restOfLine.isEmpty()) {
                int doIndex = restOfLine.indexOf("do()");
                int dontIndex = restOfLine.indexOf("don't()");
                int instructionIndex = restOfLine.indexOf("mul(");
                List<Integer> indices = Stream.of(doIndex, dontIndex, instructionIndex).filter(i -> i > -1).toList();

                if (instructionIndex < 0) {
                    break;
                }
                if (doIndex == Collections.min(indices)) {
                    mulEnabled = true;
                    restOfLine = restOfLine.substring(doIndex + 4);
                    continue;
                }
                if (dontIndex == Collections.min(indices)) {
                    mulEnabled = false;
                    restOfLine = restOfLine.substring(dontIndex + 7);
                    continue;
                }
                if (!mulEnabled) {
                    restOfLine = restOfLine.substring(instructionIndex + 4);
                    continue;
                }

                int endOfInstructionIndex = restOfLine.substring(instructionIndex).indexOf(")") + instructionIndex;
                if (endOfInstructionIndex < 0) {
                    break;
                }

                String[] numbers = restOfLine.substring(instructionIndex + 4, endOfInstructionIndex).split(",");

                if (numbers.length == 2 && numbers[0].length() < 4 && numbers[1].length() < 4) {
                    try {
                        int leftNumber = Integer.parseInt(numbers[0]);
                        int rightNumber = Integer.parseInt(numbers[1]);
                        result += leftNumber * rightNumber;
                    } catch (NumberFormatException ignored) {
                    }
                }

                restOfLine = restOfLine.substring(instructionIndex + 4);
            }
        }
        return result;
    }
}
