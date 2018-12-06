/**
 * Main entry-point for program
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class Driver {
    public static void main(String[] args) {
        long currentTime = System.nanoTime();
        System.out.println("Day 01");
        new ChronicleCalibration("./Sample Input/Day01.txt").run();
        double elapsedTime = (System.nanoTime() - currentTime) / 1000000000.0;
        System.out.printf("Time Taken: %f%n", elapsedTime);

        currentTime = System.nanoTime();
        System.out.println("\nDay02");
        new Checksum("./Sample Input/Day02.txt").run();
        elapsedTime = (System.nanoTime() - currentTime) / 1000000000.0;
        System.out.printf("Time Taken: %f%n", elapsedTime);

        currentTime = System.nanoTime();
        System.out.println("\nDay03");
        new Intersection("./Sample Input/Day03.txt").run();
        elapsedTime = (System.nanoTime() - currentTime) / 1000000000.0;
        System.out.printf("Time Taken: %f%n", elapsedTime);

        currentTime = System.nanoTime();
        System.out.println("\nDay04");
        new SleepyGuard("./Sample Input/Day04.txt").run();
        elapsedTime = (System.nanoTime() - currentTime) / 1000000000.0;
        System.out.printf("Time Taken: %f%n", elapsedTime);

        currentTime = System.nanoTime();
        System.out.println("\nDay05");
        new Polymerization("./Sample Input/Day05.txt").run();
        elapsedTime = (System.nanoTime() - currentTime) / 1000000000.0;
        System.out.printf("Time Taken: %f%n", elapsedTime);
    }
}
