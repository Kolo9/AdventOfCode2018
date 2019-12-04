package com.kolo.adventofcode.y2019;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Puzzle03 {
    private static class Node extends Point {
        int steps;

        Node(int x, int y, int steps) {
            super(x, y);
            this.steps = steps;
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle03.class.getResource("in03").toURI()));
        Set<Node> wire1 = parseWire(input.get(0));
        Set<Node> wire2 = parseWire(input.get(1));
        List<Node> thisIsBad = new ArrayList<>(wire2);
        wire1.retainAll(wire2);
        
        System.out.println(wire1.stream().mapToInt(n -> Math.abs(n.x) + Math.abs(n.y)).min().getAsInt());
        System.out.println(wire1.stream().mapToInt(n -> n.steps + thisIsBad.get(thisIsBad.indexOf(n)).steps).min().getAsInt());
    }

    private static Set<Node> parseWire(String input) {
        Set<Node> wire = new HashSet<>();
        int x = 0;
        int y = 0;
        int steps = 1;
        for (String edge : input.split(",")) {
            char dir = edge.charAt(0);
            String length = edge.substring(1);
            for (int i = 0; i < Integer.parseInt(length); i++) {
                switch(dir) {
                case 'R':
                    x++;
                    break;
                case 'L':
                    x--;
                    break;
                case 'D':
                    y++;
                    break;
                case 'U':
                    y--;
                    break;
                }
                wire.add(new Node(x, y, steps++));

            }
        }
        return wire;
    }
}


