package com.foley.advent18.day24;

import com.foley.advent18.AdventMaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Group[] groups = parseInput(input);
        int test = 0;
    }

    private Group[] parseInput(String[] input) {
        ArrayList<Group> groups = new ArrayList<>();
        Map<String, Integer> attackTypes = new HashMap<>();
        attackTypes.put("cold", 1);
        attackTypes.put("fire", 2);
        attackTypes.put("slashing", 4);
        attackTypes.put("radiation", 8);
        attackTypes.put("bludgeoning", 16);

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
                i++;
                // Change team
                isInfection = !isInfection;
            }
            // Split by weaknesses/immunities
            String[] part = str.split("\\)");
            // No weaknesses or immunities
            if(part.length < 2) {
                String mainPart = part[0];
                // Pull out main values here
                Pattern digitPattern = Pattern.compile("\\d+");
                Matcher digitMatcher = digitPattern.matcher(mainPart);
                int[] dat = new int[4];
                int index = 0;
                while(digitMatcher.find()) {
                    dat[index] = Integer.parseInt(digitMatcher.group());
                    index++;
                }
                String[] attackString = mainPart.split(" damage ");
                String key = attackString[0].substring(attackString[0].lastIndexOf(' ') + 1);
                int attackType = attackTypes.get(key);
                Group grp = new Group(dat[0], dat[1], attackType, dat[2], dat[3], isInfection);
                groups.add(grp);
            } else {
                String backPart = part[1];
                // Pull out weaknesses/immunities
                String[] part2 = part[0].split("\\(");
                String frontPart = part2[0];
                // Pull out main values here
                Pattern digitPattern = Pattern.compile("\\d+");
                Matcher digitMatcher = digitPattern.matcher(frontPart);
                int[] dat = new int[4];
                int index = 0;
                while(digitMatcher.find()) {
                    dat[index] = Integer.parseInt(digitMatcher.group());
                    index++;
                }
                String[] attackString = backPart.split(" damage ");
                String key = attackString[0].substring(attackString[0].lastIndexOf(' ') + 1);
                int attackType = attackTypes.get(key);
                digitMatcher = digitPattern.matcher(backPart);
                while(digitMatcher.find()) {
                    dat[index] = Integer.parseInt(digitMatcher.group());
                    index++;
                }
                Group grp = new Group(dat[0], dat[1], attackType, dat[2], dat[3], isInfection);
                // Determine if there are multiple parts inside the parentheses
                String[] part3 = part2[1].split("; ++");
                // Work each token that was parsed (either 1 or 2)
                for(String tkn : part3) {
                    String[] types = tkn.split(", ++");
                    boolean isImmune = types[0].startsWith("immune");
                    int begin = isImmune ? 10 : 8;
                    // Pull value out from map
                    key = types[0].substring(begin);
                    int type = attackTypes.get(key);
                    // If necessary, parse more values here
                    for(int j = 1; j < types.length; j++) {
                        key = types[j];
                        type = type | attackTypes.get(key);
                    }
                    // Push the weaknesses/immunities to the group
                    if(isImmune) {
                        grp.immunities = type;
                    } else {
                        grp.weaknesses = type;
                    }
                }
                groups.add(grp);
            }

        }
        return groups.toArray(new Group[groups.size()]);
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

        public Group(int numUnits, int hitPoints, int attackType, int attackDamage, int initiative, boolean isInfection) {
            this.numUnits = numUnits;
            this.hitPoints = hitPoints;
            this.attackType = attackType;
            this.attackDamage = attackDamage;
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
