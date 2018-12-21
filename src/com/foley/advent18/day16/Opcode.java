package com.foley.advent18.day16;

import com.foley.advent18.AdventMaster;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day16 of Advent of Code
 *
 * @author Evan Foley
 * @version 17 Dec 2018
 */
public class Opcode extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Opcode(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    protected void task() {
        String[][] taskInputs = separateTaskInput(input);
        task1(taskInputs[0]);
        task2(taskInputs[1]);
    }

    /**
     * Completes the first task for the day
     *
     * @param input The input
     */
    private void task1(String[] input) {
        int applicableOpcodes = 0;
        // Pull in each 3 line test
        for(int index = 0; index < input.length; index += 3) {
            int[] starting = extractArray(input[index]);
            int[] op = extractArray(input[index + 1]);
            int[] goal = extractArray(input[index + 2]);
            // Initialize the registers to the startin gpoint
            Registers reg = new Registers(starting);
            // If this opcode tests out for more than 3 potential operations, increment the count
            if (reg.runTests(op, goal)) {
                applicableOpcodes++;
            }
        }
        // Print the count
        System.out.printf("The number of samples that behave like 3 or more opcodes is %d%n", applicableOpcodes);
    }

    /**
     * Completes the second task for the day
     *
     * @param input The input
     */
    private void task2(String[] input) {
        // Starting register
        Registers reg = new Registers(new int[]{0, 0, 0, 0});
        // Parse each op and execute it
        for(String str : input) {
            int[] op = extractArray(str);
            // Determine operation based on opcode
            int opcode = op[0];
            switch(opcode) {
                case 0:
                    reg.eqir(op[1], op[2], op[3]);
                    break;
                case 1:
                    reg.seti(op[1], op[2], op[3]);
                    break;
                case 2:
                    reg.eqri(op[1], op[2], op[3]);
                    break;
                case 3:
                    reg.eqrr(op[1], op[2], op[3]);
                    break;
                case 4:
                    reg.addi(op[1], op[2], op[3]);
                    break;
                case 5:
                    reg.setr(op[1], op[2], op[3]);
                    break;
                case 6:
                    reg.gtrr(op[1], op[2], op[3]);
                    break;
                case 7:
                    reg.gtri(op[1], op[2], op[3]);
                    break;
                case 8:
                    reg.muli(op[1], op[2], op[3]);
                    break;
                case 9:
                    reg.bori(op[1], op[2], op[3]);
                    break;
                case 10:
                    reg.bani(op[1], op[2], op[3]);
                    break;
                case 11:
                    reg.borr(op[1], op[2], op[3]);
                    break;
                case 12:
                    reg.gtir(op[1], op[2], op[3]);
                    break;
                case 13:
                    reg.banr(op[1], op[2], op[3]);
                    break;
                case 14:
                    reg.addr(op[1], op[2], op[3]);
                    break;
                case 15:
                    reg.mulr(op[1], op[2], op[3]);
                    break;
            }
        }
        // Operations are complete
        System.out.printf("The value contained in register 0 after all operations have been performed is %d%n", reg.current[0]);
    }

    /**
     * Extracts the array information from the string
     *
     * @param reg The string
     * @return The array made up of the digits from the string
     */
    private int[] extractArray(String reg) {
        int[] registers = new int[4];
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(reg);
        int index = 0;
        while(matcher.find()) {
            registers[index] = Integer.parseInt(matcher.group());
            index++;
        }
        return registers;
    }

    /**
     * Separates the task inputs
     *
     * @param input The input
     * @return The input separated by task
     */
    private String[][] separateTaskInput(String[] input) {
        String[][] tasks = new String[2][];
        List<String> task1 = new ArrayList<>();
        List<String> task2 = new ArrayList<>();
        int index = 0;
        String line = input[index];
        // Pull task 1 input in groups of 3 lines until encountering a blank line
        while(!"".equals(line)) {
            task1.add(line);
            task1.add(input[++index]);
            task1.add(input[++index]);
            // Skip blank line between inputs
            index += 2;
            line = input[index];
        }
        tasks[0] = task1.toArray(new String[task1.size()]);
        // Skip blank lines separating two inputs
        index += 2;
        // Pull in remaining input for task 2
        while(index < input.length) {
            task2.add(input[index]);
            index++;
        }
        tasks[1] = task2.toArray(new String[task2.size()]);
        return tasks;
    }

    /**
     * Holds registers
     */
    private class Registers {
        int[] registers;
        int[] current = new int[4];

        /**
         * Creates a new register object
         *
         * @param registers The starting state of the registers
         */
        public Registers(int[] registers) {
            this.registers = registers;
            reset();
        }

        public boolean runTests(int[] opset, int[] goal) {
            int matches = 0;
            addr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            addi(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            mulr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            muli(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            banr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            bani(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            borr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            bori(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            setr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            seti(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            gtir(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            gtri(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            gtrr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            eqir(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            reset();
            eqri(opset[1], opset[2], opset[3]);
            if(match(goal)) {
               matches++;
            }
            reset();
            eqrr(opset[1], opset[2], opset[3]);
            if(match(goal)) {
                matches++;
            }
            return matches > 2  ;
        }

        /**
         * Resets the current registers to the starting state
         */
        public void reset() {
            current[0] = registers[0];
            current[1] = registers[1];
            current[2] = registers[2];
            current[3] = registers[3];
        }

        private boolean match(int[] goal) {
            return (current[0] == goal[0]) && (current[1] == goal[1]) && (current[2] == goal[2]) && (current[3] == goal[3]);
        }

        /**
         * Stores into register c the result of adding register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void addr(int a, int b, int c) {
            current[c] = current[a] + current[b];
        }

        /**
         * Stores into register c the result of adding register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void addi(int a, int b, int c) {
            current[c] = current[a] + b;
        }

        /**
         * Stores into register c the result of multiplying register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void mulr(int a, int b, int c) {
            current[c] = current[a] * current[b];
        }

        /**
         * Stores into register c the result of multiplying register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void muli(int a, int b, int c) {
            current[c] = current[a] * b;
        }

        /**
         * Stores into register c the result of the bitwise AND register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void banr(int a, int b, int c) {
            current[c] = current[a] & current[b];
        }

        /**
         * Stores into register c the result of the bitwise AND of register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void bani(int a, int b, int c) {
            current[c] = current[a] & b;
        }

        /**
         * Stores into register c the result of the bitwise OR register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void borr(int a, int b, int c) {
            current[c] = current[a] | current[b];
        }

        /**
         * Stores into register c the result of the bitwise OR of register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void bori(int a, int b, int c) {
            current[c] = current[a] | b;
        }

        /**
         * Copies the contents of register a into register c (register b is ignored)
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void setr(int a, int b, int c) {
            current[c] = current[a];
        }

        /**
         * Stores value a into register c (register b is ignored)
         *
         * @param a Value a
         * @param b Register b
         * @param c Register c
         */
        private void seti(int a, int b, int c) {
            current[c] = a;
        }

        /**
         * Sets register c to 1 if value a is greater than register b. Otherwise, register c is set to 0
         *
         * @param a Value a
         * @param b Register b
         * @param c Register c
         */
        private void gtir(int a, int b, int c) {
            current[c] = (a > current[b]) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is greater than value b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void gtri(int a, int b, int c) {
            current[c] = (current[a] > b) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is greater than register b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void gtrr(int a, int b, int c) {
            current[c] = (current[a] > current[b]) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if value a is equal to register b. Otherwise, register c is set to 0
         *
         * @param a Value a
         * @param b Register b
         * @param c Register c
         */
        private void eqir(int a, int b, int c) {
            current[c] = (a == current[b]) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is equal to value b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void eqri(int a, int b, int c) {
            current[c] = (current[a] == b) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is equal to register b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void eqrr(int a, int b, int c) {
            current[c] = (current[a] == current[b]) ? 1 : 0;
        }
    }
}
