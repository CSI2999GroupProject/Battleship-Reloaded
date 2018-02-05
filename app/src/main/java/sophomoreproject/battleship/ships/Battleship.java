package sophomoreproject.battleship.ships;

import sophomoreproject.battleship.ShipInterface;

/**
 * Created by isaac on 1/31/2018.
 */

public class Battleship extends Ship implements ShipInterface{
    public Battleship() {
        setName("Battleship");
        setShipSize(3);
    }

    @Override
    public void ability() {

    }
}
