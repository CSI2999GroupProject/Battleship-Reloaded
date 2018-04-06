package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class Submarine extends Ship{

    public Submarine(Context context, int row, int column)
    {
        super(context, row, column);
        setName("submarine");
        setShipSize(3);//the length of the ship
        setShipCost(2);//the amount it costs to place a ship
        setnMove(3);//number of total spaces a ship can move
        setHitpoints(1200);//the ships health
        setdamage(300);//the amount of damage a ship deals per ship
        setnShots(1);//number of total shots per ship
        setpShots(0);//number of shots used per ship
        setDamageCost(1);//the amount it costs for each specific ship to shoot
        setFrange(3);//fire range of the ship
        setpmove(0);//number of spaces a ship has moved
        shipBox = new Rect(0, 0, 128*getShipSize(), 128);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine), 128*getShipSize(), 128, false);
    }


}
