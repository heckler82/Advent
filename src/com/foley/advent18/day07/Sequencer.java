package com.foley.advent18.day07;

import com.foley.advent18.AdventMaster;

/**
 * Solution for Day07 of Advent of Code
 *
 * @author Evan Foley
 * @version 07 Dec 2018
 */
public class Sequencer extends AdventMaster {

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Sequencer(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        System.out.printf("The sequence of steps is %s%n", getProperSequence());
        System.out.printf("The time taken to complete the sequence is %d seconds%n", getTimedSequence());
    }

    /**
     * Determines the proper sequence
     *
     * @return The proper sequence
     */
    private String getProperSequence() {
        int[][] steps = prepData();
        StringBuilder result = new StringBuilder();

        // Continue to get the priority id until no more steps remain
        int priority;
        while((priority = getPriorityID(steps)) != -1){
            // Consume the step and add it to the result
            steps[priority][27] = -1;
            result.append((char) (priority + 'A'));

            // Consume the step in all tasks that need it consumed
            for (int i = 0; i < steps.length; i++) {
                if (steps[i][priority] > 0) {
                    steps[i][priority] = 0;
                    steps[i][26]--;
                }
            }
        }
        return result.toString();
    }

    /**
     * Determines the amount of time it takes to complete the sequence
     *
     * @return The time taken to complete the sequence
     */
    private int getTimedSequence() {
        int totalTime = 0;
        int[][] steps = prepData();
        StringBuilder result = new StringBuilder();

        // Fill the initial job orders if they are available
        int[][] workers = new int[5][2];
        for(int i = 0; i < workers.length; i++) {
            // Initialize all workers to idle
            workers[i][0] = -1;
            // Go through the list to see what steps are available
            for (int j = 0; j < steps.length; j++) {
                // If active and has no prereqs, mark as inactive and assign to a worker
                if(steps[j][26] == 0 && steps[j][27] == 0) {
                    steps[j][27] = -1;
                    workers[i][0] = j;
                    workers[i][1] = 61 + j;
                    break;
                }
            }
        }

        // Continue to consume while there is work to be done
        while(stillWorking(workers, steps)) {
            // Increase time
            totalTime++;
            // Update all workers that have a job
            for(int i = 0; i < workers.length; i++) {
                if(workers[i][0] != -1) {
                    workers[i][1]--;
                    if (workers[i][1] == 0) {
                        // consume id here
                        int id = workers[i][0];
                        workers[i][0] = -1;

                        // Consume the step in all tasks that need it consumed
                        for (int j = 0; j < 26; j++) {
                            if (steps[j][id] > 0) {
                                steps[j][id] = 0;
                                steps[j][26]--;
                            }
                        }
                    }
                }
            }
        }

        return totalTime;
    }

    /**
     * Gets the index of the next step
     *
     * @param steps The list of steps
     * @return The priority step
     */
    private int getPriorityID(int[][] steps) {
        // Get the priority step
        int priorityID = -1;
        for(int i = 0; i < 26; i++) {
            int consumed = steps[i][27];
            if(consumed == 0) {
                int remainingPrereqs = steps[i][26];
                if(remainingPrereqs == 0) {
                    priorityID = i;
                    break;
                }
            }
        }
        return priorityID;
    }

    /**
     * Prepares the input for use
     *
     * @return The prepared data
     */
    private int[][] prepData() {
        int[][] steps = new int[26][28];

        // Fill out all the prereq tables
        int maxID = Integer.MIN_VALUE;
        for(String str : input) {
            char prereq = str.charAt(5);
            char id = str.charAt(36);
            if(id - 65 > maxID) {
                maxID = id - 65;
            }
            steps[id - 65][prereq - 65] = 1;
            steps[id - 65][26]++;
        }
        for(int i = maxID + 1; i < steps.length; i++) {
            steps[i][27] = -1;
        }
        return steps;
    }

    /**
     * Determines if the workers are still working
     *
     * @param workers The list of workers
     * @param steps The list of steps
     * @return True if any workers are still working
     */
    private boolean stillWorking(int[][] workers, int[][] steps) {
        int stillWorking = 0;
        for(int i = 0; i < workers.length; i++) {
            if(workers[i][0] > -1) {
                stillWorking++;
            } else {
                // Get any available id here
                int id = getPriorityID(steps);
                // If id was found, assign to a worker
                if (id != -1) {
                    steps[id][27] = -1;
                    workers[i][0] = id;
                    workers[i][1] = 61 + id;
                    stillWorking++;
                }
            }
        }
        return stillWorking > 0;
    }
}
