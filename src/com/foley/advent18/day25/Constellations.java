package com.foley.advent18.day25;

import com.foley.advent18.AdventMaster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description goes here
 *
 * @author Evan Foley
 * @version 26 Dec 2018
 */
public class Constellations extends AdventMaster {
    /**
     * Creates a new instance
     *
     * @param fileName The name of the input file
     */
    public Constellations(String fileName) {
        super(fileName);
    }

    @Override
    /**
     * Accomplishes the task for the day
     */
    protected void task() {
        // Get star coordinates
        Star[] stars = parseInput(input);
        // Create constellation list and seed with initial constellation
        List<Constellation> constellations = new ArrayList<>();
        Constellation c = new Constellation();
        c.addStar(stars[0]);
        constellations.add(c);
        // Go through remaining stars and add to constellation list
        for(int i = 1; i < stars.length; i++) {
            Star s = stars[i];
            // Test against all constellations
            Constellation match = null;
            for(int j = constellations.size() - 1; j > -1; j--) {
                Constellation con = constellations.get(j);
                // Attempt addition of the star to the constellation
                if(con.addStar(s)) {
                    // If this is the second time a star matched a constellation, merge the two constellations
                    if(match != null) {
                        match.mergeConstellation(con);
                        // Remove the current constellation and keep the match one
                        constellations.remove(j);
                    } else {
                        match = con;
                    }
                }
            }
            // Star was not added to any constellation, add new constellation
            if(match == null) {
                Constellation newConst = new Constellation();
                newConst.addStar(s);
                constellations.add(newConst);
            }
        }
        System.out.printf("The number of constellations is %d%n", constellations.size());
    }

    private Star[] parseInput(String[] input) {
        List<Star> stars = new ArrayList<>();
        for(String str : input) {
            Pattern pattern = Pattern.compile("-?\\d+");
            Matcher matcher = pattern.matcher(str);
            int index = 0;
            int[] dat = new int[4];
            while(matcher.find()) {
                dat[index] = Integer.parseInt(matcher.group());
                index++;
            }
            stars.add(new Star(dat));
        }
        return stars.toArray(new Star[stars.size()]);
    }

    private class Star {
        int[] coords;

        public Star(int[] coords) {
            if(coords.length < 4) {
                throw new IllegalArgumentException("Coordinate must be at least 4 in length");
            }
            this.coords = new int[4];
            for(int i = 0; i < 4; i++) {
                this.coords[i] = coords[i];
            }
        }

        public Star(int x, int y, int z, int w) {
            this.coords = new int[]{x, y, z, w};
        }

        public int manhattanDistance(Star s) {
            int dist = Math.abs(coords[0] - s.coords[0]) + Math.abs(coords[1] - s.coords[1]) + Math.abs(coords[2] - s.coords[2]) + Math.abs(coords[3] - s.coords[3]);
            return dist;
        }

        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }

            if(this == o) {
                return true;
            }

            if(!(o instanceof Star)) {
                return false;
            }

            Star s = (Star)o;
            return coords[0] == s.coords[0] && coords[1] == s.coords[1] && coords[2] == s.coords[2] && coords[3] == s.coords[3];
        }
    }

    private class Constellation {
        Set<Star> stars;

        public Constellation() {
            stars = new HashSet<>();
        }

        public boolean addStar(Star s) {
            for(Star star : stars) {
                if(s.manhattanDistance(star) <= 3) {
                    return stars.add(s);
                }
            }
            if(stars.isEmpty()) {
                return stars.add(s);
            }
            return false;
        }

        public void mergeConstellation(Constellation c) {
            stars.addAll(c.stars);
        }
    }
}
