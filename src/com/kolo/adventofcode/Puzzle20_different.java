package com.kolo.adventofcode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.kolo.adventofcode.Puzzle20.Point;

public class Puzzle20_different {	
	
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
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle20"));
		String s = ss.get(0).substring(1, ss.get(0).length() - 1);
		helper(new StringBuilder(s), new Point(0, 0), new StringBuilder());
		System.out.println(tmp.size());
	}
	private static ArrayList<String> tmp = new ArrayList<>();
	private static void helper(StringBuilder s, Point curPoint, StringBuilder b) {
		if (s.length() == 0) {
			tmp.add(b.toString());
			return;
		}
//		System.out.println(s);
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
//				System.out.println("Options: " + String.join(", ", options));
				for (String option : options) {
					helper(new StringBuilder(option + s.substring(i)), curPoint, new StringBuilder(b));
				}
				break;
			case 'N':
				Point north = new Point(curPoint.x, curPoint.y - 1);
				helper(s.deleteCharAt(0), north, b.append("N"));
				break;
			case 'E':
				Point east = new Point(curPoint.x + 1, curPoint.y);
				helper(s.deleteCharAt(0), east, b.append("E"));
				break;
			case 'S':
				Point south = new Point(curPoint.x, curPoint.y + 1);
				helper(s.deleteCharAt(0), south, b.append("S"));
				break;
			case 'W':
				Point west = new Point(curPoint.x - 1, curPoint.y);
				helper(s.deleteCharAt(0), west, b.append("W"));
				break;
			default:
			 System.err.println("WTF?");
			 System.exit(1);
			 break;
		}
	}
}