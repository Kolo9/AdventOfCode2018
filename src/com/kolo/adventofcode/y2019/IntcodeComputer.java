package com.kolo.adventofcode.y2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntcodeComputer {

    private List<Integer> state;
    private int pointer;

    public IntcodeComputer(String filePath) {
        String input;
        try {
            input = String.join("\n", Files.readAllLines(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Could not open file " + filePath, e);
        }
        this.state = Arrays.asList(input.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        this.pointer = 0;
    }

    public IntcodeComputer(Integer[] input) {
        this.state = Arrays.asList(input);
        this.pointer = 0;

    }

    public List<Integer> getState() {
        return new ArrayList<>(state);
    }

    public void run() {
        while (pointer < state.size()) {
            int op = state.get(pointer++);
            System.out.println("op " + op + ": " + state.get(0));
            switch(op) {
            case 1:
                add();
                break;
            case 2:
                multiply();
                break;
            case 99:
                return;
            default:
                throw new UnsupportedOperationException("Unknown opcode " + op);
            }
        }
    }

    private void add() {
        int first = state.get(state.get(pointer++));
        int second = state.get(state.get(pointer++));
        int dest = state.get(pointer++);
        state.set(dest, first + second);
        System.out.println(dest + " is now " + (first + second));
    }

    private void multiply() {
        int first = state.get(state.get(pointer++));
        int second = state.get(state.get(pointer++));
        int dest = state.get(pointer++);
        state.set(dest, first * second);
        System.out.println(dest + " is now " + (first * second));
    }
}


