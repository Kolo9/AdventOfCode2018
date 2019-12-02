package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

public class Puzzle20 {	
	
	 static class Point3d extends Point {
		 int z;
		 
		 Point3d(int x, int y, int z) {
			 super(x, y);
			 
			 this.z = z;
		 }

		 @Override
			public int hashCode() {
				return Objects.hash(x, y, z);
			}
			
			@Override
			public boolean equals(Object other) {
				if (!(other instanceof Point3d)) {
					return false;
				}
				Point3d otherPair = (Point3d) other;
				return otherPair.x == x && otherPair.y == y && otherPair.z == z;
			}
		
			@Override
			public String toString() {
				return String.format("(%d, %d, %d)", x, y, z);
			}
	 }

	 static class Point {
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
	
	private static enum Func {
		ADDR, ADDI, MULR, MULI, BANR, BANI, BORR, BORI, SETR, SETI, GTIR, GTRI, GTRR, EQIR, EQRI, EQRR;
	}
	
	private static long[] p(Func func, long[] r, long in1, long in2, long out) {
//		System.out.println("Doing " + func + " " + in1 + " " + in2 + " " + out);
		r = Arrays.copyOf(r, r.length);
		switch (func) {
		case ADDI:
			r[(int)out] = r[(int)in1] + in2;
			break;
		case ADDR:
			r[(int)out] = r[(int)in1] + r[(int)in2];
			break;
		case BANI:
			r[(int)out] = r[(int)in1] & in2;
			break;
		case BANR:
			r[(int)out] = r[(int)in1] & r[(int)in2];
			break;
		case BORI:
			r[(int)out] = r[(int)in1] | in2;
			break;
		case BORR:
			r[(int)out] = r[(int)in1] | r[(int)in2];
			break;
		case EQIR:
			r[(int)out] = in1 == r[(int)in2] ? 1 : 0;
			break;
		case EQRI:
			r[(int)out] = r[(int)in1] == in2 ? 1 : 0;
			break;
		case EQRR:
			r[(int)out] = r[(int)in1] == r[(int)in2] ? 1 : 0;
			break;
		case GTIR:
			r[(int)out] = in1 > r[(int)in2] ? 1 : 0;
			break;
		case GTRI:
			r[(int)out] = r[(int)in1] > in2 ? 1 : 0;
			break;
		case GTRR:
			r[(int)out] = r[(int)in1] > r[(int)in2] ? 1 : 0;
			break;
		case MULI:
			r[(int)out] = r[(int)in1] * in2;
			break;
		case MULR:
			r[(int)out] = r[(int)in1] * r[(int)in2];
			break;
		case SETI:
			r[(int)out] = in1;
			break;
		case SETR:
			r[(int)out] = r[(int)in1];
			break;
		default:
			System.err.println("WTF?");
			System.exit(1);
			break;
		}
		return r;
	}
	private static class Parameters {
		String s;
		Point curPoint;
		
		Parameters(String s, Point curPoint) {
			this.s = s;
			
			this.curPoint = curPoint;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(curPoint, s);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Parameters)) {
				return false;
			}
			Parameters o = (Parameters) obj;
			return curPoint.equals(o.curPoint) && s.equals(o.s);
		}
	}
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle20"));
		String z = ss.get(0).substring(1, ss.get(0).length() - 1);
		MutableGraph<Point> g = GraphBuilder.directed().build();
		
		Set<Parameters> seen = new HashSet<>();
		List<Parameters> stack = new LinkedList<>();
		stack.add(new Parameters(z, new Point(0, 0)));
		while (!stack.isEmpty()) {
			Parameters parameters = stack.remove(0);
			String s = parameters.s;
			Point curPoint = parameters.curPoint;
			if (stack.size() % 10 == 0) {
				System.out.println(stack.size() + " - " + s.length());
			}
			
			if (s.isEmpty()) {
				continue;
			}
//			System.out.println(s);
			char c = s.charAt(0);
			switch (c) {
				case '(':
					int paren = 1;
					int i = 1;
					List<String> options = new ArrayList<>();
					options.add("");
					while(paren > 0) {
						if (s.charAt(i) == '(') {
							paren++;
							options.set(options.size() - 1, options.get(options.size() - 1) + s.charAt(i)); 
						} else if (s.charAt(i) == ')') {
							paren--;
							if (paren != 0) {
								options.set(options.size() - 1, options.get(options.size() - 1) + s.charAt(i)); 
							}
						} else if (s.charAt(i) == '|') {
							if (paren == 1) {
							options.add("");
							} else {
								options.set(options.size() - 1, options.get(options.size() - 1) + s.charAt(i)); 
							}
						} else {
							options.set(options.size() - 1, options.get(options.size() - 1) + s.charAt(i)); 
						}
						i++;
					}
//					System.out.println("Options: " + String.join(", ", options));
					for (String option : options) {
						Parameters p = new Parameters(option + s.substring(i), curPoint);
						if (seen.add(p)) stack.add(p);
					}
					break;
				case 'N':
					Point north = new Point(curPoint.x, curPoint.y - 1);
					g.putEdge(curPoint, north);
					g.putEdge(north,  curPoint);
					Parameters p = new Parameters(s.substring(1), north);
					if (seen.add(p)) stack.add(p);
					break;
				case 'E':
					Point east = new Point(curPoint.x + 1, curPoint.y);
					g.putEdge(curPoint, east);
					g.putEdge(east, curPoint);
					p = new Parameters(s.substring(1), east);
					if (seen.add(p)) stack.add(p);
					break;
				case 'S':
					Point south = new Point(curPoint.x, curPoint.y + 1);
					g.putEdge(curPoint, south);
					g.putEdge(south, curPoint);
					p = new Parameters(s.substring(1), south);
					if (seen.add(p)) stack.add(p);
					break;
				case 'W':
					Point west = new Point(curPoint.x - 1, curPoint.y);
					g.putEdge(curPoint, west);
					g.putEdge(west, curPoint);
					p = new Parameters(s.substring(1), west);
					if (seen.add(p)) stack.add(p);
					break;
				default:
				 System.out.println("WTF?");
				 System.exit(1);
				 break;
			}
		}
		//helper(g, s, new Point(0, 0));
		System.out.println(g);
		
		Djikstras d = new Djikstras(g);
		d.execute(new Point(0, 0));
		System.out.println(d.distance.values().stream().mapToInt(Integer::intValue).max().getAsInt());
		System.out.println(d.distance.values().stream().filter(v -> v >= 1000).count());
	}
}