/**
 * Solution for Day05 of Advent of Code
 *
 * @author Evan Foley
 * @version 05 Dec 2018
 */
public class Polymerization extends AdventMaster {
    public Polymerization(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Runs the program
     */
    public void run() {
        System.out.printf("The number of remaining units after fully reacting the polymer is %d%n", reactPolymer(input[0]));
        System.out.printf("The minimum length polymer that can be created is %d units long", fullyReactPolymer(input[0]));
    }

    /**
     * Reduces the input String down until no reacting pairs are found
     *
     * @param input The input String
     * @return The final reduced String
     */
    private int reactPolymer(String input) {
        StringBuilder finalPolymer = new StringBuilder();
        finalPolymer.append(input);
        int numDestroyed;
        // Cycle until no pairs to destroy are found
        do {
            numDestroyed = 0;
            for (int i = finalPolymer.length() - 1; i > 0; i--) {
                char firstUnit = finalPolymer.charAt(i);
                char secondUnit = finalPolymer.charAt(i - 1);
                // Do characters "react" with each other
                if((secondUnit ^ firstUnit) == 32) {
                    finalPolymer.delete(i - 1, i + 1);
                    numDestroyed += 2;
                    // decrement i by 1 to ensure it stays on the correct character
                    i--;
                }
            }
        } while(numDestroyed > 0);
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
        for(int i = 'A'; i <= 'Z'; i++) {
            // Remove all "A" and "a" characters before reducing
            String pattern = "[" + Character.toString((char)i) + Character.toString(Character.toLowerCase((char)i)) + "]";
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
