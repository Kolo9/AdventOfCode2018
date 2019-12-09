package com.kolo.adventofcode.y2019;

final class Puzzle09 {

    public static void main(String[] args) {
        IntcodeComputer computer = new IntcodeComputer("in09");
        computer.addInput(1).run();
        System.out.println("Part 1: " + computer.getOutputList());
        
        computer = new IntcodeComputer("in09");
        computer.addInput(2).run();
        System.out.println("Part 2: " + computer.getOutputList());
    }

}
