import java.util.HashSet;
import java.util.Set;

/**
 * Solution for Day01 of Advent of Code
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class ChronicleCalibration extends AdventMaster{
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public ChronicleCalibration(String fileName) {
        super(fileName);
    }

    /**
     * Runs the calculations
     */
    public void run() {
        int[] freqs = parseInput(input);

        // Print result of calculations
        System.out.println("Starting frequency is 0");
        System.out.printf("Final adjusted frequency is %d%n", findFinalAdjustedFrequency(freqs));
        System.out.printf("First duplicate frequency is %d%n", findFirstDuplicateFrequency(freqs));
    }

    /**
     * Converts raw input into numerical data for calculation
     *
     * @param input The raw input
     * @return Numerical form of the raw input
     */
    private int[] parseInput(String[] input) {
        int[] rawIn = new int[input.length];
        for(int i = 0; i < input.length; i++) {
            try {
                rawIn[i] = Integer.parseInt(input[i]);
            } catch(NumberFormatException e) {
                System.err.print("[WARNING] Could not parse the given String to an Integer");
            }
        }
        return rawIn;
    }

    /**
     * Given a list of frequency adjustments, find the final adjusted frequency
     *
     * @param adjustments The list of adjustments
     * @return The final adjusted frequency
     */
    private int findFinalAdjustedFrequency(int[] adjustments) {
        // Start at 0
        int freq = 0;
        for(int adj : adjustments) {
            freq += adj;
        }
        return freq;
    }

    /**
     * Given a list of frequency adjustments, continuously loop through and adjust the master frequency until the first repeat frequency is found
     *
     * @param adjustments The list of adjustments
     * @return The first repeat frequency
     */
    private int findFirstDuplicateFrequency(int[] adjustments) {
        // Start at 0
        int freq = 0;

        // Container for adjusted frequencies, will only hold unique values
        Set<Integer> uniqueFreqs = new HashSet<>();
        uniqueFreqs.add(freq);

        // Loop all provided adjustments until duplicate hit found
        boolean duplicateFound = false;
        int i = 0;
        while(!duplicateFound) {
            // Calculate adjusted frequency
            freq += adjustments[i];
            // Attempt to add freq, stop search if already in pool
            if(uniqueFreqs.add(freq)) {
                // Clamp i between 0 and args.length - 1
                i = ++i % adjustments.length;
            }
            else {
                duplicateFound = true;
            }
        }
        return freq;
    }
}
