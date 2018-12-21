package com.foley.advent18.day10;

import com.foley.advent18.AdventMaster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day10 of Advent of Code
 *
 * @author Evan Foley
 * @version 14 Dec 2018
 */
public class HiddenMessage extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public HiddenMessage(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        // Get the points from the input
        List<Point> points = getPoints(input);
        // Find the message
        int second = 0;
        while(true) {
            // Increase the time step
            second++;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            // Move all points and find min/max values
            for(Point p : points) {
                p.move();
                if(p.x < minX) {
                    minX = p.x;
                } else {
                    if(p.x > maxX) {
                        maxX = p.x;
                    }
                }
                if(p.y < minY) {
                    minY = p.y;
                } else {
                    if(p.y > maxY) {
                        maxY = p.y;
                    }
                }
            }

            // Calculate dimensions
            int extra = 1;
            int width = extra + maxX - minX;
            int height = extra + maxY - minY;
            // If height is letter height, then print message
            if(height <= extra + 9) {
                drawPlot(points, width, height, minX, minY);
                System.out.printf("Message took %d seconds to appear%n", second);
                return;
            }
        }
    }

    /**
     * Draws the current plot
     *
     * @param points The list of point
     * @param width The width
     * @param height The height
     * @param minX The minimum x value
     * @param minY The minimum y value
     */
    private void drawPlot(List<Point> points, int width, int height, int minX, int minY) {
        int offsetX = -minX;
        int offsetY = -minY;
        // Initially fill array
        char[][] grid = new char[height][width];
        for(int i = 0; i < height; i++) {
            Arrays.fill(grid[i], ' ');
        }
        // Plot points
        for(Point p : points) {
            grid[p.y + offsetY][p.x + offsetX] = '#';
        }
        // Print
        for(char[] c : grid) {
            System.out.println(c);
        }
    }

    /**
     * Converst the input into point data
     *
     * @param input The input
     * @return The list of points
     */
    private List<Point> getPoints(String[] input) {
        ArrayList<Point> points = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?[\\d]+");
        for(String str : input) {
            Matcher matcher = pattern.matcher(str);
            matcher.find();
            int x = Integer.parseInt(matcher.group());
            matcher.find();
            int y = Integer.parseInt(matcher.group());
            matcher.find();
            int velX = Integer.parseInt(matcher.group());
            matcher.find();
            int velY = Integer.parseInt(matcher.group());
            points.add(new Point(x, y, velX, velY));
        }
        return points;
    }

    /**
     * Helper class
     */
    private class Point {
        int x;
        int y;
        int velX;
        int velY;

        public Point(int x, int y, int velX, int velY) {
            this.x = x;
            this.y = y;
            this.velX = velX;
            this.velY = velY;
        }

        public void move() {
            x += velX;
            y += velY;
        }
    }
}
