/**
 * Main entry-point for program
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class Driver {
    public static void main(String[] args) {
        ChronicleCalibration chronicle = new ChronicleCalibration("./Sample Input/Day01.txt");
        chronicle.run();
    }
}
