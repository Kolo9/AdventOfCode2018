package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class Puzzle08 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle08.class.getResource("in08").toURI())).stream()
                .collect(Collectors.toList());

        List<Integer> uniqueSegmentCounts = Arrays.asList(2, 3, 4, 7);
        // Part 1.
        int uniqueSegmentDigitCount = 0;
        for (String line : input) {
            line = line.substring(line.indexOf("|") + 1);
            String[] digits = line.split(" ");
            for (String digit : digits) {
                if (uniqueSegmentCounts.contains(digit.length())) {
                    uniqueSegmentDigitCount++;
                }
            }
        }
        System.out.println(uniqueSegmentDigitCount);

        // Part 2.
        // TODO(never): are the digits unique on {# segments, segment rarity}?
        List<String> digitSegments = Arrays.asList("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf",
                "abcdefg", "abcdfg");
        int sumOfDecodedNumbers = 0; 
        for (String line : input) {
            String[] mappingsAndDigits = line.split("\\|");
            String[] mappings = mappingsAndDigits[0].trim().split(" ");
            String[] digits = mappingsAndDigits[1].trim().split(" ");
            Map<Character, Integer> charCounts = new HashMap<>();
            Multimap<Integer, String> mappingsByLength = HashMultimap.create();
            for (String mapping : mappings) {
                for (char c : mapping.toCharArray()) {
                    charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
                }
                mappingsByLength.put(mapping.length(), mapping);
            }
            Map<Character, Character> charMappings = new HashMap<>();
            for (Entry<Character, Integer> charCount : charCounts.entrySet()) {
                if (charCount.getValue() == 9) {
                    charMappings.put(charCount.getKey(), 'f');
                } else if (charCount.getValue() == 6) {
                    charMappings.put(charCount.getKey(), 'b');
                } else if (charCount.getValue() == 4) {
                    charMappings.put(charCount.getKey(), 'e');
                }
            }

            for (char c : Iterables.getOnlyElement(mappingsByLength.get(4)).toCharArray()) {
                if (charCounts.get(c) == 7 && !charMappings.containsKey(c)) {
                    charMappings.put(c, 'd');
                } else if (charCounts.get(c) == 8 && !charMappings.containsKey(c)) {
                    charMappings.put(c, 'c');
                }
            }

            for (char c : Iterables.getOnlyElement(mappingsByLength.get(7)).toCharArray()) {
                if (charCounts.get(c) == 7 && !charMappings.containsKey(c)) {
                    charMappings.put(c, 'g');
                } else if (charCounts.get(c) == 8 && !charMappings.containsKey(c)) {
                    charMappings.put(c, 'a');
                }
            }

            String decodedNumber = "";
            for (String digit : digits) {
                String mappedDigit = "";
                for (char c : digit.toCharArray()) {
                    mappedDigit += charMappings.get(c);
                }
                char[] sortedMappedDigit = mappedDigit.toCharArray();
                Arrays.sort(sortedMappedDigit);
                int decodedDigit = digitSegments.indexOf(new String(sortedMappedDigit));
                if (decodedDigit == -1) {
                    throw new IllegalStateException("Invalid mapping: " + new String(sortedMappedDigit));
                }
                decodedNumber += decodedDigit;
            }
            sumOfDecodedNumbers += Integer.parseInt(decodedNumber);
        }
        System.out.println(sumOfDecodedNumbers);
    }
}
