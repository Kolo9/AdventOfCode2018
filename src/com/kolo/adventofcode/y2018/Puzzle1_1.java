package com.kolo.adventofcode.y2018;

import java.nio.file.Paths;
import java.util.Scanner;

public class Puzzle1_1 {

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(Paths.get("puzzle1"));
		int total = 0;
		while(in.hasNextInt()) {
			total += in.nextInt();
		}
		System.out.println(total);
	}

}


