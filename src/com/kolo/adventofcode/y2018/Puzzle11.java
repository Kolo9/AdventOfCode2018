package com.kolo.adventofcode.y2018;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle11 {
	private static int minX = Integer.MAX_VALUE;
	private static int minY = Integer.MAX_VALUE;
	private static int maxX = Integer.MIN_VALUE;
	private static int maxY = Integer.MIN_VALUE;
	private static char c = 'A';
	private static final class Point {
		int x;
		int y;
		int vx;
		int vy;
		char id;

		Point(int x, int y, int vx, int vy) {
			this.x = x;
			this.y = y;
			this.vx = vx;
			this.vy = vy;
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

	private static int SERIAL = 9221;
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle11"));

		Pattern p = Pattern.compile("position=< ?(-?\\d+),  ?(-?\\d+)> velocity=< ?(-?\\d+),  ?(-?\\d+)>");
		int[][] grid = new int[300][300];
		for (int y = 0; y < 300; y++) {
			for (int x = 0; x < 300; x++) {
				int rackId = x + 1 + 10;
				grid[y][x] = rackId * (y + 1);
				grid[y][x] += SERIAL;
				grid[y][x] *= rackId;
				grid[y][x] /= 100;
				grid[y][x] %= 10;
				grid[y][x] -= 5;
			}
		}

		int bestX = -1;
		int bestY = -1;
		int bestPower = Integer.MIN_VALUE;
		int bestSize = -1;
		for (int size = 1; size <= 300; size++) {
			System.out.println("Checking size " + size);
			for (int tlx = 0; tlx < 300 - size + 1; tlx++) {
				for (int tly = 0; tly < 300 - size + 1; tly++) {
					int power = 0;
					for (int x = tlx; x < tlx + size; x++) {
						for (int y = tly; y < tly + size; y++) {
							power += grid[y][x];
						}
					}
					if (power > bestPower) {
						bestPower = power;
						bestSize = size;
						bestX = tlx;
						bestY = tly;
					}
				}
			}
		}

		System.out.println((bestX + 1) + " " + (bestY + 1) + " " + bestSize + " " + bestPower);
	}

}