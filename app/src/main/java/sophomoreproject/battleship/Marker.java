package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by isaac on 1/31/2018.
 */

public class Marker implements Panel { // extends Ship {
    private int x, y, type, data, cost;
    private Ship originalShip;
    private Bitmap image;
    private GameBoard gb;
    private GamePanel gp;
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
     * @param //data Specific data to be held to be used by the marker when clicked. Might hold cost, or something else when necessary.
     */
    public Marker(Context context, GamePanel gp, int type, Ship originalShip, int x, int y, int cost) {

        this.type = type;
        this.gp = gp;
        this.gb = gp.getBoard();
        this.originalShip = originalShip;
        this.x = x;
        this.y = y;
        //this.data = data;
        this.cost = cost;

        switch (type)
        {
            case 0:     //Fire
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_button), 128, 128, false);
                break;
            case 1:     //Move
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.move_icon), 128, 128, false);
                break;
            case 2:     //Rotate
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.move_icon), 128, 128, false);
                break;
            case 3:     //Rotate
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.move_icon), 128, 128, false);
                break;
            case 4:     //Ability
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_button), 128, 128, false);
                break;
            default:
                break;
        }
    }


    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, imageBox.left, imageBox.top, null);
    }

    @Override
    public void update() {
        Point masterPoint = gb.getMasterPoint();
        imageBox.set(masterPoint.x + 128*x, masterPoint.y + 128*y, masterPoint.x + 128*x + imageBox.width(), masterPoint.y + 128*y + imageBox.height());
    }

    @Override
    public boolean contains(Point point)
    {
        return imageBox.contains(point.x, point.y);
    }

    @Override
    public void onTouchEvent(MotionEvent event)
    {
        int currentPlayer = gb.getPlayerTurn() + 1;

        switch (type)
        {
            case 0:     //Fire
                gb.Fire(originalShip.getdamage(),x,y);
                gb.setPoints(gb.getPoints() - originalShip.getDamageCost());
                originalShip.setpShots(originalShip.getpShots()+1);
                break;
            case 1:     //Move
                if(cost <= originalShip.getnMove() - originalShip.getpmove()) {
                    if(originalShip.getpmove() == 0) //Only charge player points if they moved the ship for the first time.
                        gb.setPoints(gb.getPoints()-1);

                    originalShip.setpmove(originalShip.getpmove() + cost);
                    int lastViewableColumn = gb.lastViewableColumn();
                    gb.move(originalShip, originalShip.getColumnCoord(), originalShip.getRowCoord(), cost);
                    if(lastViewableColumn != gb.lastViewableColumn()) //player moved a ship further into enemy waters, or retreated their front ship
                    {
                        if(currentPlayer == 0)
                        {
                            gb.getMasterPoint().x = -128 * gb.xPosOfShip(gb.getP2()) + gb.VIEW_RANGE - 128;
                        }
                        else
                        {
                            gb.getMasterPoint().x = -128 * gb.xPosOfShip(gb.getP1()) - gb.VIEW_RANGE + gb.SCREEN_WIDTH;
                        }
                    }
                }
                break;
            case 2:     //Rotate Left
                gb.rotateLeft(originalShip, x, y);
                originalShip.setpmove(originalShip.getnMove());
                break;
            case 3:     //Rotate Right
                gb.rotateRight(originalShip, x, y);
                originalShip.setpmove(originalShip.getnMove());
                break;
            case 4:
                int maxHP = originalShip.maxHealth;
                int currentHP = originalShip.getHitpoints();
                double bonusDamage = (maxHP - currentHP) * 1.1;
                switch(originalShip.getName()) {
                    case "Battleship":
                        gb.Fire(originalShip.getdamage(),x,y);

                        gb.setPoints(gb.getPoints() - 3);
                        break;
                    case "Aircraft Carrier":
                        gb.Fire(originalShip.getdamage(),x,y);

                        gb.setPoints(gb.getPoints() - 6);
                        break;
                    case "destroyer":
                        gb.Fire(100 + (int)bonusDamage, x, y);
                        System.out.println(100 + bonusDamage);
                        gb.setPoints((gb.getPoints() - 10));
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        gp.getBoard().purgeOldPanels();
    }
}