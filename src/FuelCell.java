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
     * Accomplishes the task for this day
     */
    public void task() {
        // Get the serial number
        int serNo = Integer.parseInt(input[0]);
        // Create the power grid
        int[][] grid = getPowerGrid(serNo);
        int[] max = findBestSubMatrix(grid, 3);
        System.out.printf("The largest 3 x 3 sub matrix begins at (%d, %d) with a total power of %d%n", max[0], max[1], max[3]);
        max = new int[]{0, 0, 0, 0};
        for(int i = 1; i <= grid.length; i++) {
            int[] temp = findBestSubMatrix(grid, i);
            if (temp[3] > max[3]) {
                max = temp;
            }
        }
        System.out.printf("The largest available sub matrix begins at (%d, %d) and is size %d with a total power of %d%n", max[0], max[1], max[2], max[3]);
    }

    /**
     * Finds the 3 x 3 matrix with the largest value
     *
     * @param grid The power grid
     * @return The values of the max sub matrix
     */
    private int[] findBestSubMatrix(int[][] grid, int size) {
        // Pre-process the input grid
        int[][] sum = new int[grid.length][grid.length];
        sum[0][0] = grid[0][0];

        // Pre-process first row
        for(int j = 1; j < grid.length; j++) {
            sum[0][j] = grid[0][j] + sum[0][j - 1];
        }

        // Pre-process first column
        for(int i = 1; i < grid.length; i++) {
            sum[i][0] = grid[i][0] + sum[i - 1][0];
        }

        // Pre-process rest of the grid
        for(int i = 1; i < grid.length; i++) {
            for(int j = 1; j < grid.length; j++) {
                sum[i][j] = grid[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
            }
        }

        // Tracker values
        int total = 0;
        int max = Integer.MIN_VALUE;
        int xPos = 0;
        int yPos = 0;

        // Find the max sub matrix
        for(int i = size - 1; i < grid.length; i++) {
            for(int j = size - 1; j < grid.length; j++) {
                // i and j are the bottom right corner coordinates of sub matrix of size k
                total = sum[i][j];
                if(i - size >= 0) {
                    total = total - sum[i - size][j];
                }

                if(j - size >= 0) {
                    total = total - sum[i][j - size];
                }

                if(i - size >= 0 && j - size >= 0) {
                    total = total + sum[i - size][j - size];
                }

                if(total > max) {
                    max = total;
                    xPos = j - size + 2;
                    yPos = i - size + 2;
                }
            }
        }
        return new int[]{xPos, yPos, size, max};
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
