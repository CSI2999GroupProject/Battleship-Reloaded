package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
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
    private Paint panelPaint, selectedPaint;
    private Bitmap aircraftcarrier, battleship, cruiser, destroyer, submarine, turnLeft, turnRight;
    private Bitmap lastButtonPress, selected;
    private boolean isHorizontal = true, direction = true;
    private String selectedShip = "";
    private Point lastMotion = new Point(0, 0);

    public FleetBuildPanel(Context context, GameBoard board)
    {
        this.context = context;
        this.board = board;

        panel = new Rect();
        panel.set(0, 0, 160, 800);
        panelPaint = new Paint();
        panelPaint.setColor(Color.GRAY);

        turnLeftBox = new Rect(16, 720, 80, 784);
        turnLeft = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_left_button), 64, 64, false);

        turnRightBox = new Rect(80, 720, 144, 784);
        turnRight = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_right_button), 64, 64, false);

        selectedPaint = new Paint();
        selectedPaint.setAlpha(175);

        aircraftcarrierBox = new Rect();
        aircraftcarrierBox.set(16, 16, 16 + 128, 16 + 26);
        aircraftcarrier = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.aircraftcarrier), 128, 26, false);

        battleshipBox = new Rect();
        battleshipBox.set(16, 160, 16 + 128, 160 + 43);
        battleship = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.battleship), 128, 43, false);

        cruiserBox = new Rect();
        cruiserBox.set(16, 304, 16 + 128, 304 + 64);
        cruiser = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser), 128, 64, false);
        
        destroyerBox = new Rect();
        destroyerBox.set(16, 448, 16 + 128, 448 + 32);
        destroyer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer), 128, 32, false);

        submarineBox = new Rect();
        submarineBox.set(16, 592, 16 + 128, 592 + 43);
        submarine = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine), 128, 43, false);

        buttons.add(cruiserBox);
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
        if(selected != null)
        {
            if(isHorizontal)
                if(direction)
                    canvas.drawBitmap(selected, lastMotion.x - selected.getWidth() + 64, lastMotion.y - 64, selectedPaint);
                else
                    canvas.drawBitmap(selected, lastMotion.x - 64, lastMotion.y - 64, selectedPaint);
            else
                if(direction)
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
            case MotionEvent.ACTION_DOWN:
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
                    selectedShip = "Cruiser";
                }
                else if(destroyerBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(destroyer, 512, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(destroyer, 128, 512, false);
                    selectedShip = "Destroyer";
                }
                else if(submarineBox.contains(x, y))
                {
                    if(isHorizontal)
                        selected = Bitmap.createScaledBitmap(submarine, 384, 128, false);
                    else
                        selected = Bitmap.createScaledBitmap(submarine, 128, 384, false);
                    selectedShip = "Submarine";
                }
                else if(turnLeftBox.contains(x, y))
                {
                    lastButtonPress = turnLeft;
                }
                else if(turnRightBox.contains(x, y))
                {
                    lastButtonPress = turnRight;
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
                            board.addShip(ship, row, column);
                            System.out.println("Added an aircraft carrier at (" + row + ", " + column + ")");
                            break;
                        case "Battleship":
                            ship = new Battleship(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShip(ship, row, column);
                            System.out.println("Added a battleship at (" + row + ", " + column + ")");
                            break;
                        case "Cruiser":
                            ship = new Cruiser(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShip(ship, row, column);
                            System.out.println("Added a cruiser at (" + row + ", " + column + ")");
                            break;
                        case "Destroyer":
                            ship = new Destroyer(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShip(ship, row, column);
                            System.out.println("Added a destroyer at (" + row + ", " + column + ")");
                            break;
                        case "Submarine":
                            ship = new Submarine(context, row, column);
                            ship.setHorizontal(isHorizontal);
                            ship.setDirection(direction);
                            ship.applyRotate();
                            board.addShip(ship, row, column);
                            System.out.println("Added a submarine at (" + row + ", " + column + ")");
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
