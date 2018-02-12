package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class Cruiser extends Ship implements ShipInterface{

    private Rect display;
    private Bitmap water;
    private Drawable ship;


    public Cruiser(Context context)
    {
        setName("Cruiser");
        setShipSize(2);
        display = new Rect(0,0,128*this.getShipSize(),128);
        water = BitmapFactory.decodeResource(context.getResources(), R.drawable.water);
        ship = context.getResources().getDrawable(R.drawable.ship);
    }

    @Override
    public void ability() {

    }

    //@Override
    public void update() {

    }

    public void update(Point point)
    {
        display.set(point.x, point.y, point.x + display.width(), point.y + display.height());
        ship.setBounds(display);
    }

    //@Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawBitmap(water, display.left, display.top, null);
        ship.draw(canvas);
        //canvas.drawRect(display, paint);
    }
}
