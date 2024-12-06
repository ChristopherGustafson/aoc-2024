package com.github.christophergustafson;

import static com.github.christophergustafson.Utils.parse2DGrid;
import static com.github.christophergustafson.Utils.readInputFile;

@SuppressWarnings("Duplicates")
public class Day6 {

    public static void main(String[] args) {

        String testInput = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...""";
        String realInput = readInputFile("day6.txt");

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
        Position startingPosition = getStartingPosition(grid);
        int y = startingPosition.y;
        int x = startingPosition.x;

        boolean[][] visited = new boolean[grid.length][grid[0].length];
        int visitedCount = 0;
        Direction direction = Direction.NORTH;
        try {
            while (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
                if (!visited[y][x]) {
                    visited[y][x] = true;
                    visitedCount++;
                }
                switch (direction) {
                    case NORTH -> {
                        if (grid[y - 1][x] == '#') {
                            direction = Direction.EAST;
                        } else {
                            y--;
                        }
                    }
                    case SOUTH -> {
                        if (grid[y + 1][x] == '#') {
                            direction = Direction.WEST;
                        } else {
                            y++;
                        }
                    }
                    case WEST -> {
                        if (grid[y][x - 1] == '#') {
                            direction = Direction.NORTH;
                        } else {
                            x--;
                        }
                    }
                    case EAST -> {
                        if (grid[y][x + 1] == '#') {
                            direction = Direction.SOUTH;
                        } else {
                            x++;
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }

        return visitedCount;
    }

    enum Direction {
        NORTH,
        SOUTH,
        WEST,
        EAST,
    }

    private static Position getStartingPosition(char[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == '^') {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalArgumentException("Grid does not contain starting position character");
    }

    record Position(int x, int y) { }

    static int part2(char[][] grid) {
        Position startingPosition = getStartingPosition(grid);
        int result = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if(getsStuckInLoop(grid, startingPosition.y, startingPosition.x, y, x)) {
                    result++;
                }
            }
        }
        return result;
    }

    private static boolean getsStuckInLoop(char[][] grid, int startingY, int startingX, int newObstacleY, int newObstacleX) {
        int y = startingY;
        int x = startingX;
        int steps = 0;
        Direction direction = Direction.NORTH;

        try {
            while (y >= 0 && y < grid.length && x >= 0 && x < grid[0].length) {
                if(steps > 100000) {
                    return true;
                }
                steps++;
                switch (direction) {
                    case NORTH -> {
                        if (grid[y - 1][x] == '#' || (y-1 == newObstacleY && x == newObstacleX)) {
                            direction = Direction.EAST;
                        } else {
                            y--;
                        }
                    }
                    case SOUTH -> {
                        if (grid[y + 1][x] == '#' || (y+1 == newObstacleY && x == newObstacleX)) {
                            direction = Direction.WEST;
                        } else {
                            y++;
                        }
                    }
                    case WEST -> {
                        if (grid[y][x - 1] == '#' || (y == newObstacleY && x-1 == newObstacleX)) {
                            direction = Direction.NORTH;
                        } else {
                            x--;
                        }
                    }
                    case EAST -> {
                        if (grid[y][x + 1] == '#' || (y == newObstacleY && x+1 == newObstacleX)) {
                            direction = Direction.SOUTH;
                        } else {
                            x++;
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) { }
        return false;
    }
}
