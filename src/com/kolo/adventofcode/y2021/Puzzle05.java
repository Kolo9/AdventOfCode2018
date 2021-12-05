package com.kolo.adventofcode.y2021;

import java.awt.Point;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Puzzle05 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get(Puzzle05.class.getResource("in05").toURI())).stream()
                .collect(Collectors.toList());

        Map<String, Integer> linesByPoint = new HashMap<>();
        Pattern inputSegmentPattern = Pattern.compile("(-?\\d+),(-?\\d+) -> (-?\\d+),(-?\\d+)");
        for (String segment : input) {
            Matcher matcher = inputSegmentPattern.matcher(segment);
            matcher.matches();
            Point start = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            Point end = new Point(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
            if (start.x == end.x) {
                for (int y = Math.min(start.y, end.y); y <= Math.max(start.y, end.y); y++) {
                    String key = start.x + " " + y;
                    linesByPoint.put(key, linesByPoint.getOrDefault(key, 0) + 1);
                }
            } else if (start.y == end.y) {
                for (int x = Math.min(start.x, end.x); x <= Math.max(start.x, end.x); x++) {
                    String key = x + " " + start.y;
                    linesByPoint.put(key, linesByPoint.getOrDefault(key, 0) + 1);
                }
            } else {
                int dX = end.x > start.x ? 1 : -1;
                int dY = end.y > start.y ? 1 : -1;
                Point cur = new Point(start);
                while (!cur.equals(end)) {
                    String key = cur.x + " " + cur.y;
                    linesByPoint.put(key, linesByPoint.getOrDefault(key, 0) + 1);
                    cur.x += dX;
                    cur.y += dY;
                }
                String key = cur.x + " " + cur.y;
                linesByPoint.put(key, linesByPoint.getOrDefault(key, 0) + 1);
            }
        }
        System.out.println(linesByPoint.values().stream().filter(v -> v > 1).count());
    }
}
