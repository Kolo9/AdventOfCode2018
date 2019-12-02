package com.kolo.adventofcode.y2018;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.kolo.adventofcode.y2018.Puzzle20.Point;

public class Djikstras {
private Graph<Point> graph;
private Set<Point> settledNodes;
private Set<Point> unSettledNodes;
private Map<Point, Point> predecessors;
public Map<Point, Integer> distance;

public Djikstras(Graph<Point> graph) {
    // create a copy of the array so that we can operate on this array
    this.graph = graph;
}

public void execute(Point source) {
    settledNodes = new HashSet<Point>();
    unSettledNodes = new HashSet<Point>();
    distance = new HashMap<Point, Integer>();
    predecessors = new HashMap<Point, Point>();
    distance.put(source, 0);
    unSettledNodes.add(source);
    while (unSettledNodes.size() > 0) {
        Point node = getMinimum(unSettledNodes);
        settledNodes.add(node);
        unSettledNodes.remove(node);
        findMinimalDistances(node);
    }
}

private void findMinimalDistances(Point node) {
    List<Point> adjacentNodes = getNeighbors(node);
    for (Point target : adjacentNodes) {
        if (getShortestDistance(target) > getShortestDistance(node)
                + getDistance(node, target)) {
            distance.put(target, getShortestDistance(node)
                    + getDistance(node, target));
            predecessors.put(target, node);
            unSettledNodes.add(target);
        }
    }

}

private int getDistance(Point node, Point target) {
	return 1;
//    for (EndpointPair<Point> edge : edges) {
//        if (edge.nodeU().equals(node)
//                && edge.nodeV().equals(target)) {
//            return 1;
//        }
//    }
//    throw new RuntimeException("Should not happen");
}

private List<Point> getNeighbors(Point node) {
    List<Point> neighbors = new ArrayList<Point>();
    for (Point p : graph.adjacentNodes(node)) {
        if (!isSettled(p)) {
            neighbors.add(p);
        }
    }
    return neighbors;
}

private Point getMinimum(Set<Point> vertexes) {
    Point minimum = null;
    for (Point vertex : vertexes) {
        if (minimum == null) {
            minimum = vertex;
        } else {
            if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                minimum = vertex;
            }
        }
    }
    return minimum;
}

private boolean isSettled(Point vertex) {
    return settledNodes.contains(vertex);
}

private int getShortestDistance(Point destination) {
    Integer d = distance.get(destination);
    if (d == null) {
        return Integer.MAX_VALUE;
    } else {
        return d;
    }
}

/*
 * This method returns the path from the source to the selected target and
 * NULL if no path exists
 */
public LinkedList<Point> getPath(Point target) {
    LinkedList<Point> path = new LinkedList<Point>();
    Point step = target;
    // check if a path exists
    if (predecessors.get(step) == null) {
        return null;
    }
    path.add(step);
    while (predecessors.get(step) != null) {
        step = predecessors.get(step);
        path.add(step);
    }
    // Put it into the correct order
    Collections.reverse(path);
    return path;
}
	
}