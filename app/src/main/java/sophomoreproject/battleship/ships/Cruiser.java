package sophomoreproject.battleship.ships;

/**
 * Created by isaac on 1/31/2018.
 */

public class Cruiser extends Ship implements ShipInterface{
    public Cruiser() {
        setName("Cruiser");
        setShipSize(2);
        setSC(1);
        setnMove(3);
        setdamage(200);
        setDC(1);
        setHitpoints(800);
        //mines are thier own thing
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser), 128*getShipSize(), 128, false);
    }

    @Override
    public void ability() {

    }
}
