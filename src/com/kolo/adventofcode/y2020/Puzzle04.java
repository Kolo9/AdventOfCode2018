package com.kolo.adventofcode.y2020;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Puzzle04 {
    public static void main(String[] args) throws Exception {
        String[] passports = new String(Files.readAllBytes(Paths.get(Puzzle03.class.getResource("in04").toURI())))
                .split("\\s*\\n\\s*\\n\\s*");
        int validPassportCountPart1 = 0;
        int validPassportCountPart2 = 0;
        for (String passport : passports) {
            passport = passport.replace("\n", " ");
            passport = passport.replace("\r", " ");
            if (passport.contains("byr:") && passport.contains("iyr:") && passport.contains("eyr:")
                    && passport.contains("hgt:") && passport.contains("hcl:") && passport.contains("ecl:")
                    && passport.contains("pid:")) {
                validPassportCountPart1++;

                Matcher m = Pattern.compile(".*byr:(\\d{4})\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }
                int byr = Integer.parseInt(m.group(1));
                if (byr < 1920 || byr > 2002) {
                    continue;
                }

                m = Pattern.compile(".*iyr:(\\d{4})\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }
                int iyr = Integer.parseInt(m.group(1));
                if (iyr < 2010 || iyr > 2020) {
                    continue;
                }

                m = Pattern.compile(".*eyr:(\\d{4})\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }
                int eyr = Integer.parseInt(m.group(1));
                if (eyr < 2020 || eyr > 2030) {
                    continue;
                }

                m = Pattern.compile(".*hgt:(\\d+)(cm|in)\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }
                int hgt = Integer.parseInt(m.group(1));
                boolean isCm = m.group(2).equals("cm");
                if (isCm) {
                    if (hgt < 150 || hgt > 193) {
                        continue;
                    }
                } else {
                    if (hgt < 59 || hgt > 76) {
                        continue;
                    }
                }

                m = Pattern.compile(".*hcl:#[0-9a-f]{6}\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }

                m = Pattern.compile(".*ecl:(amb|blu|brn|gry|grn|hzl|oth)\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }
                m = Pattern.compile(".*pid:\\d{9}\\b.*").matcher(passport);
                if (!m.matches()) {
                    continue;
                }
                validPassportCountPart2++;
            }
        }
        System.out.println(validPassportCountPart1);
        System.out.println(validPassportCountPart2);
    }
}