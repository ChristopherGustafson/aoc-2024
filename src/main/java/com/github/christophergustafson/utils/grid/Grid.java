package com.github.christophergustafson.utils.grid;

public class Grid {

    private final char[][] grid;

    public Grid(char[][] grid) {
        this.grid = grid;
    }

    public int width() {
        return grid[0].length;
    }

    public int height() {
        return grid.length;
    }

    public char get(int x, int y) {
        return grid[y][x];
    }

    public char get(Position c) {
        return get(c.x, c.y);
    }

    public char getNext(Position position, Direction direction) {
        switch (direction) {
            case NORTH -> {
                return grid[position.y-1][position.x];
            }
            case SOUTH -> {
                return grid[position.y+1][position.x];
            }
            case EAST -> {
                return grid[position.y][position.x+1];
            }
            case WEST -> {
                return grid[position.y][position.x-1];
            }
        }
        // Should never happen
        return '-';
    }

    public Position find(char c) {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if(get(x, y) == c) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }
}

