package sophomoreproject.battleship.ships;

/**
 * Created by isaac on 1/31/2018.
 */

public class Destroyer extends Ship implements ShipInterface{
    public Destroyer() {
        setName("Destroyer");
        setShipSize(4);
        setSC(3);
        setnMove(2);
        setHitpoints(2500);
        setdamage(125);
        setnShots(4);
        setDC(1);
        setfdamage(600);
        setFDC(4);
        setbdamage(400);
        setBDC(3);
    }

    @Override
    public void ability() {

    }
}
