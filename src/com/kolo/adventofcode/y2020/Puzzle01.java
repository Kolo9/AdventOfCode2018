package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

class Puzzle01 {
    public static void main(String[] args) throws Exception {
        // Assumes no duplicates.

        Set<Integer> input = Files.readAllLines(Paths.get(Puzzle01.class.getResource("in01").toURI())).stream().map(Integer::parseInt).collect(Collectors.toSet());
        List<Integer> twoSum = findTwoSum(input, 2020, ImmutableSet.of());
        System.out.println(twoSum);
        System.out.println(twoSum.get(0) * twoSum.get(1));

        twoSum = null;
        for (int i : input) {
            int j = 2020 - i;
            twoSum = findTwoSum(input, j, ImmutableSet.of(i));
            if (twoSum != null) {
                System.out.println(i + " " + twoSum);
                System.out.println(i * twoSum.get(0) * twoSum.get(1));
                break;
            }
        }
    }

    private static List<Integer> findTwoSum(Set<Integer> input, int sum, Set<Integer> toIgnore) {
        for (int i : input) {
            if (toIgnore.contains(i)) {
                continue;
            }
            int j = sum - i;
            if (i == j) {
                continue;
            }
            if (input.contains(j)) {
                return ImmutableList.of(i, j);
            }
        }
        return null;
    }
}
