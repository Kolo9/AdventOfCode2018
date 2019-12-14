package com.kolo.adventofcode.y2019;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

final class Puzzle14 {

    private static final String ORE = "ORE";
    private static final String FUEL = "FUEL";
    
    private static final class Ingredient {
        String chemical;
        int amount;

        Ingredient(String s) {
            this(s.split(" ")[1], Integer.parseInt(s.split(" ")[0]));
        }
        
        Ingredient(String chemical, int amount) {
            this.chemical = chemical;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return amount + chemical;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Ingredient)) {
                return false;
            }
            Ingredient other = (Ingredient) obj;
            return chemical.equals(other.chemical);
        }

        @Override
        public int hashCode() {
            return Objects.hash(chemical);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> conversions = Files.readAllLines(Paths.get(Puzzle03.class.getResource("in14").toURI()));
        
        // Every chemical is produced by one reaction.
        Multimap<Ingredient, Ingredient> reactions = ArrayListMultimap.create();
        reactions.put(new Ingredient(ORE, 1), new Ingredient(ORE, 1));
        
        for (String conversion : conversions) {
            Matcher m = Pattern.compile("(.*) => (.*)").matcher(conversion);
            m.matches();

            Ingredient result = new Ingredient(m.group(2));
            List<Ingredient> requirements = new ArrayList<>();
            for (String requirement : m.group(1).split(", ")) {
                requirements.add(new Ingredient(requirement));
            }
            reactions.putAll(result, requirements);
        }

        // :(
        Map<String, Integer> leftovers = new LinkedHashMap<>();
        long remainingOre = 1_000_000_000_000L;
        boolean part1Printed = false;
        int fuel = 0;
        while (remainingOre > 0) {
            Map<String, Integer> requirements = new LinkedHashMap<>();
            requirements.put(FUEL, 1);
            while(requirements.size() > 1 || !requirements.containsKey(ORE)) {
                Map<String, Integer> nextRequired = new LinkedHashMap<>();
                for (Entry<String, Integer> requirement : requirements.entrySet()) {
                    String requirementChemical = requirement.getKey();
                    int requirementAmount = requirement.getValue();
                    if (leftovers.containsKey(requirementChemical)) {
                        if (leftovers.get(requirementChemical) > requirementAmount) {
                            leftovers.put(requirementChemical, leftovers.get(requirementChemical) - requirementAmount);
                            continue;
                        }
                        requirementAmount -= leftovers.get(requirementChemical);
                        leftovers.remove(requirementChemical);
                    }
    
                    Ingredient willProduce = reactions.keySet().stream().filter(i -> i.chemical.equals(requirementChemical)).findFirst().get();
                    Collection<Ingredient> requirementIngredients = reactions.get(new Ingredient(requirementChemical, 0));
                    int timesToMake = (int) Math.ceil((double)requirementAmount / willProduce.amount);
                    for (Ingredient requirementIngredient : requirementIngredients) {
                        nextRequired.put(requirementIngredient.chemical, nextRequired.getOrDefault(requirementIngredient.chemical, 0) + requirementIngredient.amount * timesToMake);
                    }
                    if (timesToMake * willProduce.amount > requirementAmount) { 
                        leftovers.put(requirementChemical, leftovers.getOrDefault(requirementChemical, 0) + (timesToMake * willProduce.amount) - requirementAmount);
                    }
                }
                requirements = nextRequired;
            }
            if (!part1Printed) {
                System.out.println("Part 1: " + requirements.get(ORE));
                part1Printed = true;
            }
            remainingOre -= requirements.get(ORE);
            fuel++;
        }

        if (remainingOre < 0) {
            fuel--;
        }

        System.out.println("Part 2: " + fuel);
    }
}
 