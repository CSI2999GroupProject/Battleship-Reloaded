package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

import sophomoreproject.battleship.ships.AircraftCarrier;
import sophomoreproject.battleship.ships.Battleship;
import sophomoreproject.battleship.ships.Cruiser;
import sophomoreproject.battleship.ships.Destroyer;
import sophomoreproject.battleship.ships.Ship;
import sophomoreproject.battleship.ships.Submarine;

/**
 * Created by Jacob on 2/20/2018.
 */

public class FleetBuildPanel implements Panel
{
    private Context context;
    private GameBoard board;
    private ArrayList<Rect> buttons = new ArrayList<>();
    private Rect panel, aircraftcarrierBox, battleshipBox, cruiserBox, destroyerBox, submarineBox, turnLeftBox, turnRightBox;
    private Rect finishBox, fleetPointsBox;
    private Paint panelPaint, selectedPaint, fontColor, costFontColor;
    private Bitmap aircraftcarrier, battleship, cruiser, destroyer, submarine, turnLeft, turnRight;
    private Bitmap lastButtonPress, selected;
    private Bitmap finishButton, fleetPointsDisplay;
    private boolean isHorizontal = true, direction = true;
    private String selectedShip = "";
    private Point lastMotion = new Point(0, 0);
    private int initialSeq = 0, h, w;

