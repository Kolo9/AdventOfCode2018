package com.kolo.adventofcode.y2019;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kolo.adventofcode.common.Direction;

final class Puzzle15 {
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private static final int EAST = 4;
    private static final Map<Integer, Direction> DIRECTION_MAP = new HashMap<>();
    static {
        DIRECTION_MAP.put(NORTH, Direction.NORTH);
        DIRECTION_MAP.put(SOUTH, Direction.SOUTH);
        DIRECTION_MAP.put(WEST, Direction.WEST);
        DIRECTION_MAP.put(EAST, Direction.EAST);
    }

    // :)
    private static int shortestPathLengthToOxygen = Integer.MAX_VALUE;
    private static Point oxygen = null;
    private static Set<Point> seen = new HashSet<>();
    private static Point cur = new Point(0, 0);
    private static Set<Point> walkablePoints = new HashSet<>();
    private static Set<Point> pointsWithOxygen = new HashSet<>();
    
    public static void main(String[] args) throws Exception {
        IntcodeComputer computer = new IntcodeComputer("in15");

        seen.add(new Point(cur));
        walkablePoints.add(new Point(cur));
        dfs(computer, new ArrayList<>(), 0);

        print();
        System.out.println("Part 1: " + shortestPathLengthToOxygen);

        int oxygenFloodTime = -1;
        Set<Point> next = new LinkedHashSet<>();
        next.add(oxygen);
        while (pointsWithOxygen.size() != walkablePoints.size()) {
            Set<Point> tmp = new LinkedHashSet<>();
            for (Point p : next) {
                pointsWithOxygen.add(p);
                for (Direction direction : Direction.values()) {
                    if (walkablePoints.contains(direction.apply(p))) {
                        tmp.add(direction.apply(p));
                    }
                }
            }
            oxygenFloodTime++;
            next = tmp;
        }
        
        
        System.out.println("Part 2: " + oxygenFloodTime);
        
    }

    private static final void dfs(IntcodeComputer computer, List<Integer> moves, int depth) throws Exception {
        if (computer.hasOutput() && computer.getLastOutput().intValueExact() == 2) {
            shortestPathLengthToOxygen = Math.min(depth, shortestPathLengthToOxygen);
            oxygen = new Point(cur);
            return;
        }
        for (int direction : Arrays.asList(NORTH, EAST, SOUTH, WEST)) {
            int oppositeDirection = direction + (direction % 2 == 0 ? -1 : 1); 
            cur = DIRECTION_MAP.get(direction).apply(cur);
            if (!seen.add(new Point(cur))) {                
                cur = DIRECTION_MAP.get(oppositeDirection).apply(cur);
                continue;
            }
            computer.addInput(direction).run();
            if (computer.getLastOutput().intValueExact() != 0) {
                walkablePoints.add(new Point(cur));
                moves.add(direction);
                dfs(computer, moves, depth + 1);
                computer.addInput(oppositeDirection).run();
                moves.remove(moves.size() - 1);
            }
            cur = DIRECTION_MAP.get(oppositeDirection).apply(cur);
        }
    }

    private static void print() {
        int minX = (int) walkablePoints.stream().mapToDouble(Point::getX).min().getAsDouble();
        int minY = (int) walkablePoints.stream().mapToDouble(Point::getY).min().getAsDouble();
        int maxX = (int) walkablePoints.stream().mapToDouble(Point::getX).max().getAsDouble();
        int maxY = (int) walkablePoints.stream().mapToDouble(Point::getY).max().getAsDouble();
        for (int y = minY - 1; y <= maxY + 1; y++) {
            for (int x = minX - 1; x <= maxX + 1; x++) {
                Point p = new Point(x, y);
                System.out.print((p.equals(oxygen) ? "!" : x == 0 && y == 0 ? "#" : pointsWithOxygen.contains(p) ? "~" : walkablePoints.contains(p) ? " " : "X") + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
 