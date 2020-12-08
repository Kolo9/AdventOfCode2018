package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Puzzle08 {  
    private static class CompletedInstruction {
        int pointer;
        int acc;
        public CompletedInstruction(int pointer, int acc) {
            this.pointer = pointer;
            this.acc = acc;
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> instructions = Files.readAllLines(Paths.get(Puzzle08.class.getResource("in08").toURI()));
        System.out.println(parseInstruction(0, 0, new HashSet<>(), instructions, -1).acc);

        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i).startsWith("nop") || instructions.get(i).startsWith("jmp")) {
                CompletedInstruction run = parseInstruction(0, 0, new HashSet<>(), instructions, i);
                if (run.pointer >= instructions.size()) {
                    System.out.println(run.acc);
                    break;
                }
            }
        }
    }

    private static CompletedInstruction parseInstruction(int pointer, int acc, Set<Integer> seen, List<String> instructions, int overrideIndex) {
        if (pointer >= instructions.size() || seen.contains(pointer)) {
            return new CompletedInstruction(pointer, acc);
        }
        seen.add(pointer);
        String[] parts = instructions.get(pointer).split(" ");
        if (pointer == overrideIndex) {
            if (parts[0] == "nop") {
                parts[0] = "jmp";
            } else {
                parts[0] = "nop";
            }
        }
        switch(parts[0]) {
        case "nop":
            pointer++;
            break;
        case "acc":
            acc += Integer.parseInt(parts[1]);
            pointer++;
            break;
        case "jmp":
            pointer += Integer.parseInt(parts[1]);
            break;
        default:
            throw new IllegalStateException("Unknown instruction " + parts[0]);
        }
        return parseInstruction(pointer, acc, seen, instructions, overrideIndex);
    }
}