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
        //System.out.println("Day 01");
        //new ChronicleCalibration("./Sample Input/Day01.txt").run(timer);

        // Day 02
        //System.out.println("\nDay02");
        //new Checksum("./Sample Input/Day02.txt").run(timer);

        // Day 03
        //System.out.println("\nDay03");
        //new Intersection("./Sample Input/Day03.txt").run(timer);

        // Day 04
        //System.out.println("\nDay04");
        //new SleepyGuard("./Sample Input/Day04.txt").run(timer);

        // Day 05
        //System.out.println("\nDay05");
        //new Polymerization("./Sample Input/Day05.txt").run(timer);

        // Day 06
        //System.out.println("\nDay05");
        //new Plot("./Sample Input/Day06.txt").run(timer);

        // Day 07
        //System.out.println("\nDay07");
        //new Sequencer("./Sample Input/Day07.txt").run(timer);

        // Day 08
        //System.out.println("\nDay08");
        //new Nodes("./Sample Input/Day08.txt").run(timer);

        // Day 09
        //System.out.println("\nDay09");
        //new MarbleGame("./Sample Input/Day09.txt").run(timer);

        // Day 10
        //System.out.println("\nDay10");
        //new HiddenMessage("./Sample Input/Day10.txt").run(timer);

        // Day 11
        //System.out.println("\nDay11");
        //new FuelCell("./Sample Input/Day11.txt").run(timer);

        // Day 12
        //System.out.println("\nDay12");
        //new Plants("./Sample Input/Day12.txt").run(timer);

        // Day 13
        //System.out.println("\nDay13");
        //new MineCart("./Sample Input/Day13.txt").run(timer);

        // Day 14
        //System.out.println("\nDay14");
        //new ChocolateCharts("./Sample Input/Day14.txt").run(timer);

        // Day 15
        //System.out.println("\nDay15");
        //new CaveBattle("./Sample Input/Day15.txt").run(timer);

        // Day 16
        //System.out.println("\nDay16");
        //new Opcode("./Sample Input/Day16.txt").run(timer);

        // Day 17
        //System.out.println("\nDay17");
        new Reservoir("./Sample Input/Day17.txt").run(timer);

        // Day 18
        //System.out.println("\nDay18");
        //new GameOfLumber("./Sample Input/Day18.txt").run(timer);

        System.out.println();
        timer.printTotalTime(SimpleTimer.Units.SECONDS);
    }
}
