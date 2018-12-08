/**
 * Main entry-point for program
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class Driver {
    public static void main(String[] args) {
        // Timing mechanism
        SimpleTimer timer = new SimpleTimer();

        // Day 01
        System.out.println("Day 01");
        new ChronicleCalibration("./Sample Input/Day01.txt").run();
        timer.tick();
        timer.printTick();

        // Day 02
        System.out.println("\nDay02");
        new Checksum("./Sample Input/Day02.txt").run();
        timer.tick();
        timer.printTick();

        // Day 03
        System.out.println("\nDay03");
        new Intersection("./Sample Input/Day03.txt").run();
        timer.tick();
        timer.printTick();

        // Day 04
        System.out.println("\nDay04");
        new SleepyGuard("./Sample Input/Day04.txt").run();
        timer.tick();
        timer.printTick();

        // Day 05
        System.out.println("\nDay05");
        new Polymerization("./Sample Input/Day05.txt").run();
        timer.tick();
        timer.printTick();

        // Day 06
        System.out.println("\nDay05");
        new Plot("./Sample Input/Day06.txt").run();
        timer.tick();
        timer.printTick();

        // Day 07
        System.out.println("\nDay07");
        new Sequencer("./Sample Input/Day07.txt").run();
        timer.tick();
        timer.printTick();
    }
}
