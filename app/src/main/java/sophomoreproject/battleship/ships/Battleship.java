package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import sophomoreproject.battleship.R;
/**
 * Created by isaac on 1/31/2018.
 */

public class Battleship extends Ship implements ShipInterface{
    public Battleship(Context context, int row, int column) {
        super(context, row, column);
        setName("Battleship");
        setShipSize(3);
        setShipCost(2);
        setnMove(2);
        setHitpoints(1500);
        setfdamage(100);
        setFDamageCost(1);
        setdamage(300);
        setDamageCost(2);
        setbdamage(50);
        setBDamageCost(1);
        setRange(5);
        setnShots(2);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.battleship), 128*getShipSize(), 128, false);
    }

    @Override
    public void ability()
    {

    }
}
