package com.kolo.adventofcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle14 {	
	public static void main(String[] args) throws Exception {
		int elf1 = 0;
		int elf2 = 1;
		List<Long> scores = new ArrayList<>(50_000_000);
		scores.add(3L);
		scores.add(7L);
		List<Long> input = Arrays.asList(9L, 0L, 9L, 4L, 4L, 1L);
		//List<Long> input = Arrays.asList(5L, 1L, 5L, 8L, 9L);
		while (true) {
			List<Long> toAdd = parse2((long)scores.get(elf1) + scores.get(elf2));
			scores.addAll(toAdd);
			int start = -1;
			for (start = scores.size() - toAdd.size() - input.size(); start < scores.size() - input.size() && start >= 0; start++) {
				boolean found = true;
				for (int numChecked = 0; numChecked < input.size(); numChecked++) {
					if (scores.get(start + numChecked) != input.get(numChecked)) {
						found = false;
						break;
					}
				}
				if (found) {
					System.out.println(start);
					System.exit(0);
				}
			}

			elf1 += 1 + scores.get(elf1);
			elf1 %= scores.size();
			elf2 += 1 + scores.get(elf2);
			elf2 %= scores.size();
		}

//		for (int i = input; i < input + 10; i++) {
//			System.out.print(scores.get(i));
//		}

		//System.out.println(Arrays.toString(scores.toArray()));
	}

	private static List<Long> parse(long i) {
		List<Long> l = new ArrayList<>();
		for (char c : Long.toString(i).toCharArray()) {
			l.add(Long.parseLong("" + c));
		}
		return l;
	}
	private static List<Long> parse2(long i) {
		List<Long> l = new ArrayList<>();
		if (i == 0) {
			l.add(0L);
			return l;
		}
		while (i > 0) {
			l.add(i % 10);
			i /= 10;
		}
		Collections.reverse(l);
		return l;
	}
}