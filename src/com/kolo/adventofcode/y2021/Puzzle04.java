package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Iterators;

public class Puzzle04 {
    private static class Board {
        int[][] nums = new int[5][5];

        // TODO(never): Optimize by passing in each number as they're called and
        // only checking the relevant lines.
        boolean bingo(Set<Integer> called) {
            return IntStream.range(0, 5).anyMatch(i ->
                    // Rows.
                    IntStream.range(0, 5).allMatch(j -> called.contains(nums[i][j]))
                    // Columns.
                    || IntStream.range(0, 5).allMatch(j -> called.contains(nums[j][i])))
            // // TL->BR diagonal.
            // || IntStream.range(0, 5).allMatch(i ->
            // called.contains(nums[i][i]))
            // // TR->BL diagonal.
            // || IntStream.range(0, 5).allMatch(i -> called.contains(nums[i][4
            // - i]))
            ;
        }

        int score(LinkedHashSet<Integer> called) {
            int score = 0;
            for (int i = 0; i < nums.length; i++) {
                for (int j = 0; j < nums[i].length; j++) {
                    if (!called.contains(nums[i][j])) {
                        score += nums[i][j];
                    }
                }
            }
            int multiplier = Iterators.getLast(called.iterator());
            return score * multiplier;
        }

        @Override
        public String toString() {
            return Arrays.stream(nums).map(Arrays::toString).collect(Collectors.joining("\n"));
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle04.class.getResource("in04").toURI())).stream()
                .collect(Collectors.toList());

        List<Integer> draws = Arrays.stream(input.get(0).split(",")).map(Integer::parseInt)
                .collect(Collectors.toList());
        List<Board> boards = new ArrayList<>();
        for (int i = 2; i < input.size(); i += 6) {
            Board board = new Board();
            for (int j = 0; j < 5; j++) {
                board.nums[j] = Arrays.stream(input.get(i + j).trim().split(" +")).mapToInt(Integer::parseInt)
                        .toArray();
            }
            boards.add(board);
        }

        LinkedHashSet<Integer> called = new LinkedHashSet<>();
        int winnerScore = 0;
        int loserScore = 0;
        for (int i = 0; i < draws.size(); i++) {
            called.add(draws.get(i));
            List<Board> winnersOnDraw = boards.stream().filter(b -> b.bingo(called)).collect(Collectors.toList());
            for (Board winnerOnDraw : winnersOnDraw) {
                if (winnerScore == 0) {
                    winnerScore = winnerOnDraw.score(called);
                }
                loserScore = winnerOnDraw.score(called);
                boards.remove(winnerOnDraw);
                if (boards.isEmpty()) {
                    break;
                }
            }
        }
        System.out.println(winnerScore);
        System.out.println(loserScore);
    }
}
