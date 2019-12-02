package com.kolo.adventofcode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Puzzle8 {
	private static final class Node {
		int children;
		int metadata;
		int totalChildren;
		List<Integer> ms = new ArrayList<>();
		int score = 0;

		Node(int children, int metadata) {
			this.totalChildren = this.children = children;
			this.metadata = metadata;
		}

		@Override
		public int hashCode() {
			return Objects.hash(children, metadata);
		}
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Node)) {
				return false;
			}
			Node otherPair = (Node) other;
			return otherPair.children == children && otherPair.metadata == metadata;
		}
	
		@Override
		public String toString() {
			return String.format("(%d, %s, %d)", totalChildren, ms, score);
		}
	}

	public static void main(String[] args) throws Exception {
		List<String> tmp = Files.readAllLines(Paths.get("puzzle8"));
		String[] ss = tmp.get(0).split(" ");
		System.out.println(Arrays.toString(ss));

		List<Node> nodes = new ArrayList<>();
		List<Node> allNodes = new ArrayList<>();
		
		int ans = 0;
		for (int i = 0; i < ss.length;) {
			System.out.println("nodes: " + nodes);
			if (nodes.isEmpty()) {
				nodes.add(new Node(Integer.parseInt(ss[i++]), Integer.parseInt(ss[i++])));
				allNodes.add(nodes.get(nodes.size() - 1));
			} else {
				Node n = nodes.get(nodes.size() - 1);
				System.out.println("latest node is " + n);
				if (n.children > 0) {
					nodes.add(new Node(Integer.parseInt(ss[i++]), Integer.parseInt(ss[i++])));
					System.out.println("Added new node " + nodes.get(nodes.size() - 1));
					allNodes.add(nodes.get(nodes.size() - 1));
					n.children--;
				} else if (n.metadata > 0) {
					ans += Integer.parseInt(ss[i]);
					n.ms.add(Integer.parseInt(ss[i++]));
					n.metadata--;
				}
				if (n.children == 0 && n.metadata == 0) {
					nodes.remove(nodes.size() - 1);
				}
			}
		}
		System.out.println(allNodes);
		for (int i = allNodes.size() - 1; i >= 0; i--) { 
			Node n = allNodes.get(i);
			for (int m : n.ms) {
				if (n.totalChildren == 0) {
					n.score += m;
				} else if (m > 0 && m <= n.totalChildren) {
					int curChild = 1;
					int curIdx = i + 1;
					int childrenLeft = 1;
					while (curChild != m) {
						childrenLeft += allNodes.get(curIdx).totalChildren;
						childrenLeft--;
						curIdx++;
						if (childrenLeft == 0) {
							curChild++;
							childrenLeft = 1;
						}
					}
					n.score += allNodes.get(curIdx).score;
				}
			}
		}
		System.out.println(allNodes);
		System.out.println(allNodes.get(0).score);
		

//	    BufferedWriter writer = new BufferedWriter(new FileWriter("out6"));
//	     
//		for (Point[] row : debugGrid) {
//			for (Point point : row) {
//				writer.write(((point == null) ? '.' : point.id) + ", ");
//			}
//			writer.write("\n");
//		}
//
//		writer.close();

	}

}