package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import sophomoreproject.battleship.R;
/**
 * Created by isaac on 1/31/2018.
 */

public class Destroyer extends Ship implements ShipInterface{
    public Destroyer(Context context, int row, int column) {
        super(context, row, column);
        setName("destroyer");
        setShipSize(4);
        setShipCost(3);
        setnMove(2);
        setHitpoints(2500);
        setdamage(400);
        setnShots(1);
        setpShots(0);
        setDamageCost(5);
        //setfdamage(600);
        //setFDamageCost(4);
        //setbdamage(400);
        //setBDamageCost(3);
        setRange(8);
        setFrange(8);
        setpmove(0);
        shipBox = new Rect(0, 0, 128*getShipSize(), 128);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer), 128*getShipSize(), 128, false);
    }

    @Override
    public void ability() {

    }

}
