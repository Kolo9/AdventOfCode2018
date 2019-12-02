package com.kolo.adventofcode;

import java.nio.file.Files;
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

public class Puzzle5 {
	public static void main(String[] args) throws Exception {
		String polymer = String.join("", Files.readAllLines(Paths.get("puzzle5")));

		int shortestLength = polymer.length();
		for (int i = (int) 'a'; i <= (int) 'z'; i++) {
			char c = (char) i;
			String newPolymer = polymer.replaceAll("(?i)" + c, "");
			String regex = "([A-Za-z])(?!\\1)(?i)\\1";
			while (newPolymer.matches(".*" + regex + ".*")) {
				newPolymer = newPolymer.replaceAll(regex, "");
			}
			shortestLength = Math.min(shortestLength, newPolymer.length());
		}

		System.out.println(shortestLength);
	}

}