package com.kolo.adventofcode.y2021;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.kolo.adventofcode.common.Direction;

public class Puzzle09 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle09.class.getResource("in09").toURI())).stream()
                .collect(Collectors.toList());
        int[][] grid = new int[input.size() + 2][input.get(0).length() + 2];
        for (int i = 0; i < grid.length; i++) {
            grid[i][0] = 9;
            grid[i][grid[i].length - 1] = 9;
        }
        for (int i = 0; i < grid[0].length; i++) {
            grid[0][i] = 9;
            grid[grid.length - 1][i] = 9;
        }
        
        for (int i = 1; i < grid.length - 1; i++) {
            for (int j = 1; j < grid[i].length - 1; j++) {
                grid[i][j] = Integer.parseInt("" + input.get(i-1).charAt(j-1));
            }
        }

        int lowPointsRiskSum = 0;
        List<Point> lowPoints = new ArrayList<>();
        for (int i = 1; i < grid.length - 1; i++) {
            for (int j = 1; j < grid[i].length - 1; j++) {
                if (grid[i][j] < grid[i-1][j] && grid[i][j] < grid[i+1][j] && grid[i][j] < grid[i][j-1] && grid[i][j] < grid[i][j+1]) {
                    int risk = grid[i][j] + 1;
                    lowPoints.add(new Point(j, i));
                    lowPointsRiskSum += risk;
                }
            }
        }
        System.out.println(lowPointsRiskSum);

        List<Integer> basinSizes = new ArrayList<>();
        for (Point lowPoint : lowPoints) {
            int basinSize = getBasinSize(grid, lowPoint);
            basinSizes.add(basinSize);
        }

        Collections.sort(basinSizes, Collections.reverseOrder());
        System.out.println(basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }

    private static int getBasinSize(int[][] grid, Point lowPoint) {
        List<Point> toCheck = new ArrayList<>();
        Set<Point> seen = new HashSet<>();
        int nines = 0;
        toCheck.add(lowPoint);
        while (!toCheck.isEmpty()) {
            Point cur = toCheck.remove(0);
            if (!seen.add(cur)) {
                continue;
            }
            if (grid[cur.y][cur.x] == 9) {
                nines++;
                continue;
            }
            for (Direction dir : Direction.values()) {
                Point neighbor = dir.apply(cur);
                if (!seen.contains(neighbor)) {
                    toCheck.add(dir.apply(cur));
                }
            }
        }
        return seen.size() - nines;
    }
}
