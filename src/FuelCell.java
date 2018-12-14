/**
 * Solution for Day11 of Advent of Code
 *
 * @author Evan Foley
 * @version 14 Dec 2018
 */
public class FuelCell extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public FuelCell(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Runs the program
     */
    public void run() {
        // Get the serial number
        int serNo = Integer.parseInt(input[0]);
        // Create the power grid
        int[][] grid = getPowerGrid(serNo);
        findBestSubMatrix(grid);
    }

    /**
     * Finds the 3 x 3 matrix with the largest value
     *
     * @param grid The power grid
     */
    private void findBestSubMatrix(int[][] grid) {
        // Track current max and top left position
        int maxSum = Integer.MIN_VALUE;
        int xPos = 0;
        int yPos = 0;
        int bestSize = 0;

        // Calculate the value of all 3x3 sub matrices and find greatest sum
        for(int size = 0; size < grid.length; size++) {
            System.out.println("Tick: " + size);
            for (int i = 0; i < grid.length - size; i++) {
                for (int j = 0; j < grid[i].length - size; j++) {
                    int sum = sumMatrix(grid, j, i, size);
                    if (sum > maxSum) {
                        maxSum = sum;
                        xPos = j + 1;
                        yPos = i + 1;
                        bestSize = size;
                    }
                }
            }
        }

        System.out.printf("The sub matrix with the best power starts at (%d, %d) and is size %d%n", xPos, yPos, bestSize);
    }

    /**
     * Computes the sum of a sub matrix
     *
     * @param grid The main grid
     * @param startX The starting x index
     * @param startY The starting y index
     * @param size The size of the sub matrix
     * @return The sum of the sub matrix
     */
    private int sumMatrix(int[][] grid, int startX, int startY, int size) {
        int res = 0;
        for(int row = startY; row < startY + size; row++) {
            for(int col = startX; col < startX + size; col++) {
                res += grid[row][col];
            }
        }
        return res;
    }

    /**
     * Gets the grid with power levels set
     *
     * @param serialNumber The serial number of the grid
     * @return The grid
     */
    private int[][] getPowerGrid(int serialNumber) {
        // Create the power grid
        int[][] grid = new int[300][300];
        // Fill in the fuel cell power values
        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[y].length; x++) {
                // Rack id is x coordinate + 10
                int rackID = x + 11;
                // Initial power level is rack id * y coordinate
                int pLevel = rackID * (y + 1);
                // Increase power level by serial number
                pLevel += serialNumber;
                // Set power level to pLevel * rackID
                pLevel *= rackID;
                // Strip out the hundreds digit
                pLevel = (pLevel / 100) % 10;
                // subtract 5 for final value
                grid[y][x] = pLevel - 5;
            }
        }
        return grid;
    }
}
