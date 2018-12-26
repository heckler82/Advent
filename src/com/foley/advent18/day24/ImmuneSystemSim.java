package com.foley.advent18.day24;

import com.foley.advent18.AdventMaster;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description goes here
 *
 * @author Evan Foley
 * @version 24 Dec 2018
 */
public class ImmuneSystemSim extends AdventMaster {
    int infectCount = 0;
    int immuneCount = 0;

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
        for(int boost = 0; boost > Integer.MIN_VALUE; boost++) {
            System.out.printf("Boost: %d%n", boost);
            Group[][] groups = parseInput(input);
            for (Group grp : groups[2]) {
                if (grp.isAlive) {
                    grp.attackDamage += boost;
                }
            }
            while (infectCount > 0 && immuneCount > 0) {
                // Print out current status
                /**System.out.println("Immune System:");
                for (Group grp : groups[2]) {
                    if (grp.isAlive) {
                        System.out.printf("Group %d contains %d units%n", grp.id, grp.numUnits);
                    }
                }
                System.out.println("Infection:");
                for (Group grp : groups[1]) {
                    if (grp.isAlive) {
                        System.out.printf("Group %d contains %d units%n", grp.id, grp.numUnits);
                    }
                }
                System.out.println();*/
                // Sort by effective power
                Arrays.sort(groups[0]);
                // Target selection
                Group[] main = groups[0];
                int numTargeted = 0;
                for (Group grp : main) {
                    // Only process if alive
                    if (grp.isAlive) {
                        Group[] enemy;
                        // Select the appropriate list
                        if (grp.isInfection) {
                            enemy = groups[2];
                        } else {
                            enemy = groups[1];
                        }
                        Group target = null;
                        int highestDamage = 0;
                        for (Group potential : enemy) {
                            if (potential.isAlive && !potential.isTargeted) {
                                // If immune to attack type, ignore all damage
                                int totalAttackDamage = grp.getEffectivePower();
                                if ((grp.attackType & potential.immunities) > 0) {
                                    continue;
                                } else {
                                    // Damage is double for weakness
                                    if ((grp.attackType & potential.weaknesses) > 0) {
                                        totalAttackDamage *= 2;
                                    }
                                }
                                // Match target if found
                                if (totalAttackDamage > highestDamage) {
                                    highestDamage = totalAttackDamage;
                                    target = potential;
                                } else {
                                    if (totalAttackDamage == highestDamage) {
                                        // Break tie utilizing effectivePower
                                        if (potential.getEffectivePower() > target.getEffectivePower()) {
                                            target = potential;
                                        } else {
                                            // If still tied, break with initiative
                                            if (potential.getEffectivePower() == target.getEffectivePower() && potential.initiative > target.initiative) {
                                                target = potential;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Target is either found or not
                        if (target != null) {
                            // Mark as claimed
                            target.isTargeted = true;
                            grp.target = target;
                            numTargeted++;
                        }
                    }
                }
                // Target selection phase over, begin combat
                // Sort by initiative
                Arrays.sort(main, new InitiativeComparator());
                int noHits = 0;
                int attempted = 0;
                for (Group grp : main) {
                    // Only process if alive
                    if (grp.isAlive) {
                        // If group found a target, attack it
                        if (grp.target != null) {
                            attempted++;
                            int prior = grp.target.numUnits;
                            if (!grp.target.takeDamage(grp.getEffectivePower(), grp.attackType)) {
                                if (grp.target.isInfection) {
                                    infectCount--;
                                } else {
                                    immuneCount--;
                                }
                            }
                            prior = grp.target.numUnits - prior;
                            if(prior == 0) {
                                noHits++;
                            }
                            grp.target.isTargeted = false;
                            grp.target = null;
                        }
                    } else {
                        // Unlock target if this group was killed before it could attack
                        if (grp.target != null) {
                            grp.target.isTargeted = false;
                            grp.target = null;
                        }
                    }
                }
                if(noHits == attempted) {
                    System.out.println("================================================== Stalemate ==================================================");
                    break;
                }
            }
            Group[] remaining;
            String winning;
            if (infectCount > 0) {
                remaining = groups[1];
                winning = "Infection";
            } else {
                remaining = groups[2];
                winning = "Immune System";
            }
            int total = 0;
            for (Group grp : remaining) {
                if (grp.isAlive) {
                    total += grp.numUnits;
                }
            }
            System.out.printf("The %s wins the fight. They have %d units left%n%n", winning, total);
            if("Immune System".equals(winning)) {
                break;
            }
        }
    }

    private Group[][] parseInput(String[] input) {
        ArrayList<ArrayList<Group>> groups = new ArrayList<>();
        groups.add(new ArrayList<Group>());
        groups.add(new ArrayList<Group>());
        groups.add(new ArrayList<Group>());
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
                continue;
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
                int id = isInfection ? groups.get(1).size() + 1 : groups.get(2).size() + 1;
                Group grp = new Group(id, dat[0], dat[1], attackType, dat[2], dat[3], isInfection);
                groups.get(0).add(grp);
                if(grp.isInfection) {
                    groups.get(1).add(grp);
                } else {
                    groups.get(2).add(grp);
                }
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
                int id = isInfection ? groups.get(1).size() + 1 : groups.get(2).size() + 1;
                Group grp = new Group(id, dat[0], dat[1], attackType, dat[2], dat[3], isInfection);
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
                groups.get(0).add(grp);
                if(grp.isInfection) {
                    groups.get(1).add(grp);
                } else {
                    groups.get(2).add(grp);
                }
            }
        }
        Group[][] newGroups = new Group[groups.size()][];
        for(int i = 0; i < newGroups.length; i++) {
            newGroups[i] = groups.get(i).toArray(new Group[groups.get(i).size()]);
        }
        infectCount = newGroups[1].length;
        immuneCount = newGroups[2].length;
        return newGroups;
    }

    /**
     * Helper class
     */
    private class Group implements Comparable<Group> {
        int id;
        int numUnits = 0;
        int hitPoints = 0;
        int attackDamage = 0;
        int attackType = 0;
        int initiative = 0;
        int weaknesses = 0;
        int immunities = 0;
        boolean isInfection;
        boolean isAlive;
        boolean isTargeted;
        Group target;

        public Group(int id, int numUnits, int hitPoints, int attackType, int attackDamage, int initiative, boolean isInfection) {
            this.id = id;
            this.numUnits = numUnits;
            this.hitPoints = hitPoints;
            this.attackType = attackType;
            this.attackDamage = attackDamage;
            this.initiative = initiative;
            this.isInfection = isInfection;
            this.isAlive = true;
            this.isTargeted = false;
            this.target = null;
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
            isAlive = numUnits > 0;
            isTargeted = false;
            return isAlive;
        }

        public int compareTo(Group grp) {
            int comp = Integer.compare(grp.getEffectivePower(), getEffectivePower());
            if(comp == 0) {
                return Integer.compare(grp.initiative, initiative);
            }
            return comp;
        }
    }

    private class InitiativeComparator implements Comparator<Group> {
        public int compare(Group u, Group v) {
            return Integer.compare(v.initiative, u.initiative);
        }
    }
}
