package com.kolo.adventofcode.y2018;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import com.kolo.adventofcode.y2018.Puzzle20.Point;

public class Puzzle22 {	
	private static enum Equipment {
		GEAR, TORCH, NEITHER;
	}
	private static enum Type {
		ROCKY('.', Equipment.GEAR, Equipment.TORCH),
		WET('=', Equipment.GEAR, Equipment.NEITHER),
		NARROW('|', Equipment.TORCH, Equipment.NEITHER);
		
		final char symbol;
		final EnumSet<Equipment> validEquips;
		Type(char symbol, Equipment e1, Equipment e2) {
			this.symbol = symbol;
			this.validEquips = EnumSet.of(e1, e2);
		}
		
		@Override
		public String toString() {
			return "" + symbol;
		}
	}
	
	private static final int DEPTH = 11820;
	private static final int TARGET_X = 7;
	private static final int TARGET_Y = 782;
	private static final int PADDING = 5000;
	public static void main(String[] args) throws Exception {
		Type[][] grid = new Type[TARGET_Y+PADDING][TARGET_X+PADDING];
		long[][] el = new long[TARGET_Y+PADDING][TARGET_X+PADDING];
		int ans = 0;
		for (int i = 0; i < TARGET_Y+PADDING; i++) {
			for (int j = 0; j < TARGET_X+PADDING; j++) {
				if (i == 0 && j == 0) {
					el[i][j] = 0;
				} else if (i == TARGET_Y && j == TARGET_X) {
					el[i][j] = 0;
				} else if (i == 0) {
					el[i][j] = j * 16807;
				} else if (j == 0) {
					el[i][j] = i * 48271;
				} else {
					el[i][j] = el[i-1][j] * el[i][j-1];
				}
				
				el[i][j] += DEPTH;
				el[i][j] %= 20183;
				grid[i][j] = Type.values()[(int) (el[i][j] % Type.values().length)];
				if (i <= TARGET_Y && j <= TARGET_X) ans += grid[i][j].ordinal();
			}
		}
//		for (int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				System.out.print(grid[i][j]);
//			}
//			System.out.println();
//		}
		System.out.println(ans);
		
		PriorityQueue<BfsNode> q = new PriorityQueue<>();
		Set<BfsNode> locked = new HashSet<>();
		BfsNode cur = new BfsNode(0, 0, Equipment.TORCH);
		cur.distance = 0;
		q.add(cur);
		while(!q.isEmpty()) {
			cur = q.poll();
			if (!locked.add(cur)) {
				continue;
			}
			if (cur.x == TARGET_X && cur.y == TARGET_Y) {
				System.out.println(cur.e + " " + cur.distance);
			}

			for (Equipment e : Equipment.values()) {
				if (!grid[cur.y][cur.x].validEquips.contains(e)) {
					continue;
				}

				BfsNode right = new BfsNode(cur.x + 1, cur.y, e);
				if (grid[right.y][right.x].validEquips.contains(e)) {
					if (e == cur.e) {
						right.distance = cur.distance + 1;
					} else {
						right.distance = cur.distance + 8;
					}
					q.add(right);
				}
				
				if (cur.x > 0) {
					BfsNode left = new BfsNode(cur.x - 1, cur.y, e);
					if (grid[left.y][left.x].validEquips.contains(e)) {
						if (e == cur.e) {
							left.distance = cur.distance + 1;
						} else {
							left.distance = cur.distance + 8;
						}
						q.add(left);
					}
				}
				
				if (cur.y > 0) {
					BfsNode up = new BfsNode(cur.x, cur.y - 1, e);
					if (grid[up.y][up.x].validEquips.contains(e)) {
						if (e == cur.e) {
							up.distance = cur.distance + 1;
						} else {
							up.distance = cur.distance + 8;
						}
						q.add(up);
					}
				}
				
				
				BfsNode down = new BfsNode(cur.x, cur.y + 1, e);
				if (grid[down.y][down.x].validEquips.contains(e)) {
					if (e == cur.e) {
						down.distance = cur.distance + 1;
					} else {
						down.distance = cur.distance + 8;
					}
					q.add(down);
				}
			}
		}
	}
	
	private static final class BfsNode extends Point implements Comparable {
		int distance = Integer.MAX_VALUE;
		final Equipment e;
		BfsNode(int x, int y, Equipment e) {
			super(x, y);
			this.e = e;
		}
		
		@Override
		public String toString() {
			return String.format("(%d, %d) %s %d", x, y, e, distance);
		}
	
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof BfsNode)) {
				return false;
			}
			return super.equals(other) && ((BfsNode) other).e == e;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(x, y, e);
		}
		
		@Override
		public int compareTo(Object o) {
			if (!(o instanceof BfsNode)) {
				return 0;
			}
			BfsNode other = (BfsNode) o;
			return distance - other.distance;
		}
	}
}