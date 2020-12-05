package com.kolo.adventofcode.y2019;

import java.awt.Point;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.kolo.adventofcode.common.Direction;

final class Puzzle17 {
    
    public static void main(String[] args) throws Exception {
        IntcodeComputer computer = new IntcodeComputer("in17");
        computer.run();

        List<Integer> output = computer.getOutputList().stream().map(BigInteger::intValueExact).collect(Collectors.toList());
        int lineLength = output.indexOf(10);
        output.removeIf(i -> i == 10);
        char[][] map = new char[output.size() / lineLength][lineLength];
        Point pos = null;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = (char) output.get(i * lineLength + j).intValue();
                if (map[i][j] == '^') {
                    pos = new Point(j, i);
                }
                System.out.print(map[i][j]);
            }
            System.out.println();
        }

        int alignmentParameterSum = 0;
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map[i].length - 1; j++) {
                if (map[i][j] == '#' && map[i-1][j] == '#' && map[i+1][j] == '#' && map[i][j-1] == '#' && map[i][j+1] == '#') {
                    alignmentParameterSum += i * j;
                }
            }
        }

        System.out.println("Part 1: " + alignmentParameterSum);

        computer = new IntcodeComputer("in17");
        computer.setState(0, 2);
        Direction dir = Direction.NORTH;
        List<String> instructions = new ArrayList<>();
        while (true) {
            if (isScaffolding(map, dir.cw().apply(pos))) {
                instructions.add("R");
                dir = dir.cw();
            } else if (isScaffolding(map, dir.ccw().apply(pos))) {
                instructions.add("L");
                dir = dir.ccw();
            } else {
                break;
            }
            int stepCount = 0;
            while (isScaffolding(map, dir.apply(pos))) {
                pos = dir.apply(pos);
                stepCount++;
            }
            instructions.add("" + stepCount);
        }
        
        List<String> routineA = shittyGreedySub(instructions, "A");
        List<String> routineB = shittyGreedySub(instructions, "B");
        List<String> routineC = shittyGreedySub(instructions, "C");
        System.out.println(instructions);
        System.out.println(routineA);
        System.out.println(routineB);
        System.out.println(routineC);
        for (List<String> instructionsToConvert : Arrays.asList(instructions, routineA, routineB, routineC)) {
            String instructionString = String.join(",", instructionsToConvert) + "\n";
            List<Integer> convertedInstructions = IntStream.range(0, instructionString.length()).mapToObj(i -> (int) instructionString.charAt(i)).collect(Collectors.toList());
            System.out.println("Passing " + convertedInstructions);
            convertedInstructions.forEach(computer::addInput);
        }
        computer.addInput((int) 'n');
        computer.addInput(10);
        
        computer.run();
        System.out.println(computer.getLastOutput());
    }

    private static List<String> shittyGreedySub(List<String> instructions, String replacement) {
        Multimap<List<String>, Integer> seen = ArrayListMultimap.create();
        for (int i = 0; i < instructions.size(); i++) {
            for (int j = i + 1; j <= instructions.size(); j++) {
                List<String> sub = instructions.subList(i, j);
                //System.out.println("Saw " + sub);
                if (seen.containsKey(sub) && Iterables.getLast(seen.get(sub)) > i - sub.size()) {
                    continue;
                }
                seen.put(sub, i);
            }
        }
        
        List<List<String>> keysToRemove = seen.keySet().stream().filter(k -> seen.get(k).size() == 1).collect(Collectors.toList());
        keysToRemove.forEach(key -> seen.removeAll(key));

        List<String> instructionsToReplace = new ArrayList<>(seen.keySet().stream().max(Comparator.comparing(ins -> ins.size() * seen.get(ins).size())).get());
        Collection<Integer> indicesToReplace = seen.get(instructionsToReplace);
        Collections.reverse((List<Integer>) indicesToReplace);
        int replacedInstructionCount = instructionsToReplace.size();
        for (int i : indicesToReplace) {
            int j = replacedInstructionCount;
            while (j-- > 0) {
                instructions.remove(i);
            }
            instructions.add(i, replacement);
        }
        
        return instructionsToReplace;
    }

    private static boolean isScaffolding(char[][] map, Point pos) {
        if (pos.y >= map.length || pos.y < 0 || pos.x >= map[0].length || pos.x < 0) {
            return false;
        }
        return map[pos.y][pos.x] == '#';
    }
}
