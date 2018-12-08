/**
 * Keeps track of elapsed time
 *
 * @author Evan Foley
 * @version 07 Dec 2018
 */
public class SimpleTimer {
    private long startTime;
    private long elapsedTime;

    /**
     * Creates a new simple timer
     */
    public SimpleTimer() {
        startTime = System.nanoTime();
    }

    /**
     * Records the time since the last call to tick, or from creation if this is the first time calling
     */
    public void tick() {
        long now = System.nanoTime();
        elapsedTime = now - startTime;
        startTime = now;
    }

    /**
     * Prints out the last recorded tick results
     */
    public void printTick() {
        System.out.printf("Time taken: %dms%n", elapsedTime / 1000000);
    }
}