    public FleetBuildPanel(Context context, GameBoard board)
    {
        this.context = context;
        this.board = board;

        h = board.SCREEN_HEIGHT;
        w = board.SCREEN_WIDTH;

        panel = new Rect();
        panel.set(0, 0, w/5, h);
        panelPaint = new Paint();
        panelPaint.setColor(Color.GRAY);

        turnLeftBox = new Rect(panel.left + panel.width()/8, (int)(panel.height()*6/8+.5*(panel.height()/8 - panel.width()*2/8)), panel.left + panel.width()*3/8, (int)(panel.height()*7/8-.5*(panel.height()/8 - panel.width()*2/8)));
        turnLeft = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_left_button), turnLeftBox.width(), turnLeftBox.width(), false);

        turnRightBox = new Rect(panel.right - panel.width()*3/8, turnLeftBox.top, panel.right - panel.width()/8, turnLeftBox.bottom);
        turnRight = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_right_button), turnRightBox.width(), turnRightBox.width(), false);

        selectedPaint = new Paint();
        selectedPaint.setAlpha(175);

        aircraftcarrierBox = new Rect();
        aircraftcarrierBox.set(panel.width()/16, panel.height()/16 - panel.width()*7/(32*5) , panel.width()/2, panel.height()/16 + panel.width()*7/(32*5));
        aircraftcarrier = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.aircraftcarrier), aircraftcarrierBox.width(), aircraftcarrierBox.height(), false);

        battleshipBox = new Rect();
        battleshipBox.set(panel.width()/16, panel.height()*3/16 - panel.width()*7/(32*3) , panel.width()/2, panel.height()*3/16 + panel.width()*7/(32*3));
        battleship = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.battleship), battleshipBox.width(), battleshipBox.height(), false);

        cruiserBox = new Rect();
        cruiserBox.set(panel.width()/16, panel.height()*5/16 - panel.width()*7/(32*2) , panel.width()/2, panel.height()*5/16 + panel.width()*7/(32*2));
        cruiser = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser), cruiserBox.width(), cruiserBox.height(), false);

        destroyerBox = new Rect();
        destroyerBox.set(panel.width()/16, panel.height()*7/16 - panel.width()*7/(32*4) , panel.width()/2, panel.height()*7/16 + panel.width()*7/(32*4));
        destroyer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer), destroyerBox.width(), destroyerBox.height(), false);

        submarineBox = new Rect();
        submarineBox.set(panel.width()/16, panel.height()*9/16 - panel.width()*7/(32*3) , panel.width()/2, panel.height()*9/16 + panel.width()*7/(32*3));
        submarine = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine), submarineBox.width(), submarineBox.height(), false);

        finishBox = new Rect(panel.width()/8, panel.height()*7/8, panel.width()*7/8, panel.height()*7/8 + panel.width()*3/8);
        finishButton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shipplacing_button_on), finishBox.width(), finishBox.height(), false);

        fleetPointsBox = new Rect(w*3/8, h - (w * 3)/(4 * 8) , w*5/8, h);
        fleetPointsDisplay = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.points_title), fleetPointsBox.width(), fleetPointsBox.height(), false);

        fontColor = new Paint();
        fontColor.setTextSize(fleetPointsBox.height()/6);
        fontColor.setColor(Color.WHITE);
        fontColor.setTextAlign(Paint.Align.CENTER);

        costFontColor = new Paint();
        costFontColor.setTextSize(aircraftcarrierBox.height());
        costFontColor.setColor(Color.BLUE);

        buttons.add(cruiserBox);


    }

    public int getInitialSeq() {
        return initialSeq;
    }

    public void setInitialSeq(int initialSeq) {
        this.initialSeq = initialSeq;
    }

    /**
     * A method to update the panel (currently unused)
     */
    public void update()
    {

    }

    /**
     * A method to draw the panel onto the screen
     *
     * @param canvas the main canvas of the screen
     */
    public void draw(Canvas canvas)
    {

        canvas.drawRect(panel, panelPaint);

        canvas.drawBitmap(aircraftcarrier, aircraftcarrierBox.left, aircraftcarrierBox.top, null);
        canvas.drawBitmap(battleship, battleshipBox.left, battleshipBox.top, null);
        canvas.drawBitmap(cruiser, cruiserBox.left, cruiserBox.top, null);
        canvas.drawBitmap(destroyer, destroyerBox.left, destroyerBox.top, null);
        canvas.drawBitmap(submarine, submarineBox.left, submarineBox.top, null);
        canvas.drawBitmap(turnLeft, turnLeftBox.left, turnLeftBox.top, null);
        canvas.drawBitmap(turnRight, turnRightBox.left, turnRightBox.top, null);
        canvas.drawBitmap(finishButton, finishBox.left, finishBox.top, null);
        canvas.drawBitmap(fleetPointsDisplay, fleetPointsBox.left, fleetPointsBox.top, null);
        canvas.drawText("" + board.getP1().getAvailablePoints(), fleetPointsBox.left + (fleetPointsBox.right - fleetPointsBox.left)*2/5, fleetPointsBox.top + (fleetPointsBox.bottom - fleetPointsBox.top)*35/48, fontColor);
        canvas.drawText("" + board.getP2().getAvailablePoints(), fleetPointsBox.left + (fleetPointsBox.right - fleetPointsBox.left)*35/40, fleetPointsBox.top + (fleetPointsBox.bottom - fleetPointsBox.top)*35/48, fontColor);
        canvas.drawText("4 points", aircraftcarrierBox.right + panel.width()/16, aircraftcarrierBox.centerY() + costFontColor.getTextSize()/2, costFontColor);
        canvas.drawText("2 points", battleshipBox.right + panel.width()/16, battleshipBox.centerY() + costFontColor.getTextSize()/2, costFontColor);
        canvas.drawText("1 point", cruiserBox.right + panel.width()/16, cruiserBox.centerY() + costFontColor.getTextSize()/2, costFontColor);
        canvas.drawText("3 points", destroyerBox.right + panel.width()/16, destroyerBox.centerY() + costFontColor.getTextSize()/2, costFontColor);
        canvas.drawText("2 points", submarineBox.right + panel.width()/16, submarineBox.centerY() + costFontColor.getTextSize()/2, costFontColor);

        if (selected != null) {
            if (isHorizontal)
                if (direction)
                    canvas.drawBitmap(selected, lastMotion.x - selected.getWidth() + 64, lastMotion.y - 64, selectedPaint);
                else
                    canvas.drawBitmap(selected, lastMotion.x - 64, lastMotion.y - 64, selectedPaint);
            else if (direction)
                canvas.drawBitmap(selected, lastMotion.x - 64, lastMotion.y - 64, selectedPaint);
            else
                canvas.drawBitmap(selected, lastMotion.x - 64, lastMotion.y - selected.getHeight() + 64, selectedPaint);
        }

    }

    private Bitmap applyRotate(Bitmap image, int degrees)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, false);

    }

    private Rect adjustClickBox(Rect r)
    {
        int w = r.height();
        int h = r.width();

        r.set(r.left, r.top, r.left + w, r.top + h);
        return r;
    }

    public boolean contains(Point point)
    {
        return panel.contains(point.x, point.y);
    }

    public void onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();
        lastMotion.set(x, y);

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN: //What happens after any of the Rect is clicked



                if(aircraftcarrierBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(aircraftcarrier, 640, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(aircraftcarrier, 128, 640, false);
                    selectedShip = "Aircraft Carrier";
                }
                else if(battleshipBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(battleship, 384, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(battleship, 128, 384, false);
                    selectedShip = "Battleship";
                }
                else if(cruiserBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(cruiser, 256, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(cruiser, 128, 256, false);
                    selectedShip = "cruiser";
                }
                else if(destroyerBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(destroyer, 512, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(destroyer, 128, 512, false);
                    selectedShip = "destroyer";
                }
                else if(submarineBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(submarine, 384, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(submarine, 128, 384, false);
                    selectedShip = "submarine";
                }
                else if(turnLeftBox.contains(x, y))
                {
                    lastButtonPress = turnLeft;
                }
                else if(turnRightBox.contains(x, y))
                {
                    lastButtonPress = turnRight;
                } else if(finishBox.contains(x, y)) { //
                    lastButtonPress = finishButton;
                }





            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                selected = null;






                if(lastButtonPress == turnLeft && turnLeftBox.contains(x, y))
                {
                    aircraftcarrier = applyRotate(aircraftcarrier, -90);
                    aircraftcarrierBox = adjustClickBox(aircraftcarrierBox);
                    battleship = applyRotate(battleship, -90);
                    battleshipBox = adjustClickBox(battleshipBox);
                    cruiser = applyRotate(cruiser, -90);
                    cruiserBox = adjustClickBox(cruiserBox);
                    destroyer = applyRotate(destroyer, -90);
                    destroyerBox = adjustClickBox(destroyerBox);
                    submarine = applyRotate(submarine, -90);
                    submarineBox = adjustClickBox(submarineBox);
                    if(isHorizontal)
                    {
                        isHorizontal = false;
                    }
                    else
                    {
                        isHorizontal = true;
                        direction = !direction;
                    }
                }
                else if(lastButtonPress == turnRight && turnRightBox.contains(x, y))
                {
                    aircraftcarrier = applyRotate(aircraftcarrier, 90);
                    aircraftcarrierBox = adjustClickBox(aircraftcarrierBox);
                    battleship = applyRotate(battleship, 90);
                    battleshipBox = adjustClickBox(battleshipBox);
                    cruiser = applyRotate(cruiser, 90);
                    cruiserBox = adjustClickBox(cruiserBox);
                    destroyer = applyRotate(destroyer, 90);
                    destroyerBox = adjustClickBox(destroyerBox);
                    submarine = applyRotate(submarine, 90);
                    submarineBox = adjustClickBox(submarineBox);
                    if(isHorizontal)
                    {
                        isHorizontal = false;
                        direction = !direction;
                    }
                    else
                    {
                        isHorizontal = true;
                    }
                } else if(lastButtonPress == finishButton && finishBox.contains(x, y)) {
                    if(board.getPlayerTurn() == 0) {
                        board.setPlayerTurn(1);
                        board.getMasterPoint().set(-128*12, 0);
                    } else if(board.getPlayerTurn() == 1) {
                        board.setPlayerTurn(0);
                        board.getMasterPoint().x = -128 * board.xPosOfShip(board.getP1()) - board.VIEW_RANGE + board.SCREEN_WIDTH;
                        initialSeq = 2;
                        board.setPoints(Player.POINTS_PER_TURN);
                    }

                }





                lastButtonPress = null;

                if(panel.contains(x, y))
                    break;

                int row = (x - board.getMasterPoint().x)/128;
                int column = (y - board.getMasterPoint().y)/128;




                try
                {
                    Ship ship;

                    switch(selectedShip)
                    {
                        case "Aircraft Carrier":
                            ship = new AircraftCarrier(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShipWithCost(ship, row, column);
                            break;
                        case "Battleship":
                            ship = new Battleship(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShipWithCost(ship, row, column);
                            break;
                        case "cruiser":
                            ship = new Cruiser(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShipWithCost(ship, row, column);
                            break;
                        case "destroyer":
                            ship = new Destroyer(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShipWithCost(ship, row, column);
                            break;
                        case "submarine":
                            ship = new Submarine(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShipWithCost(ship, row, column);
                            break;
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Failed to place a ship at (" + row + ", " + column + ") because: " + e.getMessage());
                }

                selectedShip = "";
        }
    }
}
