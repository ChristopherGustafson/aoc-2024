package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.findIntegers;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;
import static java.lang.Thread.sleep;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("Duplicates")
public class Day14 {

    public static void main(String[] args) {

        String testInput = """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3""";
        String realInput = readInputFile("day14.txt");

        int part1Test = part1(parseInput(testInput), 11, 7);
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput), 101, 103);
        System.out.println("Part 1, real: " + part1Real);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static List<Robot> parseInput(String input) {
        return Arrays.stream(input.split("\n"))
            .map(line -> {
                List<Integer> numbers = findIntegers(line);
                return new Robot(numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3));
            }).toList();
    }

    record Robot(int x, int y, int vx, int vy) { }

    static int part1(List<Robot> input, int width, int height) {
        int steps = 100;
        int xMid = (width-1) / 2;
        int yMid = (height-1) / 2;

        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;

        for (Robot robot : input) {
            int x = (robot.x + robot.vx * steps) % width;
            if (x < 0) {
                x = width + x;
            }
            int y = (robot.y + robot.vy * steps) % height;
            if (y < 0) {
                y = height + y;
            }

            if (x > xMid && y < yMid) {
                q1++;
            } else if (x < xMid && y < yMid) {
                q2++;
            } else if (x < xMid && y > yMid) {
                q3++;
            } else if (x > xMid && y > yMid) {
                q4++;
            }
        }

        return q1 * q2 * q3 * q4;
    }

    static int part2(List<Robot> input) {
        int width = 101;
        int height = 103;

        List<Robot> robots = input;
        int second = 1;
        while(true) {
            boolean[][] grid = new boolean[height][width];
            robots = robots.stream().map(robot -> {
                Robot moved = move(robot, width, height);
                grid[moved.y][moved.x] = true;
                return moved;
            }).toList();

            // Look for a 9x9 area with robots and print the matrix to see if there is a Christmas tree
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[0].length; x++) {
                    try {
                        boolean largeArea = true;
                        for (int ty = y-4; ty < y+5; ty++) {
                            for (int tx = x-4; tx < x+5; tx++) {
                                if (!grid[ty][tx]) {
                                    largeArea = false;
                                }
                            }
                        }
                        if(largeArea) {
                            printGrid(grid);
                            return second;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) { }
                }
            }
            second++;
        }
    }

    static void printGrid(boolean[][] grid) {
        for (boolean[] row: grid) {
            for(boolean hasRobot: row)
                if(hasRobot) {
                    System.out.print("R");
                } else {
                    System.out.print(".");
                }
            System.out.println();
        }
    }

    static Robot move(Robot robot, int width, int height) {
        int x = (robot.x + robot.vx) % width;
        if (x < 0) {
            x = width + x;
        }
        int y = (robot.y + robot.vy) % height;
        if (y < 0) {
            y = height + y;
        }
        return new Robot(x, y, robot.vx, robot.vy);
    }
}
