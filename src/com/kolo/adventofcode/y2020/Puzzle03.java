package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Puzzle03 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle03.class.getResource("in03").toURI()));
        int height = input.size();
        int width = input.get(0).length();
        boolean treeGrid[][] = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                treeGrid[i][j] = input.get(i).charAt(j) == '#';
            }
        }
        System.out.println(checkTrees(treeGrid, 3, 1));
        System.out.println(checkTrees(treeGrid, 1, 1) * checkTrees(treeGrid, 3, 1) * checkTrees(treeGrid, 5, 1) * checkTrees(treeGrid, 7, 1) * checkTrees(treeGrid, 1, 2));
    }

    private static long checkTrees(boolean[][] treeGrid, int dX, int dY) {
        int curX = 0;
        int curY = 0;
        int treeCount = 0;
        while (curY + dY < treeGrid.length) {
            curX += dX;
            curX %= treeGrid[0].length;
            curY += dY;
            if (treeGrid[curY][curX]) {
                treeCount++;
            }
        }
        return treeCount;
    }
}
