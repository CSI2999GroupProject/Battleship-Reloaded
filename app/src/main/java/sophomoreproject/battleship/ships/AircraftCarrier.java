package sophomoreproject.battleship.ships;

/**
 * Created by isaac on 1/31/2018.
 */

public class AircraftCarrier extends Ship implements ShipInterface{
    public AircraftCarrier() {
        setName("Aircraft Carrier");
        setShipSize(5);
        setSC(4);
        setnMove(1);
        setHitpoints(4000);
        setdamage(350);
        setDC(1);
        setnShots(3);
    }

    @Override
    public void ability() {

    }
}
