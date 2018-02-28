package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class Submarine extends Ship implements ShipInterface{

    public Submarine(Context context, int row, int column)
    {
        super(context, row, column);
        setName("Submarine");
        setShipSize(3);
        setSC(2);
        setDC(1);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine), 128*getShipSize(), 128, false);
    }
    @Override
    public void ability()
    {

    }
}
