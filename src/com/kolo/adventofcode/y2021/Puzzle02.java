package com.kolo.adventofcode.y2021;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.kolo.adventofcode.common.Direction;

public class Puzzle02 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle02.class.getResource("in02").toURI())).stream()
                .collect(Collectors.toList());
        Point pos = new Point(0, 0);
        for (String command : input) {
            String[] commandComponents = command.split(" ");
            Direction dir;
            switch (commandComponents[0]) {
            case "forward":
                dir = Direction.EAST;
                break;
            case "down":
                dir = Direction.SOUTH;
                break;
            case "up":
                dir = Direction.NORTH;
                break;
            default:
                throw new IllegalStateException("Unknown direction: " + commandComponents[0]);
            }

            for (int i = 0; i < Integer.parseInt(commandComponents[1]); i++) {
                pos = dir.apply(pos);
            }
        }

        System.out.println(pos.x * pos.y);

        pos = new Point(0, 0);
        int aim = 0;
        for (String command : input) {
            String[] commandComponents = command.split(" ");
            String dir = commandComponents[0];
            int amt = Integer.parseInt(commandComponents[1]);
            switch (commandComponents[0]) {
            case "forward":
                pos.x += amt;
                pos.y += amt * aim;
                break;
            case "down":
                aim += amt;
                break;
            case "up":
                aim -= amt;
                break;
            default:
                throw new IllegalStateException("Unknown direction: " + commandComponents[0]);
            }
        }
        System.out.println(pos.x * pos.y);
    }
}
