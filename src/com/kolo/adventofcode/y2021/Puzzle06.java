package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Puzzle06 {
    public static void main(String[] args) throws Exception {
        List<Integer> input = Arrays
                .stream(Files.readAllLines(Paths.get(Puzzle06.class.getResource("in06").toURI())).get(0).split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        Map<Integer, Long> lanternfishByTimer = new HashMap<>();
        for (int lanternfish : input) {
            lanternfishByTimer.put(lanternfish, lanternfishByTimer.getOrDefault(lanternfish, 0L) + 1);
        }

        for (int day = 0; day < 256; day++) {
            Map<Integer, Long> updatedLanternfishByTimer = new HashMap<>();
            for (int i = 0; i < 8; i++) {
                updatedLanternfishByTimer.put(i, lanternfishByTimer.getOrDefault(i + 1, 0L));
            }
            updatedLanternfishByTimer.put(6, updatedLanternfishByTimer.getOrDefault(6, 0L) + lanternfishByTimer.getOrDefault(0, 0L));
            updatedLanternfishByTimer.put(8, lanternfishByTimer.getOrDefault(0, 0L));
            lanternfishByTimer = updatedLanternfishByTimer;
            if (day == 79) {
                System.out.println(lanternfishByTimer.values().stream().mapToLong(Long::longValue).sum());
            }
        }
        System.out.println(lanternfishByTimer.values().stream().mapToLong(Long::longValue).sum());
    }
}