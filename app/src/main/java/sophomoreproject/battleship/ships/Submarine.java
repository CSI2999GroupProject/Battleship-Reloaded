package sophomoreproject.battleship.ships;

/**
 * Created by isaac on 1/31/2018.
 */

public class Submarine extends Ship implements ShipInterface{
    public Submarine() {
        setName("Submarine");
        setShipSize(3);
        setSC(2);
        setDC(1);
    }



    @Override
    public void ability() {

    }
}
