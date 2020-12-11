package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Puzzle11 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(Puzzle11.class.getResource("in11").toURI())).stream().collect(Collectors.toList());
        char[][] originalMap = new char[lines.size() + 2][lines.get(0).length() + 2];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                originalMap[i+1][j+1] = lines.get(i).charAt(j);
            }
        }
        for (int i = 0; i < originalMap.length; i++) {
            originalMap[i][0] = '.';
            originalMap[i][originalMap[i].length - 1] = '.';
        }
        for (int i = 0; i < originalMap[0].length; i++) {
            originalMap[0][i] = '.';
            originalMap[originalMap.length - 1][i] = '.';
        }

        char[][] stabilizedMapPart1 = stabilizePart1(clone(originalMap));
        System.out.println(countOccupiedSeats(stabilizedMapPart1));
        
        char[][] stabilizedMapPart2 = stabilizePart2(clone(originalMap));
        System.out.println(countOccupiedSeats(stabilizedMapPart2));
    }

    private static char[][] clone(char[][] original) {
        char[][] cloned = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                cloned[i][j] = original[i][j];
            }
        }
        return cloned;
    }

    private static char[][] stabilizePart1(char[][] map) {
        boolean stable = false;
        while (!stable) {
            stable = true;

            char[][] after = new char[map.length][map[0].length];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    after[i][j] = map[i][j];
                    if (map[i][j] == '.') {
                        continue;
                    }
                    int occupiedAdjacent = countOccupiedAdjacentPart1(map, i, j);
                    if (map[i][j] == 'L' && occupiedAdjacent == 0) {
                        stable = false;
                        after[i][j] = '#';
                    } else if (map[i][j] == '#' && occupiedAdjacent >= 4) {
                        stable = false;
                        after[i][j] = 'L';
                    }
                }
            }

            map = after;
        }
        return map;
    }

    private static char[][] stabilizePart2(char[][] map) {
        boolean stable = false;
        while (!stable) {
            stable = true;

            char[][] after = new char[map.length][map[0].length];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    after[i][j] = map[i][j];
                    if (map[i][j] == '.') {
                        continue;
                    }
                    int occupiedAdjacent = countOccupiedAdjacentPart2(map, i, j);
                    if (map[i][j] == 'L' && occupiedAdjacent == 0) {
                        stable = false;
                        after[i][j] = '#';
                    } else if (map[i][j] == '#' && occupiedAdjacent >= 5) {
                        stable = false;
                        after[i][j] = 'L';
                    }
                }
            }

            map = after;
        }
        return map;
    }

    private static int countOccupiedSeats(char[][] map) {
        int occupiedSeats = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '#') {
                    occupiedSeats++;
                }
            }
        }
        return occupiedSeats;
    }

    private static int countOccupiedAdjacentPart1(char[][] map, int i, int j) {
        int numOccupied = 0;
        for (int i1 = i - 1; i1 <= i + 1; i1++) {
            for (int j1 = j - 1; j1 <= j + 1; j1++) {
                if (i1 == i && j1 == j) {
                    continue;
                }
                if (map[i1][j1] == '#') {
                    numOccupied++;
                }
            }
        }
        return numOccupied;
    }

    private static int countOccupiedAdjacentPart2(char[][] map, int i, int j) {
        int numOccupied = 0;
        List<int[]> dirs = new ArrayList<>();
        dirs.add(new int[]{1, 1});
        dirs.add(new int[]{1, 0});
        dirs.add(new int[]{1, -1});
        dirs.add(new int[]{0, 1});
        dirs.add(new int[]{0, -1 });
        dirs.add(new int[]{-1, 1});
        dirs.add(new int[]{-1, 0});
        dirs.add(new int[]{-1, -1});
        for (int[] dir : dirs) {
            int curI = i;
            int curJ = j;
            do {
                curI += dir[0];
                curJ += dir[1];
                if (map[curI][curJ] == 'L') {
                    break;
                } else if (map[curI][curJ] == '#') {
                    numOccupied++;
                    break;
                }
            } while (curI > 0 && curI < map.length - 1 && curJ > 0 && curJ < map[0].length - 1);
        }
        return numOccupied;
    }
}