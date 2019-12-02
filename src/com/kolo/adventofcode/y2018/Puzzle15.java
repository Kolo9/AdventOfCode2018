package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Puzzle15 {	
	private static class Point {
		int x;
		int y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		List<Point> getAdj(int width, int height) {
			List<Point> ret = new ArrayList<>();
			ret.add(new Point(x, y-1));
			ret.add(new Point(x, y+1));
			ret.add(new Point(x-1, y));
			ret.add(new Point(x+1, y));
			ret.removeIf(p -> p.x < 0 || p.x >= width || p.y < 0 || p.y >= height);
			return ret;
		}
		
		int dist(Point other, Tile[][] map) {
			if (this.equals(other)) {
				return 0;
			}
			Set<Point> explored = new HashSet<>();
			List<Point> possible = new ArrayList<>();
			Map<Point, Integer> dists = new HashMap<>();
			possible.add(this);
			dists.put(this, 0);
			while (!possible.isEmpty()) {
				Point p = possible.remove(0);
				if (!explored.add(p)) {
					continue;
				}
				List<Point> adjs = p.getAdj(map[0].length, map.length);
				for (Point adj : adjs) {
					if (explored.contains(adj)) {
						continue;
					}
					if (adj.equals(other)) {
						if (this.equals(new Point(4, 4))) {
//						System.err.println(this + " to " + other + " is " + (dists.get(p) + 1));
						}
						return dists.get(p) + 1;
					}
					if (map[adj.y][adj.x].unit != null
							|| !map[adj.y][adj.x].usable) {
						continue;
					}
					possible.add(adj);
					dists.put(adj, dists.get(p) + 1);
				}
			}
			return Integer.MAX_VALUE;
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
	private static final class Unit extends Point {
		int hp = 200;
		boolean isGoblin;
	Unit(int x, int y, boolean isGoblin) {
		super(x, y);
		this.isGoblin = isGoblin;
		//System.out.println((isGoblin ? "Goblin" : "Elf") + " at " + this);
	}
	@Override
	public String toString() {
		return String.format("(%d, %d, %s, %d)", x, y, isGoblin ? "goblin" : "elf", hp);
	}
	
	}
	private static class Tile {
		boolean usable;
		Unit unit = null;
		Tile(boolean usable) {
			this.usable = usable;
		}
	}
	
	private static final boolean DEBUG = false;
	public static void main(String[] args) throws Exception {
		int elfAttack = 25;
		for (int exampleScore : Arrays.asList(0)) {
	//for (int exampleScore : Arrays.asList(18740, 27730, 27755, 28944, 36334, 39514, 0)) {
		List<String> ss = Files.readAllLines(Paths.get("puzzle15" + (exampleScore == 0 ? "" : "_"+exampleScore)));
		Tile[][] map = new Tile[ss.size()][ss.get(0).length()];
		List<Unit> goblins = new ArrayList<>();
		List<Unit> elves = new ArrayList<>();
		for (int i = 0; i < ss.size(); i++) {
			String s = ss.get(i);
			for (int j = 0; j < s.length(); j++) {
				map[i][j] = new Tile(s.charAt(j) != '#');
				if (s.charAt(j) == 'G') {
					goblins.add(new Unit(j, i, true));
					map[i][j].unit = goblins.get(goblins.size() - 1);
				} else if (s.charAt(j) == 'E') {
					elves.add(new Unit(j, i, false));
					map[i][j].unit = elves.get(elves.size() - 1);
				}
			}
		}
		
		int rounds = 0;
		outerouter:
		while (!goblins.isEmpty() && !elves.isEmpty()) {
			List<Unit> units = new ArrayList<>(goblins);
			units.addAll(elves);
			units.sort((u1, u2) -> {
				if (u1.y == u2.y) {
					return u1.x - u2.x;
				}
				return u1.y - u2.y;
			});
			if (DEBUG) {
			System.out.println(rounds);
//			System.out.println(elves);
//			System.out.println(goblins);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					char c;
					if (!map[i][j].usable) {
						c = '#';
					} else if (map[i][j].unit == null) {
						c = '.';
					} else if (map[i][j].unit.isGoblin) {
						c = 'G';
					} else {
						c = 'E';
					}
					System.out.print(c);
				}
				final int z = i;
				List<Unit> unitsToPrint = units.stream().filter(u -> u.y == z).sorted((u1, u2) -> u1.x - u2.x).collect(Collectors.toList());
				System.out.println(" " + unitsToPrint);
			}
			System.out.println();
		}
			for (Unit u : units) {
				if(u.hp<=0)continue;
					List<Unit> enemies = u.isGoblin ? elves : goblins;
					enemies.removeIf(e -> e.hp <= 0);
					if (enemies.size() == 0) {
						// lol.
						break outerouter;
					}
					Point pointToReach = null;
					int bestMhDist = -1;
					outer:
					for (Unit enemy : enemies) {
						List<Point> nmeAdjPoints = enemy.getAdj(map[0].length, map.length);
						nmeAdjPoints.removeIf(p -> !map[p.y][p.x].usable);
						Iterator<Point> iter = nmeAdjPoints.iterator();
						while (iter.hasNext()) {
							Point p = iter.next();
							if (p.equals(u)) {
								pointToReach = p;
								bestMhDist = 0;
								// lol
								break outer;
							}
 							boolean found = false;
							for (Unit g : goblins) {
								if (g == u) continue;
								if (g.equals(p)) {
									iter.remove();
									found = true;
									break;
								}
							}
							if (found) {
								continue;
							}
							for (Unit e : elves) {
								if (e == u) continue;
								if (e.equals(p)) {
									iter.remove();
									found = true;
									break;
								}
							}
						}
						
						for (Point nmeAdjPoint : nmeAdjPoints) {
							int mhDist = u.dist(nmeAdjPoint, map);
							if (mhDist == Integer.MAX_VALUE) {
								continue;
							}
							if (pointToReach == null || mhDist < bestMhDist) {
								pointToReach = nmeAdjPoint;
								bestMhDist = mhDist;
							} else if (mhDist == bestMhDist && (nmeAdjPoint.y < pointToReach.y || (nmeAdjPoint.y == pointToReach.y && nmeAdjPoint.x < pointToReach.x))) {
								pointToReach = nmeAdjPoint;
								bestMhDist = mhDist;
							}
						}
					}
					
					if (pointToReach == null) {
						continue;
					} else if (!u.isGoblin) {
//						System.err.println("Elf point to reach is " + pointToReach);
					}
					
					List<Point> moves = u.getAdj(map[0].length, map.length);
					
					moves.removeIf(p -> !map[p.y][p.x].usable);
					Iterator<Point> iter = moves.iterator();
					while (iter.hasNext()) {
						Point p = iter.next();
							boolean found = false;
						for (Unit g : goblins) {
							if (g == u) continue;
							if (g.equals(p)) {
								iter.remove();
								found = true;
								break;
							}
						}
						if (found) {
							continue;
						}
						for (Unit e : elves) {
							if (e == u) continue;
							if (e.equals(p)) {
								iter.remove();
								found = true;
								break;
							}
						}
					}
					
					Point bestMove = null;
					int bestMoveDist = -1;
					if (u.x == pointToReach.x && u.y == pointToReach.y) {
						bestMove = pointToReach;
					} else {
					for (Point move : moves) {
						int dist = move.dist(pointToReach, map);
						 if (!u.isGoblin) {
//								System.err.println("Elf dist to move " + move + " is " + dist);
							}
						if (dist == Integer.MAX_VALUE) {
							continue;
						}
						if (bestMove == null
								|| dist < bestMoveDist
								|| (dist == bestMoveDist && (move.y < bestMove.y || (move.y == bestMove.y && move.x < bestMove.x)))) {
							bestMoveDist = dist;
							bestMove = move;
						}
					}
					}
					
					if (bestMove != null) {
						map[u.y][u.x].unit = null;  
						u.x = bestMove.x;
						u.y = bestMove.y;
						map[u.y][u.x].unit = u;  
					}

					Unit attackTarget = null;
//					System.err.println(u + " enemies are " + enemies);
					for (Unit enemy : enemies) {
						if (enemy.hp <= 0) continue;
						int d = Math.abs(u.x - enemy.x) + Math.abs(u.y - enemy.y);
						if (d == 1) {
							if (attackTarget == null
									|| enemy.hp < attackTarget.hp
									|| (enemy.hp == attackTarget.hp && (enemy.y < attackTarget.y || (enemy.y == attackTarget.y && enemy.x < attackTarget.x)))) {
								attackTarget = enemy;
							}
						}
					}
				
					if (attackTarget != null) {
						attackTarget.hp -= u.isGoblin ? 3 : elfAttack;
						//System.err.println(u + " attacked " + attackTarget);
						if (attackTarget.hp <= 0) {
							map[attackTarget.y][attackTarget.x].unit = null;
						}
					}
			}
			goblins.removeIf(g -> g.hp <= 0);
			elves.removeIf(e -> e.hp <= 0);
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					map[i][j].unit = null;
					for (Unit u : goblins) {
						if (u.x == j && u.y == i) {
							map[i][j].unit = u;
							break;
						}
					}
					for (Unit u : elves) {
						if (u.x == j && u.y == i) {
							map[i][j].unit = u;
							break;
						}
					}
				}
			}
			rounds++;
		}
		int hp = goblins.stream().mapToInt(g -> g.hp).sum() + elves.stream().mapToInt(e -> e.hp).sum();
		System.out.println("Rounds: " + rounds);
		System.out.println("Goblins: " + goblins);
		System.out.println("Elves: " + elves.size() + " " + elves);
		System.out.println("Actual: " + hp * rounds);
		System.out.println("Expected: " + exampleScore);
	}
	}
}