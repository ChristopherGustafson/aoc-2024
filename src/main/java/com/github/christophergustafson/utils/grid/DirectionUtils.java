package com.github.christophergustafson.utils.grid;

public class DirectionUtils {

    public static Direction turnLeft(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return Direction.WEST;
            }
            case SOUTH -> {
                return Direction.EAST;
            }
            case EAST -> {
                return Direction.NORTH;
            }
            case WEST -> {
                return Direction.SOUTH;
            }
        }
        return null;
    }

    public static Direction turnRight(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return Direction.EAST;
            }
            case SOUTH -> {
                return Direction.WEST;
            }
            case EAST -> {
                return Direction.SOUTH;
            }
            case WEST -> {
                return Direction.NORTH;
            }
        }
        return null;
    }

    public static Direction opposite(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return Direction.SOUTH;
            }
            case SOUTH -> {
                return Direction.NORTH;
            }
            case EAST -> {
                return Direction.WEST;
            }
            case WEST -> {
                return Direction.EAST;
            }
        }
        return null;
    }

}
