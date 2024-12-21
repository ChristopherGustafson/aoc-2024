package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parse2DGrid;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class Day8 {

    public static void main(String[] args) {

        String testInput = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............""";
        String realInput = readInputFile("day8.txt");

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

    static int part1(char[][] input) {
        Map<Character, List<Point>> antennas = findAntennas(input);

        int result = 0;
        boolean[][] counted = new boolean[input.length][input[0].length];
        for (List<Point> frequencyLocations : antennas.values()) {
            for (Point a1: frequencyLocations) {
                for (Point a2: frequencyLocations) {
                    int xDistance = a2.x - a1.x;
                    int yDistance = a2.y - a1.y;

                    if(xDistance == 0 && yDistance == 0) {
                        continue;
                    }

                    int antinodeX = a2.x + xDistance;
                    int antinodeY = a2.y + yDistance;

                    try {
                        if (!counted[antinodeY][antinodeX]) {
                            counted[antinodeY][antinodeX] = true;
                            result++;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) { }
                }
            }
        }

        return result;
    }

    private static Map<Character, List<Point>> findAntennas(char[][] grid) {
        Map<Character, List<Point>> antennas = new HashMap<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != '.') {
                    if(antennas.containsKey(grid[y][x])) {
                        antennas.get(grid[y][x]).add(new Point(x, y));
                    } else {
                        antennas.put(grid[y][x], new ArrayList<>(List.of(new Point(x, y))));
                    }
                }
            }
        }
        return antennas;
    }

    record Point(int x, int y) { }

    static int part2(char[][] input) {
        Map<Character, List<Point>> antennas = findAntennas(input);

        int result = 0;
        boolean[][] counted = new boolean[input.length][input[0].length];
        for (List<Point> frequencyLocations : antennas.values()) {
            for (Point a1: frequencyLocations) {
                for (Point a2: frequencyLocations) {
                    int xDistance = a2.x - a1.x;
                    int yDistance = a2.y - a1.y;

                    if(xDistance == 0 && yDistance == 0) {
                        continue;
                    }

                    int i = 0;
                    while (true) {
                        int antinodeX = a2.x + xDistance * i;
                        int antinodeY = a2.y + yDistance * i;

                        try {
                            if (!counted[antinodeY][antinodeX]) {
                                counted[antinodeY][antinodeX] = true;
                                result++;
                            }
                        } catch (ArrayIndexOutOfBoundsException ignored) {
                            break;
                        }

                        i++;
                    }
                }
            }
        }
        return result;
    }
}
