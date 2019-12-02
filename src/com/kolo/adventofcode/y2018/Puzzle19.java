package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle19 {	

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
		List<String> ss = Files.readAllLines(Paths.get("puzzle19"));
		Pattern ins = Pattern.compile("(.*) (\\d+) (\\d+) (\\d+)");
		long inst = 0;
		int instP = 1;
		long[] registers = new long[6];
		registers[0] = 1;
		
		//////
		//4:eqrr 3 4 3
		//[0, 4, 236, 236, 10551261, 1]
		//registers = new long[] {0, 4, 10551261, 10551261, 10551261, 2};
		registers = new long[] {0, 4, 6, 6, 6, 0};
		inst = 4;
		//////
		long z = 0;
		while (inst < ss.size()) {
			//if (inst == 2) {
				System.out.println(z);
				System.out.println(inst + ":" + ss.get((int) inst));
				System.out.println(Arrays.toString(registers));
				System.out.println();
			//}
			z++;
//			System.out.println(Arrays.toString(registers));
//			System.out.println(inst);
				Matcher insM = ins.matcher(ss.get((int) inst));
				insM.find();
				registers = p(Func.valueOf(insM.group(1).toUpperCase()),
						registers,
						Long.parseLong(insM.group(2)),
						Long.parseLong(insM.group(3)),
						Long.parseLong(insM.group(4)));
				registers[instP]++;
			
			inst = registers[instP];
		}
		registers[instP]--;
		System.out.println(Arrays.toString(registers));
	}
}