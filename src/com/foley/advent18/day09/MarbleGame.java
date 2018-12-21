package com.foley.advent18.day09;

import com.foley.advent18.AdventMaster;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day09 of Advent of Code
 *
 * @author Evan Foley
 * @version 09 Dec 2018
 */
public class MarbleGame extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public MarbleGame(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        // Convert String data to integer data
        int[] data = getNumericalData(input);

        // Keep track of elf scores
        long[] scores = new long[data[0]];

        // Game control data
        int stoppingMarble = data[1];

        // Marble list
        Node root = getNewList(0);

        // Get the results of the game
        System.out.printf("The highest score is %d%n", winningScore(root, scores, stoppingMarble));

        // Reset game
        root = getNewList(0);
        scores = new long[data[0]];
        System.out.printf("The highest score when the last marble is 100 times larger is %d%n", winningScore(root, scores, stoppingMarble * 100));
    }

    /**
     * Finds the winning score of the game
     *
     * @param root The list of marbles played
     * @param scores The running scores
     * @param stoppingMarble The last marble to be played
     * @return The winning score
     */
    private long winningScore(Node root, long[]scores, int stoppingMarble) {
        // Prep needed data
        int currentPlayingMarble = 1;
        long currentHighScore = 0;
        int currentElf = 0;
        Node current = root;

        // Play the game
        boolean playContinue = true;
        while(playContinue) {
            // Determine if this is a scoring marble
            if(currentPlayingMarble % 23 == 0) {
                // Determine the marble to remove
                Node remove = current.prev.prev.prev.prev.prev.prev.prev;
                // Remove the marble and update next
                remove.prev.next = remove.next;
                remove.next.prev = remove.prev;
                current = remove.next;
                // Get the removed marble value and add it to the elf's score
                int addScore = remove.value;
                int currentScore = currentPlayingMarble + addScore;
                scores[currentElf] += currentScore;
                // Update max high score is needed
                if(scores[currentElf] > currentHighScore) {
                    currentHighScore = scores[currentElf];
                }
            }
            else {
                // Create the newest node and get the node to add after
                Node newNode = new Node(currentPlayingMarble);
                Node next = current.next;
                // Add the new node to the list, and update references
                newNode.next = next.next;
                next.next.prev = newNode;
                newNode.prev = next;
                next.next = newNode;
                // Update current
                current = newNode;
            }
            // Determine exit conditions
            if(currentPlayingMarble == stoppingMarble) {
                playContinue = false;
            } else {
                // Ensure current elf stays within parameters
                currentElf = ++currentElf % scores.length;
                currentPlayingMarble++;
            }
        }
        return currentHighScore;
    }

    /**
     * Gets a new list
     *
     * @param startingValue The value initially in the list
     * @return The new list
     */
    private Node getNewList(int startingValue) {
        Node n = new Node(startingValue);
        n.next = n;
        n.prev = n;
        return n;
    }

    /**
     * Gets the input in numerical form
     *
     * @param input The input
     * @return The input in numerical form
     */
    private int[] getNumericalData(String[] input) {
        // Create container
        int[] data = new int[2];
        // Prepare pattern to match
        Pattern pattern = Pattern.compile("[\\d]+");
        Matcher matcher = pattern.matcher(input[0]);
        int index = 0;
        // Pull out the numerical pieces
        while(matcher.find()) {
            data[index] = Integer.parseInt(matcher.group());
            index++;
        }
        return data;
    }

    /**
     * Helper class
     */
    private class Node {
        Node next;
        Node prev;
        int value;

        public Node(int value) {
            this.value = value;
        }
    }
}
