package com.kolo.adventofcode.y2018;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Puzzle9 {
	private static final int NUM_PLAYERS = 470;
	private static final int NUM_MARBLES = 7217000 + 1;

	public static void main(String[] args) throws Exception {
		List<Integer> marbles = new ArrayList<>();
		marbles.add(0);
		int curIndex = 0;
		
		Map<Integer, Long> playerScores = new HashMap<>();
		for (int i = 0; i < NUM_PLAYERS; i++) {
			playerScores.put(i, 0L);
		}
		int curPlayer = 0;
		long ans = 0;
		for(int curMarble = 1; curMarble < NUM_MARBLES; curMarble++, curPlayer++, curPlayer %= playerScores.size()) {
			if (curMarble % 100000 == 0) System.out.println(curMarble);
			if (curMarble % 23 == 0) {
				int rmIdx = mod(curIndex - 7, marbles.size());
				int marbleValue = marbles.remove(rmIdx);
				curIndex = mod(rmIdx, marbles.size());
				//System.out.println("Player " + curPlayer + " keeps " + curMarble + " and " + marbleValue);
				playerScores.put(curPlayer, playerScores.get(curPlayer) + curMarble + marbleValue);
				ans = Math.max(ans, playerScores.get(curPlayer));
			} else {
				int idx = mod(curIndex + 2, marbles.size());
				if (idx == 0) {
					idx = marbles.size();
				}
				marbles.add(idx, curMarble);
				curIndex = idx;
			}
			//System.out.println(curPlayer + ": " + marbles + ": " + marbles.get(curIndex));
		}

		System.out.println(ans);
	}

	private static int mod(int n, int m) {
		return ((n % m) + m) % m;
	}
}