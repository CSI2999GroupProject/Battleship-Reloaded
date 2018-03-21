package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by Jacob on 3/5/2018.
 */

public class ShipPanel implements Panel
{
    private final int BUTTON_TOTAL = 4;                             //The total number of buttons on the panel
    private Rect panel;                                             //The back panel of the selection screen
    private Rect[] buttonBoxes = new Rect[BUTTON_TOTAL];            //Rects representing the dimensions and positions of the buttons
    private Bitmap[] buttonImages = new Bitmap[BUTTON_TOTAL];       //The images of the buttons (must match buttonBoxes perfectly)
    private Paint panelPaint = new Paint(), maxHealthPaint = new Paint(), healthPaint = new Paint(), textPaint = new Paint();
    private int lastButtonClicked = -1;                             //Keeps track of what button the user pressed down on. If it's not the same as the button they let go over, don't do anything.

    private Rect maxHealthDisp, healthDisp;

    private Ship ship;
    private GamePanel gp;



    public ShipPanel(Context context, Ship ship, GamePanel gp)
    {
        this.ship = ship;
        this.gp = gp;

        final int SCREEN_WIDTH = context.getResources().getSystem().getDisplayMetrics().widthPixels;
        final int SCREEN_HEIGHT = context.getResources().getSystem().getDisplayMetrics().heightPixels;

        panel = new Rect(0, 0, SCREEN_WIDTH/8, SCREEN_HEIGHT);

        for(int i = 0; i < BUTTON_TOTAL; i++)
        {
            buttonBoxes[i] = new Rect(panel.left + panel.width()/6, panel.width()/6 + i*panel.width()*5/6, panel.width()*5/6, panel.width()*5/6 + i*panel.width()*5/6);
        }

        maxHealthDisp = new Rect(panel.width()/6, buttonBoxes[BUTTON_TOTAL-1].bottom + panel.width()/6, panel.width()*5/6, buttonBoxes[BUTTON_TOTAL-1].bottom + panel.width()*2/6);
        healthDisp = new Rect();

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(panel.width()/12);

        buttonImages[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.move_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_left_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);

        panelPaint.setColor(Color.GRAY);
        maxHealthPaint.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas)
    {
        update();

        canvas.drawRect(panel, panelPaint);
        canvas.drawRect(maxHealthDisp, maxHealthPaint);
        canvas.drawRect(healthDisp, healthPaint);
        canvas.drawText("Health: " + ship.getHitpoints() + "/" + ship.maxHealth, maxHealthDisp.left, maxHealthDisp.bottom + panel.width()/12, textPaint);

        for(int i = 0; i < BUTTON_TOTAL; i++)
        {
            canvas.drawBitmap(buttonImages[i], buttonBoxes[i].left, buttonBoxes[i].top, null);
        }
    }

    @Override
    public void update()
    {
        double healthPercent = (double)ship.getHitpoints() / (double)ship.maxHealth;

        if(healthPercent > .5)
            healthPaint.setColor(Color.argb(255, 0, 140, 0)); //Green
        else if(healthPercent > .2)
            healthPaint.setColor(Color.YELLOW);
        else
            healthPaint.setColor(Color.RED);

        healthDisp.set(maxHealthDisp.left, maxHealthDisp.top, maxHealthDisp.left + (int)(healthPercent*(maxHealthDisp.right - maxHealthDisp.left)), maxHealthDisp.bottom);
    }

    @Override
    public boolean contains(Point point)
    {
        return panel.contains(point.x, point.y);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        //Find which button got pressed
        int i = 0;
        while (i < BUTTON_TOTAL)
            if (buttonBoxes[i].contains((int) event.getX(), (int) event.getY()))
                break;
            else
                i++;

        if (i != BUTTON_TOTAL) //Event happened on one of the buttons
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastButtonClicked = i;
                    break;
                case MotionEvent.ACTION_UP:
                    if (lastButtonClicked == i) //The button the player let go of was the same button they last pressed
                    {

                        int points=gp.getBoard().getPoints();
                        if (points > 0) {
                            switch (i) {
                                case 0: //Fire button pressed

                                    if (ship.getnShots()>ship.getpShots()) {
                                        int xPos;
                                        int yPos;
                                        int n=ship.getnShots();
                                        Ship aship = gp.getBoard().AttackedShip(xPos, yPos);
                                        gp.getBoard().HitShips(aship, ship.getdamage());
                                        System.out.println("Fire!");
                                        ship.setpShots(n++);
                                    }else{
                                    System.out.println("Maximum amount of shots");
                                    }
                                    break;
                                case 1:
                                    int pmove = ship.getpmove();
                                    System.out.println(pmove);

                                    if (pmove < ship.getnMove()) {
                                        gp.getBoard().move(ship, ship.getColumnCoord(), ship.getRowCoord(), 1);
                                        System.out.println("Move!");
                                        gp.getBoard().setPoints(points-1);

                                    } else {
                                        System.out.println("You have moved this ship its maximum number of spaces!");
                                    }
                                    ship.setpmove(pmove = pmove + 1);
                                    break;
                                case 2: //Rotate left button pressed this is the rotate that we currently have

                                    if (ship.getpmove() == 0) {
                                        ship.setpmove(ship.getnMove());
                                        gp.getBoard().rotateLeft(ship, ship.getRowCoord(), ship.getColumnCoord());
                                        System.out.println("Rotate Left!");
                                        gp.getBoard().setPoints( points - ship.getShipSize() - 1);

                                    } else {
                                        System.out.println("You cannot rotate and move");
                                    }
                                    break;
                                case 3: //Debug test button
                                    ship.applyDamage(100);
                                    break;
                                default:
                                    System.out.println("Something unexpected happened.");
                            }
                            System.out.println(points);
                        } else {
                            System.out.println("you are out of points");
                        }

                        gp.panels.remove(this);
                    }

                    lastButtonClicked = -1;
                    break;
            }
        }
    }


}
