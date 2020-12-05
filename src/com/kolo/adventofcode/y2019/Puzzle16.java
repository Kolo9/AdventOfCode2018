package com.kolo.adventofcode.y2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

public final class Puzzle16 {

    public static void main(String[] args) {
        String inputString = "59756772370948995765943195844952640015210703313486295362653878290009098923609769261473534009395188480864325959786470084762607666312503091505466258796062230652769633818282653497853018108281567627899722548602257463608530331299936274116326038606007040084159138769832784921878333830514041948066594667152593945159170816779820264758715101494739244533095696039336070510975612190417391067896410262310835830006544632083421447385542256916141256383813360662952845638955872442636455511906111157861890394133454959320174572270568292972621253460895625862616228998147301670850340831993043617316938748361984714845874270986989103792418940945322846146634931990046966552";
        System.out.println("Part 1: " + part1(inputString));
        System.out.println("Part 2: " + part2(inputString));
    }

    public static String part1(String cur) {
        String next = "";
        int[] basePattern = new int[]{0, 1, 0, -1};
        for (int phase = 0; phase < 100; phase++) {
            for (int outputIndex = 1; outputIndex <= cur.length(); outputIndex++) {
                int output = 0;
                for (int i = 0; i < cur.length(); i++) {
                    int inputDigit = Integer.parseInt("" + cur.charAt(i));
                    int pattern = basePattern[((i + 1) / outputIndex) % 4];
                    output += inputDigit * pattern;
                }
                next += Math.abs(output % 10);
            }
            cur = next;
            next = "";
        }
        return cur.substring(0, 8);
    }

    public static String part2(String inputString) {
        List<Integer> input = Arrays.stream(inputString.split("")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        int offset = Integer.parseInt(inputString.substring(0, 7));
        int totalLength = input.size() * 10_000;
        int necessary = totalLength - offset;
        int fullRepeats = necessary / input.size();
        int remaining = necessary % input.size();
        List<Integer> cur = new ArrayList<>(necessary);
        cur.addAll(input.subList(input.size() - remaining, input.size()));
        while (fullRepeats-- > 0) {
            cur.addAll(input);
        }
        Preconditions.checkState(cur.size() == necessary);

        for (int i = 0; i < 100; i++) {
            for (int j = cur.size() - 2; j >= 0; j--) {
                cur.set(j, (cur.get(j) + cur.get(j + 1)) % 10);
            }
        }
        
        return Joiner.on("").join(cur.subList(0, 8));
    }
}


