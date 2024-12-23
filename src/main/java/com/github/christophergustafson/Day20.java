package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parse2DGrid;
import static com.github.christophergustafson.utils.ParseUtils.parseGrid;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;
import static com.github.christophergustafson.utils.grid.Direction.EAST;
import static com.github.christophergustafson.utils.grid.Direction.NORTH;
import static com.github.christophergustafson.utils.grid.Direction.SOUTH;
import static com.github.christophergustafson.utils.grid.Direction.WEST;
import static com.github.christophergustafson.utils.grid.DirectionUtils.turnLeft;
import static com.github.christophergustafson.utils.grid.DirectionUtils.turnRight;

import com.github.christophergustafson.utils.grid.Direction;
import com.github.christophergustafson.utils.grid.Grid;
import com.github.christophergustafson.utils.grid.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class Day20 {

    public static void main(String[] args) {

        String testInput = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############""";
        String realInput = readInputFile("day20.txt");

        int part1Test = part1(parseInput(testInput), 64);
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput), 100);
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput), 76);
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput), 100);
        System.out.println("Part 2, real: " + part2Real);
    }

    static Grid parseInput(String input) {
        return parseGrid(input);
    }

    static int part1(Grid grid, int saveAtLeast) {
        // Find distances from each part of track.
        Map<Position, Integer> distances = new HashMap<>();
        List<Position> route = new LinkedList<>();

        Position current = grid.find('S');
        Direction direction = NORTH;
        int distance = 0;
        if (grid.get(current.move(NORTH)) == '#') {
            direction = Direction.SOUTH;
        }

        while (grid.get(current) != 'E') {
            distances.put(current, distance);
            route.add(current);

            if (grid.get(current.move(direction)) != '#') {
                current = current.move(direction);
            } else if (grid.get(current.move(turnLeft(direction))) != '#') {
                current = current.move(turnLeft(direction));
                direction = turnLeft(direction);
            } else if (grid.get(current.move(turnRight(direction))) != '#') {
                current = current.move(turnRight(direction));
                direction = turnRight(direction);
            }
            distance++;
        }

        distances.put(current, distance);
        route.add(current);

        int result = 0;
        for (Position p : route) {
            try {
                if (grid.get(p.move(NORTH)) == '#' && grid.get(p.move(NORTH, 2)) != '#') {
                    if (distances.get(p.move(NORTH, 2)) - distances.get(p) > saveAtLeast) {
                        result++;
                    }
                }

            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            try {
                if (grid.get(p.move(SOUTH)) == '#' && grid.get(p.move(SOUTH, 2)) != '#') {
                    if (distances.get(p.move(SOUTH, 2)) - distances.get(p) > saveAtLeast) {
                        result++;
                    }
                }

            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            try {
                if (grid.get(p.move(EAST)) == '#' && grid.get(p.move(EAST, 2)) != '#') {
                    if (distances.get(p.move(EAST, 2)) - distances.get(p) > saveAtLeast) {
                        result++;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
            try {
                if (grid.get(p.move(WEST)) == '#' && grid.get(p.move(WEST, 2)) != '#') {
                    if (distances.get(p.move(WEST, 2)) - distances.get(p) > saveAtLeast) {
                        result++;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }

        return result;
    }

    static int part2(Grid grid, int saveAtLeast) {
        // Find distances from each part of track.
        Map<Position, Integer> distances = new HashMap<>();
        List<Position> route = new LinkedList<>();

        Position current = grid.find('S');
        Direction direction = NORTH;
        int distance = 0;
        if (grid.get(current.move(NORTH)) == '#') {
            direction = Direction.SOUTH;
        }

        while (grid.get(current) != 'E') {
            distances.put(current, distance);
            route.add(current);
            if (grid.get(current.move(direction)) != '#') {
                current = current.move(direction);
            } else if (grid.get(current.move(turnLeft(direction))) != '#') {
                current = current.move(turnLeft(direction));
                direction = turnLeft(direction);
            } else if (grid.get(current.move(turnRight(direction))) != '#') {
                current = current.move(turnRight(direction));
                direction = turnRight(direction);
            }
            distance++;
        }

        distances.put(current, distance);
        route.add(current);

        int result = 0;
        for (Position p : route) {
            for (int steps = 2; steps < 21; steps++) {
                for (Position cheatTo: route) {
                    if(p.manhattanDistance(cheatTo) == steps &&
                        distances.get(cheatTo) - distances.get(p) - steps >= saveAtLeast) {
                        result++;
                    }
                }
            }
        }

        return result;
    }
}
