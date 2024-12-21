package com.github.christophergustafson;

import static com.github.christophergustafson.utils.ParseUtils.parse2DGrid;
import static com.github.christophergustafson.utils.ParseUtils.readInputFile;

@SuppressWarnings("Duplicates")
public class Day12 {

    public static void main(String[] args) {

        String testInput = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE""";
        String realInput = readInputFile("day12.txt");

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

        boolean[][] checked = new boolean[grid.length][grid[0].length];

        int result = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (!checked[y][x]) {
                    Price regionPrice = checkPrice(grid, checked, y, x);
                    result += regionPrice.area * regionPrice.perimeter;
                }
            }
        }
        return result;
    }

    static Price checkPrice(char[][] grid, boolean[][] checked, int y, int x) {
        int area = 0;
        int perimeter = 0;
        char plant = grid[y][x];
        if (!checked[y][x]) {
            checked[y][x] = true;
            area++;
            try {
                if (grid[y + 1][x] != plant) {
                    perimeter++;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                perimeter++;
            }
            try {
                if (grid[y - 1][x] != plant) {
                    perimeter++;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                perimeter++;
            }
            try {
                if (grid[y][x + 1] != plant) {
                    perimeter++;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                perimeter++;
            }
            try {
                if (grid[y][x - 1] != plant) {
                    perimeter++;
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
                perimeter++;
            }
        }

        // Check south
        try {
            if (grid[y + 1][x] == plant && !checked[y + 1][x]) {
                Price southPrice = checkPrice(grid, checked, y + 1, x);
                area += southPrice.area;
                perimeter += southPrice.perimeter;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        // Check north
        try {
            if (grid[y - 1][x] == plant && !checked[y - 1][x]) {
                Price northPrice = checkPrice(grid, checked, y - 1, x);
                area += northPrice.area;
                perimeter += northPrice.perimeter;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        // Check east
        try {
            if (grid[y][x + 1] == plant && !checked[y][x + 1]) {
                Price eastPrice = checkPrice(grid, checked, y, x + 1);
                area += eastPrice.area;
                perimeter += eastPrice.perimeter;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        // Check west
        try {
            if (grid[y][x - 1] == plant && !checked[y][x - 1]) {
                Price westPrice = checkPrice(grid, checked, y, x - 1);
                area += westPrice.area;
                perimeter += westPrice.perimeter;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return new Price(area, perimeter);
    }

    record Price(int area, int perimeter) { }

    static int part2(char[][] grid) {

        boolean[][] checked = new boolean[grid.length][grid[0].length];

        int result = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (!checked[y][x]) {
                    DiscountedPrice regionPrice = checkDiscountedPrice(grid, checked, y, x);
                    result += regionPrice.area * regionPrice.sides;
                }
            }
        }
        return result;
    }

    static DiscountedPrice checkDiscountedPrice(char[][] grid, boolean[][] checked, int y, int x) {
        int area = 0;
        int sides = 0;
        char plant = grid[y][x];
        if (!checked[y][x]) {
            checked[y][x] = true;
            area++;

            boolean southIsSamePlant = y + 1 < grid.length && grid[y + 1][x] == plant;
            boolean northIsSamePlant = y - 1 > -1 && grid[y - 1][x] == plant;
            boolean eastIsSamePlant = x + 1 < grid[0].length && grid[y][x + 1] == plant;
            boolean westIsSamePlant = x - 1 > -1 && grid[y][x - 1] == plant;

            // South-east
            boolean soutEastIsSamePlant = y + 1 < grid.length
                && x + 1 < grid[0].length
                && grid[y + 1][x + 1] == plant;
            // Outwards corner
            if (!southIsSamePlant && !eastIsSamePlant) {
                sides++;
            }
            // Inwards corner
            if(southIsSamePlant && eastIsSamePlant && !soutEastIsSamePlant) {
                sides++;
            }

            // South-west
            boolean southWestIsSamePlant = y + 1 < grid.length
                && x - 1 > -1
                && grid[y + 1][x - 1] == plant;
            // Outwards corner
            if (!southIsSamePlant && !westIsSamePlant) {
                sides++;
            }
            // Inwards corner
            if(southIsSamePlant && westIsSamePlant && !southWestIsSamePlant) {
                sides++;
            }

            // North-east
            boolean northEastIsSamePlant = y - 1 > -1
                && x + 1 < grid[0].length
                && grid[y - 1][x + 1] == plant;
            // Outwards corner
            if (!northIsSamePlant && !eastIsSamePlant) {
                sides++;
            }
            // Inwards corner
            if(northIsSamePlant && eastIsSamePlant && !northEastIsSamePlant) {
                sides++;
            }

            // North-west
            boolean northWestIsSamePlant = y - 1 > -1
                && x - 1 > -1
                && grid[y - 1][x - 1] == plant;
            // Outwards corner
            if (!northIsSamePlant && !westIsSamePlant) {
                sides++;
            }
            // Inwards corner
            if(northIsSamePlant && westIsSamePlant && !northWestIsSamePlant) {
                sides++;
            }
        }

        // Check south
        try {
            if (grid[y + 1][x] == plant && !checked[y + 1][x]) {
                DiscountedPrice southPrice = checkDiscountedPrice(grid, checked, y + 1, x);
                area += southPrice.area;
                sides += southPrice.sides;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        // Check north
        try {
            if (grid[y - 1][x] == plant && !checked[y - 1][x]) {
                DiscountedPrice northPrice = checkDiscountedPrice(grid, checked, y - 1, x);
                area += northPrice.area;
                sides += northPrice.sides;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        // Check east
        try {
            if (grid[y][x + 1] == plant && !checked[y][x + 1]) {
                DiscountedPrice eastPrice = checkDiscountedPrice(grid, checked, y, x + 1);
                area += eastPrice.area;
                sides += eastPrice.sides;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        // Check west
        try {
            if (grid[y][x - 1] == plant && !checked[y][x - 1]) {
                DiscountedPrice westPrice = checkDiscountedPrice(grid, checked, y, x - 1);
                area += westPrice.area;
                sides += westPrice.sides;
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return new DiscountedPrice(area, sides);
    }

    record DiscountedPrice(int area, int sides) { }
}
