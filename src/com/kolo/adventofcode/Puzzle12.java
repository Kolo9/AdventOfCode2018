package com.kolo.adventofcode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle12 {
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
		List<String> ss = Files.readAllLines(Paths.get("puzzle12"));
		String state = ss.remove(0);
		System.out.println("Initial state: " + state);
		Pattern p = Pattern.compile("(.*) => (.*)");
		StringBuilder newState = new StringBuilder();
		Map<String, String> rules = new HashMap<>();
		for (String rule : ss) {
			Matcher m = p.matcher(rule);
			m.find();
			String llcrr = m.group(1);
			String n = m.group(2);
			rules.put(llcrr, n);
		}
		long prefixChars = 0;
		for (int gen = 0; gen < 5000; gen++) {
			if (gen == 5000 || gen == 50000 || gen == 500000) {
				System.out.println("Generation: " + gen);
				System.out.println("State: " + state);
				System.out.println("Prefix chars: " + prefixChars);
				System.out.println();
			}
			for (int i = -2; i < state.length() + 2; i++) {
				boolean found = false;
				for (Entry<String, String> rule : rules.entrySet()) {
					if (i >= 2 && i < state.length() - 2) {
						if (state.substring(i - 2, i + 3).equals(rule.getKey())) {
							newState.append(rule.getValue());
							found = true;
						}
					} else if (i == 0) {
						if (rule.getKey().substring(0, 2).equals("..") && state.substring(0, 3).equals(rule.getKey().substring(2))) {
							newState.append(rule.getValue());
							found = true;
						}
					} else if (i == 1) {
						if (rule.getKey().charAt(0) == '.' && state.substring(0, 4).equals(rule.getKey().substring(1))) {
							newState.append(rule.getValue());
							found = true;
						}					
					} else if (i == state.length() - 2) {
						if (rule.getKey().charAt(4) == '.' && state.substring(state.length() - 4).equals(rule.getKey().substring(0, 4))) {
							newState.append(rule.getValue());
							found = true;
						}
					} else if (i == state.length() - 1) {
						if (rule.getKey().substring(3).equals("..") && state.substring(state.length() - 3).equals(rule.getKey().substring(0, 3))) {
							newState.append(rule.getValue());
							found = true;
						}
					} else if (i == -2) {
						if (rule.getKey().equals("...." + state.substring(0, 1))) {
							newState.append(rule.getValue());
							found = true;
						}
					} else if (i == -1) {
						if (rule.getKey().equals("..." + state.substring(0, 2))) {
							newState.append(rule.getValue());
							found = true;
						}
					} else if (i == state.length()) {
						if (rule.getKey().equals(state.substring(state.length() - 2) + "...")) {
							newState.append(rule.getValue());
							found = true;
						}						
					} else {
						// i == state.length() + 1
						if (rule.getKey().equals(state.substring(state.length() - 1) + "....")) {
							newState.append(rule.getValue());
							found = true;
						}						
					}
					if (found) {
						break;
					}
				}
				if (!found) {
					newState.append(".");
				}
			}
			prefixChars += 2;
			while(newState.charAt(0) == '.') {
				newState.deleteCharAt(0);
				prefixChars--;
			}
			while(newState.charAt(newState.length() - 1) == '.') {
				newState.deleteCharAt(newState.length() - 1);
			}
			state = newState.toString();
			newState.setLength(0);
		}
//		Initial state: ##.#############........##.##.####..#.#..#.##...###.##......#.#..#####....##..#####..#.#.##.#.##
//		Generation: 5000
//		State: #...#...#...#...#...#...#...#...#.####...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#..####...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#..####
//		Prefix chars: -4918
//
//		Generation: 50000
//		State: #...#...#...#...#...#...#...#...#.####...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#..####...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#..####
//		Prefix chars: -49918
//
//		Generation: 500000
//		State: #...#...#...#...#...#...#...#...#.####...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#..####...#...#...#...#...#...#...#...#...#...#...#...#...#...#...#..####
//		Prefix chars: -499918

//		Generation: 50000000000
		prefixChars = -49999999918L;
		System.out.println(state);
		long ans = 0;
		for (int i = 0; i < state.length(); i++) {
			if (state.charAt(i) == '#') {
				ans += (i - prefixChars);
			}
		}
		System.out.println(ans);
	}

}