package sophomoreproject.battleship.ships;

import sophomoreproject.battleship.ShipInterface;

/**
 * Created by isaac on 1/31/2018.
 */

public class Cruiser extends Ship implements ShipInterface{
    public Cruiser() {
        setName("Cruiser");
        setShipSize(2);
    }

    @Override
    public void ability() {

    }
}
