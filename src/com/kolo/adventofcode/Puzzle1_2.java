package com.kolo.adventofcode;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Puzzle1_2 {

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(Paths.get("puzzle1"));
		List<Integer> frequencies = new ArrayList<>();
		while(in.hasNextInt()) {
			frequencies.add(in.nextInt());
		}

		Set<Integer> totalsSeen = new HashSet<>();
		int total = 0;
		int index = 0;
		do {
			total += frequencies.get(index++ % frequencies.size());
		} while (totalsSeen.add(total));
		System.out.println(total);
	}

}


