package com.kolo.adventofcode.y2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

public class IntcodeComputer {
    
    private static enum Mode {
        POSITION, INTERMEDIATE;
    }

    private List<Integer> state;
    private List<Mode> modes = new LinkedList<>();
    private int pointer = 0;
    private List<Integer> input = new ArrayList<>();
    private List<Integer> output = new ArrayList<>();
    private boolean done = false;

    public IntcodeComputer(String filePath) {
        String state;
        try {
            state = String.join("\n", Files.readAllLines(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Could not open file " + filePath, e);
        }
        this.state = Arrays.asList(state.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
        this.input = new ArrayList<>(input);
    }

    public IntcodeComputer(Integer[] state) {
        this.state = new ArrayList<>(Arrays.asList(state));
    }

    public List<Integer> getState() {
        return new ArrayList<>(state);
    }

    public IntcodeComputer addInput(Integer i) {
        this.input.add(i);
        return this;
    }

    public IntcodeComputer addInput(List<Integer> i) {
        this.input.addAll(i);
        return this;
    }

    public boolean done() {
        return done;
    }

    public boolean run() {
        while (pointer < state.size()) {
            int val = state.get(pointer++);
            int op = val % 100;
            modes.clear();
            val /= 100;
            while (val > 0) {
                modes.add(Mode.values()[val % 10]);
                val /= 10;
            }
            switch(op) {
            case 1:
                add();
                break;
            case 2:
                multiply();
                break;
            case 3:
                if (!input()) {
                    return false;
                }
                break;
            case 4:
                output();
                break;
            case 5:
                jumpIfTrue();
                break;
            case 6:
                jumpIfFalse();
                break;
            case 7:
                lessThan();
                break;
            case 8:
                equals();
                break;
            case 99:
                done = true;
                return true;
            default:
                throw new UnsupportedOperationException("Unknown opcode " + op);
            }
        }

        throw new IllegalStateException("pointer out of bounds: " + pointer);
    }

    private void add() {
        int first = next();
        int second = next();
        int dest = nextDest();
        state.set(dest, first + second);
    }

    private void multiply() {
        int first = next();
        int second = next();
        int dest = nextDest();
        state.set(dest, first * second);
    }

    private boolean input() {
        if (input.isEmpty()) {
            pointer--;
            return false;
        }
        int dest = nextDest();
        state.set(dest, input.remove(0));
        return true;
    }

    private void output() {
        this.output.add(next());
    }

    public int getOutput(int index) {
        Preconditions.checkArgument(index < output.size());
        return output.get(0);
    }

    public int getLastOutput() {
        return output.get(output.size() - 1);
    }

    private void jumpIfTrue() {
        int first = next();
        int second = next();
        if (first != 0) {
            pointer = second;
        }
    }

    private void jumpIfFalse() {
        int first = next();
        int second = next();
        if (first == 0) {
            pointer = second;
        }
    }

    private void lessThan() {
        int first = next();
        int second = next();
        int dest = nextDest();
        state.set(dest, first < second ? 1 : 0);
    }

    private void equals() {
        int first = next();
        int second = next();
        int dest = nextDest();
        state.set(dest, first == second ? 1 : 0);
    }

    private int next() {
        Mode mode = Mode.POSITION;
        if (!modes.isEmpty()) {
            mode = modes.remove(0);
        }
        if (mode == Mode.POSITION) {
            return state.get(state.get(pointer++));
        }
        return state.get(pointer++);
    }

    private int nextDest() {
        return state.get(pointer++);
    }
}


