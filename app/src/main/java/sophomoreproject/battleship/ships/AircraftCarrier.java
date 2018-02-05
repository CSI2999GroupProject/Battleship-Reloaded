package sophomoreproject.battleship.ships;

import sophomoreproject.battleship.ShipInterface;

/**
 * Created by isaac on 1/31/2018.
 */

public class AircraftCarrier extends Ship implements ShipInterface{
    public AircraftCarrier() {
        setName("Aircraft Carrier");
        setShipSize(5);
    }

    @Override
    public void ability() {

    }
}
