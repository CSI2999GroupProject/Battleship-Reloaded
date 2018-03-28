package sophomoreproject.battleship;

import java.util.HashSet;
import java.util.Iterator;


import sophomoreproject.battleship.ships.Ship;


/**
 * Created by isaac on 3/4/2018.
 */

public class Player {

    private HashSet<Ship> playerSet;
    private int availablePoints;
    private boolean isSetup;


    public Player() {
        playerSet = new HashSet<>();
        availablePoints = 100; // the cost of 1 of every ship
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public boolean isSetup() {
        return isSetup;
    }

    public void setSetup(boolean setup) {
        isSetup = setup;
    }

    public HashSet<Ship> getPlayerSet() {
        return playerSet;
    }

    public void resetPMove() {
        System.out.println("Reset " + playerSet.size() + " ships in the set");

        for(Ship ship : playerSet ) {

            ship.setpmove(0);
        }
    }

}
