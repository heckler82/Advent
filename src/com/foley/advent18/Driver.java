package com.foley.advent18;

import com.foley.advent18.day01.ChronicleCalibration;
import com.foley.advent18.day02.Checksum;
import com.foley.advent18.day03.Intersection;
import com.foley.advent18.day04.SleepyGuard;
import com.foley.advent18.day05.Polymerization;
import com.foley.advent18.day06.Plot;
import com.foley.advent18.day07.Sequencer;
import com.foley.advent18.day08.Nodes;
import com.foley.advent18.day09.MarbleGame;
import com.foley.advent18.day10.HiddenMessage;
import com.foley.advent18.day11.FuelCell;
import com.foley.advent18.day12.Plants;
import com.foley.advent18.day13.MineCart;
import com.foley.advent18.day14.ChocolateCharts;
import com.foley.advent18.day15.CaveBattle;
import com.foley.advent18.day16.Opcode;
import com.foley.advent18.day17.Reservoir;
import com.foley.advent18.day18.GameOfLumber;
import com.foley.advent18.day19.FlowControl;
import com.foley.advent18.day20.MasterMap;

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
        new ChronicleCalibration("/com/foley/advent18/day01//Day01.txt").run(timer);

        // Day 02
        System.out.println("\nDay02");
        new Checksum("/com/foley/advent18/day02//Day02.txt").run(timer);

        // Day 03
        System.out.println("\nDay03");
        new Intersection("/com/foley/advent18/day03//Day03.txt").run(timer);

        // Day 04
        System.out.println("\nDay04");
        new SleepyGuard("/com/foley/advent18/day04//Day04.txt").run(timer);

        // Day 05
        System.out.println("\nDay05");
        new Polymerization("/com/foley/advent18/day05//Day05.txt").run(timer);

        // Day 06
        System.out.println("\nDay05");
        new Plot("/com/foley/advent18/day06//Day06.txt").run(timer);

        // Day 07
        System.out.println("\nDay07");
        new Sequencer("/com/foley/advent18/day07//Day07.txt").run(timer);

        // Day 08
        System.out.println("\nDay08");
        new Nodes("/com/foley/advent18/day08//Day08.txt").run(timer);

        // Day 09
        System.out.println("\nDay09");
        new MarbleGame("/com/foley/advent18/day09//Day09.txt").run(timer);

        // Day 10
        System.out.println("\nDay10");
        new HiddenMessage("/com/foley/advent18/day10//Day10.txt").run(timer);

        // Day 11
        System.out.println("\nDay11");
        new FuelCell("/com/foley/advent18/day11//Day11.txt").run(timer);

        // Day 12
        System.out.println("\nDay12");
        new Plants("/com/foley/advent18/day12//Day12.txt").run(timer);

        // Day 13
        System.out.println("\nDay13");
        new MineCart("/com/foley/advent18/day13//Day13.txt").run(timer);

        // Day 14
        System.out.println("\nDay14");
        new ChocolateCharts("/com/foley/advent18/day14//Day14.txt").run(timer);

        // Day 15
        System.out.println("\nDay15");
        new CaveBattle("/com/foley/advent18/day15//Day15.txt").run(timer);

        // Day 16
        System.out.println("\nDay16");
        new Opcode("/com/foley/advent18/day16//Day16.txt").run(timer);

        // Day 17
        System.out.println("\nDay17");
        new Reservoir("/com/foley/advent18/day17//Day17.txt").run(timer);

        // Day 18
        System.out.println("\nDay18");
        //new GameOfLumber("/com/foley/advent18/day18//Day18.txt").run(timer);

        // Day 19
        System.out.println("\nDay19");
        new FlowControl("/com/foley/advent18/day19//Day19.txt").run(timer);

        // Day 20
        //System.out.println("\nDay20");
        //new MasterMap("/com/foley/advent18/day20/Day20.txt").run(timer);

        // Day 21
        //System.out.println("\nDay21");
        //new UnderFlow("/Day21.txt").run(timer);

        System.out.println();
        timer.printTotalTime(SimpleTimer.Units.SECONDS);
    }
}
