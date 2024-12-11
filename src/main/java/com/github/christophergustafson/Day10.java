package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.parseInteger2DGrid;
import static com.github.christophergustafson.Utils.readInputFile;

@SuppressWarnings("Duplicates")
public class Day10 {

    public static void main(String[] args) {

        String testInput = """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732""";
        String realInput = readInputFile("day10.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static int[][] parseInput(String input) {
        return parseInteger2DGrid(input);
    }

    static int part1(int[][] input) {
        int result = 0;
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                if(input[y][x] == 0) {
                    result += score(input, y, x);
                }
            }
        }

        return result;
    }

    private static int score(int[][] grid, int y, int x) {
        return solveScore(grid, y, x, new boolean[grid.length][grid[0].length]);
    }

    private static int solveScore(int[][] grid, int y, int x, boolean[][] reached) {
        int currentHeight = grid[y][x];

        if(currentHeight == 9) {
            if(!reached[y][x]) {
                reached[y][x] = true;
                return 1;
            } else {
                return 0;
            }
        }

        int score = 0;
        // South
        try {
            if(grid[y+1][x] - currentHeight == 1) {
                score += solveScore(grid, y+1, x, reached);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
        // North
        try {
            if(grid[y-1][x] - currentHeight == 1) {
                score += solveScore(grid, y-1, x, reached);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }

        // East
        try {
            if(grid[y][x+1] - currentHeight == 1) {
                score += solveScore(grid, y, x+1, reached);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
        // West
        try {
            if(grid[y][x-1] - currentHeight == 1) {
                score += solveScore(grid, y, x-1, reached);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }

        return score;
    }

    static int part2(int[][] input) {
        int result = 0;
        for (int y = 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                if(input[y][x] == 0) {
                    result += rating(input, y, x);
                }
            }
        }

        return result;
    }

    private static int rating(int[][] grid, int y, int x) {
        int currentHeight = grid[y][x];

        if(currentHeight == 9) {
            return 1;
        }

        int score = 0;
        // South
        try {
            if(grid[y+1][x] - currentHeight == 1) {
                score += rating(grid, y+1, x);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
        // North
        try {
            if(grid[y-1][x] - currentHeight == 1) {
                score += rating(grid, y-1, x);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }

        // East
        try {
            if(grid[y][x+1] - currentHeight == 1) {
                score += rating(grid, y, x+1);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
        // West
        try {
            if(grid[y][x-1] - currentHeight == 1) {
                score += rating(grid, y, x-1);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }

        return score;
    }
}
