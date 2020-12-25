package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

class Puzzle13 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Puzzle13.class.getResource("in13").toURI())).stream()
                .collect(Collectors.toList());
        int curTime = Integer.parseInt(lines.get(0));
        String[] buses = lines.get(1).split(",");
        List<Integer> validBuses = Arrays.stream(buses).filter(s -> !s.equals("x")).map(Integer::parseInt)
                .collect(Collectors.toList());
        int closestBusId = -1;
        int closestBusTime = Integer.MAX_VALUE;
        for (int i = 0; i < validBuses.size(); i++) {
            int busId = validBuses.get(i);
            int busTime = busId * (int) Math.ceil((double) curTime / busId);
            if (busTime < closestBusTime) {
                closestBusId = busId;
                closestBusTime = busTime;
            }
        }
        System.out.println(closestBusId * (closestBusTime - curTime));

        List<List<Integer>> busesWithMod = IntStream.rangeClosed(0, buses.length - 1).boxed()
                .filter(i -> !buses[i].equals("x")).map(mod -> Lists
                        .<Integer> newArrayList(Integer.parseInt(buses[mod]), mod % Integer.parseInt(buses[mod])))
                .collect(Collectors.toList());
        for (List<Integer> busWithMod : busesWithMod) {
            busWithMod.set(1, (busWithMod.get(0) - busWithMod.get(1)) % busWithMod.get(0));
        }
        Collections.sort(busesWithMod, (b1, b2) -> b2.get(0) - b1.get(0));
        long curMultiple = busesWithMod.get(0).get(0);
        long curValue = busesWithMod.get(0).get(1);
        System.out.println(busesWithMod);
        for (int i = 1; i < busesWithMod.size(); i++) {
            List<Integer> busWithMod = busesWithMod.get(i);
            System.out.println("Bus " + busWithMod.get(0));
            while (curValue % busWithMod.get(0) != busWithMod.get(1)) {
                curValue += curMultiple;
            }
            curMultiple *= busWithMod.get(0);
        }
        System.out.println(curValue);
    }
}