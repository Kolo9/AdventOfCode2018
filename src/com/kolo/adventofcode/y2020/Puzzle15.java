package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Puzzle15 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Puzzle15.class.getResource("in15").toURI())).stream()
                .collect(Collectors.toList());
       List<Integer> sequence = Arrays.asList(lines.get(0).split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
       Map<Integer, Integer> lastSeen = new HashMap<>();
       int i = 0;
       for (i = 0; i < sequence.size() - 1; i++) {
           lastSeen.put(sequence.get(i), i);
       }

       while (sequence.size() < 30_000_000) {
           int prev = sequence.get(sequence.size() - 1);
           if (lastSeen.containsKey(prev)) {
               sequence.add(i - lastSeen.get(prev));
           } else {
               sequence.add(0);
           }
           lastSeen.put(sequence.get(sequence.size() - 2), i++);
       }
       System.out.println(sequence.get(2020 - 1));
       System.out.println(sequence.get(30_000_000 - 1));
    }
}