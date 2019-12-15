package com.kolo.adventofcode.y2019;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

public class IntcodeComputer {
    
    private static enum Mode {
        POSITION, IMMEDIATE, RELATIVE;
    }

    private static final class Memory extends ArrayList<BigInteger> {
        @Override
        public BigInteger get(int index) {
            grow(index);
            return super.get(index);
        }

        @Override
        public BigInteger set(int index, BigInteger element) {
            grow(index);
            return super.set(index, element);
        }

        private void grow(int index) {
            while (index >= size()) {
                // TODO: Use a map if necessary due to sparseness.
                add(BigInteger.ZERO);
            }
        }
    }

    private Memory state = new Memory();
    private List<Mode> modes = new LinkedList<>();
    private int pointer = 0;
    private List<BigInteger> input = new ArrayList<>();
    private List<BigInteger> output = new ArrayList<>();
    private int relativeBase = 0;
    private boolean done = false;

    public IntcodeComputer(String filePath) {
        String state;
        try {
            state = String.join("\n", Files.readAllLines(Paths.get(IntcodeComputer.class.getResource(filePath).toURI())));
        } catch (IOException|URISyntaxException e) {
            throw new RuntimeException("Could not open file " + filePath, e);
        }
        Arrays.stream(state.split(",")).map(i -> new BigInteger(i)).forEach(this.state::add);
        this.input = new ArrayList<>(input);
    }

    public IntcodeComputer(Integer[] state) {
        Arrays.stream(state).map(BigInteger::valueOf).forEach(this.state::add);
    }

    public List<BigInteger> getState() {
        return new ArrayList<>(state);
    }

    public void setState(int index, int val) {
        setState(index, BigInteger.valueOf(val));
    }

    public void setState(int index, BigInteger val) {
        state.set(index, val);
    }

    public IntcodeComputer addInput(BigInteger i) {
        this.input.add(i);
        return this;
    }

    public IntcodeComputer addInput(Integer i) {
        addInput(BigInteger.valueOf(i));
        return this;
    }

    public IntcodeComputer addInput(List<Integer> i) {
        i.forEach(this::addInput);
        return this;
    }

    public boolean done() {
        return done;
    }

    public boolean run() {
        if (done) {
            return true;
        }
        while (pointer < state.size()) {
            int val = state.get(pointer++).intValueExact();
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
            case 9:
                adjustRelativeBase();
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
        BigInteger first = next();
        BigInteger second = next();
        int dest = nextDest();
        state.set(dest, first.add(second));
    }

    private void multiply() {
        BigInteger first = next();
        BigInteger second = next();
        int dest = nextDest();
        state.set(dest, first.multiply(second));
    }

    private boolean input() {
        if (input.isEmpty()) {
            pointer--;
            return false;
        }
        int dest = nextDest();
        BigInteger val = input.remove(0);

        state.set(dest, val);
        return true;
    }

    private void output() {
        this.output.add(next());
    }

    public BigInteger getOutput(int index) {
        Preconditions.checkArgument(index < output.size());
        return output.get(index);
    }

    public boolean hasOutput() {
        return !output.isEmpty();
    }

    public BigInteger getLastOutput() {
        Preconditions.checkState(!output.isEmpty());
        return output.get(output.size() - 1);
    }
    
    public List<BigInteger> getOutputList() {
        return new ArrayList<>(output);
    }

    private void jumpIfTrue() {
        BigInteger first = next();
        BigInteger second = next();
        if (first.compareTo(BigInteger.ZERO) != 0) {
            pointer = second.intValueExact();
        }
    }

    private void jumpIfFalse() {
        BigInteger first = next();
        BigInteger second = next();
        if (first.compareTo(BigInteger.ZERO) == 0) {
            pointer = second.intValueExact();
        }
    }

    private void lessThan() {
        BigInteger first = next();
        BigInteger second = next();
        int dest = nextDest();
        state.set(dest, first.compareTo(second) == -1 ? BigInteger.ONE : BigInteger.ZERO);
    }

    private void equals() {
        BigInteger first = next();
        BigInteger second = next();
        int dest = nextDest();
        state.set(dest, first.compareTo(second) == 0 ? BigInteger.ONE : BigInteger.ZERO);
    }

    private void adjustRelativeBase() {
        int adjustment = next().intValueExact();
        relativeBase += adjustment;
    }

    private BigInteger next() {
        Mode mode = Mode.POSITION;
        if (!modes.isEmpty()) {
            mode = modes.remove(0);
        }
        BigInteger val = state.get(pointer++);
        switch (mode) {
            case POSITION:
                return state.get(val.intValueExact());
            case IMMEDIATE:
                return val;
            case RELATIVE:
                return state.get(relativeBase + val.intValueExact());
            default:
                throw new IllegalStateException("Unimplemented mode " + mode);
        }
    }

    private int nextDest() {
        Mode mode = Iterables.getOnlyElement(modes, Mode.POSITION);
        int dest = 0;
        switch (mode) {
            case RELATIVE:
                dest = relativeBase;
            case POSITION:
                dest += state.get(pointer++).intValueExact();
                break;
            default:
                throw new IllegalStateException("Unimplemented mode " + mode);
        }

        return dest;
    }
}


