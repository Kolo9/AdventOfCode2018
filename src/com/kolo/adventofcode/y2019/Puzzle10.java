package com.kolo.adventofcode.y2019;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.LinkedListMultimap;

final class Puzzle10 {

    static class Cell extends Point {
        char c;
        int numVisible = 0;
        Cell(int x, int y, char c) {
            super(x, y);
            this.c = c;
        }

        double slope(Cell other) {
            if (other.x == x) {
                if (other.y > y) {
                    return 99999999;
                }
                return -99999999;
            }
            return (other.y - y) / (double)(other.x - x); 
        }

        double angle(Cell other) {
            return Math.atan2(other.y - y, other.x - x) * 180 / (2 * Math.PI);            
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", x, y);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle03.class.getResource("in10").toURI()));
        Cell[][] belt = new Cell[input.size()][input.get(0).length()];
        List<Cell> asteroids = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).length(); j++) {
                belt[i][j] = new Cell(j, i, input.get(i).charAt(j));
                if (input.get(i).charAt(j) == '#') {
                    asteroids.add(belt[i][j]);
                }
            }
        }

        for (Cell a1 : asteroids) {
            for (Cell a2 : asteroids) {
                boolean visible = true;
                if (a1 == a2) {
                    continue;
                }

                for (Cell a3 : asteroids) {
                    if (a1 == a3 || a2 == a3) {
                        continue;
                    }
                    if (a1.distanceSq(a3) > a1.distanceSq(a2)) {
                        continue;
                    }
                    if (a1.slope(a3) != a1.slope(a2)) {
                        continue;
                    }
                    if (((a1.x - a3.x > 0) != (a1.x - a2.x > 0)) || ((a1.y - a3.y > 0) != (a1.y - a2.y > 0))) {
                        continue;
                    }
                    visible = false;
                    break;
                }

                if (visible) {
                    a1.numVisible++;
                }
             }
        }
        print(belt);

        int part1 = asteroids.stream().mapToInt(a -> a.numVisible).max().getAsInt();
        System.out.println(part1);
               
     
        // Part 2.
        Cell station = asteroids.stream().filter(a -> a.numVisible == part1).findFirst().get();
        asteroids.removeIf(a -> a == station);
        LinkedListMultimap<Double, Cell> asteroidAngles = LinkedListMultimap.create();
        asteroids.sort((a1, a2) -> (int) (station.distanceSq(a1) - station.distanceSq(a2)));
        for (Cell a : asteroids) {
            double angle = station.angle(a);
            angle -= 315;
            while (angle < 0) {
                angle += 360;
            }
            asteroidAngles.put(angle, a);
        }
        List<Double> orderedAngles = asteroidAngles.keySet().stream().sorted().collect(Collectors.toList());
        Cell lastDestroyed = null;
        int angleIndex = 0;
        for (int numDestroyed = 0; numDestroyed < 200; numDestroyed++) {
            double nextAngle = orderedAngles.get(angleIndex);
            while (asteroidAngles.get(nextAngle).isEmpty()) {
                angleIndex++;
                angleIndex %= orderedAngles.size();
                nextAngle = orderedAngles.get(angleIndex); 
            }            
            lastDestroyed = asteroidAngles.get(nextAngle).remove(0);
            angleIndex++;
            angleIndex %= orderedAngles.size();
        }
        System.out.println(lastDestroyed);
    }

    private static void print(Cell[][] belt) {
        for (int i = 0; i < belt.length; i++) {
            for (int j = 0; j < belt[i].length; j++) {
                System.out.print(belt[i][j]);
            }
            System.out.println();
        }
    }
}
