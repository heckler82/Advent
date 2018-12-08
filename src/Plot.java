import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Solution for Day06 of Advent of Code
 *
 * @author Evan Foley
 * @version 07 Dec 2018
 */
public class Plot extends AdventMaster {
    private int maxX;
    private int maxY;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Plot(String fileName) {
        super(fileName);
        maxX = 0;
        maxY = 0;
    }

    @Override
    /**
     * Runs the program
     */
    public void run() {
        Point[] points = getPoints();
        System.out.printf("The largest non-infinite area is %d%n", findLargestArea(points));
        System.out.printf("The largest area with limited Manhattan Distance is %d%n", findLargestRegionLimit(points, 10000));
    }

    /**
     * Finds the largest non-infinite area
     *
     * @param points The list of points
     * @return The largest non-infinite area
     */
    private int findLargestArea(Point[] points) {
        // Maintain the area for the points
        int[] scores = new int[points.length];
        int[][] graph = new int[maxY][maxX];

        // Loop through every point in the graph
        for(int i = 0; i < maxY; i++) {
            for(int j = 0; j < maxX; j++) {
                // Min distance and index of closest point
                int minManhattan = Integer.MAX_VALUE;
                int minPoint = -1;
                // Check every point to see which is closest
                for(int k = 0; k < points.length; k++) {
                    Point p = points[k];
                    int currentManhattan = Math.abs(p.x - j) + Math.abs(p.y - i);
                    if(currentManhattan < minManhattan) {
                        minManhattan = currentManhattan;
                        minPoint = k;
                    } else {
                        // Ties cause -1 and doesn't count towards area
                        if(currentManhattan == minManhattan) {
                            minPoint= -1;
                        }
                    }
                }
                // Mark index of min point
                graph[i][j] = minPoint;
                if(minPoint >= 0) {
                    scores[minPoint]++;
                }
            }
        }
        // Strip infinite areas along top and bottom edges
        for(int x = 0; x < maxX; x++) {
            if(graph[0][x] >= 0) {
                scores[graph[0][x]] = 0;
            }
            if(graph[maxY - 1][x] >= 0) {
                scores[graph[maxY - 1][x]] = 0;
            }
        }
        // Strip infinite areas along left and right edges
        for(int y = 0; y < maxY; y++) {
            if(graph[y][0] >= 0) {
                scores[graph[y][0]] = 0;
            }
            if(graph[y][maxX - 1] >= 0) {
                scores[graph[y][maxX - 1]] = 0;
            }
        }
        // Get largest remaining area
        int largestArea = 0;
        for(int i = 0; i < scores.length; i++) {
            if(scores[i] > largestArea) {
                largestArea = scores[i];
            }
        }
        return largestArea;
    }

    /**
     * Finds the largest area with a limit on total Manhattan Distance
     *
     * @param points The list of points
     * @param limit The total Manhattan Distance limit
     * @return The largest non-infinite area
     */
    private int findLargestRegionLimit(Point[] points, int limit) {
        // Safe Area count
        int safeArea = 0;

        // Loop through every point in the graph
        for(int i = 0; i < maxY; i++) {
            for(int j = 0; j < maxX; j++) {
                // Min distance and index of closest point
                int totalManhattan = 0;
                // Check every point to see which is closest
                for(int k = 0; k < points.length; k++) {
                    Point p = points[k];
                    totalManhattan += Math.abs(p.x - j) + Math.abs(p.y - i);
                }
                if(totalManhattan < 10000) {
                    safeArea++;
                }
            }
        }
        return safeArea;
    }

    /**
     * Gets the list of points from the input
     *
     * @return The list of points
     */
    private Point[] getPoints() {
        // Pull out the points from the input
        List<Point> list = new ArrayList<>();
        for(String str : input) {
            String[] coord = str.split(",\\s+");
            int x = Integer.parseInt(coord[0]);
            if(x > maxX) {
                maxX = x;
            }
            int y = Integer.parseInt(coord[1]);
            if(y > maxY) {
                maxY = y;
            }
            list.add(new Point(x, y));
        }
        return list.toArray(new Point[list.size()]);
    }
}
