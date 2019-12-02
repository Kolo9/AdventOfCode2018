package com.kolo.adventofcode.y2018;

import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle4 {
	private static class Guard {
		int id;
		int shifts = 0;
		int totalMinutesAsleep = 0;
		Map<Integer, Integer> minuteToTimesAsleep = new HashMap<>();
		
		Guard(int id) {
			this.id = id;
		}

		private void sleep(int sleepMinute, int wakeMinute) {
			totalMinutesAsleep += wakeMinute - sleepMinute;
			for (int minute = sleepMinute; minute < wakeMinute; minute++) {
				minuteToTimesAsleep.put(minute, minuteToTimesAsleep.getOrDefault(minute, 0) + 1);
			}
		}

		private Entry<Integer, Integer> getSleepiestMinute() {
			Optional<Entry<Integer, Integer>> sleepiestMinute = minuteToTimesAsleep.entrySet().stream().max((e1, e2) -> e1.getValue() - e2.getValue());
			if (sleepiestMinute.isPresent()) {
				return sleepiestMinute.get();
			} else {
				return new AbstractMap.SimpleEntry(-1, -1);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(Paths.get("puzzle5"));
	     
	
		Pattern recordPattern = Pattern.compile("\\[.*? \\d\\d:(\\d\\d)\\] (.*)");
		Pattern newGuardPattern = Pattern.compile("Guard #(\\d+) begins shift");
	    List<String> records = new ArrayList<>();
		int duplicates = 0;
		while(in.hasNextLine()) {
			String record = in.nextLine();
			records.add(record);
		}
		
		// Records all 00:xx except potentially when guard starts shift (23:xx)
		
		Map<Integer, Guard> guards = new HashMap<>();
		int curGuardId = -1;
		for (int i = 0; i < records.size(); i++) {
			String record = records.get(i);
			Matcher recordMatcher = recordPattern.matcher(record);
			recordMatcher.find();
			String action = recordMatcher.group(2);
			Matcher newGuardMatcher = newGuardPattern.matcher(action);
			if (newGuardMatcher.find()) {
				int guardId = Integer.parseInt(newGuardMatcher.group(1));
				if (!guards.containsKey(guardId)) {
					guards.put(guardId, new Guard(guardId));
				}
				guards.get(guardId).shifts++;
				curGuardId = guardId;
			} else {
				int sleepMinute = Integer.parseInt(recordMatcher.group(1));
				i++;
				record = records.get(i);
				recordMatcher = recordPattern.matcher(record);
				recordMatcher.find();
				int wakeMinute = Integer.parseInt(recordMatcher.group(1));
				guards.get(curGuardId).sleep(sleepMinute, wakeMinute);
			}
		}

		Guard sleepiestGuard = guards.values().stream().max((g1, g2) -> g1.totalMinutesAsleep - g2.totalMinutesAsleep).get();
		System.out.println(sleepiestGuard.id);
		System.out.println(sleepiestGuard.getSleepiestMinute().getKey());
		System.out.println(sleepiestGuard.id * sleepiestGuard.getSleepiestMinute().getKey());
		
		System.out.println();
		
		Guard sleepiestGuard2 = guards.values().stream().max((g1, g2) -> g1.getSleepiestMinute().getValue() - g2.getSleepiestMinute().getValue()).get();
		System.out.println(sleepiestGuard2.id);
		System.out.println(sleepiestGuard2.getSleepiestMinute().getKey());
		System.out.println(sleepiestGuard2.id * sleepiestGuard2.getSleepiestMinute().getKey());
	}

}


