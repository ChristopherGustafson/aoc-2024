package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.readInputFile;
import static com.github.christophergustafson.utils.grid.DirectionUtils.turnLeft;
import static com.github.christophergustafson.utils.grid.DirectionUtils.turnRight;

import com.github.christophergustafson.Day16.DirectedPosition;
import com.github.christophergustafson.Day16.Node;
import com.github.christophergustafson.utils.grid.Direction;
import com.github.christophergustafson.utils.grid.Grid;
import com.github.christophergustafson.utils.grid.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

@SuppressWarnings("Duplicates")
public class Day18 {

    public static void main(String[] args) {

        String testInput = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0""";
        String realInput = readInputFile("day18.txt");

        int part1Test = part1(parseInput(testInput), 7, 12);
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput), 71, 1024);
        System.out.println("Part 1, real: " + part1Real);

        String part2Test = part2(parseInput(testInput), 7, 12);
        System.out.println("Part 2, test: " + part2Test);

        String part2Real = part2(parseInput(realInput), 71, 1024);
        System.out.println("Part 2, real: " + part2Real);
    }

    static Position[] parseInput(String input) {
        return Arrays.stream(input.split("\n")).map(line -> {
            String[] split = line.split(",");
            return new Position(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        }).toArray(Position[]::new);
    }

    static int part1(Position[] input, int gridSize, int bytesFallen) {
        boolean[][] isCorrupted = new boolean[gridSize][gridSize];

        for (int i = 0; i < bytesFallen; i++) {
            isCorrupted[input[i].y][input[i].x] = true;
        }

        Position start = new Position(0, 0);
        Position end = new Position(gridSize - 1, gridSize - 1);
        return dijkstra(start, end, isCorrupted);
    }

    static int dijkstra(Position start, Position end, boolean[][] isCorrupted) {
        PriorityQueue<Node> q = new PriorityQueue<>(Comparator.comparingInt(o -> o.cost));
//        HashMap<Position, Integer> costs = new HashMap<>();
        HashSet<Position> visited = new HashSet<>();

        q.add(new Node(start, 0));

        while (!q.isEmpty()) {
            Node current = q.remove();

            if (current.p.equals(end)) {
                return current.cost;
            }


            // Add neighbors
            try {
                Position north = current.p.move(Direction.NORTH);
                if (!isCorrupted[north.y][north.x] && !visited.contains(north)) {
                    q.add(new Node(north, current.cost + 1));
                    visited.add(north);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}

            try {
                Position south = current.p.move(Direction.SOUTH);
                if (!isCorrupted[south.y][south.x] && !visited.contains(south)) {
                    q.add(new Node(south, current.cost + 1));
                    visited.add(south);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}

            try {
                Position east = current.p.move(Direction.EAST);
                if (!isCorrupted[east.y][east.x] && !visited.contains(east)) {
                    q.add(new Node(east, current.cost + 1));
                    visited.add(east);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}

            try {
                Position west = current.p.move(Direction.WEST);
                if (!isCorrupted[west.y][west.x] && !visited.contains(west)) {
                    q.add(new Node(west, current.cost + 1));
                    visited.add(west);
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }
        return -1;
    }

    record Node(Position p, int cost) {

    }

    static String part2(Position[] input, int gridSize, int bytesFallen) {
        boolean[][] isCorrupted = new boolean[gridSize][gridSize];

        for (int i = 0; i < bytesFallen; i++) {
            isCorrupted[input[i].y][input[i].x] = true;
        }

        Position start = new Position(0, 0);
        Position end = new Position(gridSize - 1, gridSize - 1);
        for (int i = bytesFallen; i < input.length; i++) {
            isCorrupted[input[i].y][input[i].x] = true;
            if(dijkstra(start, end, isCorrupted) == -1) {
                return input[i].x + "," + input[i].y;
            }
        }
        return "No answer";
    }
}
