package com.foley.advent18.day01;

import com.foley.advent18.AdventMaster;

import java.util.*;

/**
 * Solution for com.foley.advent18.day01 of Advent of Code
 *
 * @author Evan Foley
 * @version 01 Dec 2018
 */
public class ChronicleCalibration extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public ChronicleCalibration(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        // Get numerical data for calculation
        int[] freqs = parseInput(input);

        List<Integer> list = new ArrayList<>();
        for(int i : freqs) {
            list.add(i);
        }

        // Print result of calculations
        System.out.println("Starting frequency is 0");
        System.out.printf("Final adjusted frequency is %d%n", findFinalAdjustedFrequency(list));
        System.out.printf("First duplicate frequency is %d%n", findFirstDuplicateFrequency(list));
    }

    /**
     * Converts raw input into numerical data for calculation
     *
     * @param input The raw input
     * @return Numerical form of the raw input
     */
    private int[] parseInput(String[] input) {
        int[] rawIn = new int[input.length];
        for(int i = 0; i < input.length; i++) {
            try {
                rawIn[i] = Integer.parseInt(input[i]);
            } catch(NumberFormatException e) {
                System.err.print("[WARNING] Could not parse the given String to an Integer");
            }
        }
        return rawIn;
    }

    /**
     * Given a list of frequency adjustments, find the final adjusted frequency
     *
     * @param adjustments The list of adjustments
     * @return The final adjusted frequency
     */
    private int findFinalAdjustedFrequency(List<Integer> adjustments) {
        // Start at 0
        int freq = 0;
        for(int adj : adjustments) {
            freq += adj;
        }
        return freq;
    }

    /**
     * Given a list of frequency adjustments, continuously loop through and adjust the master frequency until the first repeat frequency is found
     *
     * @param adjustments The list of adjustments
     * @return The first repeat frequency
     */
    private int findFirstDuplicateFrequency(List<Integer> adjustments) {
        // Start at 0
        int freq = 0;

        // Container for adjusted frequencies, will only hold unique values
        Set<Integer> uniqueFreqs = new HashSet<>();
        uniqueFreqs.add(freq);

        // Loop all provided adjustments until duplicate hit found
        int i = 0;
        while(true) {
            // Calculate adjusted frequency
            freq += adjustments.get(i);
            // Attempt to add freq, stop search if already in pool
            if(uniqueFreqs.add(freq)) {
                // Clamp i between 0 and args.length - 1
                i = ++i % adjustments.size();
            }
            else {
                return freq;
            }
        }
    }
}
