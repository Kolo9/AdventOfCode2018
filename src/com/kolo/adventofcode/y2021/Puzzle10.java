package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Puzzle10 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle10.class.getResource("in10").toURI())).stream()
                .collect(Collectors.toList());
        Map<Character, Character> openClosePair = new HashMap<>();
        openClosePair.put('(', ')');
        openClosePair.put('[', ']');
        openClosePair.put('{', '}');
        openClosePair.put('<', '>');
        Map<Character, Integer> illegalCharValues = new HashMap<>();
        illegalCharValues.put(')', 3);
        illegalCharValues.put(']', 57);
        illegalCharValues.put('}', 1197);
        illegalCharValues.put('>', 25137);
        Map<Character, Integer> addedCharValues = new HashMap<>();
        addedCharValues.put(')', 1);
        addedCharValues.put(']', 2);
        addedCharValues.put('}', 3);
        addedCharValues.put('>', 4);

        int illegalCharScore = 0;
        List<Long> addedCharScores = new ArrayList<>();
        for (String line : input) {
            Deque<Character> stack = new ArrayDeque<Character>();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (openClosePair.containsKey(c)) {
                    stack.push(c);
                } else if (stack.isEmpty() || c != openClosePair.get(stack.pop())) {
                    illegalCharScore += illegalCharValues.get(c);
                    stack.clear();
                    break;
                }
            }
            if (stack.isEmpty()) {
                continue;
            }
            long lineAddedCharScore = 0;
            for (char c : stack) {
                lineAddedCharScore *= 5;
                lineAddedCharScore += addedCharValues.get(openClosePair.get(c));
            }
            addedCharScores.add(lineAddedCharScore);
        }
        Collections.sort(addedCharScores);
        
        System.out.println(illegalCharScore);
        System.out.println(addedCharScores.get(addedCharScores.size() / 2));
    }
}
