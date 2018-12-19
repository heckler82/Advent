import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for Day19 of Advent of Code
 *
 * @author Evan Foley
 * @version 19 Dec 2018
 */
public class FlowControl extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public FlowControl(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    protected void task() {
        getFinalRegisterValue(input);
    }

    /**
     * Performs actions on the registers based on the input
     *
     * @param input The input
     */
    private void getFinalRegisterValue(String[] input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input[0]);
        if(!matcher.find()) {
            throw new IllegalArgumentException("Unable to match numerical value for instruction register");
        }
        int ptrSeed = Integer.parseInt(matcher.group());
        Registers reg = new Registers(ptrSeed);

        // Cache instructions to prevent having to pull each time
        String[] ops = new String[input.length - 1];
        int[][] args = new int[input.length - 1][];
        for(int i = 0; i < input.length - 1; i++) {
            String line = input[i + 1];
            String op = line.substring(0, 4);
            int[] codes = extractArray(line);
            ops[i] = op;
            args[i] = codes;
        }

        // Go through each instruction and execute
        Set<Integer> set = new HashSet<>();
        int sum = 0;
        System.out.println("Begin Part 1");
        while (reg.instrptr < ops.length) {
            reg.perform(ops[reg.instrptr], args[reg.instrptr]);
            if(!set.add(reg.instrptr)) {
                sum = sumFactors(reg.registers[3]);
                break;
            }
        }
        System.out.printf("The value in register zero is %d%n%n", sum);
        reg.reset(0, 1, ptrSeed);
        set.clear();
        // Part 2
        System.out.println("Begin Part 2");
        while (reg.instrptr < ops.length) {
            reg.perform(ops[reg.instrptr], args[reg.instrptr]);
            if(!set.add(reg.instrptr)) {
                sum = sumFactors(reg.registers[3]);
                break;
            }
        }
        System.out.printf("The value in register zero when initialized to 1 is %d%n", sum);
    }

    /**
     * Gets the sum of all the factors of value
     *
     * @param value The value
     * @return The sum of value's factors
     */
    private int sumFactors(int value) {
        int sum = 0;
        for(int i = 1; i <= value; i++) {
            sum += value % i == 0 ? i : 0;
        }
        return sum;
    }

    /**
     * Extracts the array information from the string
     *
     * @param reg The string
     * @return The array made up of the digits from the string
     */
    private int[] extractArray(String reg) {
        int[] registers = new int[3];
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
     * Holds registers
     */
    private class Registers {
        int[] registers;
        int instreg;
        int instrptr;
        final Map<String, Consumer<int[]>> opCodes;

        /**
         * Creates a new register object
         */
        public Registers(int instructionRegister) {
            instreg = instructionRegister;
            instrptr = 0;
            registers = new int[6];
            //registers[0] = 1;
            opCodes = new HashMap<>();
            // Fill out instructions
            opCodes.put("addr", (int[] args) -> this.addr(args[0], args[1], args[2]));
            opCodes.put("addi", (int[] args) -> this.addi(args[0], args[1], args[2]));
            opCodes.put("mulr", (int[] args) -> this.mulr(args[0], args[1], args[2]));
            opCodes.put("muli", (int[] args) -> this.muli(args[0], args[1], args[2]));
            opCodes.put("banr", (int[] args) -> this.banr(args[0], args[1], args[2]));
            opCodes.put("bani", (int[] args) -> this.bani(args[0], args[1], args[2]));
            opCodes.put("borr", (int[] args) -> this.borr(args[0], args[1], args[2]));
            opCodes.put("bori", (int[] args) -> this.bori(args[0], args[1], args[2]));
            opCodes.put("setr", (int[] args) -> this.setr(args[0], args[1], args[2]));
            opCodes.put("seti", (int[] args) -> this.seti(args[0], args[1], args[2]));
            opCodes.put("gtir", (int[] args) -> this.gtir(args[0], args[1], args[2]));
            opCodes.put("gtri", (int[] args) -> this.gtri(args[0], args[1], args[2]));
            opCodes.put("gtrr", (int[] args) -> this.gtrr(args[0], args[1], args[2]));
            opCodes.put("eqir", (int[] args) -> this.eqir(args[0], args[1], args[2]));
            opCodes.put("eqri", (int[] args) -> this.eqri(args[0], args[1], args[2]));
            opCodes.put("eqrr", (int[] args) -> this.eqrr(args[0], args[1], args[2]));
        }

        /**
         * Resets all register values to 0 and resets the instruction pointer and register
         *
         * @param ptrSeed The instruction pointer
         */
        public void reset(int ptrSeed) {
            instreg = ptrSeed;
            instrptr = 0;
            registers[0] = 0;
            registers[1] = 0;
            registers[2] = 0;
            registers[3] = 0;
            registers[4] = 0;
            registers[5] = 0;
        }
        /**
         * Resets all register values to 0, resets the instruction pointer and register, and seeds one of the registers with an initial value
         *
         * @param seedRegister The register to seed
         * @param seed The value to seed the register with
         * @param ptrSeed The instruction pointer
         */
        public void reset(int seedRegister, int seed, int ptrSeed) {
            reset(ptrSeed);
            registers[seedRegister] = seed;
        }

        /**
         * Performs the requested operation with the requested arguments
         *
         * @param op The operation
         * @param args The arguments
         */
        public void perform(String op, int[] args) {
            if(opCodes.containsKey(op)) {
                //System.out.printf("ip=%d [%d, %d, %d, %d, %d, %d] %s %d %d %d ", instrptr, registers[0], registers[1], registers[2], registers[3], registers[4], registers[5],
                //        op, args[0], args[1], args[2]);
                registers[instreg] = instrptr;
                opCodes.get(op).accept(args);
                instrptr = registers[instreg] + 1;
                //System.out.printf("[%d, %d, %d, %d, %d, %d]%n", registers[0], registers[1], registers[2], registers[3], registers[4], registers[5]);
            }
        }

        /**
         * Stores into register c the result of adding register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void addr(int a, int b, int c) {
            registers[c] = registers[a] + registers[b];
        }

        /**
         * Stores into register c the result of adding register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void addi(int a, int b, int c) {
            registers[c] = registers[a] + b;
        }

        /**
         * Stores into register c the result of multiplying register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void mulr(int a, int b, int c) {
            registers[c] = registers[a] * registers[b];
        }

        /**
         * Stores into register c the result of multiplying register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void muli(int a, int b, int c) {
            registers[c] = registers[a] * b;
        }

        /**
         * Stores into register c the result of the bitwise AND register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void banr(int a, int b, int c) {
            registers[c] = registers[a] & registers[b];
        }

        /**
         * Stores into register c the result of the bitwise AND of register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void bani(int a, int b, int c) {
            registers[c] = registers[a] & b;
        }

        /**
         * Stores into register c the result of the bitwise OR register a and register b
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void borr(int a, int b, int c) {
            registers[c] = registers[a] | registers[b];
        }

        /**
         * Stores into register c the result of the bitwise OR of register a and value b
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void bori(int a, int b, int c) {
            registers[c] = registers[a] | b;
        }

        /**
         * Copies the contents of register a into register c (register b is ignored)
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void setr(int a, int b, int c) {
            registers[c] = registers[a];
        }

        /**
         * Stores value a into register c (register b is ignored)
         *
         * @param a Value a
         * @param b Register b
         * @param c Register c
         */
        private void seti(int a, int b, int c) {
            registers[c] = a;
        }

        /**
         * Sets register c to 1 if value a is greater than register b. Otherwise, register c is set to 0
         *
         * @param a Value a
         * @param b Register b
         * @param c Register c
         */
        private void gtir(int a, int b, int c) {
            registers[c] = (a > registers[b]) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is greater than value b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void gtri(int a, int b, int c) {
            registers[c] = (registers[a] > b) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is greater than register b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void gtrr(int a, int b, int c) {
            registers[c] = (registers[a] > registers[b]) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if value a is equal to register b. Otherwise, register c is set to 0
         *
         * @param a Value a
         * @param b Register b
         * @param c Register c
         */
        private void eqir(int a, int b, int c) {
            registers[c] = (a == registers[b]) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is equal to value b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Value b
         * @param c Register c
         */
        private void eqri(int a, int b, int c) {
            registers[c] = (registers[a] == b) ? 1 : 0;
        }

        /**
         * Sets register c to 1 if register a is equal to register b. Otherwise, register c is set to 0
         *
         * @param a Register a
         * @param b Register b
         * @param c Register c
         */
        private void eqrr(int a, int b, int c) {
            registers[c] = (registers[a] == registers[b]) ? 1 : 0;
        }
    }
}
