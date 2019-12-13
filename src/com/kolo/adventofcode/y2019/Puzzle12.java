package com.kolo.adventofcode.y2019;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kolo.adventofcode.common.MoreMath;
import com.kolo.adventofcode.common.Point3D;

final class Puzzle12 {

    private static final class Moon {
        Point3D pos;
        Point3D velocity = new Point3D(0, 0, 0);

        Moon(int x, int y, int z) {
            this.pos = new Point3D(x, y, z);
        }

        int energy() {
            int potentialEnergy = (int) (Math.abs(pos.getX()) + Math.abs(pos.getY()) + Math.abs(pos.getZ()));
            int kineticEnergy = (int) (Math.abs(velocity.getX()) + Math.abs(velocity.getY()) + Math.abs(velocity.getZ()));
            return potentialEnergy * kineticEnergy;
        }

        @Override
        public String toString() {
            return String.format("%s--%s", pos, velocity);
        }
    }

    public static void main(String[] args) {
        List<Moon> moons = new ArrayList<>();
        moons.add(new Moon(16, -8, 13));
        moons.add(new Moon(4, 10, 10));
        moons.add(new Moon(17, -5, 6));
        moons.add(new Moon(13, -3, 0));

        for (int step = 0; step < 1000; step++) {
            step(moons);
        }

        int totalEnergy = moons.stream().mapToInt(Moon::energy).sum();
        
        System.out.println("Part 1: " + totalEnergy);

        moons = new ArrayList<>();
        moons.add(new Moon(16, -8, 13));
        moons.add(new Moon(4, 10, 10));
        moons.add(new Moon(17, -5, 6));
        moons.add(new Moon(13, -3, 0));
        int repeatX = 0;
        int repeatY = 0;
        int repeatZ = 0;
        Set<List<Integer>> seenX = new HashSet<>();
        Set<List<Integer>> seenY = new HashSet<>();
        Set<List<Integer>> seenZ = new HashSet<>();

        int steps = 0;
        while (repeatX == 0 || repeatY == 0 || repeatZ == 0) {
            List<Integer> moonsX = new ArrayList<>();
            List<Integer> moonsY = new ArrayList<>();
            List<Integer> moonsZ = new ArrayList<>();
            for (Moon m : moons) {
                moonsX.add((int) m.pos.getX());
                moonsX.add((int) m.velocity.getX());
                moonsY.add((int) m.pos.getY());
                moonsY.add((int) m.velocity.getY());
                moonsZ.add((int) m.pos.getZ());
                moonsZ.add((int) m.velocity.getZ());
            }

            if (repeatX == 0 && !seenX.add(moonsX)) {
                repeatX = steps;
            }
            if (repeatY == 0 && !seenY.add(moonsY)) {
                repeatY = steps;
            }
            if (repeatZ == 0 && !seenZ.add(moonsZ)) {
                repeatZ = steps;
            }

            step(moons);
            steps++;
        }

       
        System.out.println("Part 2: " + MoreMath.lcm(repeatX, repeatY, repeatZ));
    }

    private static void step(List<Moon> moons) {
        for (int i = 0; i < moons.size(); i++) {
            for (int j = i + 1; j < moons.size(); j++) {
                Moon m1 = moons.get(i);
                Moon m2 = moons.get(j);

                int x = m1.pos.getX() > m2.pos.getX() ? -1 : m1.pos.getX() < m2.pos.getX() ? 1 : 0;
                int y = m1.pos.getY() > m2.pos.getY() ? -1 : m1.pos.getY() < m2.pos.getY() ? 1 : 0;
                int z = m1.pos.getZ() > m2.pos.getZ() ? -1 : m1.pos.getZ() < m2.pos.getZ() ? 1 : 0;
                m1.velocity = m1.velocity.add(x, y, z);
                m2.velocity = m2.velocity.add(-x, -y, -z);
            }
        }
        for (Moon m : moons) {
            m.pos = m.pos.add(m.velocity);
        }
    }
}
