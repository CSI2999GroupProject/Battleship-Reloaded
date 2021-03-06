package sophomoreproject.battleship;

import java.util.HashSet;


import sophomoreproject.battleship.ships.Ship;


/**
 * Created by isaac on 3/4/2018.
 */

public class Player {

    public final static int POINTS_PER_TURN = 12;
    public final static int POINTS_FOR_FLEET = 12;

    private HashSet<Ship> playerSet;
    private int availablePoints;
    private boolean isSetup;


    public Player() {
        playerSet = new HashSet<>();
        availablePoints = POINTS_FOR_FLEET; // the cost of 1 of every ship
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

        for(Ship ship : playerSet ) {

            ship.setpmove(0);//resets the ship move counter after each turn
            ship.setpShots(0);//return the ship shot counter after each turn
        }
    }

    public int endgame() {
        if(playerSet.isEmpty()) {
            System.out.println("Fuck you");
            return 0;

        }else{
            return 1;
        }

    }
}

