package com.foley.advent18.day20;

import com.foley.advent18.AdventMaster;
import java.awt.Point;
import java.util.*;

/**
 * Solution for Day20 of Advent of Code
 *
 * @author Evan Foley
 * @version 20 Dec 2018
 */
public class MasterMap extends AdventMaster {
    static int NODE_ID = 0;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public MasterMap(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    protected void task() {
        // Create initial room
        Node root = new Node(NODE_ID, 0, 0);
        String routes = input[0].substring(1, input[0].length() - 1);
        Graph g = createGraph(routes, 0, root);

        // Find paths to all nodes from the specified location here
        Dijkstra dijkstra = new Dijkstra(g, root);

        int longestPath = 0;
        int minGrand = 0;
        for(Point p : g.vertices.keySet()) {
            int path = dijkstra.getPathCost(g.vertices.get(p));
            if(path > longestPath) {
                longestPath = path;
            }
            if(path >= 1000) {
                minGrand++;
            }
        }

        printGraph(g);

        System.out.printf("The longest path is %d doors away%n", longestPath);
        System.out.printf("The number of paths that are at least 1000 doors away is %d%n", minGrand);
    }

    /**
     * Creates the graph
     *
     * @param routes The paths to create the graph
     * @param index The current index
     * @param root The root node
     * @return The graph
     */
    private Graph createGraph(String routes, int index, Node root){
        Graph graph = new Graph(root);
        Node current = root;
        Node next;
        char c;
        String cardinal = "NSEW";
        Stack<Node> saved = new Stack<>();

        while(index < routes.length()) {
            c = routes.charAt(index);
            switch(c) {
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    next = current.move(cardinal.indexOf(c));
                    if(!graph.addVertex(next)) {
                        next = graph.vertices.get(next.p);
                        NODE_ID--;
                    }
                    graph.addEdge(current, next);
                    current = next;
                    break;
                case '(':
                    saved.push(current);
                    break;
                case '|':
                    current = saved.peek();
                    break;
                case ')':
                    current = saved.pop();
                    break;
            }
            index++;
        }
        return graph;
    }

    private void printGraph(Graph graph) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Determine the range of the graph
        int count = 0;
        for(Point p : graph.vertices.keySet()) {
            count++;
            int x = p.x;
            int y = p.y;
            // Check x values
            if(x < minX) {
                minX = x;
            } else {
                if(x > maxX) {
                    maxX = x;
                }
            }
            // Check y values
            if(y < minY) {
                minY = y;
            } else {
                if(y > maxY) {
                    maxY = y;
                }
            }
        }
        // Add one to account for 0, 0
        int dx = Math.abs(maxX - minX) + 1;
        int dy = Math.abs(maxY - minY) + 1;
        // Display offsets
        int ox = Math.abs(minX);
        int oy = Math.abs(minY);
        // Make the char array and populate with wall characters
        char[][] print = new char[(2 * dy) + 1][(2 * dx) + 1];
        for(char[] arr : print) {
            Arrays.fill(arr, '#');
        }
        // Loop through graph vertices again to mark locations properly
        for(Point p : graph.vertices.keySet()) {
            // Normalize values to array lengths (minimum values should be set to 0 relative to the array)
            int x = p.x + ox;
            int y = p.y + oy;
            // Change array value
            int px = (2 * x) + 1;
            int py = (2 * y) + 1;
            print[py][px] = '.';
            // Mark surrounding doors
            for(Point p2 : graph.edges.get(graph.vertices.get(p).id)) {
                // Change array value
                Node n = graph.vertices.get(p2);
                int nx = n.p.x - p.x;
                int ny = n.p.y - p.y;
                print[py + ny][px + nx] = (ny == 0) ? '|' : '-';
            }
        }
        // Mark the origin point
        int startX = graph.root.p.x + ox;
        int startY = graph.root.p.y + oy;
        print[(2 * startY) + 1][(2 * startX) + 1] = 'X';
        // Print the array
        for(int y = 0; y < print.length; y++) {
            for(int x = 0; x < print[y].length; x++) {
                System.out.print(print[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String stripOptionalRoutes(String input) {
        String newInput = input;

        newInput = newInput.replaceAll("\\([A-Z]{2,}.\\)", "");

        return newInput;
    }

    /**
     * Helper class
     */
    private class Graph {
        Map<Integer, List<Point>> edges;
        Map<Point, Node> vertices;
        Node root;

        public Graph() {
            edges = new HashMap<>();
            vertices = new HashMap<>();
        }

        public Graph(Node root) {
            this();
            this.root = root;
            addVertex(root);
        }

        public void addEdge(Node from, Node to) {
            // Add first edge
            if(edges.containsKey(from.id)) {
                List<Point> connected = edges.get(from.id);
                if(!connected.contains(to.p)) {
                    connected.add(to.p);
                }
            } else {
                ArrayList<Point> connected = new ArrayList<>();
                connected.add(to.p);
                edges.put(from.id, connected);
            }

            // Add second edge
            if(edges.containsKey(to.id)) {
                List<Point> connected = edges.get(to.id);
                if(!connected.contains(from.p)) {
                    connected.add(from.p);
                }
            } else {
                ArrayList<Point> connected = new ArrayList<>();
                connected.add(from.p);
                edges.put(to.id, connected);
            }
        }

        public void setRoot(Node root) {
            this.root = root;
        }

        public boolean addVertex(Node n) {
            if(!vertices.containsKey(n.p)) {
                vertices.put(n.p, n);
                return true;
            }
            return false;
        }

        public int getNumberOfVertices() {
            return vertices.values().size();
        }
    }

    private class Dijkstra {
        DNode[] nodes;
        ArrayList<Integer> counts;

        public Dijkstra(Graph graph, Node from) {
            // Setup available nodes
            nodes = new DNode[graph.getNumberOfVertices()];
            for(Point p : graph.vertices.keySet()) {
                Node n = graph.vertices.get(p);
                nodes[n.id] = new DNode(n);
            }

            // Get the starting node, update score, and queue up
            Queue<DNode> q = new PriorityQueue<>();
            DNode current = nodes[from.id];
            current.score = 0;
            current.visited = true;
            q.offer(current);

            // Continue to poll until all nodes visited
            while(!q.isEmpty()) {
                current = q.poll();

                // Process all neighbors of current
                for(Point p : graph.edges.get(current.n.id)) {
                    Node n = graph.vertices.get(p);
                    int newScore = current.score + 1;
                    DNode dn = nodes[n.id];
                    // If the new score is less than the old score, then update and place into the queue
                    if(newScore < dn.score) {
                        dn.score = newScore;
                        dn.prev = current;
                        dn.visited = true;
                        q.offer(dn);
                    }
                }
            }
        }

        public int getPathCost(Node to) {
            return nodes[to.id].score;
        }
    }

    private class DNode implements Comparable<DNode> {
        int score;
        DNode prev;
        boolean visited;
        Node n;

        public DNode(Node n) {
            this(n, Integer.MAX_VALUE);
        }

        public DNode(Node n, int score) {
            this.score = score;
            this.n = n;
        }

        public int compareTo(DNode dn) {
            return Integer.compare(dn.score, score);
        }
    }

    private class Node {
        int id;
        Point p;

        public Node(int id, int x, int y) {
            this.id = id;
            p = new Point(x, y);
            NODE_ID++;
        }

        public Node move(int direction) {
            int dx = 0;
            int dy = 0;
            if(direction < 2) {
                dy = direction == 0 ? -1 : 1;
            } else {
                dx = direction == 2 ? 1 : -1;
            }
            return new Node(NODE_ID, p.x + dx, p.y + dy);
        }

        @Override
        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }
            if(o == this) {
                return true;
            }
            if(!(o instanceof Node)) {
                return false;
            }
            Node n = (Node)o;
            return p.equals(n.p);
        }
    }
}
