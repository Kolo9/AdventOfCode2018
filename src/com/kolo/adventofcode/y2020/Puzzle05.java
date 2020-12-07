package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Puzzle05 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Puzzle05.class.getResource("in05").toURI()));
        Set<Integer> seats = lines.stream().map(Puzzle05::parseSeat).collect(Collectors.toSet());
        int minSeat = Collections.min(seats);
        int maxSeat = Collections.max(seats);
        System.out.println(maxSeat);
        for (int i = minSeat; i <= maxSeat; i++) {
            if (!seats.contains(i)) {
                System.out.println(i);
                break;
            }
        }
    }

    private static int parseSeat(String in) {
        String seatBinary = in.replaceAll("F", "0").replaceAll("B", "1").replaceAll("L", "0").replaceAll("R", "1");
        return Integer.parseInt(seatBinary, 2);
    }
}