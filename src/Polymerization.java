/**
 * Solution for Day05 of Advent of Code
 *
 * @author Evan Foley
 * @version 05 Dec 2018
 */
public class Polymerization extends AdventMaster {
    private String res = "";

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Polymerization(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Runs the program
     */
    public void run() {
        System.out.printf("The number of remaining units after fully reacting the polymer is %d%n", reactPolymer(input[0]));
        System.out.printf("The minimum length polymer that can be created is %d units long%n", fullyReactPolymer(res));
    }

    /**
     * Reduces the input String down until no reacting pairs are found
     *
     * @param input The input String
     * @return The final reduced String
     */
    private int reactPolymer(String input) {
        StringBuilder finalPolymer = new StringBuilder(input);
        // Cycle until no pairs to destroy are found
        boolean isChanged;
        do {
            isChanged = false;
            for (int i = finalPolymer.length() - 1; i > 0; i--) {
                char firstUnit = finalPolymer.charAt(i);
                char secondUnit = finalPolymer.charAt(i - 1);
                // Do characters "react" with each other
                if((secondUnit ^ firstUnit) == 32) {
                    // Remove reacted pair
                    finalPolymer.deleteCharAt(i);
                    finalPolymer.deleteCharAt(i - 1);
                    isChanged = true;
                    // decrement i by 1 to ensure it stays on the correct character
                    i--;
                }
            }
        } while(isChanged);
        res = finalPolymer.toString();
        return finalPolymer.toString().length();
    }

    /**
     * Remove reacting pairs from initial String, and find shortest resulting String
     *
     * @param input
     * @return
     */
    private int fullyReactPolymer(String input) {
        int minPolymer = Integer.MAX_VALUE;
        // Must run from A to Z (26 times)
        for(char i = 'A'; i <= 'Z'; i++) {
            // Remove all "A" and "a" characters before reducing
            String pattern = "[" + i + (char)(i + 32) + "]";
            String newPolymer = input.replaceAll(pattern, "");
            int length = reactPolymer(newPolymer);
            // Mark if this String shorter than the previous one
            if(length < minPolymer) {
                minPolymer = length;
            }
        }
        return minPolymer;
    }
}
