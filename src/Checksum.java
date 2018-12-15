import java.util.Arrays;
import java.util.HashMap;

/**
 * Solution for Day02 of Advent of Code
 *
 * @author Evan Foley
 * @version 02 Dec 2018
 */
public class Checksum extends AdventMaster{
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Checksum(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        System.out.printf("The checksum for the box IDs is %d%n", computeChecksum(input));
        System.out.printf("The common characters between the matching boxes are: %s%n", commonBoxID(input));

    }

    /**
     * Computes the checksum according to the given rules for Day02. Only IDs that have characters that occur two or
     * three times provide a hit (an ID can count as a hit for both 2 or 3, but cannot double count. For example, ababa
     * counts as both a hit for 2 and a hit for 3; ababab counts as a single hit for 3). The checksum is computed by
     * taking the number of 2 hits and multiplying it by the number of 3 hits.
     *
     * @param input The list of IDs
     * @return The checksum for the list of IDs
     */
    private int computeChecksum(String[] input) {
        int[] counts = new int[2];
        // Iterate through input array
        for(String str : input) {
            int[] chars = new int[26];
            // Determine frequencies for each character in the String
            for(char c : str.toCharArray()) {
                chars[c - 'a']++;
            }
            boolean twoFound = false;
            boolean threeFound = false;
            // Find the hits for two and three
            for(int i : chars) {
                if(i == 2 && !twoFound) {
                    counts[0]++;
                    twoFound = true;
                } else {
                    if(i == 3 && !threeFound) {
                        counts[1]++;
                        threeFound = true;
                    }
                }
                // If exhausted options already, go to next word in input
                if(twoFound && threeFound) break;
            }
        }

        // Return the final checksum
        return counts[0] * counts[1];
    }

    /**
     * Finds the common box IDs. This is accomplished by finding the two IDs that differ by only a single character. The
     * common ID is composed of only the characters that occur in both IDs
     *
     * @param input The list of IDs
     * @return The characters that occur in both of the common IDs
     */
    private String commonBoxID(String[] input) {
        // Sort input for easier comparisons (common IDs will be neighbors)
        Arrays.sort(input);
        StringBuilder res = new StringBuilder();
        // Test every String with its neighbor to the right
        for(int i = 0; i < input.length - 1; i++) {
            // Reset difference counter and get current ID and its right neighbor
            int remainingDifference = 1;
            char[] firstID = input[i].toCharArray();
            char[] secondID = input[i + 1].toCharArray();
            // Compare characters
            for(int j = 0; j < firstID.length; j++) {
                // Reduce remaining differences for a miss, terminate iteration if no more differences are allowed
                if(firstID[j] != secondID[j]) {
                    remainingDifference--;
                    if(remainingDifference < 0) {
                        res.setLength(0);
                        break;
                    }
                }
                else {
                    // Add character to the result for a hit
                    res.append(firstID[j]);
                }
            }
            // Iteration complete. If remaining differences is zero, common IDs are found
            if(remainingDifference == 0) {
                return res.toString();
            }
        }
        // No answer
        return "No common IDs found";
    }
}
