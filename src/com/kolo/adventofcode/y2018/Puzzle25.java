package com.kolo.adventofcode.y2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.kolo.adventofcode.y2018.Puzzle20.Point3d;

public class Puzzle25 {	
	  
	private static class DisjointUnionSets 
	{ 
		Map<Point4d, Integer> ranks;
	    Map<Point4d, Point4d> parents;
	    List<Point4d> points;
	    int n; 
	  
	    // Constructor 
	    public DisjointUnionSets(List<Point4d> points) 
	    { 
	        ranks = new HashMap<>();
	        parents = new HashMap<>();
	        this.n = points.size(); 
	        this.points = points;
	        makeSet(); 
	    } 
	  
	    // Creates n sets with single item in each 
	    void makeSet() 
	    { 
	        for (int i=0; i<n; i++) 
	        { 
	            // Initially, all elements are in 
	            // their own set. 
	            parents.put(points.get(i), points.get(i));
	            ranks.put(points.get(i), 0);
	         } 
	    } 
	  
	    // Returns representative of x's set 
	    Point4d find(Point4d x) 
	    { 
	        // Finds the representative of the set 
	        // that x is an element of 
	        if (!parents.get(x).equals(x))
	        { 
	            // if x is not the parent of itself 
	            // Then x is not the representative of 
	            // his set, 
	            parents.put(x, find(parents.get(x))); 
	  
	            // so we recursively call Find on its parent 
	            // and move i's node directly under the 
	            // representative of this set 
	        } 
	  
	        return parents.get(x); 
	    } 
	  
	    // Unites the set that includes x and the set 
	    // that includes x 
	    void union(Point4d x, Point4d y) 
	    { 
	        // Find representatives of two sets 
	        Point4d xRoot = find(x), yRoot = find(y); 
	  
	        // Elements are in the same set, no need 
	        // to unite anything. 
	        if (xRoot.equals(yRoot)) 
	            return; 
	  
	         // If x's rank is less than y's rank 
	        if (ranks.get(xRoot) < ranks.get(yRoot)) 
	  
	            // Then move x under y  so that depth 
	            // of tree remains less 
	            parents.put(xRoot, yRoot); 
	  
	        // Else if y's rank is less than x's rank 
	        else if (ranks.get(yRoot) < ranks.get(xRoot)) 
	  
	            // Then move y under x so that depth of 
	            // tree remains less 
	            parents.put(yRoot, xRoot); 
	  
	        else // if ranks are the same 
	        { 
	            // Then move y under x (doesn't matter 
	            // which one goes where) 
	            parents.put(yRoot, xRoot); 
	  
	            // And increment the the result tree's 
	            // rank by 1 
	            ranks.put(xRoot, ranks.get(xRoot) + 1); 
	        } 
	    } 
	} 
	
	
	static class Point4d extends Point3d {
		 int zz;
		 
		 Point4d(int x, int y, int z, int zz) {
			 super(x, y, z);
			 
			 this.zz = zz;
		 }

		 @Override
			public int hashCode() {
				return Objects.hash(x, y, z, zz);
			}
			
			@Override
			public boolean equals(Object other) {
				if (!(other instanceof Point4d)) {
					return false;
				}
				Point4d otherPair = (Point4d) other;
				return otherPair.zz == zz && otherPair.x == x && otherPair.y == y && otherPair.z == z;
			}
		
			@Override
			public String toString() {
				return String.format("(%d, %d, %d, %d)", x, y, z, zz);
			}
	 }
	
	static int minX, minY, minZ = Integer.MAX_VALUE;
	static int maxX, maxY, maxZ = Integer.MIN_VALUE;
	public static void main(String[] args) throws Exception {
		List<String> ss = Files.readAllLines(Paths.get("puzzle25"));
		Pattern p = Pattern.compile("(-?\\d+),(-?\\d+),(-?\\d+),(-?\\d+)");
	
		List<Point4d> points = new ArrayList<>();
		for (String s : ss) {
			Matcher m = p.matcher(s);
			m.find();
			Point4d point = new Point4d(
					Integer.parseInt(m.group(1)),
					Integer.parseInt(m.group(2)),
					Integer.parseInt(m.group(3)),
					Integer.parseInt(m.group(4))
					);
			points.add(point);
		}
		System.out.println(points.size());
		
		DisjointUnionSets d = new DisjointUnionSets(points);
		for (Point4d p1 : points) {
			for (Point4d p2 : points) {
				if (p1.equals(p2)) continue;
				
				if (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z) + Math.abs(p1.zz - p2.zz) <= 3) {
					d.union(p1, p2);
				}
			}
		}
		// Final find to update parents.
		points.forEach(d::find);
		
		System.out.println(new HashSet<>(d.parents.values()).size());
		Multimap<Point4d, Point4d> z = HashMultimap.create();
		for (Entry<Point4d, Point4d> e : d.parents.entrySet()) {
			z.put(e.getValue(), e.getKey());
		}
		for (Point4d k1 : z.keySet()) {
			for (Point4d k2 : z.keySet()) {
				if (k1.equals(k2)) continue;
				for (Point4d p1 : z.get(k1)) {
					for (Point4d p2 : z.get(k2)) {
						if (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z) + Math.abs(p1.zz - p2.zz) <= 3) {
							System.err.println("SOMETHING IS WRONG. " + p1 + " " + p2);
						}
					}
				}
			}
		}
	}
}