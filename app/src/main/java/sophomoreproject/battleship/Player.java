package sophomoreproject.battleship;

import java.util.HashSet;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by isaac on 3/4/2018.
 */

public class Player {

    private HashSet<Ship> playerSet;
    private int availablePoints;
    private boolean isSetup = false;

    public Player() {
        playerSet = null;
        availablePoints = 0;
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

}
