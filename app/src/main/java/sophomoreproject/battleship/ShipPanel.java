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
import java.util.HashSet;

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
    private Paint panelPaint = new Paint(), blackPaint = new Paint(), bluePaint = new Paint(), healthPaint = new Paint(), textPaint = new Paint(), redTextPaint;
    private int lastButtonClicked = -1;                             //Keeps track of what button the user pressed down on. If it's not the same as the button they let go over, don't do anything.
    private Rect maxHealthDisp, healthDisp, maxMoveDisp, moveDisp;

    private Ship ship;
    private GamePanel gp;
    private GameBoard gb;
    private Ship[][] board;

    public ShipPanel(Context context, Ship ship, GamePanel gp)
    {
        this.ship = ship;
        this.gp = gp;
        gb = gp.getBoard();
        board = gb.getShipBoard();

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
        //switch on type of ship
        switch(ship.getName()) {
            case "cruiser":
                buttonImages[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.mine_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
                break;
            case "Aircraft Carrier":
                buttonImages[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.firing_button_x2), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
                break;
            case "submarine":
                buttonImages[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.torpedo_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
                break;
            case "Battleship":
                buttonImages[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.firing_button_x1), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
                break;
            case "destroyer":
                buttonImages[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.firing_button_2x2), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
                break;

        }
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

        Player player;

        if(gb.getPlayerTurn() == 0)
        {
            player = gb.getP1();
        }
        else
        {
            player = gb.getP2();
        }

        if(ship.getpShots() < ship.getnShots() && ship.getDamageCost() <= player.getAvailablePoints() && !gp.getBoard().possibleFireLoc(ship).isEmpty()) //Player hasn't run out of fires, and has a target in range
            canvas.drawBitmap(buttonImages[0], buttonBoxes[0].left, buttonBoxes[0].top, null);

        //IMPORTANT NOTE: If you want moving a ship to have different costs, replace 1 with the variable that holds the move cost for the ship.
        if(ship.getpmove() < ship.getnMove() && 1 <= player.getAvailablePoints() && !gp.getBoard().possibleMoveLoc(ship).isEmpty()) //Player hasn't run out of moves and has at least 1 valid location.
        {
            canvas.drawBitmap(buttonImages[1], buttonBoxes[1].left, buttonBoxes[1].top, null);
        }

        //IMPORTANT NOTE: The same thing applies here, but use the variable to hold rotation cost.
        if(ship.getpmove() == 0 && 1 <= player.getAvailablePoints() && (gp.getBoard().checkRotate(ship)[0] != null || gp.getBoard().checkRotate(ship)[1] != null) ) //Player hasn't moved yet and can turn in at least 1 direction
        {
            canvas.drawBitmap(buttonImages[2], buttonBoxes[2].left, buttonBoxes[2].top, null);
        }
        switch(ship.getName()) {
            case "Aircraft Carrier":
                if(player.getAvailablePoints() > 5 && !gp.getBoard().possibleFireLoc(ship).isEmpty()) {
                    canvas.drawBitmap(buttonImages[3], buttonBoxes[3].left, buttonBoxes[3].top, null);
                }
                break;
            case "Battleship":
                if(player.getAvailablePoints() > 2 && !gp.getBoard().possibleFireLoc(ship).isEmpty()) {
                    canvas.drawBitmap(buttonImages[3], buttonBoxes[3].left, buttonBoxes[3].top, null);
                }
                break;
            case "cruiser":
                if(player.getAvailablePoints() > 3) {
                    canvas.drawBitmap(buttonImages[3], buttonBoxes[3].left, buttonBoxes[3].top, null);
                }
                break;
            case "destroyer":
                if(player.getAvailablePoints() > 4) {
                    canvas.drawBitmap(buttonImages[3], buttonBoxes[3].left, buttonBoxes[3].top, null);
                }
                break;
            case "submarine":
                if(player.getAvailablePoints() > 2) {
                    canvas.drawBitmap(buttonImages[3], buttonBoxes[3].left, buttonBoxes[3].top, null);
                }
                break;
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
                                    cost = ship.getDamageCost();
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

                                    Point[] turnLocations = gp.getBoard().checkRotate(ship);
                                    cost = ship.getShipSize() +1;

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
                            case 3: //Ship ability button
                                int shipSize = ship.getShipSize();
                                int x = ship.getColumnCoord();
                                int y = ship.getRowCoord();
                                int j = 0;
                                boolean isHorizontal = ship.getHorizontal();
                                boolean direction = ship.getDirection();

                                switch(ship.getName()) {
                                    case "cruiser": //places a mine behind the cruiser
                                        HashSet<Point> mines = gb.getMineSet();
                                        Point pnt;
                                        if(isHorizontal) {
                                            if(direction) { //east
                                                pnt = new Point(x - shipSize, y);
                                                if(!mines.isEmpty()) {
                                                    for (Point p : mines) {
                                                        if (!pnt.equals(p.x, p.y)) {
                                                            j++;
                                                            System.out.println("j incremented");

                                                        }
                                                        if(j == mines.size()) {
                                                            mines.add(pnt);
                                                            gb.setPoints(gb.getPoints() - 4);
                                                            System.out.println("mine added cause j == size");
                                                        }
                                                    }
                                                } else {
                                                    mines.add(pnt);
                                                    gb.setPoints(gb.getPoints() - 4);
                                                    System.out.println("mine added cuz empty set");
                                                }


                                            } else { //west
                                                pnt = new Point(x + shipSize, y);
                                                if(!mines.isEmpty()) {
                                                    for (Point p : mines) {
                                                        if (!pnt.equals(p.x, p.y)) {
                                                            j++;
                                                            System.out.println("j incremented");

                                                        }
                                                        if(j == mines.size()) {
                                                            mines.add(pnt);
                                                            gb.setPoints(gb.getPoints() - 4);
                                                            System.out.println("mine added cause j == size");
                                                        }
                                                    }
                                                } else {
                                                    mines.add(pnt);
                                                    gb.setPoints(gb.getPoints() - 4);
                                                    System.out.println("mine added cuz empty set");
                                                }
                                            }
                                        } else {
                                            if(direction) { //north
                                                pnt = new Point(x, y + shipSize);
                                                if(!mines.isEmpty()) {
                                                    for (Point p : mines) {
                                                        if (!pnt.equals(p.x, p.y)) {
                                                            j++;
                                                            System.out.println("j incremented");

                                                        }
                                                        if(j == mines.size()) {
                                                            mines.add(pnt);
                                                            gb.setPoints(gb.getPoints() - 4);
                                                            System.out.println("mine added cause j == size");
                                                        }
                                                    }
                                                } else {
                                                    mines.add(pnt);
                                                    gb.setPoints(gb.getPoints() - 4);
                                                    System.out.println("mine added cuz empty set");
                                                }
                                            } else { //south
                                                pnt = new Point(x, y - shipSize);
                                                if(!mines.isEmpty()) {
                                                    for (Point p : mines) {
                                                        if (!pnt.equals(p.x, p.y)) {
                                                            j++;
                                                            System.out.println("j incremented");

                                                        }
                                                        if(j == mines.size()) {
                                                            mines.add(pnt);
                                                            gb.setPoints(gb.getPoints() - 4);
                                                            System.out.println("mine added cause j == size");
                                                        }
                                                    }
                                                } else {
                                                    mines.add(pnt);
                                                    gb.setPoints(gb.getPoints() - 4);
                                                    System.out.println("mine added cuz empty set");
                                                }
                                            }
                                        }



                                        break;
                                    case "Aircraft Carrier":
                                        validLocations = gp.getBoard().possibleFireLoc(ship);
                                        cost = 6;
                                        if(cost <= pointsLeft) {
                                            for (Point point : validLocations) {
                                                gp.panels.add(new Marker(gp.getContext(), gp, 4, ship, point.x, point.y, cost));
                                            }
                                        }
                                        break;
                                    case "submarine": //fires torpedo across the board depending on the direction the ship is
                                        //Ship ship;
                                        if(isHorizontal) {
                                            if(direction) { //east
                                                x++;
                                                while(x < 24) {
                                                    if(board[y][x] != null) {

                                                        ship = board[y][x];
                                                        ship.applyDamage(500);
                                                        System.out.println("ship found at x: " + x + " y: " + y + " name: " + ship.getName());
                                                        break;
                                                    } else {
                                                        System.out.println("no ship found");
                                                    }
                                                    System.out.println("incrementing x");
                                                    x++;
                                                }
                                            } else { //west
                                                x--;
                                                while(x >= 0) {
                                                    if(board[y][x] != null) {
                                                        ship = board[y][x];
                                                        ship.applyDamage(500);
                                                    }
                                                    x--;
                                                }
                                            }
                                        } else {
                                            if(direction) { //north
                                                y--;
                                                while(y >= 0) {
                                                    if(board[y][x] != null) {
                                                        ship = board[y][x];
                                                        ship.applyDamage(500);
                                                    }
                                                    y--;
                                                }
                                            } else { //south
                                                y++;
                                                while(y < 16) {
                                                    if(board[y][x] != null) {
                                                        ship = board[y][x];
                                                        ship.applyDamage(500);
                                                    }
                                                    y++;
                                                }
                                            }
                                        }
                                        gb.setPoints(gb.getPoints() - 3);
                                        break;
                                    case "Battleship":
                                        validLocations = gp.getBoard().possibleFireLoc(ship);
                                        cost = 3;
                                        for (Point point : validLocations) {
                                            if (cost <= pointsLeft) {
                                                gp.panels.add(new Marker(gp.getContext(), gp, 4, ship, point.x, point.y, cost));

                                            }
                                        }
                                        break;
                                    case "destroyer": //fires a shot that scales on missing health
                                        validLocations = gp.getBoard().possibleFireLoc(ship);
                                        cost = 10;
                                        for (Point point : validLocations) {
                                            if (cost <= pointsLeft) {
                                                gp.panels.add(new Marker(gp.getContext(), gp, 4, ship, point.x, point.y, cost));

                                            }
                                        }
                                        break;
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
