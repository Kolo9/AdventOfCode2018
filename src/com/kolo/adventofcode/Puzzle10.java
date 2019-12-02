package com.kolo.adventofcode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle10 {
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

	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle10"));

		Pattern p = Pattern.compile("position=< ?(-?\\d+),  ?(-?\\d+)> velocity=< ?(-?\\d+),  ?(-?\\d+)>");
		List<Point> points = new ArrayList<>();
		for (String s : ss) {
			Matcher matcher = p.matcher(s);
			matcher.find();
			Point point = new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
			points.add(point);
		}

		for (Point point : points) {
			point.x -= minX;
			point.y -= minY;
		}
		maxX -= minX;
		maxY -= minY;
		minX = 0;
		minY = 0;
        System.out.println(minX + " " + minY + " => " + maxX + " " + maxY);
		boolean done = false;
		int steps = 0;
		long curMinX, curMinY, curMaxX, curMaxY;
		long bestArea = Long.MAX_VALUE;
		List<Point> bestPoints = new ArrayList<>();
		int bestPointSec = 0;
		int sec = 0;
		while (!done) {
			curMinX = Long.MAX_VALUE;
			curMinY = Long.MAX_VALUE;
			curMaxX = Long.MIN_VALUE;
			curMaxY = Long.MIN_VALUE;
			// Assuming the letter will form somewhere within original bounds.
			for (Point point : points) {
				curMinX = Math.min(curMinX, point.x);
				curMinY = Math.min(curMinY, point.y);
				curMaxX = Math.max(curMaxX, point.x);
				curMaxY = Math.max(curMaxY, point.y);
			}
			long area = (curMaxX - curMinX) * (curMaxY - curMinY);
			System.out.println(curMinX + " " + curMinY + " => " + curMaxX + " " + curMaxY);
			System.out.println("Area is " + area);
			if (area < bestArea) {
				bestArea = area;
				bestPoints.clear();
				for (Point point : points) {
					bestPoints.add(new Point(point.x - (int)curMinX, point.y - (int)curMinY, point.vx, point.vy));
				}
				bestPointSec = sec;
			} else {
				done = true;
			}
			steps++;

			for (Point point : points) {
				point.x += point.vx;
				point.y += point.vy;
				if (!(point.x >= 0 && point.x <= maxX && point.y >= 0 && point.y <= maxY)) {
					done = true;
				}
			}
			sec++;
		}
		System.out.println("Steps: " + steps);
		System.out.println("Best area is " + bestArea);
		System.out.println(points.size());

	    BufferedWriter writer = new BufferedWriter(new FileWriter("out10"));
		curMinX = Long.MAX_VALUE;
		curMinY = Long.MAX_VALUE;
		curMaxX = Long.MIN_VALUE;
		curMaxY = Long.MIN_VALUE;
		for (Point point : bestPoints) {
			curMinX = Math.min(curMinX, point.x);
			curMinY = Math.min(curMinY, point.y);
			curMaxX = Math.max(curMaxX, point.x);
			curMaxY = Math.max(curMaxY, point.y);
		}
		boolean[][] bestGrid = new boolean[(int)curMaxY + 1][(int)curMaxX + 1];
		for (Point point : bestPoints) {
			bestGrid[point.y][point.x] = true; 
		} 
		for (boolean[] z : bestGrid) {
			for (boolean b : z) {
				writer.write(b?"#":".");
			}
			writer.write("\n");
		}
		
		writer.close();
		System.out.println("Seconds: " + bestPointSec);
	}

}