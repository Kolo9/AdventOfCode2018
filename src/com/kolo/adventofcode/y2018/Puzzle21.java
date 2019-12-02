package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle21 {	

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
	private static LinkedHashSet<Long> z = new LinkedHashSet<>();
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle21"));
		Pattern ins = Pattern.compile("(.*) (\\d+) (\\d+) (\\d+)");
		long inst = 0;
		int instP = 2;
		long[] registers = new long[6];
		registers[0] = 0;
		int tmp = 0;
		while (inst < ss.size()) {
			tmp++;
//				System.out.println(inst + ":" + ss.get((int) inst));
//				System.out.println(Arrays.toString(registers));
//				System.out.println();
			
//			System.out.println(Arrays.toString(registers));
//			System.out.println(inst);
			long[] previous = Arrays.copyOf(registers, registers.length);
				Matcher insM = ins.matcher(ss.get((int) inst));
				insM.find();
				registers = p(Func.valueOf(insM.group(1).toUpperCase()),
						registers,
						Long.parseLong(insM.group(2)),
						Long.parseLong(insM.group(3)),
						Long.parseLong(insM.group(4)));
				registers[instP]++;
			if (ss.get((int) inst).equals("eqrr 5 0 1")) {
				System.out.println(z.size() + " " + tmp + ": " + Arrays.toString(previous) + " changed to " + Arrays.toString(registers) + " via " + ss.get((int) inst));
				if(!z.add(previous[5])) {
					System.out.println(z);
					Iterator<Long> iter = z.iterator();
					long l = -1;
					while(iter.hasNext()) l = iter.next();
					System.out.println(l);
					System.exit(0);
				}
			}
			inst = registers[instP];
		}
		registers[instP]--;
		System.out.println(Arrays.toString(registers));
	}
}