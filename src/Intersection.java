import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day03 of Advent of Code
 *
 * @author Evan Foley
 * @version 03 Dec 2018
 */
public class Intersection extends AdventMaster {
    public Intersection(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Runs the program
     */
    public void run() {
        // Fabric visualization
        int[][] fabric = new int[1000][1000];
        // Parse the claims from the input into the map
        int totalArea = parseClaims(input, fabric);
        System.out.printf("There are %d square inches of fabric within two or more claims%n", totalArea);
        System.out.println("The claim that does not intersect any other claim is " + findLoneClaim(input, fabric));
    }

    /**
     * Parses claims and their rectangles
     *
     * @param input The raw input
     * @param fabric The fabric area
     * @return The total area of intersecting rectangles
     */
    private int parseClaims(String[] input, int[][] fabric) {
        int totalArea = 0;
        // Process the input
        for(String str : input) {
            // Only digits are important
            Pattern pattern = Pattern.compile("[\\d]+");
            Matcher matcher = pattern.matcher(str);
            StringBuilder build = new StringBuilder();
            while(matcher.find()) {
                build.append(matcher.group() + " ");
            }
            String[] res = build.toString().split("\\s+");
            // Pull out the rectangle dimensions for this claim
            int x = Integer.parseInt(res[1]);
            int y = Integer.parseInt(res[2]);
            int width = Integer.parseInt(res[3]);
            int height = Integer.parseInt(res[4]);
            // Mark all the square inches that this claim uses
            for(int i = x; i < x + width; i++) {
                for(int j = y; j < y + height; j++) {
                    fabric[j][i] += 1;
                }
            }
        }
        // For all square inches that are used more than once, increase the total area by 1
        for(int i = 0; i < fabric.length; i++) {
            for(int j = 0; j < fabric[i].length; j++) {
                if(fabric[j][i] > 1) {
                    totalArea++;
                }
            }
        }
        return totalArea;
    }

    /**
     * Finds the claim that is not intersected by any other claim
     *
     * @param input The raw input
     * @param fabric The fabric area
     * @return The claim that has no intersections
     */
    private int findLoneClaim(String[] input, int[][] fabric) {
        int soleClaim = 0;
        // Go through each claim to determine which one doesn't overlap
        for(String str : input) {
            // Default to the sole claim
            boolean isSoleClaim = true;
            Pattern pattern = Pattern.compile("[\\d]+");
            Matcher matcher = pattern.matcher(str);
            StringBuilder build = new StringBuilder();
            while(matcher.find()) {
                build.append(matcher.group() + " ");
            }
            String[] res = build.toString().split("\\s+");
            // Pull out the claim id in case sole claim is found
            int claim = Integer.parseInt(res[0]);
            int x = Integer.parseInt(res[1]);
            int y = Integer.parseInt(res[2]);
            int width = Integer.parseInt(res[3]);
            int height = Integer.parseInt(res[4]);
            for(int i = x; i < x + width; i++) {
                for(int j = y; j < y + height; j++) {
                    // The claim overlaps if any of its area is greater than 1
                    if(fabric[j][i] > 1 ) {
                        isSoleClaim = false;
                        break;
                    }
                }
                // If the bid for sole claim is already over for this claim, break
                if(!isSoleClaim) {
                    break;
                }
            }
            if(isSoleClaim) {
                soleClaim = claim;
            }
        }
        return soleClaim;
    }
}
