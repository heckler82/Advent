package com.foley.advent18.day04;

import com.foley.advent18.AdventMaster;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for com.foley.advent18.day04 of Advent of Code
 *
 * @author Evan Foley
 * @version 05 Dec 2018
 */
public class SleepyGuard extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public SleepyGuard(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        List<Tuple<Date, String>> events = sortEvents(input);
        Map<Integer, int[]> processedData = processData(events);

        System.out.printf("The guard who sleeps the most multiplied by the minute he is most asleep is %d%n", findSleepyGuard(processedData));
        System.out.printf("The guard who was asleep most frequently on a given minute multiplied by that minute is %d%n", findGuardMinuteFrequency(processedData));
    }

    /**
     * Processes the data into a map of guard id numbers and minutes they were asleep
     *
     * @param list The list of Dates and events
     * @return The map of guard id numbers to sleeping minutes
     */
    private Map<Integer, int[]> processData(List<Tuple<Date, String>> list) {
        Map<Integer, int[]> times = new HashMap<>();
        int currentGuard = 0;
        int asleepMinute = 0;
        for(Tuple event : list) {
            // Guard ID
            Pattern pattern = Pattern.compile("[\\d]+");
            Matcher matcher = pattern.matcher((String)event.item2);
            if(matcher.find()) {
                // Mark current guard and add to map
                currentGuard = Integer.parseInt(matcher.group());
                if(!times.containsKey(currentGuard)) {
                    times.put(currentGuard, new int[60]);
                }
            }
            else {
                // This is not a guard shift change
                if("falls asleep".equals(event.item2)) {
                    // Get the minute they fall asleep
                    Date sleep = (Date)event.item1;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((sleep));
                    asleepMinute = cal.get(Calendar.MINUTE);
                }
                else {
                    // Not a shift change or a sleep event
                    if("wakes up".equals(event.item2)) {
                        // Get the waking minute
                        Date wake = (Date)event.item1;
                        Calendar cal = Calendar.getInstance();
                        cal.setTime((wake));
                        int wakeMinute = cal.get(Calendar.MINUTE);
                        // Increment all minutes between asleep and wake by 1
                        for(int i = asleepMinute; i < wakeMinute; i++) {
                            times.get(currentGuard)[i]++;
                        }
                    }
                }
            }
        }
        return times;
    }

    /**
     * Finds the guard that sleeps the most multiplied by the most slept minute
     *
     * @param data The map of data
     * @return The product of the guard id and the most slept minute
     */
    private int findSleepyGuard(Map<Integer, int[]> data) {
        // Get the keys to the map (the guard id numbers)
        List<Integer> keys = new ArrayList<>(data.keySet());
        int sleepyGuard = keys.get(0);
        int mostMinutes = 0;
        // For every guard id, get the total number of minutes slept
        for(int i : keys) {
            int totalMin = 0;
            for(int j = 0; j < data.get(i).length; j++) {
                totalMin += data.get(i)[j];
            }
            // Mark the sleepier guard if they slept longer than the previous one
            if(totalMin > mostMinutes) {
                mostMinutes = totalMin;
                sleepyGuard = i;
            }
        }

        // Find the minute the guard was most asleep
        int mostAsleepMinute = 0;
        int currentBestMinute = 0;
        for(int i = 0; i < 60; i++) {
            // If this minute is better than the others, mark it
            if(data.get(sleepyGuard)[i] > currentBestMinute) {
                currentBestMinute = data.get(sleepyGuard)[i];
                mostAsleepMinute = i;
            }
        }
        return mostAsleepMinute * sleepyGuard;
    }

    /**
     * Finds the guard who slept the most on a particular minute and multiplies it by that minute
     *
     * @param data The map of data
     * @return The product of the guard id and the minute they slept longer than anybody
     */
    private int findGuardMinuteFrequency(Map<Integer, int[]> data) {
        List<Integer> list = new ArrayList<>(data.keySet());
        int id = list.get(0);
        int minute = 0;
        int max = 0;
        for(int i : list) {
            for(int j = 0;j < data.get(i).length; j++) {
                if(data.get(i)[j] > max) {
                    id = i;
                    minute = j;
                    max = data.get(i)[j];
                }
            }
        }
        return id * minute;
    }

    /**
     * Sorts the observed events by date
     *
     * @param input The raw input
     * @return The list of events sorted from oldest to newest date
     */
    private List<Tuple<Date, String>> sortEvents(String[] input) {
        ArrayList<Tuple<Date, String>> sorted = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // Get every line of input
        for(String str : input) {
            String[] split = str.split("]\\s+");
            Tuple event = new Tuple();
            // Attempt to parse the date from the line of input
            try {
                event.item1 = format.parse(split[0].substring(1));
            } catch(ParseException e) {
                System.err.println("[WARNING] Could not parse date from input String. Inserting default date of today");
                event.item2 = new Date();
            }
            // Write the remaining event text into the tuple
            event.item2 = split[1];
            sorted.add(event);
        }
        // Sort by date
        sorted.sort(new TupleComparator());
        return sorted;
    }

    /**
     * Helper class for organizing raw input data
     *
     * @param <X> The x component
     * @param <Y> The y component
     */
    private class Tuple<X extends Comparable, Y extends Comparable>{
        X item1;
        Y item2;
    }

    /**
     * Custom comparator for the Tuple class
     */
    private class TupleComparator implements Comparator<Tuple> {
        public int compare(Tuple e1, Tuple e2) {
            return e1.item1.compareTo(e2.item1);
        }
    }
}
