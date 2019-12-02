package com.kolo.adventofcode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;

public class Puzzle18 {	
	
	private static enum State {
		OPEN('.'), TREE('|'), LUMBERYARD('#');
		
		char symbol;
		
		State(char symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public String toString() {
			return "" + symbol;
		}
	}

	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle18"));
		int dim = ss.size();
		
		Map<Integer, Integer> solutionMap =
		    ImmutableMap.<Integer, Integer>builder()
		        .put(0, 229680)
		        .put(1, 226135)
		        .put(2,227160)
		        .put(3,225164)
		        .put(4,224237)
		        .put(5,215380)
		        .put(6,210000)
		        .put(7,205114)
		        .put(8,204336)
		        .put(9,196350)
		        .put(10,198990)
		        .put(11,197208)
		        .put(12,200772)
		        .put(13,199398)
		        .put(14,202124)
		        .put(15,198660)
		        .put(16,202070)
		        .put(17,200690)
		        .put(18,206581)
		        .put(19,206746)
		        .put(20,213624)
		        .put(21,214375)
		        .put(22,218544)
		        .put(23,217408)
		        .put(24,222534)
		        .put(25,222662)
		        .put(26,226914)
		        .put(27,226914)
		        .build();
		
		State[][] grid = new State[dim+2][dim+2];
		for (State[] row : grid) {
			Arrays.fill(row, State.OPEN);
		}
		for (int i = 0; i < ss.size(); i++) {
			for (int j = 0; j < ss.get(i).length(); j++) {
				grid[i+1][j+1] = ss.get(i).charAt(j) == '.' ? State.OPEN : ss.get(i).charAt(j) == '|' ? State.TREE : State.LUMBERYARD;
			}
		}
		

		Map<Integer, Integer> treeMap = new TreeMap<>();
		int count = 0;
		for (int t = 0; t < 100000; t++) {
			if (t >= 10010) {
				Multiset<State> counts = HashMultiset.create();
				for (int i = 1; i < grid.length - 1; i++) {
					for (int j = 1; j < grid[i].length - 1; j++) {
						counts.add(grid[i][j]);
					}
				}
				int score = counts.count(State.TREE) * counts.count(State.LUMBERYARD);
				if (solutionMap.get((t - 10010) % solutionMap.size()) != score) {
					System.out.println(t + " actual:" + score + " expected:" + solutionMap.get((t - 10010) % solutionMap.size()));
					System.exit(0);
				}
//				if (treeMap.containsValue(score)) {
//					count++;
//				} else {
//					if (count > 0) {
//						treeMap.clear();
//					}
//					count = 0;
//					treeMap.put(t, score);
//				}
			}
//			print(grid);
//			System.out.println("\n");
			State[][] gridCopy = new State[dim+2][dim+2];
			for (State[] row : gridCopy) {
				Arrays.fill(row, State.OPEN);
			}
			for (int i = 1; i < gridCopy.length - 1; i++) {
				for (int j = 1; j < gridCopy[i].length - 1; j++) {
					Multiset<State> adjacencies = HashMultiset.create();
					for (int i2 = i - 1; i2 <= i + 1; i2++) {
						for (int j2 = j - 1; j2 <= j + 1; j2++) {
							if (i2 == i && j2 == j) continue;
							adjacencies.add(grid[i2][j2]);
						}
					}
					switch (grid[i][j]) {
					case OPEN:
						if (adjacencies.count(State.TREE) >= 3) {
							gridCopy[i][j] = State.TREE;
						}
						break;
					case TREE:
						if (adjacencies.count(State.LUMBERYARD) >= 3) {
							gridCopy[i][j] = State.LUMBERYARD;
						} else {
							gridCopy[i][j] = State.TREE;
						}
						break;
					case LUMBERYARD:
						if (adjacencies.count(State.LUMBERYARD) == 0 || adjacencies.count(State.TREE) == 0) {
							gridCopy[i][j] = State.OPEN;
						} else {
							gridCopy[i][j] = State.LUMBERYARD;
						}
						break;
					}
				}
			}
			grid = gridCopy;
		}
		
		Multiset<State> counts = HashMultiset.create();
		for (int i = 1; i < grid.length - 1; i++) {
			for (int j = 1; j < grid[i].length - 1; j++) {
				counts.add(grid[i][j]);
			}
		}
		System.out.println(counts.count(State.TREE) * counts.count(State.LUMBERYARD));
		System.out.println(treeMap);
		System.out.println(count);
	}
	
	private static void print(State[][] grid) {
		for (State[] row : grid) {
			for (State s : row) {
				System.out.print(s);
			}
			System.out.println();
		}
	}
}