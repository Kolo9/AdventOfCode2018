package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

class Puzzle09 {  
    public static void main(String[] args) throws Exception {
        List<Long> sequence = Files.readAllLines(Paths.get(Puzzle09.class.getResource("in09").toURI())).stream().map(Long::parseLong).collect(Collectors.toList());
        Queue<Long> preamble = new LinkedList<>();
        int i = 0;
        for (; i < 25; i++) {
            preamble.offer(sequence.get(i));
        }
        Long invalidNumber = null;
        for (; i < sequence.size(); i++) {
            long cur = sequence.get(i);
            if (findTwoSum(preamble, cur) == null) {
                invalidNumber = cur;
                break;
            }
            preamble.poll();
            preamble.offer(cur);
        }
        System.out.println(invalidNumber);
        
        List<Long> contiguousSum = findContiguousSum(sequence, invalidNumber);
        Collections.sort(contiguousSum);
        System.out.println(contiguousSum.get(0) + contiguousSum.get(contiguousSum.size() - 1));
    }

    private static List<Long> findTwoSum(Collection<Long> input, long sum) {
        for (long i : input) {
            long j = sum - i;
            if (i == j) {
                continue;
            }
            if (input.contains(j)) {
                return ImmutableList.of(i, j);
            }
        }
        return null;
    }

    private static List<Long> findContiguousSum(List<Long> input, long sum) {
        int start = 0;
        int end = 1;
        long curSum = input.get(0) + input.get(1);
        while (end < input.size()) {
            if (curSum == sum) {
                return input.subList(start, end + 1);
            } else if (curSum > sum) {
                curSum -= input.get(start);
                start++;
            } else {
                end++;
                curSum += input.get(end);
            }
            end = Math.max(start + 1, end);
        }
        return null;
    }
}