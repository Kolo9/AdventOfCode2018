package com.kolo.adventofcode;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Puzzle2_2 {

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(Paths.get("puzzle2"));
		List<String> ids = new ArrayList<>();
		while(in.hasNext()) {
			ids.add(in.next());
		}
String ans = "";
for (String id1 : ids) {
	for (String id2 : ids ) {
		int faulty = 0;
		for (int i = 0; i < id1.length(); i++) {
			if (id1.charAt(i) != id2.charAt(i)) {
				faulty++;
			}
		}
		if (faulty == 1) {
			for (int i = 0; i < id1.length(); i++) {
				if (id1.charAt(i) == id2.charAt(i)) {
					ans+= id1.charAt(i);
				}
			}
		}
	}
}
		System.out.println(ans);
	}

}


