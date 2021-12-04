package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle01 {
    public static void main(String[] args) throws Exception {
        List<Integer> input = Files.readAllLines(Paths.get(Puzzle01.class.getResource("in01").toURI())).stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        int singleElementIncreases = 0;
        for (int i = 1; i < input.size(); i++) {
            if (input.get(i) > input.get(i - 1)) {
                singleElementIncreases++;
            }
        }
        int threeElementIncreases = 0;
        for (int i = 3; i < input.size(); i++) {
            if (input.get(i) > input.get(i - 3)) {
                threeElementIncreases++;
            }
        }
        System.out.println(singleElementIncreases);
        System.out.println(threeElementIncreases);
    }
}
