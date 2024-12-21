package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parse2DGrid;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;
import static com.github.christophergustafson.utils.grid.Direction.EAST;
import static com.github.christophergustafson.utils.grid.Direction.NORTH;
import static com.github.christophergustafson.utils.grid.Direction.SOUTH;
import static com.github.christophergustafson.utils.grid.Direction.WEST;

import com.github.christophergustafson.utils.grid.Direction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("Duplicates")
public class Day15 {

    public static void main(String[] args) {

        String testInput = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########
            
            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^""";
        String realInput = readInputFile("day15.txt");

        long part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        long part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        long part2Test = part2(parseInputPart2(testInput));
        System.out.println("Part 2, test: " + part2Test);

        long part2Real = part2(parseInputPart2(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static ParsedInput parseInput(String input) {
        String[] split = input.split("\n\n");
        char[][] grid = parse2DGrid(split[0]);
        char[] moves = split[1].replace("\n", "").toCharArray();
        return new ParsedInput(grid, moves);
    }

    static ParsedInput parseInputPart2(String input) {
        String[] split = input.split("\n\n");
        char[][] grid = Arrays.stream(parse2DGrid(split[0]))
            .map(chars -> new String(chars)
                .chars()
                .flatMap(c -> {
                    switch (c) {
                        case '#' -> {
                            return IntStream.of('#', '#');
                        }
                        case '.' -> {
                            return IntStream.of('.', '.');
                        }
                        case '@' -> {
                            return IntStream.of('@', '.');
                        }
                        case 'O' -> {
                            return IntStream.of('[', ']');
                        }
                        default -> {
                            return IntStream.empty();
                        }
                    }
                })
                .mapToObj(c -> (char) c)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString()
                .toCharArray()
            ).toArray(char[][]::new);
        char[] moves = split[1].replace("\n", "").toCharArray();
        return new ParsedInput(grid, moves);
    }

    record ParsedInput(char[][] grid, char[] moves) {

    }

    static long part1(ParsedInput input) {
        char[][] grid = input.grid;

        // Find robot starting position
        int ry = 0;
        int rx = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == '@') {
                    ry = y;
                    rx = x;
                }
            }
        }

        for (char move : input.moves) {
            switch (move) {
                case '^':
                    if (grid[ry - 1][rx] == '.') {
                        moveRobotTo(ry - 1, rx, NORTH, grid);
                        ry--;
                    } else if (grid[ry - 1][rx] == 'O' && moveBoxTo(ry - 2, rx, NORTH, grid)) {
                        moveRobotTo(ry - 1, rx, NORTH, grid);
                        ry--;
                    }
                    break;
                case 'v':
                    if (grid[ry + 1][rx] == '.') {
                        moveRobotTo(ry + 1, rx, SOUTH, grid);
                        ry++;
                    } else if (grid[ry + 1][rx] == 'O' && moveBoxTo(ry + 2, rx, SOUTH, grid)) {
                        moveRobotTo(ry + 1, rx, SOUTH, grid);
                        ry++;
                    }
                    break;
                case '<':
                    if (grid[ry][rx - 1] == '.') {
                        moveRobotTo(ry, rx - 1, WEST, grid);
                        rx--;
                    } else if (grid[ry][rx - 1] == 'O' && moveBoxTo(ry, rx - 2, WEST, grid)) {
                        moveRobotTo(ry, rx - 1, WEST, grid);
                        rx--;
                    }
                    break;
                case '>':
                    if (grid[ry][rx + 1] == '.') {
                        moveRobotTo(ry, rx + 1, EAST, grid);
                        rx++;
                    } else if (grid[ry][rx + 1] == 'O' && moveBoxTo(ry, rx + 2, EAST, grid)) {
                        moveRobotTo(ry, rx + 1, EAST, grid);
                        rx++;
                    }
                    break;
            }
        }

        long result = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == 'O') {
                    result += y * 100L + x;
                }
            }
        }

        return result;
    }

    static void moveRobotTo(int y, int x, Direction direction, char[][] grid) {
        switch (direction) {
            case NORTH -> {
                grid[y + 1][x] = '.';
                grid[y][x] = '@';
            }
            case SOUTH -> {
                grid[y - 1][x] = '.';
                grid[y][x] = '@';
            }
            case EAST -> {
                grid[y][x - 1] = '.';
                grid[y][x] = '@';
            }
            case WEST -> {
                grid[y][x + 1] = '.';
                grid[y][x] = '@';
            }
        }
    }

    static boolean moveBoxTo(int y, int x, Direction direction, char[][] grid) {
        if (grid[y][x] == '#') {
            return false;
        }
        if (grid[y][x] == 'O') {
            boolean moved = switch (direction) {
                case NORTH -> moveBoxTo(y - 1, x, direction, grid);
                case SOUTH -> moveBoxTo(y + 1, x, direction, grid);
                case EAST -> moveBoxTo(y, x + 1, direction, grid);
                case WEST -> moveBoxTo(y, x - 1, direction, grid);
            };
            if (!moved) {
                return false;
            }
        }

        grid[y][x] = 'O';
        return true;
    }

    static long part2(ParsedInput input) {
        char[][] grid = input.grid;

        // Find robot starting position
        int ry = 0;
        int rx = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == '@') {
                    ry = y;
                    rx = x;
                }
            }
        }

        for (char move : input.moves) {
            switch (move) {
                case '^':
                    if (grid[ry - 1][rx] == '.') {
                        moveRobotTo(ry - 1, rx, NORTH, grid);
                        ry--;
                    } else if (isBox(grid[ry - 1][rx]) && canMoveBox(ry - 2, rx, grid[ry - 1][rx], NORTH, grid)) {
                        moveBoxToUnchecked(ry - 2, rx, grid[ry - 1][rx], NORTH, grid, new ArrayList<>());
                        moveRobotTo(ry - 1, rx, NORTH, grid);
                        ry--;
                    }
                    break;
                case 'v':
                    if (grid[ry + 1][rx] == '.') {
                        moveRobotTo(ry + 1, rx, SOUTH, grid);
                        ry++;
                    } else if (isBox(grid[ry + 1][rx]) && canMoveBox(ry + 2, rx, grid[ry + 1][rx], SOUTH, grid)) {
                        moveBoxToUnchecked(ry + 2, rx, grid[ry + 1][rx], SOUTH, grid, new ArrayList<>());
                        moveRobotTo(ry + 1, rx, SOUTH, grid);
                        ry++;
                    }
                    break;
                case '<':
                    if (grid[ry][rx - 1] == '.') {
                        moveRobotTo(ry, rx - 1, WEST, grid);
                        rx--;
                    } else if (isBox(grid[ry][rx - 1]) && canMoveBox(ry, rx - 2, grid[ry][rx - 1], WEST, grid)) {
                        moveBoxToUnchecked(ry, rx - 2, grid[ry][rx - 1], WEST, grid, new ArrayList<>());
                        moveRobotTo(ry, rx - 1, WEST, grid);
                        rx--;
                    }
                    break;
                case '>':
                    if (grid[ry][rx + 1] == '.') {
                        moveRobotTo(ry, rx + 1, EAST, grid);
                        rx++;
                    } else if (isBox(grid[ry][rx + 1]) && canMoveBox(ry, rx + 2, grid[ry][rx + 1], EAST, grid)) {
                        moveBoxToUnchecked(ry, rx + 2, grid[ry][rx + 1], EAST, grid, new ArrayList<>());
                        moveRobotTo(ry, rx + 1, EAST, grid);
                        rx++;
                    }
                    break;
            }
        }


        long result = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == '[') {
                    result += y * 100L + x;
                }
            }
        }


        return result;
    }

    static void moveBoxToUnchecked(int y, int x, char box, Direction direction, char[][] grid, List<Point> movedBoxes) {

        // Move further boxes
        switch (direction) {
            case NORTH -> {
                if(isBox(grid[y][x])) {
                    moveBoxToUnchecked(y - 1, x, grid[y][x], NORTH, grid, movedBoxes);
                }
                if (isLeftBox(box) && isLeftBox(grid[y][x+1])) {
                    moveBoxToUnchecked(y-1, x+1, grid[y][x+1], NORTH, grid, movedBoxes);
                }
                if (isRightBox(box) && isRightBox(grid[y][x-1])) {
                    moveBoxToUnchecked(y-1, x-1, grid[y][x-1], NORTH, grid, movedBoxes);
                }
            }
            case SOUTH -> {
                if(isBox(grid[y][x])) {
                    moveBoxToUnchecked(y + 1, x, grid[y][x], SOUTH, grid, movedBoxes);
                }
                if (isLeftBox(box) && isLeftBox(grid[y][x+1])) {
                    moveBoxToUnchecked(y+1, x+1, grid[y][x+1], SOUTH, grid, movedBoxes);
                }
                if (isRightBox(box) && isRightBox(grid[y][x-1])) {
                    moveBoxToUnchecked(y+1, x-1, grid[y][x-1], SOUTH, grid, movedBoxes);
                }
            }
            case EAST -> {
                if (isBox(grid[y][x+1])) {
                    moveBoxToUnchecked(y, x+2, grid[y][x+1], EAST, grid, movedBoxes);
                }
            }
            case WEST -> {
                if (isBox(grid[y][x-1])) {
                    moveBoxToUnchecked(y, x-2, grid[y][x-1], WEST, grid, movedBoxes);
                }
            }
        }

        Point p;
        if(isLeftBox(box)) {
            p = new Point(y, x+1);
        } else {
            p = new Point(y, x-1);
        }
        if (movedBoxes.contains(new Point(y, x)) || movedBoxes.contains(p)) {
            return;
        }

        // Move box
        switch (direction) {
            case NORTH -> {
                grid[y][x] = box;
                grid[y+1][x] = '.';
                movedBoxes.add(new Point(y, x));
                if(isLeftBox(box)) {
                    grid[y][x+1] = ']';
                    grid[y+1][x+1] = '.';
                    movedBoxes.add(new Point(y, x+1));
                }
                else if(isRightBox(box)) {
                    grid[y][x-1] = '[';
                    grid[y+1][x-1] = '.';
                    movedBoxes.add(new Point(y, x-1));
                }
            }
            case SOUTH -> {
                grid[y][x] = box;
                grid[y-1][x] = '.';
                movedBoxes.add(new Point(y, x));
                if(isLeftBox(box)) {
                    grid[y][x+1] = ']';
                    grid[y-1][x+1] = '.';
                    movedBoxes.add(new Point(y, x+1));
                }
                else if(isRightBox(box)) {
                    grid[y][x-1] = '[';
                    grid[y-1][x-1] = '.';
                    movedBoxes.add(new Point(y, x-1));
                }
            }
            case EAST -> {
                grid[y][x] = '[';
                grid[y][x+1] = ']';
            }
            case WEST -> {
                grid[y][x] = ']';
                grid[y][x-1] = '[';
            }
        }
    }

    static boolean canMoveBox(int y, int x, char box, Direction direction, char[][] grid) {
        // Check for walls
        switch (direction) {
            case NORTH, SOUTH -> {
                if(isWall(grid[y][x])) {
                    return false;
                }
                if(isLeftBox(box) && isWall(grid[y][x+1])) {
                    return false;
                }
                if(isRightBox(box) && isWall(grid[y][x-1])) {
                    return false;
                }
            }
            case EAST -> {
                if(isWall(grid[y][x+1])) {
                    return false;
                }
            }
            case WEST -> {
                if(isWall(grid[y][x-1])) {
                    return false;
                }
            }
        }

        // Can move further boxes
        boolean canMove = true;
        switch (direction) {
            case NORTH -> {
                if(isBox(grid[y][x])) {
                    canMove = canMoveBox(y - 1, x, grid[y][x], NORTH, grid);
                }
                if (canMove && isLeftBox(box) && isLeftBox(grid[y][x+1])) {
                    canMove = canMoveBox(y-1, x+1, grid[y][x+1], NORTH, grid);
                }
                if (canMove && isRightBox(box) && isRightBox(grid[y][x-1])) {
                    canMove = canMoveBox(y-1, x-1, grid[y][x-1], NORTH, grid);
                }
            }
            case SOUTH -> {
                if(isBox(grid[y][x])) {
                    canMove = canMoveBox(y + 1, x, grid[y][x], SOUTH, grid);
                }
                if (canMove && isLeftBox(box) && isLeftBox(grid[y][x+1])) {
                    canMove = canMoveBox(y+1, x+1, grid[y][x+1], SOUTH, grid);
                }
                if (canMove && isRightBox(box) && isRightBox(grid[y][x-1])) {
                    canMove = canMoveBox(y+1, x-1, grid[y][x-1], SOUTH, grid);
                }
            }
            case EAST -> {
                if (isBox(grid[y][x+1])) {
                    canMove = canMoveBox(y, x+2, grid[y][x+1], EAST, grid);
                }
            }
            case WEST -> {
                if (isBox(grid[y][x-1])) {
                    canMove = canMoveBox(y, x-2, grid[y][x-1], WEST, grid);
                }
            }
        }
        return canMove;
    }

    record Point(int y, int x) { }

    static boolean isBox(char c) {
        return isLeftBox(c) || isRightBox(c);
    }

    static boolean isRightBox(char c) {
        return c == ']';
    }

    static boolean isLeftBox(char c) {
        return c == '[';
    }

    static boolean isWall(char c) {
        return c == '#';
    }
}
