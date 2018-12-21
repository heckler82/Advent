package com.foley.advent18.day17;

import com.foley.advent18.AdventMaster;
import java.awt.Point;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day17 of Advent of Code
 *
 * @author Evan Foley
 * @version 18 Dec 2018
 */
public class Reservoir extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Reservoir(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    protected void task() {
        char[][] scan = scanGround(input);
        findWater(scan);
    }

    /**
     * Finds all spaces that water can exist in the map
     *
     * @param map The map
     */
    private void findWater(char[][] map) {
        // Find the reservoir
        int resX = 0;
        for(int i = 0; i < map[0].length; i++) {
            if(map[0][i] == '+') {
                resX = i;
            }
        }

        releaseWater(map, resX, 1);

        // Calculate Water Spots
        int wetSpots = 0;
        int waterSpots = 0;
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                if(map[i][j] == '|') {
                    wetSpots++;
                }
                if(map[i][j] == '~') {
                    wetSpots++;
                    waterSpots++;
                }
            }
        }

        System.out.printf("The number of tiles that water can reach is %d%n", wetSpots);
        System.out.printf("The number of tiles that water remains in after draining %d%n", waterSpots);
    }

    private void releaseWater(char[][] map, int resX, int resY) {
        Queue<Node> q = new PriorityQueue<>(11, new NodeComparator());
        q.offer(new Node(new Point(resX, resY)));

        while(!q.isEmpty()) {
            Node n = q.poll();
            Point p = n.p;
            if(p.y + 1 < map.length) {
                char c = map[p.y + 1][p.x];
                switch(c) {
                    case '.':
                        Node n2 = new Node(new Point(p.x, p.y + 1));
                        q.offer(n2);
                        q.offer(n);
                        break;
                    case '#':
                    case '~':
                        // Find the left limit character
                        char leftLimit;
                        int startX = p.x;
                        int x = p.x;
                        int direction = -1;
                        while(x > 0) {
                            startX = x;
                            x += direction;
                            // Early exit condition
                            if(map[p.y][x] == '#' || map[p.y + 1][x] == '|' || map[p.y + 1][x] == '.') {
                                break;
                            }
                        }
                        leftLimit = map[p.y][x];

                        // Find the right limit character
                        char rightLimit;
                        int endX = p.x;
                        x = p.x;
                        direction = 1;
                        while(x < map[p.y].length - 1) {
                            endX = x;
                            x += direction;
                            // Early exit condition
                            if(map[p.y][x] == '#' || map[p.y + 1][x] == '|' || map[p.y + 1][x] == '.') {
                                break;
                            }
                        }
                        rightLimit = map[p.y][x];

                        // Determine fill character
                        char fillCharacter = '|';
                        if(leftLimit == '#' && rightLimit == '#') {
                            fillCharacter = '~';
                        } else {
                            // If one of the characters isn't a wall, find the open end and add to the queue
                            if(startX > 0 && leftLimit != '#') {
                                q.offer(new Node(new Point(startX - 1, p.y)));
                            }
                            if(endX < map[p.y].length - 1 && rightLimit != '#') {
                                q.offer(new Node(new Point(endX + 1, p.y)));
                            }
                        }
                        // Fill in the proper character on the row
                        for(int posX = startX; posX <= endX; posX++) {
                            map[p.y][posX] = fillCharacter;
                        }
                        break;
                    case '|':
                        map[p.y][p.x] = '|';
                }
            } else {
                // Reached the bottom
                map[p.y][p.x] = '|';
            }
        }
    }

    /**
     * Prints out the map to the console
     *
     * @param map The map
     */
    private void printMap(char[][] map) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Creates the map with all the clay veins
     *
     * @param input The input
     * @return The map
     */
    private char[][] scanGround(String[] input) {
        char[][] map;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        List<Point[]> pairs = new ArrayList<>();
        Point[] pair;
        Pattern pattern = Pattern.compile("\\d+");
        for(String str : input) {
            // Pull all coordinate values from line
            Matcher matcher = pattern.matcher(str);
            int[] values = new int[3];
            int index = -1;
            while(matcher.find()) {
                values[++index] = Integer.parseInt(matcher.group());
            }
            // Create a new pair for the points and assign
            pair = new Point[2];
            if(str.charAt(0) == 'x') {
                pair[0] = new Point(values[0], values[1]);
                pair[1] = new Point(values[0], values[2]);
                maxX = Math.max(maxX, values[0]);
                maxY = Math.max(maxY, Math.max(values[1], values[2]));
                minX = Math.min(minX, values[0]);
                minY = Math.min(minY, Math.min(values[1], values[2]));
            } else {
                pair[0] = new Point(values[1], values[0]);
                pair[1] = new Point(values[2], values[0]);
                maxY = Math.max(maxY, values[0]);
                maxX = Math.max(maxX, Math.max(values[1], values[2]));
                minY = Math.min(minY, values[0]);
                minX = Math.min(minX, Math.min(values[1], values[2]));
            }
            // Add to pairs list
            pairs.add(pair);
        }

        // Set up the map with default to sand
        map = new char[(maxY - minY) + 2][(maxX - minX) + 2];
        for(char[] strip : map) {
            Arrays.fill(strip, '.');
        }
        // Place the spring
        map[0][500 - minX] = '+';
        // Mark all clay areas
        for(Point[] points : pairs) {
            int startX = points[0].x - minX;
            int endX = points[1].x - minX;
            int startY = points[0].y - minY + 1;
            int endY = points[1].y - minY + 1;
            // Fill clay here
            for(int y = startY; y <= endY; y++) {
                for(int x = startX; x <= endX; x++) {
                    map[y][x] = '#';
                }
            }
        }
        return map;
    }

    /**
     * Helper class
     */
    private class Node {
        Point p;
        boolean visited;

        public Node(Point p) {
            this.p = p;
            visited = false;
        }
    }

    /**
     * Sorts Points in read order
     */
    private class NodeComparator implements Comparator<Node> {
        public int compare(Node n, Node n2) {
            int comp = Integer.compare(-n.p.y, -n2.p.y);
            if(comp < 0) {
                return comp;
            }
            return comp;
        }
    }
}
