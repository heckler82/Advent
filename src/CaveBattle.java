import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Solution for Day15 of Advent of Code
 *
 * @author Evan Foley
 * @version 16 Dec 2018
 */
public class CaveBattle extends AdventMaster {
    private List<GameObject> objects;
    private int elfCount = 0;
    private int goblinCount = 0;
    private int elfDamage = 23;
    private int goblinTeam = 1;
    private int elfTeam = 2;

    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public CaveBattle(String fileName) {
        super(fileName);
        objects = new ArrayList<>();
    }

    @Override
    /**
     * Accomplishes the task for this day
     */
    public void task() {
        int[][] map = parseMap(input);
        System.out.printf("The result of the combat is %d%n", battle(map));
    }

    private int battle(int[][] map) {
        // Initial round
        int round = 0;
        // Print out the initial state
        System.out.println("Initial State");
        printMap(map);
        // Get the initial goblin and elf counts
        int initialGoblins = goblinCount;
        int initialElves = elfCount;
        // Continue to wage war as long as both races still have soldiers to fight
        while(elfCount > 0 && goblinCount > 0) {
            boolean roundCompleted = true;
            // Preserve read order for turn priority
            Collections.sort(objects);
            for(GameObject obj : objects) {
                // If object is dead ignore it
                if(!obj.isAlive) {
                    continue;
                }
                // Identify all remaining objects of opposing team
                List<GameObject> targets = findTargets(objects, obj.team == 1 ? 2 : 1);
                if(!targets.isEmpty()) {
                    // If there is a target in range, attack it
                    List<GameObject> targetsInRange = getInRangeTargets(targets, obj);
                    if(targetsInRange.isEmpty()) {
                        // Begin move ops
                        // Find all open squares around targets (No open squares are on the edge on the map)
                        List<Point> openSquares = findOpenSquares(map, targets, obj);
                        if(!openSquares.isEmpty()) {
                            // Save squares that are reachable (Pathfinding)
                            Dijkstra dijkstra = new Dijkstra(map, openSquares, obj.x, obj.y);
                            // This is the point we want to move to
                            if(!dijkstra.nearest.isEmpty()) {
                                Point moveTo = dijkstra.constructPath(dijkstra.nearest.get(0), obj.x, obj.y);
                                obj.move(map, moveTo.x - obj.x, moveTo.y - obj.y);
                                // If in range of target, attack (select in range target utilizing read order)
                                // Identify all remaining objects of opposing team
                                targets = findTargets(objects, obj.team == 1 ? 2 : 1);
                                if (!targets.isEmpty()) {
                                    targetsInRange = getInRangeTargets(targets, obj);
                                    if (!targetsInRange.isEmpty()) {
                                        attackTargetInRange(targetsInRange, obj, map);
                                    }
                                }
                            }
                        }
                    } else {
                        attackTargetInRange(targetsInRange, obj, map);
                    }
                } else {
                    // No targets remaining, early exit
                    roundCompleted = false;
                    break;
                }
            }
            // Remove dead objects
            for(int i = objects.size() - 1; i >= 0; i--) {
                GameObject obj = objects.get(i);
                if(!obj.isAlive) {
                    objects.remove(i);
                }
            }
            // Complete round
            if(roundCompleted) {
                round++;
            }
        }
        // Get the total hit points from the remaining objects
        System.out.println("Final round: " + round);
        printMap(map);
        int answer = 0;
        for(GameObject object : objects) {
            if(object.isAlive) {
                answer += object.health;
            }
        }
        // Print out the battle stats
        System.out.printf("The elves have %d / %d soldiers remaining%n", elfCount, initialElves);
        System.out.printf("The goblins have %d / %d soldiers remaining%n", goblinCount, initialGoblins);
        // Get the completed rounds multiplied by the total remaining health
        return answer * round;
    }

    /**
     * Attacks a target that is in range
     *
     * @param targetsInRange The targets in range
     * @param obj The attacking object
     * @param map The map
     */
    private void attackTargetInRange(List<GameObject> targetsInRange, GameObject obj, int[][] map) {
        // Select target with lowest remaining health (read order is already preserved)
        GameObject target = null;
        int minHealth = Integer.MAX_VALUE;
        for (GameObject potentialTarget : targetsInRange) {
            if (potentialTarget.health < minHealth) {
                minHealth = potentialTarget.health;
                target = potentialTarget;
            }
        }
        // Attack here; if target dies, decrement count for its team
        if (!target.damage(obj.damage)) {
            map[target.y][target.x] = 1;
            if (target.team == 1) {
                goblinCount--;
            } else {
                elfCount--;
            }
        }
    }

