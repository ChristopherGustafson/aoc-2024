package com.github.christophergustafson.utils.grid;

import static java.lang.Math.abs;

import java.util.Objects;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position move(Direction direction) {
        return move(direction, 1);
    }

    public Position move(Direction direction, int steps) {
        switch (direction) {
            case NORTH -> {
                return new Position(x, y - steps);
            }
            case SOUTH -> {
                return new Position(x, y + steps);
            }
            case EAST -> {
                return new Position(x + steps, y);
            }
            case WEST -> {
                return new Position(x - steps, y);
            }
        }
        return null;
    }

    public int manhattanDistance(Position other) {
        return abs(other.x - this.x) + abs(other.y - this.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
