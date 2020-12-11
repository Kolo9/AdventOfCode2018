package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

class Puzzle10 {
    public static void main(String[] args) throws Exception {
        List<Integer> adapters = Files.readAllLines(Paths.get(Puzzle10.class.getResource("in10").toURI())).stream()
                .map(Integer::parseInt).collect(Collectors.toList());
        Collections.sort(adapters);
        // Outlet.
        adapters.add(0, 0);
        // Device built-in.
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        Multiset<Integer> diffCounts = HashMultiset.create();
        for (int i = 1; i < adapters.size(); i++) {
            int prev = adapters.get(i - 1);
            int cur = adapters.get(i);
            diffCounts.add(cur - prev);
        }
        System.out.println(diffCounts.count(1) * diffCounts.count(3));

        System.out.println(countArrangements(adapters));
    }

    private static long countArrangements(List<Integer> adapters) {
        long[] d = new long[adapters.size()];
        d[0] = 1;
        d[1] = 1;
        d[2] = adapters.get(2) - adapters.get(0) > 3 ? 1 : 2;
        for (int i = 3; i < adapters.size(); i++) {
            d[i] = d[i - 1];
            if (adapters.get(i) - adapters.get(i - 2) <= 3) {
                d[i] += d[i - 2];
            }
            if (adapters.get(i) - adapters.get(i - 3) <= 3) {
                d[i] += d[i - 3];
            }
        }
        return d[d.length - 1];
    }
}