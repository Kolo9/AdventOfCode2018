package com.kolo.adventofcode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle6_2 {
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
		List<Point> points = Files.readAllLines(Paths.get("puzzle6")).stream().map(Point::new).collect(Collectors.toList());

		int ans = 0;
		for (int x = minX - 10000; x <= maxX + 10000; x++) {
			for (int y = minY - 10000; y <= maxY + 10000; y++) {
				int totalDist = 0;
				for (Point point : points) {
					totalDist += Math.abs(point.x - x) + Math.abs(point.y - y);
				}
				if (totalDist < 10000) {
					ans++;
				}
			}
		}
		System.out.println(ans);
	}

}