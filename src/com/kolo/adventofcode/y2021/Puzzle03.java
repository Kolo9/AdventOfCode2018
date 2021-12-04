package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;

public class Puzzle03 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle03.class.getResource("in03").toURI())).stream()
                .collect(Collectors.toList());

        int[] commonBits = getCommonBits(input);
        int[] uncommonBits = getUncommonBits(input);
        String gamma = Arrays.stream(commonBits).mapToObj(String::valueOf).collect(Collectors.joining(""));
        String epsilon = Arrays.stream(uncommonBits).mapToObj(String::valueOf).collect(Collectors.joining(""));
        System.out.println(Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2));

        List<String> remaining = new ArrayList<>(input);
        int i = 0;
        while (remaining.size() > 1) {
            final int[] commonBitsFinal = commonBits;
            final int iFinal = i;
            remaining.removeIf(s -> !(("" + s.charAt(iFinal)).equals("" + commonBitsFinal[iFinal])));
            commonBits = getCommonBits(remaining);
            i++;
        }
        String oxygenGeneratorRating = Iterables.getOnlyElement(remaining);

        remaining = new ArrayList<>(input);
        i = 0;
        while (remaining.size() > 1) {
            final int[] uncommonBitsFinal = uncommonBits;
            final int iFinal = i;
            remaining.removeIf(s -> !(("" + s.charAt(iFinal)).equals("" + uncommonBitsFinal[iFinal])));
            uncommonBits = getUncommonBits(remaining);
            i++;
        }
        String co2ScrubberRating = Iterables.getOnlyElement(remaining);
        
        System.out.println(Integer.parseInt(oxygenGeneratorRating, 2) * Integer.parseInt(co2ScrubberRating, 2));
    }

    private static int[] getCommonBits(List<String> in) {
        int[] bitSums = new int[in.get(0).length()];
        for (String line : in) {
            for (int i = 0; i < line.length(); i++) {
                bitSums[i] += Integer.parseInt("" + line.charAt(i));
            }
        }
        return Arrays.stream(bitSums).map(sum -> sum >= in.size() / 2f ? 1 : 0).toArray();
    }

    private static int[] getUncommonBits(List<String> in) {
        return Arrays.stream(getCommonBits(in)).map(bit -> bit == 1 ? 0 : 1).toArray();
    }

    // private static int getPrefixLength(String in, int[] desired) {
    // for (int i = 0; i < in.length(); i++) {
    // if (!("" + in.charAt(i)).equals("" + desired[i])) {
    // System.out.println(in + " and " + Arrays.toString(desired) + " is: " +
    // i);
    // return i;
    // }
    // }
    // return in.length();
    // }
}
