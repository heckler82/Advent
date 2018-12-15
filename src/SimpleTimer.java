import java.text.DecimalFormat;

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
        SECONDS(1000000000, "sec"), MILLISECONDS(1000000, "ms"), MICROSECONDS(1000, "\u00B5s"), NANOSECONDS(1, "ns");

        private int divisor;
        private String units;

        Units(int divisor, String units) {
            this.divisor = divisor;
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
     * Sets start to the current time. tick() automatically does this, this is a convenience method for more exact timing if needed
     */
    public void setToCurrentTime() {
        startTime = System.nanoTime();
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
     * Prints out the last recorded tick results in the largest units yielding a greater than 1.0 result
     */
    public void printTick() {
        for(Units unit : Units.values()) {
            double time = (double) elapsedTime / unit.divisor;
            if(time >= 1.0) {
                DecimalFormat df = new DecimalFormat("#0");
                System.out.printf("Time taken: %s%s%n", df.format(time), unit.units);
                return;
            }
        }
    }

    /**
     * Prints out the last recorded tick results in the time units specified
     *
     * @param timeUnits The unit of time
     */
    public void printTick(Units timeUnits) {
        double time = (double) elapsedTime / timeUnits.divisor;
        String out;
        DecimalFormat df;
        if(time < 1.0) {
            df = new DecimalFormat("#0.00");
        }
        else {
            df = new DecimalFormat("#0");
        }
        out = df.format(time);
        System.out.printf("Time taken: %s%s%n", out, timeUnits.units);
    }

    /**
     * Prints out the total run time
     *
     * @param timeUnits The unit of time
     */
    public void printTotalTime(Units timeUnits) {
        double time = (double) totalTime / timeUnits.divisor;
        String out;
        DecimalFormat df = new DecimalFormat("#0.00");
        out = df.format(time);
        System.out.printf("The total run time: %s%s%n", out, timeUnits.units);
    }
}
