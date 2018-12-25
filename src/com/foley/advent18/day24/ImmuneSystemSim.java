package com.foley.advent18.day24;

import com.foley.advent18.AdventMaster;

import java.util.List;

/**
 * Class description goes here
 *
 * @author Evan Foley
 * @version 24 Dec 2018
 */
public class ImmuneSystemSim extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public ImmuneSystemSim(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for the day
     */
    protected void task() {
    }

    private void parseInput(String[] input) {
        int lastIndex = 0;
        boolean isInfection = true;
        String first = input[0];
        if("Immune System:".equals(first)) {
            isInfection = false;
        }
        // Begin parsing until blank line is encountered
        for(int i = 1; i < input.length; i++) {
            String str = input[i];
            if("".equals(str)) {
                // This will skip the title for the next list in the input
                lastIndex = i + 2;
                break;
            }
        }
        // Change team
        isInfection = !isInfection;
        // Continue parsing with other list hooked in
        for(int i = lastIndex; i < input.length; i++) {
            String str = input[i];
        }
    }

    /**
     * Helper class
     */
    private class Group implements Comparable<Group> {
        int numUnits = 0;
        int hitPoints = 0;
        int attackDamage = 0;
        int attackType = 0;
        int initiative = 0;
        int weaknesses = 0;
        int immunities = 0;
        boolean isInfection;

        public Group(int numUnits, int hitPoints, int attackType, int initiative, boolean isInfection) {
            this.numUnits = numUnits;
            this.hitPoints = hitPoints;
            this.attackType = attackType;
            this.initiative = initiative;
            this.isInfection = isInfection;
        }

        public int getEffectivePower() {
            return numUnits * attackDamage;
        }

        public boolean takeDamage(int effectiveDamage, int typeOfAttack) {
            // If immune to attack type, ignore all damage
            if((typeOfAttack & immunities) > 0) {
                return true;
            } else {
                // Damage is double for weakness
                if((typeOfAttack & weaknesses) > 0) {
                    effectiveDamage *= 2;
                }
            }

            // Do damage while a whole unit can be killed
            while(effectiveDamage >= hitPoints) {
                numUnits--;
                effectiveDamage -= hitPoints;
            }
            return numUnits > 0;
        }

        public int compareTo(Group grp) {
            return Integer.compare(initiative, grp.initiative);
        }
    }
}
