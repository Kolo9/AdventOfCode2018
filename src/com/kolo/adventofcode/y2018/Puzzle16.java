package com.kolo.adventofcode.y2018;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class Puzzle16 {	
	private static class Point {
		int x;
		int y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		List<Point> getOpenAdj(int width, int height) {
			List<Point> ret = new ArrayList<>();
			ret.add(new Point(x, y-1));
			ret.add(new Point(x, y+1));
			ret.add(new Point(x-1, y));
			ret.add(new Point(x+1, y));
			ret.removeIf(p -> p.x < 0 || p.x >= width || p.y < 0 || p.y >= height);
			return ret;
		}
		
		int mhDist(Point other) {
			return Math.abs(x - other.x) + Math.abs(y - other.y);
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
	}
	}
	private static class Tile {
		boolean usable;
		Unit unit = null;
		Tile(boolean usable) {
			this.usable = usable;
		}
	}
	
	private static enum Func {
		ADDR, ADDI, MULR, MULI, BANR, BANI, BORR, BORI, SETR, SETI, GTIR, GTRI, GTRR, EQIR, EQRI, EQRR;
	}
	
	private static long[] p(Func func, long[] r, int in1, int in2, int out) {
		System.out.println("Doing " + func + " " + in1 + " " + in2 + " " + out);
		r = Arrays.copyOf(r, r.length);
		switch (func) {
		case ADDI:
			r[out] = r[in1] + in2;
			break;
		case ADDR:
			r[out] = r[in1] + r[in2];
			break;
		case BANI:
			r[out] = r[in1] & in2;
			break;
		case BANR:
			r[out] = r[in1] & r[in2];
			break;
		case BORI:
			r[out] = r[in1] | in2;
			break;
		case BORR:
			r[out] = r[in1] | r[in2];
			break;
		case EQIR:
			r[out] = in1 == r[in2] ? 1 : 0;
			break;
		case EQRI:
			r[out] = r[in1] == in2 ? 1 : 0;
			break;
		case EQRR:
			r[out] = r[in1] == r[in2] ? 1 : 0;
			break;
		case GTIR:
			r[out] = in1 > r[in2] ? 1 : 0;
			break;
		case GTRI:
			r[out] = r[in1] > in2 ? 1 : 0;
			break;
		case GTRR:
			r[out] = r[in1] > r[in2] ? 1 : 0;
			break;
		case MULI:
			r[out] = r[in1] * in2;
			break;
		case MULR:
			r[out] = r[in1] * r[in2];
			break;
		case SETI:
			r[out] = in1;
			break;
		case SETR:
			r[out] = r[in1];
			break;
		default:
			System.err.println("WTF?");
			System.exit(1);
			break;
		}
		return r;
	}
	
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(Paths.get("puzzle16"));
		int ans = 0;
		List<Integer> tmp = new ArrayList<>();
		Multimap<Integer, Func> opMap = HashMultimap.create();
		while(in.hasNextInt()) {
			long[] start = new long[4];
			for (int i = 0; i < 4; i++) {
				start[i] = in.nextInt();
			}
			int[] op = new int[4];
			for (int i = 0; i < 4; i++) {
				op[i] = in.nextInt();
			}
			long[] end = new long[4];
			for (int i = 0; i < 4; i++) {
				end[i] = in.nextInt();
			}
			int valid = 0;
			List<Func> validFuncs = new ArrayList<>();
			//System.out.println("Checking from " + Arrays.toString(start) + " to " + Arrays.toString(end) + " via " + Arrays.toString(op));
		for (Func func : Func.values()) {
			long[] t = p(func, start, op[1], op[2], op[3]);
		if (Arrays.equals(t, end)) {
			valid++;
			validFuncs.add(func);
		}
		}
		if (opMap.containsKey(op[0])) {
			for (Func func : Func.values()) {
				if (!validFuncs.contains(func)) {
					if (op[0] == 1)
					System.out.println("Removing " + func + " for " + op[0]);
					opMap.remove(op[0], func);
				}
			}
		} else {
			if (op[0] == 1)
				System.out.println("Adding " + validFuncs + " for " + op[0]);
			opMap.putAll(op[0], validFuncs);
		}
		if (valid > 2) {
			ans++;
		}
		}
		System.out.println(ans);
		for (int i = 0 ; i < opMap.keySet().size(); i++) {
		for (int op : opMap.keySet()) {
			// Doesn't account for unseen ops, fine with my input.
			if (opMap.get(op).size() == 1) {
				for (int op2 : opMap.keySet()) {
					if (op == op2) continue;
					opMap.get(op2).remove(Iterables.getOnlyElement(opMap.get(op)));
				}
			}
		}
		}

		System.out.println(opMap);
		Scanner in2 = new Scanner(Paths.get("puzzle16_2"));
		long[] registers = new long[4];
		while (in2.hasNextInt()) {
			registers = p(Iterables.getOnlyElement(opMap.get(in2.nextInt())), registers, in2.nextInt(), in2.nextInt(), in2.nextInt());
		}
		System.out.println(Arrays.toString(registers));
	}
}