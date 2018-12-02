/**
 * Main entry-point for program
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class Driver {
    public static void main(String[] args) {
        System.out.println("Day 01");
        new ChronicleCalibration("./Sample Input/Day01.txt").run();

        System.out.println("\nDay02");
        new Checksum("./Sample Input/Day02.txt").run();
    }
}
