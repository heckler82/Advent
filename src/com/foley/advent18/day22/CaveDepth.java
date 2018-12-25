package com.foley.advent18.day22;

import com.foley.advent18.AdventMaster;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day22 of Advent of Code
 *
 * @author Evan Foley
 * @version 22 Dec 2018
 */
public class CaveDepth extends AdventMaster {
    int risk = 0;
    int targetX = 0;
    int targetY = 0;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public CaveDepth(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    protected void task() {
        int[] data = parseInput(input);
        int[][] cave = mapCave(data);
        System.out.printf("The risk level for the cave is %d%n", risk);
        System.out.printf("The shortest path from the origin to the destination takes %d minutes %n", findShortestPathToTarget(cave, data[1], data[2]));
    }

    /**
     * Finds the shortest path to the target location
     *
     * @param cave The cave
     * @param tx The target x coordinate
     * @param ty The target y coordinate
     * @return The cost to get to the goal location
     */
    private int findShortestPathToTarget(int[][] cave, int tx, int ty) {
        // Build pathfinding graph
        Node[][] g = new Node[cave.length][cave[0].length];
        for(int y = 0; y < g.length; y++) {
            for(int x = 0; x < g[y].length; x++) {
                Node n = new Node(new Point(x, y), cave[0].length, cave.length);
                g[y][x] = n;
            }
        }

        // Tools
        int NEITHER = 1;
        int TORCH = 2;
        int CLIMB = 4;
        // Regions
        int ROCKY = TORCH | CLIMB;
        int WET = NEITHER | CLIMB;
        int NARROW = NEITHER | TORCH;

        Map<Integer, Integer> allowed = new HashMap<>();
        allowed.put(0, ROCKY);
        allowed.put(1, WET);
        allowed.put(2, NARROW);

        Map<CaveNode, Integer> scores = new HashMap<>();
        Queue<CaveNode> q = new PriorityQueue<>();
        Node start = g[0][0];
        CaveNode current = new CaveNode(start, 0, TORCH, ROCKY);
        CaveNode target = new CaveNode(g[ty][tx], 0, TORCH, cave[ty][tx]);
        q.offer(current);

        while(!q.isEmpty()) {
            current = q.poll();

            // Continue if this is not a better path
            if(scores.containsKey(current) && scores.get(current) <= current.score) {
                continue;
            }
            // Add node to the queue
            scores.put(current, current.score);

            // Early exit condition
            if(current.equals(target)) {
                // This is the found path
                return scores.get(current);
            }

            // Queue up for the other valid tool
            int validTools = allowed.get(cave[current.n.p.y][current.n.p.x]);
            int newTool = current.tool ^ validTools;
            q.offer(new CaveNode(current.n, current.score + 7, newTool, current.type));

            // Get the neighbors
            for(Point p : current.n.neighbors) {
                if(p != null) {
                    Node neighbor = g[p.y][p.x];
                    if((allowed.get(cave[p.y][p.x]) & current.tool) != 0) {
                        q.offer(new CaveNode(neighbor, current.score + 1, current.tool, cave[neighbor.p.y][neighbor.p.x]));
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Maps the cave from the input
     *
     * @param data The input
     * @return The cave
     */
    private int[][] mapCave(int[] data) {
        targetX = data[1];
        targetY = data[2];
        int[][] cave = new int[data[0]][10 * data[1]];
        //int[][] cave = new int[16][16];
        int[][] erosionMap = new int[cave.length][cave[0].length];

        // Calc erosion level from geologic index
        for(int y = 0; y < cave.length; y++) {
            for(int x = 0; x < cave[y].length; x++) {
                int geologicIndex = 0;
                if((x == 0 && y == 0) || (y == data[2] && x == data[1])) {
                    geologicIndex = 0;
                } else {
                    if(y == 0) {
                        geologicIndex = x * 16807;
                    } else {
                        if(x == 0) {
                            geologicIndex = y * 48271;
                        } else {
                            geologicIndex = cave[y][x - 1] * cave[y - 1][x];
                        }
                    }
                }
                cave[y][x] = (geologicIndex + data[0]) % 20183;
                erosionMap[y][x] = cave[y][x] % 3;
                if(y <= data[2] && x <= data[1]) {
                    risk += erosionMap[y][x];
                }
            }
        }

        return erosionMap;
    }

    /**
     * Parses the numerical data out of the given input
     *
     * @param input The input
     * @return The numerical input data
     */
    private int[] parseInput(String[] input) {
        int[] data = new int[3];

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input[0]);
        if(matcher.find()) {
            data[0] = Integer.parseInt(matcher.group());
        }

        matcher = pattern.matcher(input[1]);
        int index = 1;
        while(matcher.find()) {
            data[index] = Integer.parseInt(matcher.group());
            index++;
        }

        return data;
    }

    private class Node {
        Point p;
        Point[] neighbors;
        Node prev;

        public Node(Point p, int maxX, int maxY) {
            this.p = p;
            neighbors = new Point[4];
            // Calc neighbor locations
            int index = 0;
            // NORTHERN NEIGHBOR
            if(p.y - 1 > -1) {
                neighbors[index] = new Point(p.x, p.y - 1);
                index++;
            }
            // SOUTHERN NEIGHBOR
            if(p.y + 1 < maxY) {
                neighbors[index] = new Point(p.x, p.y + 1);
                index++;
            }
            // EASTERN NEIGHBOR
            if(p.x + 1 < maxX) {
                neighbors[index] = new Point(p.x + 1, p.y);
                index++;
            }
            // WESTERN NEIGHBOR
            if(p.x - 1 > -1) {
                neighbors[index] = new Point(p.x - 1, p.y);
            }
        }
    }

    private class CaveNode implements Comparable<CaveNode> {
        Node n;
        int score;
        int tool;
        int type;

        public CaveNode(Node n, int score, int tool, int type) {
            this.n = n;
            this.score = score;
            this.tool = tool;
            this.type = type;
        }

        public int compareTo(CaveNode c) {
            return Integer.compare(score, c.score);
        }

        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }
            if(this == o) {
                return true;
            }
            if(!(o instanceof CaveNode)) {
                return false;
            }
            CaveNode c = (CaveNode)o;
            if(n.p.x == c.n.p.x && n.p.y == c.n.p.y) {
                return tool == c.tool;
            }
            return false;
        }

        public int hashCode() {
            int result = n.p.hashCode();
            result = 31 * result + (tool);
            return result;
        }
    }
}
