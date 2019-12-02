package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.kolo.adventofcode.y2018.Puzzle20.Point3d;

public class Puzzle24 {	
	private static class Group implements Comparable<Group> {
		private static int nextId = 1;
		int id;
		int unitCount;
		int hp;
		int ad;
		String attackType;
		int initiative;
		List<String> weaknesses = new ArrayList<>();
		List<String> immunities = new ArrayList<>();

		Group() {
			this.id = nextId++;
		}

		int effectivePower() {
			return unitCount * ad;
		}

		// Shit's backwards.
		void attack(Group other) {
			if (other.immunities.contains(attackType)) {
				return;
			}

			int power = effectivePower();
			if (other.weaknesses.contains(attackType)) {
				power *= 2;
			}

			int unitKillCount = power / other.hp;
			if (other.unitCount < unitKillCount) {
				other.unitCount = 0;
			} else {
				other.unitCount -= unitKillCount;
			}

			System.out.printf("Group %d does %d damage to group %d, killing %d units.\n", id, power, other.id, unitKillCount);
		}

		@Override
		public String toString() {
			return String.format("Group %d - %d units - hp: %d - ad: %d - weak: %s - immune: %s - ep: %d - init: %d", id, unitCount, hp, ad, weaknesses, immunities, effectivePower(), initiative);
		}

		@Override
		public int hashCode() {
			return id;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Group)) {
				return false;
			}

			return id ==((Group) obj).id;
		}

		@Override
		public int compareTo(Group other) {
			int ep = effectivePower();
			int oep = other.effectivePower();
			if (ep == oep) {
				return Integer.compare(other.initiative, initiative);
			}
			return (Integer.compare(oep, ep));
		}
	}
	
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle24"));
		Pattern p = Pattern.compile("(\\d+) units each with (\\d+) hit points with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)");
		Pattern pWeaknesses = Pattern.compile("weak to (.*?)[;)]");
		Pattern pImmunities = Pattern.compile("immune to (.*?)[;)]");
		List<Group> immuneSystem = new ArrayList<>();
		List<Group> infection = new ArrayList<>();

		List<Group> curArmy = null;
		
		for (String s : ss) {
			if (s.isEmpty()) {
				continue;
			} else if ("Immune System:".equals(s)) {
				curArmy = immuneSystem;
				continue;
			} else if ("Infection:".equals(s)) {
				curArmy = infection;
				continue;
			}
			
			Matcher mWeaknesses = pWeaknesses.matcher(s);
			List<String> weaknesses; 
			if (mWeaknesses.find()) {
				weaknesses = Arrays.asList(mWeaknesses.group(1).split(", "));
			} else {
				weaknesses = new ArrayList<>();
			}

			Matcher mImmunities = pImmunities.matcher(s);
			List<String> immunities;
			if (mImmunities.find()) {
				immunities = Arrays.asList(mImmunities.group(1).split(", "));
			} else {
				immunities = new ArrayList<>();
			}

			s = s.replaceAll(" \\(.*\\)", "");
			Matcher m = p.matcher(s);
			m.find();
			Group g = new Group();
			g.unitCount = Integer.parseInt(m.group(1));
			g.hp = Integer.parseInt(m.group(2));
			g.ad = Integer.parseInt(m.group(3));
			if (curArmy == immuneSystem) {
				g.ad += 59;
			}
			g.attackType = m.group(4);
			g.initiative = Integer.parseInt(m.group(5));
			g.weaknesses = weaknesses;
			g.immunities = immunities;

			curArmy.add(g);
		}

		while(immuneSystem.stream().anyMatch(g -> g.unitCount > 0)
				&& infection.stream().anyMatch(g -> g.unitCount > 0)) {
			Collections.sort(immuneSystem);
			Collections.sort(infection);
			
			// Target
			Map<Group, Group> targets = getTargets(immuneSystem, infection);
			targets.putAll(getTargets(infection, immuneSystem));
			
			// Attack
			List<Group> allGroups = new ArrayList<>(infection);
			allGroups.addAll(immuneSystem);
			Collections.sort(allGroups, (g1, g2) -> Integer.compare(g2.initiative, g1.initiative));
			for (Group g : allGroups) {
				if (targets.containsKey(g)) {
					g.attack(targets.get(g));
				}
			}

			immuneSystem.removeIf(g -> g.unitCount < 1);
			infection.removeIf(g -> g.unitCount < 1);
		}

		int unitCount =
				immuneSystem.stream().mapToInt(g -> g.unitCount).sum()
				+ infection.stream().mapToInt(g -> g.unitCount).sum();
		System.out.println(unitCount);
		if (infection.isEmpty()) {
			System.out.println("Immune");
		} else {
			System.out.println("Infection");
		}
	}

	private static Map<Group, Group> getTargets(List<Group> friendlies, List<Group> enemies) {
		Set<Group> unassignedTargets = new HashSet<>(enemies);
		Map<Group, Group> targets = new HashMap<>();
		
		for (Group friendly : friendlies) {
			List<Group> potentialTargets;
			if (unassignedTargets.stream().anyMatch(e -> e.weaknesses.contains(friendly.attackType))) {
				potentialTargets = unassignedTargets.stream().filter(e -> e.weaknesses.contains(friendly.attackType)).collect(Collectors.toList()); 
			} else if (unassignedTargets.stream().anyMatch(e -> !e.immunities.contains(friendly.attackType))) {
				potentialTargets = unassignedTargets.stream().filter(e -> !e.immunities.contains(friendly.attackType)).collect(Collectors.toList());
			} else {
				potentialTargets = new ArrayList<>();
			}

			Group target;
			if (!potentialTargets.isEmpty()) {
				Collections.sort(potentialTargets);
				target = potentialTargets.get(0);
				unassignedTargets.remove(target);

				targets.put(friendly, target);
			}
		}

		return targets;
	}
}