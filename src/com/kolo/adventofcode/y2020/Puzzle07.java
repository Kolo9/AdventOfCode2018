package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.graph.Graphs;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;

class Puzzle07 {
    private static class Bag {
        final String color;
        // <color, count>.
        Map<String, Integer> space;

        Bag(String color) {
            this.color = color;
            this.space = new HashMap<>();
        }

        void addSpace(String color, int count) {
            this.space.put(color, this.space.getOrDefault(color, 0) + count);
        }

        @Override
        public String toString() {
            return color + " -- " + space;
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> rules = Files.readAllLines(Paths.get(Puzzle07.class.getResource("in07").toURI()));
        Pattern rulePattern = Pattern.compile("(.*?) bags contain (.*)");
        Pattern spacePattern = Pattern.compile("(\\d+) (.*?) bag");
        List<Bag> bags = new ArrayList<>();
        MutableValueGraph<String, Integer> graphToContainer = ValueGraphBuilder.directed().build();
        for (String rule : rules) {
            Matcher ruleMatcher = rulePattern.matcher(rule);
            ruleMatcher.matches();
            Bag bag = new Bag(ruleMatcher.group(1));
            Matcher spaceMatcher = spacePattern.matcher(ruleMatcher.group(2));
            while (spaceMatcher.find()) {
                bag.addSpace(spaceMatcher.group(2), Integer.parseInt(spaceMatcher.group(1)));
            }
            bags.add(bag);
            graphToContainer.addNode(bag.color);
        }
        for (Bag bag : bags) {  
            for (Entry<String, Integer> space : bag.space.entrySet()) {
                graphToContainer.putEdgeValue(space.getKey(), bag.color, space.getValue());
            }
        }
        System.out.println(Graphs.reachableNodes(graphToContainer.asGraph(), "shiny gold").size() - 1);
        System.out.println(getBagsRequired(graphToContainer, "shiny gold") - 1);
    }

    private static int getBagsRequired(ValueGraph<String, Integer> graphToContainer, String bag) {
        int requiredBags = 1;
        for (String containedBag : graphToContainer.predecessors(bag)) {
            int bagsOfType = graphToContainer.edgeValue(containedBag, bag).get();
            requiredBags += bagsOfType * getBagsRequired(graphToContainer, containedBag); 
        }
        return requiredBags;
    }
}