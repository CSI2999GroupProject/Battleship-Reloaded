package sophomoreproject.battleship;

import java.util.HashSet;


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
        System.out.println(playerSet);
        return playerSet;
    }

    public void resetPMove() {
        System.out.println("Reset " + playerSet.size() + " ships in the set");

        for(Ship ship : playerSet ) {

            ship.setpmove(0);
            ship.setpShots(0);
        }
    }

    public int endgame() {
    int i=1;
        for(Ship ship : playerSet) {
            if(ship.getHitpoints()<=0)  {
                i=0;
            }else{
                System.out.println("does i =1");
                i=1;
                break;

            }
        }
        if(i==0) {
            System.out.println("Fuck you");
            return 0;

        }else{
            return 1;
        }

    }
}

