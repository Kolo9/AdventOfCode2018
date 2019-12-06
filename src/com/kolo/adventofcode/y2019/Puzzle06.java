package com.kolo.adventofcode.y2019;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import com.kolo.adventofcode.common.Djikstras;

final class Puzzle06 {

    public static void main(String[] args) throws Exception {
        MutableGraph<String> g = GraphBuilder.directed().build();
        Set<String> nodes = new HashSet<>();
        String myOrbit = null;
        String santasOrbit = null;
        for (String in : Files.readAllLines(Paths.get(Puzzle03.class.getResource("in06").toURI()))) {
            String[] edge = in.split("\\)");
            g.addNode(edge[0]);
            g.addNode(edge[1]);
            nodes.add(edge[0]);
            nodes.add(edge[1]);
            g.putEdge(edge[1], edge[0]);
            if ("SAN".equals(edge[1])) {
                santasOrbit = edge[0];
            } else if ("YOU".equals(edge[1])) {
                myOrbit = edge[0];
            }
        }
        int ans1 = 0;
        for (String node : nodes) {
            ans1 += Graphs.reachableNodes(g, node).size() - 1;
        }
        System.out.println(ans1);
        Djikstras<String> d = new Djikstras<>(g);
        d.execute(myOrbit);
        System.out.println(d.getPath(santasOrbit).size() - 1);
    }

}


