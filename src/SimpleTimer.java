/**
 * Keeps track of elapsed time
 *
 * @author Evan Foley
 * @version 07 Dec 2018
 */
public class SimpleTimer {
    private long startTime;
    private long elapsedTime;
    private long totalTime;

    /**
     * Helper enum
     */
    public enum Units {
        SECONDS(1000000000), MILLISECONDS(1000000);

        private int units;

        Units(int units) {
            this.units = units;
        }
    }

    /**
     * Creates a new simple timer
     */
    public SimpleTimer() {
        startTime = System.nanoTime();
        totalTime = 0;
    }

    /**
     * Records the time since the last call to tick, or from creation if this is the first time calling
     */
    public void tick() {
        long now = System.nanoTime();
        elapsedTime = now - startTime;
        totalTime += elapsedTime;
        startTime = now;
    }

    /**
     * Prints out the last recorded tick results
     *
     * @param timeUnits The unit of time
     */
    public void printTick(Units timeUnits) {
        System.out.printf("Time taken: %d %s%n", elapsedTime / timeUnits.units, timeUnits.name());
    }

    /**
     * Prints out the total run time
     *
     * @param timeUnits The unit of time
     */
    public void printTotalTime(Units timeUnits) {
        System.out.printf("The total run time: %d %s%n", totalTime / timeUnits.units, timeUnits.name());
    }
}
