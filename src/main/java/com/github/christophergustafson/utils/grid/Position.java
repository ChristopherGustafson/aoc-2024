package com.github.christophergustafson.utils.grid;

import java.util.Objects;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position move(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return new Position(x, y - 1);
            }
            case SOUTH -> {
                return new Position(x, y + 1);
            }
            case EAST -> {
                return new Position(x + 1, y);
            }
            case WEST -> {
                return new Position(x - 1, y);
            }
        }
        return null;
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
