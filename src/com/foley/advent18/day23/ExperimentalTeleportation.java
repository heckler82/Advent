package com.foley.advent18.day23;

import com.foley.advent18.AdventMaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day23 of Advent of Code
 *
 * @author Evan Foley
 * @version 23 Dec 2018
 */
public class ExperimentalTeleportation extends AdventMaster {

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public ExperimentalTeleportation(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for the day
     */
    protected void task() {
        NanoBot[] bots = getSortedBots(input);
        // Largest radius is automatically in front
        System.out.printf("The number of bots that are in range of the best bot is %d%n", getNumberOfBotsInRange(bots, bots[0].radius));
        System.out.printf("The distance from the origin of the best teleportation location is %d%n", getDistanceFromOrigin(bots));
    }

    /**
     * Gets the number of bots that are in range of the best radius
     *
     * @param bots The list of bots
     * @param radius The best radius
     * @return The number of bots in range
     */
    public int getNumberOfBotsInRange(NanoBot[] bots, int radius) {
        //Determine number of bots within distance
        int inRange = 0;
        for(NanoBot bot : bots) {
            if(bots[0].distanceFrom(bot) <= radius) {
                inRange++;
            }
        }
        return inRange;
    }

    /**
     * Gets the distance from the origin to the best location
     *
     * @param bots The list of bots
     * @return The distance from the origin
     */
    public int getDistanceFromOrigin(NanoBot[] bots) {
        return 0;
    }

    /**
     * Gets the nanobots from the input
     *
     * @param input The input
     * @return The list of nanobots
     */
    private NanoBot[] getSortedBots(String[] input) {
        List<NanoBot> bots = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+");
        // Parse all bots from the input
        for(String str : input) {
            int[] dat = new int[4];
            Matcher matcher = pattern.matcher(str);
            int index = 0;
            while(matcher.find()) {
                dat[index] = Integer.parseInt(matcher.group());
                index++;
            }
            // Find best radius
            bots.add(new NanoBot(dat));
        }
        Collections.sort(bots);
        return bots.toArray(new NanoBot[bots.size()]);
    }

    /**
     * Helper class
     */
    private class NanoBot implements Comparable<NanoBot> {
        int[] position;
        int radius;

        public NanoBot(int[] data) {
            if(data.length < 4) {
                throw new IllegalArgumentException("Data must have 4 values");
            }
            // Only use the first 4 values
            position = new int[]{data[0], data[1], data[2]};
            radius = data[3];
        }

        public NanoBot(int x, int y, int z, int radius) {
            this(new int[]{x, y, z}, radius);
        }

        public NanoBot(int[] position, int radius) {
            this.position = position;
            this.radius = radius;
        }

        public int distanceFrom(NanoBot bot) {
            int manDist = Math.abs(position[0] - bot.position[0]) + Math.abs(position[1] - bot.position[1]) + Math.abs(position[2] - bot.position[2]);
            return manDist;
        }

        public int distanceFrom(int[] pos) {
            int manDist = Math.abs(position[0] - pos[0]) + Math.abs(position[1] - pos[1]) + Math.abs(position[2] - pos[2]);
            return manDist;
        }

        public int compareTo(NanoBot bot) {
            return Integer.compare(bot.radius, radius);
        }
    }
}
