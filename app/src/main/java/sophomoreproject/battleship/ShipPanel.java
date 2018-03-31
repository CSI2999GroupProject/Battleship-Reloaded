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

import java.util.ArrayList;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by Jacob on 3/5/2018.
 */

public class ShipPanel implements Panel
{
    private final int BUTTON_TOTAL = 3;                             //The total number of buttons on the panel
    private Rect panel;                                             //The back panel of the selection screen
    private Rect[] buttonBoxes = new Rect[BUTTON_TOTAL];            //Rects representing the dimensions and positions of the buttons
    private Bitmap[] buttonImages = new Bitmap[BUTTON_TOTAL];       //The images of the buttons (must match buttonBoxes perfectly)
    private Paint panelPaint = new Paint(), blackPaint = new Paint(), bluePaint = new Paint(), healthPaint = new Paint(), textPaint = new Paint(), redTextPaint;
    private int lastButtonClicked = -1;                             //Keeps track of what button the user pressed down on. If it's not the same as the button they let go over, don't do anything.
    private Rect maxHealthDisp, healthDisp, maxMoveDisp, moveDisp;

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

        maxMoveDisp = new Rect(panel.width()/6, maxHealthDisp.bottom + panel.width()/6, panel.width()*5/6, maxHealthDisp.bottom + panel.width()*2/6);
        moveDisp = new Rect();

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(maxHealthDisp.height()*2/3);
        textPaint.setTextAlign(Paint.Align.CENTER);

        redTextPaint = new Paint(textPaint);
        redTextPaint.setColor(Color.RED);

        buttonImages[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.move_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_left_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);

        panelPaint.setColor(Color.GRAY);
        blackPaint.setColor(Color.BLACK);
        bluePaint.setColor(Color.BLUE);

        update();
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawRect(panel, panelPaint);

        canvas.drawRect(maxHealthDisp, blackPaint);
        canvas.drawRect(healthDisp, healthPaint);
        canvas.drawText("HP: " + ship.getHitpoints() + "/" + ship.maxHealth, maxHealthDisp.centerX(), (maxHealthDisp.centerY() + maxHealthDisp.bottom)/2, textPaint);

        canvas.drawRect(maxMoveDisp, blackPaint);
        canvas.drawRect(moveDisp, bluePaint);
        canvas.drawText("Moves: " + (ship.getnMove() - ship.getpmove()) + "/" + ship.getnMove(), maxMoveDisp.centerX(), (maxMoveDisp.centerY() + maxMoveDisp.bottom)/2, textPaint);

        if(ship.getpShots() < ship.getnShots() && !gp.getBoard().possibleFireLoc(ship).isEmpty()) //Player hasn't run out of fires, and has a target in range
            canvas.drawBitmap(buttonImages[0], buttonBoxes[0].left, buttonBoxes[0].top, null);

        if(ship.getpmove() < ship.getnMove() && !gp.getBoard().possibleMoveLoc(ship).isEmpty())
        {
            canvas.drawBitmap(buttonImages[1], buttonBoxes[1].left, buttonBoxes[1].top, null); //Player hasn't run out of moves and has at least 1 valid location
        }

        if(ship.getpmove() == 0 && (gp.getBoard().checkRotate(ship)[0] != null || gp.getBoard().checkRotate(ship)[1] != null) ) //Player hasn't moved yet and can turn in at least 1 direction
        {
            canvas.drawBitmap(buttonImages[2], buttonBoxes[2].left, buttonBoxes[2].top, null);
        }

    }

    @Override
    public void update()
    {
        double healthPercent = (double)ship.getHitpoints() / (double)ship.maxHealth;
        double movePercent = (double)(ship.getnMove() - ship.getpmove()) / (double)ship.getnMove();

        if(healthPercent > .5)
            healthPaint.setColor(Color.argb(255, 0, 140, 0)); //Green
        else if(healthPercent > .25)
            healthPaint.setColor(Color.YELLOW);
        else
            healthPaint.setColor(Color.RED);

        healthDisp.set(maxHealthDisp.left, maxHealthDisp.top, maxHealthDisp.left + (int)(healthPercent*(maxHealthDisp.right - maxHealthDisp.left)), maxHealthDisp.bottom);
        moveDisp.set(maxMoveDisp.left, maxMoveDisp.top, maxMoveDisp.left + (int)(movePercent*(maxMoveDisp.right - maxMoveDisp.left)), maxMoveDisp.bottom);
    }

    @Override
    public boolean contains(Point point)
    {
        return panel.contains(point.x, point.y);
    }

    @Override
    public void onTouchEvent(MotionEvent event)
    {
        //Find which button got pressed
        int i = 0;
        while(i < BUTTON_TOTAL)
            if(buttonBoxes[i].contains((int)event.getX(), (int)event.getY()))
                break;
            else
                i++;

        if(i != BUTTON_TOTAL) //Event happened on one of the buttons
        {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    lastButtonClicked = i;
                    break;
                case MotionEvent.ACTION_UP:
                    if(lastButtonClicked == i) //The button the player let go of was the same button they last pressed
                    {
                        ArrayList<Point> validLocations;
                        int cost;
                        int player = gp.getBoard().getPlayerTurn(); //0 for p1, 1 for p2
                        int pointsLeft;

                        if(player == 0)
                            pointsLeft = gp.getBoard().getP1().getAvailablePoints();
                        else
                            pointsLeft = gp.getBoard().getP2().getAvailablePoints();

                        switch (i) {
                            case 0: //Fire button pressed
                                if(ship.getpShots()<ship.getnShots()) {
                                    validLocations = gp.getBoard().possibleFireLoc(ship);
                                    cost = ship.getDamageCost(); // ALAN Put the actual fire cost here.
                                    for (Point point : validLocations) {
                                        if (cost <= pointsLeft) {
                                            gp.panels.add(new Marker(gp.getContext(), gp, 0, ship, point.x, point.y, cost));

                                        }
                                    }
                                }
                                break;
                            case 1: //Move button pressed
                                if (ship.getpmove() < ship.getnMove())
                                {

                                       validLocations = gp.getBoard().possibleMoveLoc(ship);

                                       for (Point point : validLocations) {
                                           cost = Math.abs(ship.getColumnCoord() - point.x) + Math.abs(ship.getRowCoord() - point.y); //finds the distance the ship is travelling


                                           if (cost <= pointsLeft && cost <= ship.getnMove() - ship.getpmove()) {
                                               gp.panels.add(new Marker(gp.getContext(), gp, 1, ship, point.x, point.y, cost));
                                           }

                                   }
                                }
                                break;
                            case 2: //Rotate left button pressed this is the rotate that we currently have
                                if (ship.getpmove() == 0)
                                {
                                    // JACOB To set cost in marker:
                                    Point[] turnLocations = gp.getBoard().checkRotate(ship);
                                    cost = ship.getShipSize() +1; //ALAN put the actual cost to turn a ship here

                                    if(turnLocations[0] != null) //player can turn left
                                    {
                                        gp.panels.add(new Marker(gp.getContext(), gp, 2, ship, turnLocations[0].x, turnLocations[0].y, cost));
                                    }
                                    if(turnLocations[1] != null) //player can turn right
                                    {
                                        gp.panels.add(new Marker(gp.getContext(), gp, 3, ship, turnLocations[1].x, turnLocations[1].y, cost));
                                    }
                                }
                                else
                                {
                                    System.out.println("You cannot rotate and move");
                                }
                                break;
                            default:
                                System.out.println("Something unexpected happened.");
                        }

                        gp.panels.remove(this);
                    }
                    lastButtonClicked = -1;
                    break;
            }
        }
    }

}
