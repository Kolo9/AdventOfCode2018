package com.kolo.adventofcode.y2019;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

final class Puzzle11 {

    private static final int BLACK = 0;
    private static final int WHITE = 1;    
    private static enum Direction {
        NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
        
        int dx;
        int dy;
        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Direction cw() {
            int i = ordinal() + 1;
            i %= Direction.values().length;
            return Direction.values()[i];
        }

        Direction ccw() {
            int i = ordinal() + 3;
            i %= Direction.values().length;
            return Direction.values()[i];
        }

        Point apply(Point p) {
            return new Point(p.x + dx, p.y + dy);
        }
    }
    private static class PaintingRobot {
        Point pos = new Point(0, 0);
        Direction direction = Direction.NORTH;
        
        
        void step() {
            this.pos = this.direction.apply(this.pos);
        }
    }

    public static void main(String[] args) {
        Map<Point, Integer> hull = new HashMap<>();   
        PaintingRobot robot = new PaintingRobot();
        IntcodeComputer computer = new IntcodeComputer("in11");

        int step = 0;
        while (!computer.done()) {
            computer.addInput(hull.getOrDefault(robot.pos, BLACK));
            computer.run();
            int colorToPaint = computer.getOutput(step++).intValueExact();
            robot.direction = computer.getOutput(step++).intValueExact() == 0 ? robot.direction.ccw() : robot.direction.cw();
            hull.put(robot.pos, colorToPaint);
            robot.step();
        }

        System.out.println("Part 1: " + hull.size());

        hull = new HashMap<>();
        robot = new PaintingRobot();
        computer = new IntcodeComputer("in11");
        step = 0;

        hull.put(robot.pos, 1);
        while (!computer.done()) {
            computer.addInput(hull.getOrDefault(robot.pos, BLACK));
            computer.run();
            int colorToPaint = computer.getOutput(step++).intValueExact();
            robot.direction = computer.getOutput(step++).intValueExact() == 0 ? robot.direction.ccw() : robot.direction.cw();
            hull.put(robot.pos, colorToPaint);
            robot.step();
        }
        
        int minX = hull.keySet().stream().mapToInt(p -> p.x).min().getAsInt();
        int minY = hull.keySet().stream().mapToInt(p -> p.y).min().getAsInt();
        int maxX = hull.keySet().stream().mapToInt(p -> p.x).max().getAsInt();
        int maxY = hull.keySet().stream().mapToInt(p -> p.y).max().getAsInt();
        int[][] hullArr = new int[maxY - minY + 1][maxX - minX + 1];
        final Map<Point, Integer> hullFinal = hull;
        hullFinal.keySet().forEach(p -> hullArr[p.y - minY][p.x - minX] = hullFinal.get(p));
        
        System.out.println("Part 2:");
        for (int i = 0; i < hullArr.length; i++) {
            for (int j = 0; j < hullArr[i].length; j++) {
                System.out.print(hullArr[i][j] == WHITE ? "#" : " ");
            }
            System.out.println();
        }
    }

}
