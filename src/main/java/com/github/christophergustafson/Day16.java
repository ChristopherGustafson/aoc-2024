package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parse2DGrid;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;
import static com.github.christophergustafson.utils.grid.DirectionUtils.opposite;
import static com.github.christophergustafson.utils.grid.DirectionUtils.turnLeft;
import static com.github.christophergustafson.utils.grid.DirectionUtils.turnRight;

import com.github.christophergustafson.Day16.DirectedPosition;
import com.github.christophergustafson.utils.grid.Position;
import com.github.christophergustafson.utils.grid.Direction;
import com.github.christophergustafson.utils.grid.Grid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

@SuppressWarnings("Duplicates")
public class Day16 {

    public static void main(String[] args) {

        String testInput = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############""";
        String realInput = readInputFile("day16.txt");

        int part1Test = part1(parseInput(testInput));
        System.out.println("Part 1, test: " + part1Test);

        int part1Real = part1(parseInput(realInput));
        System.out.println("Part 1, real: " + part1Real);

        int part2Test = part2(parseInput(testInput));
        System.out.println("Part 2, test: " + part2Test);

        int part2Real = part2(parseInput(realInput));
        System.out.println("Part 2, real: " + part2Real);
    }

    static ParsedInput parseInput(String input) {
        Grid grid = new Grid(parse2DGrid(input));
        Position start = grid.find('S');
        Position end = grid.find('E');
        return new ParsedInput(start.x, end.y, start.x, end.y, grid);
    }

    record ParsedInput(int startX, int startY, int endX, int endY, Grid grid) { }

    static int part1(ParsedInput input) {
        Position start = input.grid.find('S');
        return dijkstra(start, input.grid);
    }

    static int dijkstra(Position start, Grid grid) {
        PriorityQueue<Node> q = new PriorityQueue<>(Comparator.comparingInt(o -> o.cost));
        HashMap<DirectedPosition, Integer> costs = new HashMap<>();

        q.add(new Node(new DirectedPosition(start, Direction.EAST), 0));

        while(!q.isEmpty()) {
            Node current = q.remove();
            if (grid.get(current.d.position) == 'E') {
                return current.cost;
            }
            costs.put(current.d, current.cost);

            // Add neighbors
            Position moveForward = current.d.position.move(current.d.direction);
            DirectedPosition d = new DirectedPosition(moveForward, current.d.direction);
            if (grid.get(moveForward) != '#' && costs.getOrDefault(d, Integer.MAX_VALUE) > current.cost + 1) {
                q.add(new Node(d, current.cost + 1));
            }

            Direction rightDirection = turnRight(current.d.direction);
            assert rightDirection != null;
            Position turnRight = current.d.position.move(rightDirection);
            d = new DirectedPosition(turnRight, rightDirection);
            if (grid.get(turnRight) != '#' && costs.getOrDefault(d, Integer.MAX_VALUE) > current.cost + 1) {
                q.add(new Node(d, current.cost + 1001));
            }

            Direction leftDirection = turnLeft(current.d.direction);
            assert leftDirection != null;
            Position turnLeft = current.d.position.move(leftDirection);
            d = new DirectedPosition(turnLeft, leftDirection);
            if (grid.get(turnLeft) != '#' && costs.getOrDefault(d, Integer.MAX_VALUE) > current.cost + 1) {
                q.add(new Node(d, current.cost + 1001));
            }
        }
        throw new RuntimeException("Didn't find solution");
    }

