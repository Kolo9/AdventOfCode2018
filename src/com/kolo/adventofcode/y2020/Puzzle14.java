package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

class Puzzle14 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Puzzle14.class.getResource("in14").toURI())).stream()
                .collect(Collectors.toList());
        Map<Integer, Boolean> mask = new HashMap<>();
        Map<Long, Long> registers = new HashMap<>();
        for (String line : lines) {
            if (line.startsWith("mask")) {
                mask.clear();
                String maskString = line.substring(7);
                for (int i = 0; i < maskString.length(); i++) {
                    if (maskString.charAt(i) == '1') {
                        mask.put(35 - i, true);
                    } else if (maskString.charAt(i) == '0') {
                        mask.put(35 - i, false);
                    }
                }
            } else {
                Matcher m = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)").matcher(line);
                m.find();
                long address = Integer.parseInt(m.group(1));
                long value = Integer.parseInt(m.group(2));
                for (int pos : mask.keySet()) {
                    boolean shouldSet = mask.get(pos);
                    if (shouldSet) {
                        value |= 1L << pos;
                    } else {
                        value &= ~(1L << pos);
                    }
                }
                registers.put(address, value);
            }
        }
        System.out.println(registers.values().stream().mapToLong(i -> i).sum());

        mask = new HashMap<>();
        registers = new HashMap<>();
        for (String line : lines) {
            if (line.startsWith("mask")) {
                mask.clear();
                String maskString = line.substring(7);
                for (int i = 0; i < maskString.length(); i++) {
                    if (maskString.charAt(i) == '1') {
                        mask.put(35 - i, true);
                    } else if (maskString.charAt(i) == 'X') {
                        mask.put(35 - i, false);
                    }
                }
            } else {
                Matcher m = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)").matcher(line);
                m.find();
                int addressBase = Integer.parseInt(m.group(1));
                long value = Integer.parseInt(m.group(2));
                List<Long> addresses = getAllAddresses(addressBase, mask);
                for (long address : addresses) {
                    registers.put(address, value);
                }
            }
        }
        System.out.println(registers.values().stream().mapToLong(i -> i).sum());
    }

    private static final List<Long> getAllAddresses(long address, Map<Integer, Boolean> mask) {
        List<Integer> floatingBits = new ArrayList<>();
        for (int pos : mask.keySet()) {
            boolean shouldSet = mask.get(pos);
            if (shouldSet) {
                address |= 1L << pos;
            } else {
                floatingBits.add(pos);
            }
        }
        return getAllAddressesHelper(address, floatingBits, 0);
    }

    private static final List<Long> getAllAddressesHelper(long address, List<Integer> floatingBits, int idx) {
        if (idx == floatingBits.size()) {
            return Lists.newArrayList(address);
        }
        int pos = floatingBits.get(idx);
        long addressSet = address |= 1L << pos;
        long addressUnset = address &= ~(1L << pos);
        List<Long> addresses = getAllAddressesHelper(addressSet, floatingBits, idx + 1);
        addresses.addAll(getAllAddressesHelper(addressUnset, floatingBits, idx + 1));
        return addresses;
    }
}