package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.math.Quantiles;

public class Puzzle07 {
    public static void main(String[] args) throws Exception {
        List<Integer> crabPositions = Arrays
                .stream(Files.readAllLines(Paths.get(Puzzle07.class.getResource("in07").toURI())).get(0).split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        int min = crabPositions.stream().min(Integer::compareTo).get();
        int max = crabPositions.stream().max(Integer::compareTo).get();

        int medianPosition = (int) Math.round(Quantiles.median().compute(crabPositions));
        System.out.println(crabPositions.stream().mapToInt(i -> Math.abs(medianPosition - i)).sum());

        // ¯\_(ツ)_/¯
        int bestFuelCost = Integer.MAX_VALUE;
        int bestPos = -1;
        for (int i = min; i <= max; i++) {
            int fuelCost = 0;
            for (int pos : crabPositions) {
                int n = Math.abs(pos - i);
                fuelCost += (n * (n+1)) / 2;
            }
            if (fuelCost > bestFuelCost) {
                break;
            }
            bestFuelCost = fuelCost;
            bestPos = i;
        }
        System.out.println(bestPos + " " + bestFuelCost);
    }
}