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
    Graph g;
    int test = 0;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public MasterMap(String fileName) {
        super(fileName);
        g = new Graph();
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    protected void task() {
        // Create initial room
        Node root = new Node(NODE_ID, 0, 0);
        String routes = input[0].substring(1, input[0].length() - 1);
        routes = stripOptionalRoutes(routes);
        createGraph(routes, 0, root);

        // Find the paths here
        int longestPath = 0;
        for(Node n : g.edges.keySet()) {
            if(n != root) {
                BFSSearch s = new BFSSearch(g, root);
                int cost = s.getPathCost(n);
                if(cost > longestPath) {
                    longestPath = cost;
                    System.out.println(cost);
                }
            }
        }

        System.out.printf("The longest path is %d doors away%n", longestPath);
    }

    /**
     * Creates the graph
     *
     * @param routes The paths to create the graph
     * @param index The current index
     * @param root The root node
     * @return The position in the graph
     */
    private int createGraph(String routes, int index, Node root){
        Node next = root;
        char c;
        String cardinal = "NSEW";

        // Loop until the end of the input is detected
        while(index < routes.length()) {
            c = routes.charAt(index);
            switch(c) {
                case 'N':
                case 'S':
                case 'E':
                case 'W':
                    next = root.move(cardinal.indexOf(c));
                    g.addEdge(root, next);
                    break;
                case '(':
                    index = createGraph(routes, index + 1, root);
                    while(routes.charAt(index) == '|') {
                        index = createGraph(routes, index + 1, root);
                    }
                    break;
                case '|':
                case ')':
                    return index;
            }
            index++;
            root = next;
        }
        return index;
    }

    private String stripOptionalRoutes(String input) {
        String newInput = input;
        String[] patterns = {"NEWS", "NWES", "NWSE", "NESW", "SWEN", "SEWN", "SENW", "SWNE", "ENSW", "ESNW", "ENWS", "ESWN", "WNSE", "WSNE", "WNES", "WSEN"};

        for(String pat : patterns) {
            newInput = newInput.replace(pat, "");
        }

        return newInput;
    }

    /**
     * Helper class
     */
    private class Graph {
        Map<Node, List<Node>> edges;
        Map<Point, Node> vertices;

        public Graph() {
            edges = new HashMap<>();
            vertices = new HashMap<>();
        }

        public void addEdge(Node from, Node to) {
            // Ensure from node is not already existing
            if(vertices.get(to.p) != null) {
                NODE_ID--;
                to = vertices.get(to.p);
            }

            // Add first edge
            if(edges.containsKey(from)) {
                edges.get(from).add(to);
            } else {
                vertices.put(from.p, from);
                ArrayList<Node> connected = new ArrayList<>();
                connected.add(to);
                edges.put(from, connected);
            }

            // Add second edge
            if(edges.containsKey(to)) {
                edges.get(to).add(from);
            } else {
                vertices.put(to.p, to);
                ArrayList<Node> connected = new ArrayList<>();
                connected.add(from);
                edges.put(to, connected);
            }
        }

        public int getNumberOfVertices() {
            return vertices.values().size();
        }
    }

    private class BFSSearch {
        BFSNode[] nodes;

        public BFSSearch(Graph graph, Node from) {
            nodes = new BFSNode[graph.getNumberOfVertices()];

            for(Node n : graph.edges.keySet()) {
                nodes[n.id] = new BFSNode(n);
            }

            Queue<BFSNode> q = new PriorityQueue<>();
            BFSNode current = nodes[from.id];
            current.score = 0;
            current.visited = true;
            q.offer(current);

            while(!q.isEmpty()) {
                current = q.poll();

                for(Node neighbor : graph.edges.get(current.n)) {
                    BFSNode neighborNode = nodes[neighbor.id];
                    int newScore = current.score + 1;
                    if(!neighborNode.visited) {
                        neighborNode.score = newScore;
                        neighborNode.visited = true;
                        q.offer(neighborNode);
                    }
                }
            }
        }

        public int getPathCost(Node to) {
            return nodes[to.id].score;
        }
    }

    private class BFSNode implements Comparable<BFSNode> {
        int score;
        boolean visited;
        BFSNode prev;
        Node n;

        public BFSNode(Node n) {
            this(n, Integer.MIN_VALUE);
        }

        public BFSNode(Node n, int score) {
            this.score = score;
            visited = false;
            this.n = n;
        }

        public int compareTo(BFSNode bfs) {
            if(score < bfs.score) {
                return -1;
            }
            if(score == bfs.score) {
                return 0;
            }
            return 1;
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
                dy = direction == 0 ? 1 : -1;
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
