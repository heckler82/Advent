package com.foley.advent18.day18;

import com.foley.advent18.AdventMaster;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day18 of Advent of Code
 *
 * @author Evan Foley
 * @version 18 Dec 2018
 */
public class GameOfLumber extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public GameOfLumber(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        LumberTile[][] tiles = getTiles(input);
        System.out.printf("The total resource value after 10 minutes is %d%n", getResourceValueAfterTime(tiles, 10));
        //System.out.printf("The total resource value after 1000000000 minutes is %d%n", getResourceValueAfterTime(tiles, 1000000000));
    }

    /**
     * Gets the value of the resources after the specified time
     *
     * @param tiles The tiles
     * @param time The amount of time to simulate
     * @return The total resource value available after 'time' has passed
     */
    private int getResourceValueAfterTime(LumberTile[][] tiles, int time) {
        // Perform 'time' iterations
        int numWoods = 0;
        int numYards = 0;
        Set<Integer> s = new LinkedHashSet<>();
        for(int t = 0; t < time; t++) {
            // Reset counters
            numWoods = 0;
            numYards = 0;
            // Update previous landscape
            for(int y = 0; y < tiles.length; y++) {
                for(int x = 0; x < tiles[y].length; x++) {
                    LumberTile tile = tiles[y][x];
                    tile.previous = tile.current;
                }
            }
            for(int y = 0; y < tiles.length; y++) {
                for(int x = 0; x < tiles[y].length; x++) {
                    // Get working tile
                    LumberTile currentTile = tiles[y][x];
                    currentTile.process(tiles);
                    if(currentTile.current % '|' == 0) {
                        numWoods++;
                    } else {
                        if(currentTile.current % '#' == 0) {
                            numYards++;
                        }
                    }
                }
            }
        }
        // Get the resource value
        return numWoods * numYards;
    }

    private void printMap(LumberTile[][] tiles) {
        // Print the map
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {
                System.out.print(tiles[i][j].current + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Gets the tiles used for the game
     *
     * @param input The input
     * @return The tiles
     */
    private LumberTile[][] getTiles(String[] input) {
        LumberTile[][] tiles = new LumberTile[input.length][];
        for(int y = 0; y < input.length; y++) {
            String line = input[y];
            LumberTile[] strip = new LumberTile[line.length()];
            for(int x = 0; x < line.length(); x++) {
                LumberTile tile = new LumberTile(input.length, line.length(), x, y, line.charAt(x));
                strip[x] = tile;
            }
            tiles[y] = strip;
        }
        return tiles;
    }

    /**
     * Helper class
     */
    private class LumberTile {
        int x;
        int y;
        char previous;
        char current;
        List<Point> neighbors = new ArrayList<>();

        public LumberTile(int yLimit, int xLimit, int x, int y, char current) {
            this.x = x;
            this.y = y;
            this.current = current;
            previous = current;
            // Get neighbor coordinates
            for(int py = y - 1; py < y + 2; py++) {
                // Ensure y coordinate stays in bounds
                if(py > -1 && py < yLimit) {
                    for(int px = x - 1; px < x + 2; px++) {
                        // Do not count this node as a neighbor
                        if(px == x && py == y) {
                            continue;
                        }
                        // Ensure x coordinate stays in bounds
                        if(px > -1 && px < xLimit) {
                            neighbors.add(new Point(px, py));
                        }
                    }
                }
            }
        }

        public void process(LumberTile[][] tiles) {
            // Get the neighbor values for this current tile
            StringBuilder adjacent = new StringBuilder();
            for(Point p : neighbors) {
                adjacent.append(tiles[p.y][p.x].previous);
            }
            // Find out which rules to apply
            Pattern pattern;
            Matcher matcher;
            switch(current) {
                case '.':
                    int woodNeighbors = 0;
                    pattern = Pattern.compile("\\|");
                    matcher = pattern.matcher(adjacent);
                    while(matcher.find()) {
                        woodNeighbors++;
                    }
                    if(woodNeighbors > 2) {
                        current = '|';
                    }
                    break;
                case '|':
                    int yardNeighbors = 0;
                    pattern = Pattern.compile("#");
                    matcher = pattern.matcher(adjacent);
                    while(matcher.find()) {
                        yardNeighbors++;
                    }
                    if(yardNeighbors > 2) {
                        current = '#';
                    }
                    break;
                case '#':
                    pattern = Pattern.compile("#");
                    Pattern pattern2 = Pattern.compile("\\|");
                    matcher = pattern.matcher(adjacent);
                    Matcher matcher2 = pattern2.matcher(adjacent);
                    if(!(matcher.find() && matcher2.find())) {
                        current = '.';
                    }
                    break;
            }
        }
    }
}
