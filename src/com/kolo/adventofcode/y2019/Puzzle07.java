package com.kolo.adventofcode.y2019;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

final class Puzzle07 {

    public static void main(String[] args) {
        Integer[] input = new Integer[] { 3, 8, 1001, 8, 10, 8, 105, 1, 0, 0, 21, 34, 59, 76, 101, 114, 195, 276, 357, 438, 99999, 3, 9, 1001, 9, 4, 9, 1002, 9, 4, 9, 4, 9, 99, 3, 9, 102, 4, 9, 9, 101, 2, 9, 9, 102, 4, 9, 9, 1001, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 101, 4, 9, 9, 102, 5, 9, 9, 101, 5, 9, 9, 4, 9, 99, 3, 9, 102, 2, 9, 9, 1001, 9, 4, 9, 102, 4, 9, 9, 1001, 9, 4, 9, 1002, 9, 3, 9, 4, 9, 99, 3, 9, 101, 2, 9, 9, 1002, 9, 3, 9, 4, 9, 99, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 99, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 99, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1001, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 99, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 102, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 99, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 3, 9, 101, 1, 9, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1002, 9, 2, 9, 4, 9, 3, 9, 1001, 9, 1, 9, 4, 9, 3, 9, 101, 2, 9, 9, 4, 9, 99 };

        List<Integer> possiblePhasesPart1 = ImmutableList.of(0, 1, 2, 3, 4);
        int maxSignalPart1 = Integer.MIN_VALUE;

        for (List<Integer> phases : Collections2.permutations(possiblePhasesPart1)) {
            List<IntcodeComputer> amps = setUpAmps(input, phases);

            int previousOutput = 0;
            for (IntcodeComputer amp : amps) {
                amp.addInput(previousOutput).run();
                previousOutput = amp.getLastOutput();
            }
            maxSignalPart1 = Math.max(maxSignalPart1, previousOutput);
        }

        // Part 2.

        List<Integer> possiblePhasesPart2 = ImmutableList.of(5, 6, 7, 8, 9);
        int maxSignalPart2 = Integer.MIN_VALUE;

        for (List<Integer> phases : Collections2.permutations(possiblePhasesPart2)) {
            List<IntcodeComputer> amps = setUpAmps(input, phases);

            int previousOutput = 0;
            boolean done = false;
            while (!done) {
                done = true;
                for (IntcodeComputer amp : amps) {
                    amp.addInput(previousOutput).run();
                    previousOutput = amp.getLastOutput();
                    if (!amp.done()) {
                        done = false;
                    }
                }
            }
            maxSignalPart2 = Math.max(maxSignalPart2, amps.get(4).getLastOutput());
        }

        System.out.println("Max signal part 1: " + maxSignalPart1);
        System.out.println("Max signal part 2: " + maxSignalPart2);
    }

    private static List<IntcodeComputer> setUpAmps(Integer[] input, List<Integer> phases) {
        List<IntcodeComputer> amps = new ArrayList<>();
        for (int i = 0; i < phases.size(); i++) {
            int phase = phases.get(i);
            amps.add(new IntcodeComputer(input).addInput(phase));
        }
        return amps;
    }
}
