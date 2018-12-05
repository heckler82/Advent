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
     * Runs the program
     */
    public void run() {
        System.out.printf("The checksum for the box IDs is %d%n", computeChecksum(input));
        System.out.printf("The common characters between the matching boxes are: %s%n", commonBoxID(input, 1));

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
        int twoCount = 0;
        int threeCount = 0;
        // Iterate through input array
        for(String str : input) {
            // Holds the characters and their frequency of occurrence
            HashMap<Character, Integer> freqs = new HashMap<>();
            // Iterate through characters in the current ID
            for (char c : str.toCharArray()) {
                // Put the current character with either 1 or increment the current value
                freqs.put(c, freqs.getOrDefault(c, 0) + 1);
            }
            // Determine if the current ID is a 2 hit or a 3 hit
            boolean twoFound = false;
            boolean threeFound = false;
            // Iterate through the keys for the map
            for (char c : freqs.keySet()) {
                int val = freqs.get(c);
                // Determine hit status
                if (val == 2 && !twoFound) {
                    twoCount++;
                    twoFound = true;
                } else {
                    if (val == 3 && !threeFound) {
                        threeCount++;
                        threeFound = true;
                    }
                }
            }
        }

        // Return the final checksum
        return twoCount * threeCount;
    }

    /**
     * Finds the common box IDs. This is accomplished by finding the two IDs that differ by only a single character. The
     * common ID is composed of only the characters that occur in both IDs
     *
     * @param input The list of IDs
     * @param differencesAllowed The number of differences allowed between IDs
     * @return The characters that occur in both of the common IDs
     */
    private String commonBoxID(String[] input, int differencesAllowed) {
        // Sort input for easier comparisons (common IDs will be neighbors)
        Arrays.sort(input);
        StringBuilder res = new StringBuilder();
        // Test every String with its neighbor to the right
        for(int i = 0; i < input.length - 1; i++) {
            // Refresh the result if previous iteration found not answer
            res.delete(0, res.length());
            // Reset difference counter and get current ID and its right neighbor
            int remainingDifference = differencesAllowed;
            String firstID = input[i];
            String secondID = input[i + 1];
            // Compare characters
            for(int j = 0; j < firstID.length(); j++) {
                // Reduce reamining differences for a miss, terminate iteration if no more differences are allowed
                if(firstID.charAt(j) != secondID.charAt(j)) {
                    remainingDifference--;
                    if(remainingDifference < 0) {
                        break;
                    }
                }
                else {
                    // Add character to the result for a hit
                    res.append(firstID.charAt(j));
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
