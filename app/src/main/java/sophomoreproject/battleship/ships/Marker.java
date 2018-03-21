package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import sophomoreproject.battleship.GameBoard;
import sophomoreproject.battleship.Panel;
import sophomoreproject.battleship.R;

/**
 * Created by isaac on 1/31/2018.
 */

public class Marker implements Panel { // extends Ship {
    private int x, y, type, data, cost;
    private Ship originalShip;
    private Bitmap image;
    private GameBoard gb;
    private Rect imageBox = new Rect(0,0,128,128);

    /**
     * Constructor for a marker
     *
     * @param type an integer specifying what it is marking.
     *             0: Fire
     *             1: Move
     *             2: Rotate Left
     *             3: Rotate Right
     * @param originalShip the ship that was originally clicked on to create this marker
     * @param x the x coordinate of the marker icon
     * @param y the y coordinate of the marker icon
     * @param data Specific data to be held to be used by the marker when clicked. Might hold cost, or something else when necessary.
     */
    public Marker(Context context, GameBoard gb, int type, Ship originalShip, int x, int y, int data, int cost) {

        this.type = type;
        this.gb = gb;
        this.originalShip = originalShip;
        this.x = x;
        this.y = y;
        this.data = data;
        this.cost = cost;

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.temp), 128, 128, false);

        //TEMPORARY CODE BEGINS HERE

    }


    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, imageBox.left, imageBox.top, null);
    }

    @Override
    public void update() {
        //Unused update method so game wont complain about not importing everything from the interface
    }


    public void update(Point masterPoint)
    {
        imageBox.set(masterPoint.x + 128*x, masterPoint.y + 128*y, masterPoint.x + 128*x + imageBox.width(), masterPoint.y + 128*y + imageBox.height());
    }

    @Override
    public boolean contains(Point point)
    {
        return false;
    }

    @Override
    public void onTouchEvent(MotionEvent event)
    {
        int currentPlayer = gb.getPlayerTurn() + 1;

        switch (type)
        {
            case 0:     //Fire
                //Insert fire code here when it is completed
                break;
            case 1:     //Move
                gb.move(originalShip, x, y, data);
                break;
            case 2:     //Rotate
                gb.rotateLeft(originalShip, x, y);
                break;
            case 3:     //Rotate
                gb.rotateRight(originalShip, x, y);
                break;
            default:
                break;
        }
    }
}
