package com.kolo.adventofcode.y2021;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Puzzle12 {

    private static List<String> paths = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle12.class.getResource("in12").toURI())).stream()
                .collect(Collectors.toList());
        Multimap<String, String> nodeAdj = HashMultimap.create();
        for(String line : input) {
            String[] nodes = line.split("-");
            nodeAdj.put(nodes[0], nodes[1]);
            nodeAdj.put(nodes[1], nodes[0]);
        }
        calculatePaths(nodeAdj, "start", "end", "start", false, false);
        System.out.println(paths.size());
        paths.clear();
        calculatePaths(nodeAdj, "start", "end", "start", true, false);
        System.out.println(paths.size());
    }

    private static void calculatePaths(Multimap<String, String> nodeAdj, String cur, String end, String curPath, boolean allowSingleSmallCaveTwice, boolean smallCaveVisitedTwice) {
        if (cur.equals(end)) {
            paths.add(curPath);
            return;
        }
        for (String adj : nodeAdj.get(cur)) {
            boolean isSmallCaveRevisit = adj.equals(adj.toLowerCase()) && curPath.matches(".*(,|^)" + adj + "(,|$).*");
            if (isSmallCaveRevisit) {
                if (adj.equals("start") || adj.equals("end") || !allowSingleSmallCaveTwice || smallCaveVisitedTwice) {
                    continue;
                }
            }
            calculatePaths(nodeAdj, adj, end, curPath + "," + adj, allowSingleSmallCaveTwice, smallCaveVisitedTwice || isSmallCaveRevisit);
        }
    }
}
