package sophomoreproject.battleship.ships;

/**
 * Created by isaac on 1/31/2018.
 */

public class Battleship extends Ship implements ShipInterface{
    public Battleship() {
        setName("Battleship");
        setShipSize(3);
        setSC(2);
        setnMove(2);
        setHitpoints(1500);
        setfdamage(100);
        setFDC(1);
        setdamage(300);
        setDC(2);
        setbdamage(50);
        setBDC(1);
    }

    @Override
    public void ability() {

    }
}
