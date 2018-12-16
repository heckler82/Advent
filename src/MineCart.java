import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Solution for Day13.txt of Advent of Code
 *
 * @author Evan Foley
 * @version 15 Dec 2018
 */
public class MineCart extends AdventMaster {
    public MineCart(String fileName) {
        super(fileName);
    }

    public void task() {
        char[][] track = parseTrack(input);
        List<Cart> carts = parseCarts(track);
        int currentTick = 0;
        while(carts.size() > 1) {
            currentTick++;
            carts.sort(new CartComparator());
            for(int i = carts.size() - 1; i >= 0; i--) {
                Cart cart = carts.get(i);
                cart.move(track);
                int collisionIndex = checkCollisions(carts, i);
                if(collisionIndex > -1) {
                    carts.remove(Math.max(i, collisionIndex));
                    carts.remove(Math.min(i, collisionIndex));
                    if(collisionIndex < i) {
                        i--;
                    }
                    System.out.printf("The location of the collision is (%d, %d) at tick %d%n", cart.x, cart.y, currentTick);
                }
            }
        }
        Cart finalCart = carts.get(0);
        System.out.printf("The final cart is located at (%d, %d) at tick %d%n", finalCart.x, finalCart.y, currentTick);
    }

    /**
     * Check for collisions against all other carts
     *
     * @param carts The list of carts
     * @param currentCart The current cart
     * @return The index of the first cart that collides with the current cart
     */
    private int checkCollisions(List<Cart> carts, int currentCart) {
        Cart cart = carts.get(currentCart);
        for(int i = 0; i < carts.size(); i++) {
            Cart testCart = carts.get(i);
            if(i != currentCart) {
                if((testCart.x == cart.x) && (testCart.y == cart.y)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Parses the track from the input
     *
     * @param input The input
     * @return The parsed track
     */
    private char[][] parseTrack(String[] input) {
        ArrayList<char[]> lines = new ArrayList<>();
        for(String str : input) {
            lines.add(str.toCharArray());
        }
        char[][] track = new char[lines.size()][];
        for(int i = 0; i < lines.size(); i++) {
            track[i] = lines.get(i);
        }
        return track;
    }

    /**
     * Gets all the carts from the track
     *
     * @param track The track
     * @return All the carts in the track
     */
    private List<Cart> parseCarts(char[][] track) {
        // Container for the carts
        List<Cart> carts = new ArrayList<>();
        // Find all carts in the track
        for(int i = 0; i < track.length; i++) {
            for(int j = 0; j < track[i].length; j++) {
                // Create a new cart when a hit is found
                if(track[i][j] == '^') {
                    track[i][j] = '|';
                    Cart c = new Cart();
                    c.x = j;
                    c.y = i;
                    c.dir[1] = -1;
                    carts.add(c);
                    continue;
                }
                if(track[i][j] == '<') {
                    track[i][j] = '-';
                    Cart c = new Cart();
                    c.x = j;
                    c.y = i;
                    c.dir[0] = -1;
                    carts.add(c);
                    continue;
                }
                if(track[i][j] == '>') {
                    track[i][j] = '-';
                    Cart c = new Cart();
                    c.x = j;
                    c.y = i;
                    c.dir[0] = 1;
                    carts.add(c);
                    continue;
                }
                if(track[i][j] == 'v') {
                    track[i][j] = '|';
                    Cart c = new Cart();
                    c.x = j;
                    c.y = i;
                    c.dir[1] = 1;
                    carts.add(c);
                    continue;
                }
            }
        }
        return carts;
    }

    /**
     * Helper class
     */
    private class Cart {
        int x;
        int y;
        int[] dir = new int[2];
        int currentTurn = 0;

        public void move(char[][] track) {
            x += dir[0];
            y += dir[1];
            char piece = track[y][x];
            int dx = dir[0];
            if(piece == '\\') {
                if(dx != 0) {
                    dir[0] = 0;
                    dir[1] = dx == 1 ? 1 : -1;
                } else {
                    int dy = dir[1];
                    dir[1] = 0;
                    dir[0] = dy == 1 ? 1 : -1;
                }
            }
            if(piece == '/') {
                if(dx != 0) {
                    dir[0] = 0;
                    dir[1] = dx == 1 ? -1 : 1;
                } else {
                    int dy = dir[1];
                    dir[1] = 0;
                    dir[0] = dy == 1 ? -1 : 1;
                }
            }
            if(piece == '+') {
                turn();
            }
        }

        public void turn() {
            // Cart is moving horizontally, turn on vertical axis
            if(dir[0] != 0) {
                // Current turn will be 1 when the "turn" is travel straight so ignore it
                if(currentTurn != 1) {
                    int dx = dir[0];
                    dir[0] = 0;
                    dir[1] = currentTurn == 0 ? -1 * dx : 1 * dx;
                }
            } else {
                // Current turn will be 1 when the "turn" is travel straight so ignore it
                if(currentTurn != 1) {
                    int dy = dir[1];
                    dir[1] = 0;
                    dir[0] = currentTurn == 0 ? 1 * dy : -1 * dy;
                }
            }
            // Change which turn will be next
            currentTurn = ++currentTurn % 3;
        }

        public int compareTo(Cart cart) {
            if(y < cart.y) {
                return 1;
            } else {
                if(y == cart.y) {
                    if(x < cart.x) {
                        return 1;
                    } else {
                        return x == cart.x ? 0 : -1;
                    }
                }
            }
            return -1;
        }
    }

    /**
     * Helper class
     */
    private class CartComparator implements Comparator<Cart> {
        public int compare(Cart c1, Cart c2) {
            return c1.compareTo(c2);
        }
    }
}
