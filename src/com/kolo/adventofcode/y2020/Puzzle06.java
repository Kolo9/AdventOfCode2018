package com.kolo.adventofcode.y2020;

import static com.google.common.collect.ImmutableMultiset.toImmutableMultiset;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.collect.Multiset;

class Puzzle06 {
    public static void main(String[] args) throws Exception {
        String[] groups = new String(Files.readAllBytes(Paths.get(Puzzle06.class.getResource("in06").toURI())))
                .split("\\s*\\n\\s*\\n\\s*");
        int sumOfAnyoneYes = 0;
        int sumOfEveryoneYes = 0;
        for (String group : groups) {
            sumOfAnyoneYes += getAnyoneYesCount(group);
            sumOfEveryoneYes += getEveryoneYesCount(group);
        }
        System.out.println(sumOfAnyoneYes);
        System.out.println(sumOfEveryoneYes);
    }

    private static int getAnyoneYesCount(String group) {
        group = group.replaceAll("\\s+", "");
        return (int) group.chars().distinct().count();
    }

    private static int getEveryoneYesCount(String group) {
        int numPeople = group.split("\n").length;
        Multiset<Integer> yesCounts = group.replaceAll("\\s+", "").chars().boxed().collect(toImmutableMultiset());
        return (int) yesCounts.elementSet().stream().filter(question -> yesCounts.count(question) == numPeople).count();
    }
}