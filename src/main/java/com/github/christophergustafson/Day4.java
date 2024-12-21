package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parse2DGrid;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class Day4 {

    public static void main(String[] args) {
        String testInput = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX""";
        String realInput = readInputFile("day4.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static char[][] parseInput(String input) {
        return parse2DGrid(input);
    }

    static int part1(char[][] grid) {
        int occurrences = 0;
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {

                if (grid[y][x] == 'X') {
                    // Horizontal
                    try {
                        if (grid[y][x + 1] == 'M' && grid[y][x + 2] == 'A' && grid[y][x + 3] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Horizontal backwards
                    try {
                        if (grid[y][x - 1] == 'M' && grid[y][x - 2] == 'A' && grid[y][x - 3] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Vertical
                    try {
                        if (grid[y + 1][x] == 'M' && grid[y + 2][x] == 'A' && grid[y + 3][x] == 'S') {
                            occurrences++;
                       }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Vertical backwards
                    try {
                        if (grid[y - 1][x] == 'M' && grid[y - 2][x] == 'A' && grid[y - 3][x] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Diagonal up right
                    try {
                        if (grid[y - 1][x + 1] == 'M' && grid[y - 2][x + 2] == 'A' && grid[y - 3][x + 3] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Diagonal down right
                    try {
                        if (grid[y + 1][x + 1] == 'M' && grid[y + 2][x + 2] == 'A' && grid[y + 3][x + 3] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Diagonal down left
                    try {
                        if (grid[y + 1][x - 1] == 'M' && grid[y + 2][x - 2] == 'A' && grid[y + 3][x - 3] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }

                    // Diagonal up left
                    try {
                        if (grid[y - 1][x - 1] == 'M' && grid[y - 2][x - 2] == 'A' && grid[y - 3][x - 3] == 'S') {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }
                }
            }
        }
        return occurrences;
    }

    static int part2(char[][] grid) {
        int occurrences = 0;
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[y][x] == 'A') {
                    try {
                        if(Arrays.asList(grid[y+1][x+1], grid[y-1][x-1]).containsAll(Arrays.asList('M', 'S'))
                            && Arrays.asList(grid[y+1][x-1], grid[y-1][x+1]).containsAll(Arrays.asList('M', 'S'))) {
                            occurrences++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignore) {
                    }
                }
            }
        }
        return occurrences;
    }
}