    /**
     * Prints out the state of the map
     *
     * @param map The map
     */
    private void printMap(int[][] map) {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                // If open space, print blank
                int space = map[i][j];
                char print = '#';
                switch(space) {
                    case 1:
                        print = ' ';
                        break;
                    case 2:
                        print = 'G';
                        break;
                    case 3:
                        print = 'E';
                        break;
                }
                System.out.print(print);
            }
            // Print current object stats
            for(GameObject obj : objects) {
                if(obj.y == i && obj.isAlive) {
                    System.out.printf("\t%c(%d)", obj.team == 2 ? 'E' : 'G',obj.health);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Gets all targets that are in range
     *
     * @param targets The target list
     * @param object The current object
     * @return Targets that are in range
     */
    private List<GameObject> getInRangeTargets(List<GameObject> targets, GameObject object) {
        ArrayList<GameObject> list = new ArrayList<>();
        for(GameObject obj : targets) {
            // If the target is one step away from the current object, add it
            if((Math.abs(obj.y - object.y) == 1 && obj.x == object.x) || (Math.abs(obj.x - object.x) == 1 && obj.y == object.y)) {
                list.add(obj);
            }
        }
        // Preserve read order
        Collections.sort(list);
        return list;
    }

    /**
     * Finds all the open squares around the targets in the list
     *
     * @param map The map
     * @param targets The list of targets
     * @param currentObject The current active object
     * @return The list of open squares around each target
     */
    private List<Point> findOpenSquares(int[][] map, List<GameObject> targets, GameObject currentObject) {
        ArrayList<Point> list = new ArrayList<>();
        // Temporarily free up the space occupied by currentObject
        map[currentObject.y][currentObject.x] = 1;
        for(GameObject obj : targets) {
            int y = obj.y;
            int x = obj.x;
            // North
            if(map[y - 1][x] == 1) {
                list.add(new Point(x, y - 1));
            }
            // South
            if(map[y + 1][x] == 1) {
                list.add(new Point(x, y + 1));
            }
            // East
            if(map[y][x + 1] == 1) {
                list.add(new Point(x + 1, y));
            }
            // West
            if(map[y][x - 1] == 1) {
                list.add(new Point(x - 1, y));
            }
        }
        // Re-close the space that was freed
        map[currentObject.y][currentObject.x] = currentObject.team + 1;
        return list;
    }

    /**
     * Finds all targets of the specified team
     *
     * @param list The list of game objects
     * @param team The team to search for
     * @return All targets from the specified team
     */
    private List<GameObject> findTargets(List<GameObject> list, int team) {
        ArrayList<GameObject> targets = new ArrayList<>();
        // Get all live members of the other team
        for(GameObject obj : list) {
            if(obj.team == team && obj.isAlive) {
                targets.add(obj);
            }
        }
        // Preserve read order
        Collections.sort(targets);
        return targets;
    }

    /**
     * Parses the map from the input
     *
     * @param input The input
     * @return The map
     */
    private int[][] parseMap(String[] input) {
        // Create temp map
        int[][] map = new int[input.length][];
        // Build the map
        for(int i = 0; i < input.length; i++) {
            // Get the current line of input
            String line = input[i];
            // Create the line on the map
            int[] strip = new int[line.length()];
            for(int j = 0; j < strip.length; j++) {
                // Get the current character in the input line
                char c = line.charAt(j);
                switch(c) {
                    case '#':
                        // Wall space
                        strip[j] = 0;
                        break;
                    case '.':
                        // Open space
                        strip[j] = 1;
                        break;
                    case 'G':
                        // Open space, but a goblin occupies it
                        strip[j] = 2;
                        GameObject goblin = new GameObject(j, i, 200, goblinTeam, 3);
                        objects.add(goblin);
                        goblinCount++;
                        break;
                    case 'E':
                        // Open space, but an elf occupies it
                        strip[j] = 3;
                        GameObject elf = new GameObject(j, i, 200, elfTeam, elfDamage);
                        objects.add(elf);
                        elfCount++;
                }
            }
            // Add the strip to the map
            map[i] = strip;
        }
        // Return the map
        return map;
    }

    /**
     * Game object
     */
    private class GameObject implements Comparable<GameObject> {
        int x = 0;
        int y = 0;
        int health = 200;
        int damage = 0;
        boolean isAlive = true;
        public int team = 1;

        /**
         * Creates a new game object
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @param health The health
         * @param team The team
         * @param damage The damage the object inflicts
         */
        public GameObject(int x, int y, int health, int team, int damage) {
            this.x = x;
            this.y = y;
            this.health = health;
            this.team = team;
            this.damage = damage;
        }

        /**
         * Moves the object
         *
         * @param map The map
         * @param dx The y distance to move
         * @param dy The x distance to move
         */
        public void move(int[][] map, int dx, int dy) {
            // Open the current space
            map[y][x] = 1;
            // Advance
            x += dx;
            y += dy;
            // Close the new space
            map[y][x] = 1 + team;
        }

        /**
         * Damages the object
         *
         * @param damage The amount of damage taken
         * @return True if the object is still alive
         */
        public boolean damage(int damage) {
            health -= damage;
            if(health <= 0) {
                isAlive = false;
            }
            return isAlive;
        }

        /**
         * Compares this game object to another preserving read order
         *
         * @param object The game object to compare with
         * @return The resulting comparison
         */
        public int compareTo(GameObject object) {
            int comp = Integer.compare(y, object.y);
            if(comp < 0) {
                return comp;
            }
            if(comp == 0) {
                return Integer.compare(x, object.x);
            }
            return comp;
        }
    }

    /**
     * Pathfinding class
     */
    private class Dijkstra {
        ArrayList<Point> nearest;
        Node[][] grid;

        /**
         * Finds the shortest path to all target nodes from a source
         *
         * @param map The map
         * @param targets The target locations
         * @param sourceX The source x coordinate
         * @param sourceY The source y coordinate
         */
        public Dijkstra(int[][] map, List<Point> targets, int sourceX, int sourceY) {
            grid = new Node[map.length][map[0].length];
            // Fill the Node array
            for(int y = 0; y < map.length; y++) {
                for(int x = 0; x < map[y].length; x++) {
                    Node node = new Node(x, y, Integer.MAX_VALUE);
                    node.prev = null;
                    grid[y][x] = node;
                }
            }
            // Set source parameters
            grid[sourceY][sourceX].score = 0;
            // Initially fill the queue
            Queue<Node> q = new PriorityQueue<>();
            q.offer(grid[sourceY][sourceX]);

            // Continue to path as long as the queue is not empty
            while(!q.isEmpty()) {
                // Get the priority node
                Node current = q.poll();

                // Get neighbors only if the space is open
                ArrayList<Node> neigh = new ArrayList<>();
                int nx = current.x;
                int ny = current.y;
                // North
                if(map[ny - 1][nx] == 1) {
                    Node nNode = grid[ny - 1][nx];
                    neigh.add(nNode);
                }
                // West
                if(map[ny][nx - 1] == 1) {
                    Node nNode = grid[ny][nx - 1];
                    neigh.add(nNode);
                }
                // East
                if(map[ny][nx + 1] == 1) {
                    Node nNode = grid[ny][nx + 1];
                    neigh.add(nNode);
                }
                // South
                if(map[ny + 1][nx] == 1) {
                    Node nNode = grid[ny + 1][nx];
                    neigh.add(nNode);
                }
                // Process neighbors
                for(Node n : neigh) {
                    int newScore = current.score + 1;
                    // If the new score is less than the old score then update
                    if(newScore < n.score) {
                        n.score = newScore;
                        q.remove(n);
                        q.offer(n);
                        n.prev = current;
                    }
                    if(newScore == n.score) {
                        Node prevNode = n.prev;
                        PointComparator c = new PointComparator();
                        // Preserve read order between the current node and the neighbor's previous node
                        Point p2 = new Point(current.x, current.y);
                        Point p1 = new Point(prevNode.x, prevNode.y);
                        if(c.compare(p2, p1) == -1) {
                            n.prev = current;
                        }
                    }
                }
            }

            // Find all reachable target squares
            ArrayList<Point> reachable = new ArrayList<>();
            nearest = new ArrayList<>();
            for(Point p : targets) {
                int px = p.x;
                int py = p.y;
                if(grid[py][px].score != Integer.MAX_VALUE) {
                    reachable.add(p);
                }
            }
            // Find the minimum distance to a reachable target square
            int min = Integer.MAX_VALUE;
            for(Point p : reachable) {
                int score = grid[p.y][p.x].score;
                if(score < min) {
                    min = score;
                }
                //nearest.add(p);
            }
            // Put all reachable target squares that have a score of min into the nearest list
            for(Point p : reachable) {
                int score = grid[p.y][p.x].score;
                if(score == min) {
                    nearest.add(p);
                }
            }
            // Preserve read order
            Collections.sort(nearest, new PointComparator());
        }

        /**
         * Reconstructs the path from the source to the goal
         *
         * @param p The destination
         * @param px The source x coordinate
         * @param py The source y coordinate
         * @return The point to move to along the path
         */
        public Point constructPath(Point p, int px, int py) {
            Node current = grid[p.y][p.x];
            while(current.prev != grid[py][px]) {
                current = current.prev;
            }
            return new Point(current.x, current.y);
        }
    }

    /**
     * Pathfinding node
     */
    private class Node implements Comparable<Node> {
        int x;
        int y;
        int score;
        Node prev;

        /**
         * Creates a new node
         *
         * @param x The x coordinate
         * @param y The y coordinate
         * @param score The score
         */
        public Node(int x, int y, int score) {
            this.x = x;
            this.y = y;
            this.score = score;
        }

        /**
         * Compares this node to another node preserving order
         *
         * @param n The other node
         * @return The resulting comparison
         */
        public int compareTo(Node n) {
            if(score < n.score) {
                return -1;
            }
            if(score == n.score) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * Sorts Points in read order
     */
    private class PointComparator implements Comparator<Point> {
        public int compare(Point p, Point p2) {
            int comp = Integer.compare(p.y, p2.y);
            if(comp < 0) {
                return comp;
            }
            if(comp == 0) {
                int comp2 = Integer.compare(p.x, p2.x);
                if(comp2 < 0) {
                    return comp2;
                }
                return comp2;
            }
            return comp;
        }
    }
}
