package sophomoreproject.battleship.ships;

import android.content.Context;
import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class Cruiser extends Ship implements ShipInterface{

    public Cruiser(Context context, int row, int column)
    {
        super(context, row, column);
        setName("Cruiser");
        setShipSize(2);
        shipImage = context.getResources().getDrawable(R.drawable.cruiser);
    }

    @Override
    public void ability()
    {

    }
}
