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

public class Puzzle6 {
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
		System.out.println(points.size());

		Point[][] debugGrid = new Point[maxY-minY+1][maxX-minX+1];
		Map<Point, Integer> numTimesClosest = new HashMap<>();
		Set<Point> invalidPoints = new LinkedHashSet<>();
		int tiePoints = 0;
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				Point closestPoint = null;
				boolean nonUnique = false;
				int closestPointDist = Integer.MAX_VALUE;
				for (Point point : points) {
					int dist = Math.abs(point.x - x) + Math.abs(point.y - y);
					if (dist < closestPointDist) {
						closestPoint = point;
						closestPointDist = dist;
						nonUnique = false;
					} else if (dist == closestPointDist) {
						System.out.println("Same distance " + dist + " to " + new Point(x + ", " + y) + ". " + point + " and " + closestPoint);
						nonUnique = true;
					}
				}
				if (nonUnique) {
					closestPoint = null;
				}
				debugGrid[y-minY][x-minX] = closestPoint;
				
				if (closestPoint != null) {
					numTimesClosest.put(closestPoint, numTimesClosest.getOrDefault(closestPoint, 0) + 1);
					if (x == minX || x == maxX || y == minY || y == maxY) {
						invalidPoints.add(closestPoint);
					}
				} else {
					tiePoints++;
				}
			}
		}
	    BufferedWriter writer = new BufferedWriter(new FileWriter("out6"));
	     
		for (Point[] row : debugGrid) {
			for (Point point : row) {
				writer.write(((point == null) ? '.' : point.id) + ", ");
			}
			writer.write("\n");
		}

		writer.close();

		System.out.printf("(%d, %d) to (%d, %d), %d total\n", minX, minY, maxX, maxY, tiePoints + numTimesClosest.values().stream().mapToInt(Integer::intValue).sum());
		invalidPoints.forEach(numTimesClosest::remove);
		System.out.println(invalidPoints);
		System.out.println(numTimesClosest);
		System.out.println(numTimesClosest.values().stream().max(Comparator.naturalOrder()));
	}

}