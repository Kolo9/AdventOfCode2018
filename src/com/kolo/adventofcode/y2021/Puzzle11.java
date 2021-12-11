package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle11 {
    private static int flashes = 0;
    private static int stepFlashes = 0;

    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle11.class.getResource("in11").toURI())).stream()
                .collect(Collectors.toList());
        int[][] octopi = new int[input.size()][input.get(0).length()];
        for (int i = 0; i < octopi.length; i++) {
            for (int j = 0; j < octopi[i].length; j++) {
                octopi[i][j] = Integer.parseInt("" + input.get(i).charAt(j));
            }
        }

        int lastFlashes = 0;
        int step = 0;
        while (true) {
            step++;
            for (int i = 0; i < octopi.length; i++) {
                for (int j = 0; j < octopi[i].length; j++) {
                    increaseEnergy(octopi, i, j);
                }
            }
            for (int i = 0; i < octopi.length; i++) {
                for (int j = 0; j < octopi[i].length; j++) {
                    if(octopi[i][j] > 9) {
                        octopi[i][j] = 0;
                    }
                }
            }
            if (step == 100) {
                System.out.println("Step 100: " + flashes);
            }
            if (flashes - lastFlashes == octopi.length * octopi[0].length) {
                System.out.println("All flash step: " + step);
                break;
            }
            lastFlashes = flashes;
        }
    }

    private static void increaseEnergy(int[][] octopi, int i, int j) {
        octopi[i][j]++;
        if (octopi[i][j] == 10) {
            flashes++;
            for (int i2 = i - 1; i2 <= i + 1; i2++) {
                if (i2 < 0 || i2 >= octopi.length) {
                    continue;
                }
                for (int j2 = j - 1; j2 <= j + 1; j2++) {
                    if (j2 < 0 || j2 >= octopi[i2].length) {
                        continue;
                    }
                    increaseEnergy(octopi, i2, j2);
                }
            }
        }
    }
}
