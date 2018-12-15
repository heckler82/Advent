/**
 * Solution for Day08 of Advent of Code
 *
 * @author Evan Foley
 * @version 08 Dec 2018
 */
public class Nodes extends AdventMaster {
    private Node root;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Nodes(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        System.out.printf("The total sum of the meta data is %d%n", getTotalMetaData());
        System.out.printf("The value of the root node is %d%n", getRootNodeValue(root));
    }

    /**
     * Gets the sum of all the meta data in the nodes
     *
     * @return The sum of all meta data in the nodes
     */
    private int getTotalMetaData() {
        // Get numerical input
        int[] data = convertInput(input[0].split("\\s+"));
        // Parse the data into nodes, total meta data is calculated during parsing
        return parseNodes(data);
    }

    /**
     * Gets the value of the root node
     *
     * @param root The root node
     * @return The value of the root node
     */
    private int getRootNodeValue(Node root) {
        // If no children, return the total meta data of the node
        if(root.children.length == 0) {
            return root.getTotalMeta();
        }
        // Keep a running total
        int total = 0;
        // Utilize the meta data values as indexes
        for(int i = 0; i < root.meta.length; i++) {
            int childIndex = root.meta[i];
            // Use only if the current index is within the bounds of the children array
            if(childIndex <= root.children.length) {
                // Add the value of the child node to the running total
                total += getRootNodeValue(root.children[childIndex - 1]);
            }
        }
        // Return the running total
        return total;
    }

    /**
     * Parses numerical data into nodes
     *
     * @param data The input data
     * @return The total value of all the meta data
     */
    private int parseNodes(int[] data) {
        int[] total = new int[1];
        parseNodes(data, 0, null, total);
        return total[0];
    }

    /**
     * Parses nodes from the input data
     *
     * @param data The input data
     * @param startIndex The starting index
     * @param parent The parent node
     * @param total The running total reference
     * @return The updated starting index
     */
    private int parseNodes(int[] data, int startIndex, Node parent, int[] total) {
        // Get the node parameters and create a new node
        int numChildren = data[startIndex];
        int childIndex = numChildren;
        int numMeta = data[startIndex + 1];
        Node root = new Node(numChildren, numMeta);
        // While the node has available children, parse additional nodes
        while(childIndex > 0) {
            startIndex = parseNodes(data, startIndex + 2, root, total);
            childIndex--;
        }
        // Get the meta data values for the node, update running meta data total
        for(int i = 0; i < root.meta.length; i++) {
            root.meta[i] = data[startIndex + 2 + i];
            total[0] += root.meta[i];
        }
        // Add this node to its parent
        if(parent != null) {
            parent.addChild(root);
        }
        else {
            this.root = root;
        }
        // Return the updated starting index value
        return startIndex + numMeta;
    }

    /**
     * Converts the string input to integer data
     *
     * @param input The string input
     * @return The data in integer form
     */
    private int[] convertInput(String[] input) {
        int[] list = new int[input.length];
        // Convert each string input to integer input
        for(int i = 0; i < input.length; i++) {
            list[i] = Integer.parseInt(input[i]);
        }
        return list;
    }

    /**
     * Helper class
     */
    private class Node {
        Node[] children;
        int[] meta;
        int currentChild;
        int currentMeta;

        public Node(int numChildren, int numMeta) {
            children = new Node[numChildren];
            meta = new int[numMeta];
            currentChild = 0;
            currentMeta = 0;
        }

        public void addChild(Node child) {
            children[currentChild++] = child;
        }

        public void addMetaData(int metaData) {
            meta[currentMeta++] = metaData;
        }

        public int getTotalMeta() {
            int total = 0;
            for(int i = 0; i < meta.length; i++) {
                total += meta[i];
            }
            return total;
        }
    }
}
