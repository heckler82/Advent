import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Solution for Day12 of Advent of Code. Did not come up with this solution; did not enjoy this challenge at all
 *
 * @author Evan Foley
 * @version 15 Dec 2018
 */
public class Plants extends AdventMaster {
    private Set<String> grow;
    private int zero;
    private String inp;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Plants(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        grow = new HashSet<>();
        inp = input[0].substring(15);
        for (int i = 2; i < input.length; i++) {
            String[] split = input[i].split(" ");
            if (split[2].startsWith("#")) {
                grow.add(split[0]);
            }
        }
        System.out.printf("The sum of the pot numbers after 20 generations is %d%n", generatePlants());
        System.out.printf("The sum of the pot numbers after 50000000000 generations is %d%n", longGenerationPlants());
    }

    private int generatePlants() {
        for (int i = 0; i < 20; i++) {
            String last = "....." + inp + "...";
            String next = "";
            zero += 3;
            for (int j = 2; j < last.length() - 2; j++) {
                if (grow.contains(last.substring(j - 2, j + 3))) {
                    next += "#";
                } else {
                    next += ".";
                }

            }
            inp = next;
        }
        int sum = 0;
        for (int i = 0; i < inp.length(); i++) {
            sum += inp.charAt(i) == '#' ? i - zero : 0;
        }
        return sum;
    }

    private long longGenerationPlants() {
        long sum = 0L;

        // 100 iterations
        for (int i = 0; i < 4; i++) {
            sum = generatePlants();
        }

        // get factor
        int last = 0;
        int diff = 0;
        for (int i = 0; i < 3; i++) {
            int next = generatePlants();
            diff = next - last;
            last = next;
        }
        sum += ((50_000_000_000L - 100) / 20)* diff;
        return sum;
    }
}
