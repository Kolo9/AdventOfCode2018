package com.kolo.adventofcode.y2019;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

final class Puzzle13 {

    private static class Tile {
        Point p;
        int type;
        Tile(Point p, int type) {
            this.p = p;
            this.type = type;
        }

        @Override
        public String toString() {
            switch (type) {
            case 0:
                return " ";
            case 1:
                return "W";
            case 2:
                return "=";
            case 3:
                return "-";
            case 4:
                return "O";
            default:
                throw new IllegalStateException("Unknown tile type: " + type);
            }
        }
    }

    public static void main(String[] args) {
        IntcodeComputer computer = new IntcodeComputer("in13");
        computer.run();

        int blockCount = 0;
        System.out.println(computer.getOutputList());
        for (int i = 2; i < computer.getOutputList().size(); i += 3) {
            System.out.println(computer.getOutput(i).intValueExact());
            if (computer.getOutput(i).intValueExact() == 2) {
                blockCount++;
            }
        }
        System.out.println("Part 1: " + blockCount);

        computer = new IntcodeComputer("in13");
        computer.setState(0, 2);
        computer.run();
        Map<Point, Tile> tiles = new HashMap<>();
        int maxX = 0;
        int maxY = 0;
        int score = 0;
        int outputPointer = 0;
        Point paddle = null;
        while (outputPointer + 2 < computer.getOutputList().size()) {
            if (computer.getOutput(outputPointer).intValueExact() == -1) {
                score = computer.getOutput(outputPointer + 2).intValueExact();
                outputPointer += 3;
                continue;
            }
            Point p = new Point(computer.getOutput(outputPointer++).intValueExact(), computer.getOutput(outputPointer++).intValueExact());
            maxX = Math.max(p.x, maxX);
            maxY = Math.max(p.y, maxY);
            Tile t = new Tile(p, computer.getOutput(outputPointer++).intValueExact());
            tiles.put(p, t);
            if (t.type == 3) {
                paddle = new Point(p);
            }
        }

        Point ball = null;
        while(!computer.done()) {
            print(tiles.values(), maxX, maxY);
            if (ball == null) {
                computer.addInput(0);
            } else if (ball.x > paddle.x) {
                computer.addInput(1);
            } else if (ball.x < paddle.x) { 
                computer.addInput(-1);
            } else {
                computer.addInput(0);
            }
            computer.run();

            while (outputPointer + 2 < computer.getOutputList().size()) {
                if (computer.getOutput(outputPointer).intValueExact() == -1) {
                    score = computer.getOutput(outputPointer + 2).intValueExact();
                    outputPointer += 3;
                    continue;
                }
                Point p = new Point(computer.getOutput(outputPointer++).intValueExact(), computer.getOutput(outputPointer++).intValueExact());
                Tile t = new Tile(p, computer.getOutput(outputPointer++).intValueExact());
                tiles.put(p, t);
                if (t.type == 3) {
                    paddle = new Point(p);
                }
                if (t.type == 4) {
                    ball = new Point(p);
                }
            }
        }
        print(tiles.values(), maxX, maxY);
        System.out.println("Part 2: " + score);
    }

    private static void print(Collection<Tile> tiles, int maxX, int maxY) {
        Tile[][] tilesArr = new Tile[maxY + 1][maxX + 1];
        tiles.forEach(t -> tilesArr[t.p.y][t.p.x] = t);
        for (int i = 0; i < tilesArr.length; i++) {
            for (int j = 0; j < tilesArr[i].length; j++) {
                System.out.print(tilesArr[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
