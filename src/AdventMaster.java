import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Provides a template for some of the Advent of code classes
 *
 * @author Evan Foley
 * @version 02 Dec 2018
 */
public abstract class AdventMaster {
    protected String fileName;
    protected String[] input;

    /**
     * Creates a new master object
     *
     * @param fileName The name of the input file
     */
    public AdventMaster(String fileName) {
        this.fileName = fileName;
        setup();
    }

    /**
     * Prepares the input for use
     *
     * @param scan The parsing object
     * @return The input from the parser
     */
    protected String[] processInputFile(Scanner scan) {
        ArrayList<String> arr = new ArrayList<>();
        while(scan.hasNextLine()) {
            arr.add(scan.nextLine());
        }
        return arr.toArray(new String[arr.size()]);
    }

    /**
     * Sets up the data
     */
    protected void setup() {
        File file = new File(fileName);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch(FileNotFoundException e) {
            System.err.println("[CRITICAL] The requested file cannot be found");
            System.err.println("[INFORMATION] The program will now terminate");
            System.exit(1);
        }
        input = processInputFile(scan);
    }
}
