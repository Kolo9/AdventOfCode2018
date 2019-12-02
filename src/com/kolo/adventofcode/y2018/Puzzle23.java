package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.kolo.adventofcode.y2018.Puzzle20.Point;
import com.kolo.adventofcode.y2018.Puzzle20.Point3d;

public class Puzzle23 {	
	
	private static final class Nanobot extends Point3d {
		int r;
		Nanobot(int x, int y, int z, int r) {
			super(x, y, z);
			this.r = r;
		}
	
		boolean inRange(Point3d o) {
			int dist = Math.abs(x - o.x) + Math.abs(y - o.y) + Math.abs(z - o.z);
			return dist <= r;
		}
	}
	
	static int minX, minY, minZ = Integer.MAX_VALUE;
	static int maxX, maxY, maxZ = Integer.MIN_VALUE;
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle23"));
		Pattern p = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");
		
		List<Nanobot> bots = new ArrayList<>();
		for (int i = 0; i < ss.size(); i++) {
			Matcher m = p.matcher(ss.get(i));
			m.find();
			Nanobot n;
			bots.add(
			    n = new Nanobot(
			        Integer.parseInt(m.group(1)),
			        Integer.parseInt(m.group(2)),
			       	Integer.parseInt(m.group(3)),
			        Integer.parseInt(m.group(4))));
			minX = Math.min(minX, n.x);
			minY = Math.min(minY, n.y);
			minZ = Math.min(minZ, n.z);
			maxX = Math.max(maxX, n.x);
			maxY = Math.max(maxY, n.y);
			maxZ = Math.max(maxZ, n.z);
		}
		
		Nanobot largestRadiusBot = bots.stream().sorted((b1, b2) -> b2.r - b1.r).collect(Collectors.toList()).get(0);
		int ans = 0;
		for (Nanobot other : bots) {
			if (largestRadiusBot.inRange(other)) {
				ans++;
			}
		}
		System.out.println(ans);

		Multiset<Point3d> botCounts = HashMultiset.create();
		List<Point3d> bestPoints = new ArrayList<>();
		for (Nanobot bot : bots) {
			for (int x = bot.x - bot.r; x <= bot.x + bot.r; x++) {
				for (int y = bot.y - bot.r; y <= bot.y + bot.r; y++) {
					for (int z = bot.z - bot.r; z <= bot.z + bot.r; z++) {
						Point3d q = new Point3d(x, y, z);
						if (bot.inRange(q)) {
							botCounts.add(q);
							if (bestPoints.isEmpty() || botCounts.count(q) > botCounts.count(bestPoints.get(0))) {
								bestPoints.clear();
								bestPoints.add(q);
							} else if (botCounts.count(q) == botCounts.count(bestPoints.get(0))) {
								bestPoints.add(q);
							}
						}
					}
				}
			}
		}
		System.out.println(bestPoints);
		
		
//		for (int x = minX; x <= maxX; x++) {
//			for (int y = minY; y <= maxY; y++) {
//				for (int z = minZ; z <= maxZ; z++) {
//					
//				}
//			}
//		}
//		
//		
//		System.out.println(bots.stream().mapToInt(b -> {
//			int ret = 0;
//			for (Nanobot other : bots) {
//				if (b.inRange(other)) {
//					ret++;
//				}
//			}
//			return ret;
//		}).max().getAsInt());
	}
}