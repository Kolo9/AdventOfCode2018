package com.kolo.adventofcode;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle3 {
	private static final class Pair {
		int i;
		int j;

		Pair(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public int hashCode() {
			return Objects.hash(i, j);
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Pair)) {
				return false;
			}
			Pair otherPair = (Pair) other;
			return otherPair.i == i && otherPair.j == j;
		}
	
	}

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(Paths.get("puzzle3"));
	     
	
	    Pattern r = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
	    List<String> claims = new ArrayList<>();
		Map<Pair, Integer> coveredPoints = new HashMap<>();
		int duplicates = 0;
		while(in.hasNextLine()) {
			String claim = in.nextLine();
			claims.add(claim);
			Matcher m = r.matcher(claim);
			m.find();
			int left = Integer.parseInt(m.group(2));
			int top = Integer.parseInt(m.group(3));
			int width = Integer.parseInt(m.group(4));
			int height = Integer.parseInt(m.group(5));
			for (int i = left; i < left + width; i++) {
				for (int j = top; j < top + height; j++) {
					Pair p = new Pair(i, j);
					if (coveredPoints.getOrDefault(p, 0) == 1) {
						duplicates++;
					}
					coveredPoints.put(p, coveredPoints.getOrDefault(p, 0) + 1);
				}
			}
		}
		System.out.println(duplicates);

		for (String claim : claims) {
			Matcher m = r.matcher(claim);
			m.find();
			int id = Integer.parseInt(m.group(1));
			int left = Integer.parseInt(m.group(2));
			int top = Integer.parseInt(m.group(3));
			int width = Integer.parseInt(m.group(4));
			int height = Integer.parseInt(m.group(5));
			boolean validClaim = true;
			for (int i = left; i < left + width; i++) {
				for (int j = top; j < top + height; j++) {
					Pair p = new Pair(i, j);
					if (coveredPoints.get(p) > 1) {
						validClaim = false;
					}
				}
			}
			if (validClaim) {
				System.out.println(id);
			}
		}
		
	}

}


