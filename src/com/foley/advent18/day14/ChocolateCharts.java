package com.foley.advent18.day14;

import com.foley.advent18.AdventMaster;
import java.util.ArrayList;
import java.util.List;

/**
 * Solution for Day14 of Advent of Code
 *
 * @author Evan Foley
 * @version 16 Dec 2018
 */
public class ChocolateCharts extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public ChocolateCharts(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        // Get the target
        int mark = Integer.parseInt(input[0]);
        ArrayList<Integer> recipeScores = new ArrayList<>();
        // Initial score feed
        recipeScores.add(3);
        recipeScores.add(7);
        System.out.printf("The first 10 recipes after reaching %d total recipes is %s%n", mark, getFinalTenRecipes(recipeScores, mark));
        recipeScores.clear();
        recipeScores.add(3);
        recipeScores.add(7);
        System.out.printf("The number of recipes that appear before %d is %d%n", mark, getNumberOfRecipesBeforeGoal(recipeScores, input[0]));
    }

    /**
     * Gets the 10 recipe scores that appear after reaching the goal number of recipes
     *
     * @param list The list of recipe scores
     * @param goal The goal number of recipes
     * @return The first 10 scores after the goal number is reached
     */
    private String getFinalTenRecipes(List<Integer> list, int goal) {
        // Initial starting recipes
        int firstElf = 0;
        int secondElf = 1;
        // Combine recipes until complete
        while(list.size() < goal + 10) {
            combineRecipes(list, firstElf, secondElf);
            // Get next recipe
            firstElf = (firstElf + (1 + list.get(firstElf))) % list.size();
            secondElf = (secondElf + (1 + list.get(secondElf))) % list.size();
        }
        StringBuilder result = new StringBuilder();
        for(int i : list.subList(goal, goal + 10)) {
            result.append(i);
        }
        return result.toString();
    }

    /**
     * Gets the number of recipes that appear before the goal sequence is encountered
     *
     * @param list The list of recipe scores
     * @param goal The goal sequence
     * @return The number of recipes that appear before the goal sequence
     */
    private int getNumberOfRecipesBeforeGoal(List<Integer> list, String goal) {
        // Initial starting recipes
        int firstElf = 0;
        int secondElf = 1;
        int preGoalRecipes = 2;
        int index = 2;
        int checkIndex = 0;
        // Combine recipes until sequence is found
        while(true) {
            combineRecipes(list, firstElf, secondElf);
            // Get next recipe
            firstElf = (firstElf + (1 + list.get(firstElf))) % list.size();
            secondElf = (secondElf + (1 + list.get(secondElf))) % list.size();

            // Test out any new ints against the sequence
            while(index < list.size()) {
                // Get the ints to compare
                int addedInt = list.get(index);
                int compareInt = goal.charAt(checkIndex) - '0';
                // Increase the index to check if ints match
                if(addedInt == compareInt) {
                    checkIndex++;
                    // If there are no more ints to check then return the answer
                    if(checkIndex >= goal.length()) {
                        return index - goal.length() + 1;
                    }
                } else {
                    // No match, reset check index
                    checkIndex = 0;
                }
                // Increase the current digit index
                index++;
            }
        }
    }

    /**
     * Combines two recipe scores to get a new score and adds it to the list
     *
     * @param list The list of recipe scores
     * @param firstIndex The index of the first recipe
     * @param secondIndex The index of the second recipe
     */
    private void combineRecipes(List<Integer> list, int firstIndex, int secondIndex) {
        int newScore = list.get(firstIndex) + list.get(secondIndex);
        if(newScore > 9) {
            list.add(1);
        }
        list.add(newScore % 10);
    }
}
