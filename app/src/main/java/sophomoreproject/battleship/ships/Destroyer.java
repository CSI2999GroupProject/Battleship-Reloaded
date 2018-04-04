package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import sophomoreproject.battleship.R;
/**
 * Created by isaac on 1/31/2018.
 */

public class Destroyer extends Ship{
    public Destroyer(Context context, int row, int column) {
        super(context, row, column);
        setName("destroyer");
        setShipSize(4);//the length of the ship
        setShipCost(3);//the amount it costs to place a ship
        setnMove(2);//number of total spaces a ship can move
        setHitpoints(2500);//the ships health
        setdamage(600);//the amount of damage a ship deals per ship
        setnShots(1);//number of total shots per ship
        setpShots(0);//number of shots used per ship
        setDamageCost(3);//the amount it costs for each specific ship to shoot
        setFrange(8);//fire range of the ship
        setpmove(0);//number of spaces a ship has moved
        shipBox = new Rect(0, 0, 128*getShipSize(), 128);
        shipImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer), 128*getShipSize(), 128, false);
    }



}
