package com.kolo.adventofcode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Puzzle7 {
	private static int minX = Integer.MAX_VALUE;
	private static int minY = Integer.MAX_VALUE;
	private static int maxX = Integer.MIN_VALUE;
	private static int maxY = Integer.MIN_VALUE;
	private static char c = 'A';
	private static final class Point {
		int x;
		int y;
		char id;

		Point(String s) {
			String[] arr = s.split(", ");
			this.x = Integer.parseInt(arr[0]);
			this.y = Integer.parseInt(arr[1]);
			minX = Math.min(minX, x);
			minY = Math.min(minY, y);
			maxX = Math.max(maxX, x);
			maxY = Math.max(maxY, y);
			id = c;
			if (c == 'Z') {
				c = 'a';
			} else {
				c = (char) (((int) c) + 1);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Point)) {
				return false;
			}
			Point otherPair = (Point) other;
			return otherPair.x == x && otherPair.y == y;
		}
	
		@Override
		public String toString() {
			return String.format("(%d, %d)", x, y);
		}
	}

	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle7"));

		Pattern p = Pattern.compile(".*Step (.) must be finished before step (.) can begin.*");
		Map<String, Set<String>> deps = new HashMap<>();
		for (String s : ss) {
			Matcher matcher = p.matcher(s);
			matcher.find();
			if(!deps.containsKey(matcher.group(2))) {
				deps.put(matcher.group(2), new HashSet<>());
			}
			if(!deps.containsKey(matcher.group(1))) {
				deps.put(matcher.group(1), new HashSet<>());
			}
			deps.get(matcher.group(2)).add(matcher.group(1));
		}

		String ans = "";
		int min = -1;
		Map<String, Integer> work = new HashMap<>();
		while (!deps.isEmpty()) {
			Set<String> toRemove = new HashSet<>();
			for (String k : work.keySet()) {
				work.put(k, work.get(k) - 1);
				if (work.get(k) == 0) {
					ans += k;
					toRemove.add(k);
					deps.remove(k);
					for (Set<String> v : deps.values()) {
						v.remove(k);
					}
				}
			}
			toRemove.forEach(work::remove);
			System.out.println(work);
			

			List<String> available = deps.entrySet().stream().filter(e -> e.getValue().isEmpty()).map(e -> e.getKey()).collect(Collectors.toList());
			Collections.sort(available);
			available.removeAll(work.keySet());
			while(!available.isEmpty() && work.size() < 5) {
				work.put(available.get(0), 60 + (((int)available.remove(0).charAt(0)) - (int)'A' + 1));
			}
			min++;
		}
		System.out.println("DONE!");
		System.out.println(deps);
		System.out.println(ans);
		System.out.println(min);
		System.out.println(work);
		System.out.println(min);
		

//	    BufferedWriter writer = new BufferedWriter(new FileWriter("out6"));
//	     
//		for (Point[] row : debugGrid) {
//			for (Point point : row) {
//				writer.write(((point == null) ? '.' : point.id) + ", ");
//			}
//			writer.write("\n");
//		}
//
//		writer.close();

	}

}