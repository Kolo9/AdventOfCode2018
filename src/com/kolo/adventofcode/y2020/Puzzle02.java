package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

class Puzzle02 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle02.class.getResource("in02").toURI()));
        int validCountPart1 = 0;
        for (String line : input) {
            if (checkPasswordPart1(line)) {
                validCountPart1++;
            }
        }
        System.out.println(validCountPart1);

        int validCountPart2 = 0;
        for (String line : input) {
            if (checkPasswordPart2(line)) {
                validCountPart2++;
            }
        }
        System.out.println(validCountPart2);
    }

    private static boolean checkPasswordPart1(String line) {
        String[] lineSplit = line.split(" ");
        List<Integer> letterCountRange = Arrays.stream(lineSplit[0].split("-")).map(Integer::parseInt).collect(Collectors.toList());
        lineSplit[1] = lineSplit[1].substring(0, lineSplit[1].length() - 1);
        long charCount = lineSplit[2].chars().filter(c -> c == lineSplit[1].charAt(0)).count();
        return charCount >= letterCountRange.get(0) && charCount <= letterCountRange.get(1); 
    }

    private static boolean checkPasswordPart2(String line) {
        String[] lineSplit = line.split(" ");
        List<Integer> indices = Arrays.stream(lineSplit[0].split("-")).map(Integer::parseInt).collect(Collectors.toList());
        lineSplit[1] = lineSplit[1].substring(0, lineSplit[1].length() - 1);
        int firstPos = indices.get(0) - 1;
        int secondPos = indices.get(1) - 1; 
        char expectedChar = lineSplit[1].charAt(0);
        return lineSplit[2].charAt(firstPos) == expectedChar ^ lineSplit[2].charAt(secondPos) == expectedChar; 
    }
}
