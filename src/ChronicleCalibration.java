import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Solution for Day01 of Advent of Code
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class ChronicleCalibration {
    private int[] input;

    /**
     * Creates and prepares the input
     *
     * @param fileName The name of the file with the sample input
     */
    public ChronicleCalibration(String fileName) {
        File file = new File(fileName);
        try {
            Scanner scan = new Scanner(file);
            input = processInput(scan);
        } catch(FileNotFoundException e) {
            System.err.println("[CRITICAL] The requested file cannot be found");
            System.err.println("[INFORMATION] The program will now terminate");
            System.exit(1);
        }

    }

    /**
     * Prepares the input for use
     *
     * @param scan The parsing object
     * @return The input from the parser
     */
    private int[] processInput(Scanner scan) {
        ArrayList<String> container = new ArrayList<>();
        while(scan.hasNextLine()) {
            container.add(scan.nextLine());
        }
        int[] rawIn = new int[container.size()];
        for(int i = 0; i < rawIn.length; i++) {
            try {
                rawIn[i] = Integer.parseInt(container.get(i));
            } catch(NumberFormatException e) {
                System.err.print("[WARNING] Could not parse the given String to an Integer");
            }
        }
        return rawIn;
    }

    /**
     * Runs the calculations
     */
    public void run() {
        System.out.println("Starting frequency is 0");
        System.out.printf("Final adjusted frequency is %d%n", findFinalAdjustedFrequency(input));
        System.out.printf("First duplicate frequency is %d%n", findFirstDuplicateFrequency(input));
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
            // Calculate adjusted frequency and add to freq pool
            freq += adjustments[i];
            // If freq doesn't exist, add it, otherwise end search
            if(!uniqueFreqs.contains(freq)) {
                uniqueFreqs.add(freq);
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
