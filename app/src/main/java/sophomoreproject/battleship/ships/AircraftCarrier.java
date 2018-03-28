package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class AircraftCarrier extends Ship implements ShipInterface{

    public AircraftCarrier(Context context, int row, int column)
    {
        super(context, row, column);
        setName("Aircraft Carrier");
        setShipSize(5);
        setShipCost(4);
        setnMove(1);
        setpShots(0);
        setHitpoints(4000);
        setdamage(350);
        setDamageCost(1);
        setnShots(1);
        setRange(24);
        setFrange(24);
        setpmove(0);
        shipBox = new Rect(0, 0, 128*getShipSize(), 128);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.aircraftcarrier), 128*getShipSize(), 128, false);
    }

    @Override
    public void ability()
    {

    }

}