    static HashMap<DirectedPosition, Integer> dijkstra2(Position start, Grid grid) {
        PriorityQueue<Node> q = new PriorityQueue<>(Comparator.comparingInt(o -> o.cost));
        HashMap<DirectedPosition, Integer> costs = new HashMap<>();

        q.add(new Node(new DirectedPosition(start, Direction.EAST), 0));

        while(!q.isEmpty()) {
            Node current = q.remove();
            if (grid.get(current.d.position) == 'E') {
                return costs;
            }
            costs.put(current.d, current.cost);

            // Add neighbors
            Position moveForward = current.d.position.move(current.d.direction);
            DirectedPosition d = new DirectedPosition(moveForward, current.d.direction);
            if (grid.get(moveForward) != '#' && costs.getOrDefault(d, Integer.MAX_VALUE) > current.cost + 1) {
                q.add(new Node(d, current.cost + 1));
            }

            Direction rightDirection = turnRight(current.d.direction);
            assert rightDirection != null;
            Position turnRight = current.d.position.move(rightDirection);
            d = new DirectedPosition(turnRight, rightDirection);
            if (grid.get(turnRight) != '#' && costs.getOrDefault(d, Integer.MAX_VALUE) > current.cost + 1001) {
                q.add(new Node(d, current.cost + 1001));
            }

            Direction leftDirection = turnLeft(current.d.direction);
            assert leftDirection != null;
            Position turnLeft = current.d.position.move(leftDirection);
            d = new DirectedPosition(turnLeft, leftDirection);
            if (grid.get(turnLeft) != '#' && costs.getOrDefault(d, Integer.MAX_VALUE) > current.cost + 1001) {
                q.add(new Node(d, current.cost + 1001));
            }
        }
        throw new RuntimeException("Did not find solution");
    }

    static int part2(ParsedInput input) {
        Position start = input.grid.find('S');
        Position end = input.grid.find('E');
        HashMap<DirectedPosition, Integer> costs = dijkstra2(start, input.grid);
        return bestPositions(costs, start, end);
    }

    static int bestPositions(HashMap<DirectedPosition, Integer> costs, Position start, Position end) {
        HashSet<Position> bestPositions = new HashSet<>();
        bestPositions.add(start);
        bestPositions.add(end);

        List<DirectedPosition> q = new LinkedList<>(getLowestCostNeighbors(
            new DirectedPosition(end, Direction.NORTH),
            costs
        ));

        while (!q.isEmpty()) {
            DirectedPosition current = q.removeFirst();
            bestPositions.add(current.position);
            q.addAll(getLowestCostNeighbors(current, costs));
        }
        return bestPositions.size();
    }

    static List<DirectedPosition> getLowestCostNeighbors(DirectedPosition d, HashMap<DirectedPosition, Integer> costs) {
        List<DirectedPosition> neighbors = new LinkedList<>();
        int lowestCost = Integer.MAX_VALUE;

        Position fromForward = d.position.move(opposite(d.direction));
        DirectedPosition forward = new DirectedPosition(fromForward, d.direction);
        if (costs.containsKey(forward)) {
            neighbors.add(forward);
            lowestCost = costs.get(forward);
        }

        Direction rightDirection = turnRight(d.direction);
        assert rightDirection != null;
        Position turnRight = d.position.move(opposite(d.direction));
        DirectedPosition right = new DirectedPosition(turnRight, opposite(rightDirection));
        if (costs.containsKey(right)) {
            int rightCost = costs.get(right) + 1000;
            if(rightCost < lowestCost) {
                neighbors = new ArrayList<>(List.of(right));
                lowestCost = rightCost;
            } else if(rightCost == lowestCost) {
                neighbors.add(right);
            }
        }

        Direction leftDirection = turnLeft(d.direction);
        assert leftDirection != null;
        Position turnLeft = d.position.move(opposite(d.direction));
        DirectedPosition left = new DirectedPosition(turnLeft, opposite(leftDirection));
        if (costs.containsKey(left)) {
            int leftCost = costs.get(left) + 1000;
            if(leftCost < lowestCost) {
                neighbors = new ArrayList<>(List.of(left));
            } else if(leftCost == lowestCost) {
                neighbors.add(left);
            }
        }

        return neighbors;
    }

    record DirectedPosition(Position position, Direction direction) { }

    record Node(DirectedPosition d, int cost) { }
}
