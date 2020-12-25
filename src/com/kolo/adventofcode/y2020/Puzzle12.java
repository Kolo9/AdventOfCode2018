package com.kolo.adventofcode.y2020;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.kolo.adventofcode.common.Direction;

class Puzzle12 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Puzzle12.class.getResource("in12").toURI())).stream().collect(Collectors.toList());
        part1(lines);
        part2(lines);
    }

    private static void part1(List<String> lines) {
        Direction dir = Direction.EAST;
        Point loc = new Point(0, 0);
        for (String line : lines) {
            char action = line.charAt(0);
            int amount = Integer.parseInt(line.substring(1));
            switch(action) {
            case 'N':
                loc.y -= amount;
                break;
            case 'S':
                loc.y += amount;
                break;
            case 'W':
                loc.x -= amount;
                break;
            case 'E':
                loc.x += amount;
                break;
            case 'L':
                for (int i = 0; i < amount / 90; i++) {
                    dir = dir.ccw();
                }
                break;
            case 'R':
                for (int i = 0; i < amount / 90; i++) {
                    dir = dir.cw();
                }
                break;
            case 'F':
                for (int i = 0; i < amount; i++) {
                    loc = dir.apply(loc);
                }
                break;
            }
        }
        System.out.println(Math.abs(loc.x) + Math.abs(loc.y));
    }

    private static void part2(List<String> lines) {
        Point loc = new Point(0, 0);
        Point waypointOffset = new Point(10, -1);
        for (String line : lines) {
            char action = line.charAt(0);
            int amount = Integer.parseInt(line.substring(1));
            switch(action) {
            case 'N':
                waypointOffset.y -= amount;
                break;
            case 'S':
                waypointOffset.y += amount;
                break;
            case 'W':
                waypointOffset.x -= amount;
                break;
            case 'E':
                waypointOffset.x += amount;
                break;
            case 'L':
                // -y -> -x
                // -x -> +y
                // +y -> +x
                // +x -> -y
                for (int i = 0; i < amount / 90; i++) {
                    waypointOffset.setLocation(waypointOffset.y, -waypointOffset.x);
                }
                break;
            case 'R':
                // -y -> +x
                // +x -> +y
                // +y -> -x
                // -x -> -y
                for (int i = 0; i < amount / 90; i++) {
                    waypointOffset.setLocation(-waypointOffset.y, waypointOffset.x);
                }
                break;
            case 'F':
                loc.setLocation(loc.x + amount * waypointOffset.x, loc.y + amount * waypointOffset.y);
                break;
            }
        }
        System.out.println(Math.abs(loc.x) + Math.abs(loc.y));
    }
}