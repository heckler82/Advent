import java.util.HashSet;
import java.util.Set;

/**
 * Main entry-point for program
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class Driver {
    public static void main(String[] args) {
        Day01(args);
    }

    /**
     * Day 01 - Chronical Calibration
     *
     * @param args The list of frequency adjustments
     */
    public static void Day01(String[] args) {
        // Start at 0
        int freq = 0;

        // Container for adjusted frequencies, will only hold unique values
        Set<Integer> uniqueFreqs = new HashSet<Integer>();
        uniqueFreqs.add(freq);

        System.out.println("Starting frequency: " + freq);
        // Loop all provided adjustments until duplicate hit found
        boolean duplicateFound = false;
        int i = 0;
        while(!duplicateFound) {
            // Get current adjustment
            String str = args[i];
            // Calculate adjustment and add to freq pool
            try {
                freq += Integer.parseInt(str);
                // If freq doesn't exist, add it, otherwise end search
                if(!uniqueFreqs.contains(freq)) {
                    uniqueFreqs.add(freq);
                    // Clamp i between 0 and args.length - 1
                    i = ++i % args.length;
                }
                else {
                    duplicateFound = true;
                }
            } catch(NumberFormatException e) {
                System.err.print("This String is unable to be parsed into integer");
            }
        }
        System.out.println("First duplicate frequency: " + freq);
    }
}
