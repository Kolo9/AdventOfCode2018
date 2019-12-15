package com.kolo.adventofcode.common;

import java.awt.Point;


public enum Direction {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    
    int dx;
    int dy;
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction cw() {
        int i = ordinal() + 1;
        i %= Direction.values().length;
        return Direction.values()[i];
    }

    public Direction ccw() {
        int i = ordinal() + 3;
        i %= Direction.values().length;
        return Direction.values()[i];
    }

    public Point apply(Point p) {
        return new Point(p.x + dx, p.y + dy);
    }
}

