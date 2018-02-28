package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class Destroyer extends Ship implements ShipInterface{

    public Destroyer(Context context, int row, int column)
    {
        super(context, row, column);
        setName("Destroyer");
        setShipSize(4);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer), 128*getShipSize(), 128, false);
    }

    @Override
    public void ability()
    {

    }

}
