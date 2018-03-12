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
    private int initialSeq = 0;

    public FleetBuildPanel(Context context, GameBoard board)
    {
        this.context = context;
        this.board = board;

        panel = new Rect();
        panel.set(0, 0, 420, 1100);
        panelPaint = new Paint();
        panelPaint.setColor(Color.GRAY);

        turnLeftBox = new Rect(16, 720+30, 80, 784+30);
        turnLeft = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_left_button), 64, 64, false);

        turnRightBox = new Rect(80, 720+30, 144, 784+30);
        turnRight = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_right_button), 64, 64, false);

        selectedPaint = new Paint();
        selectedPaint.setAlpha(175);

        aircraftcarrierBox = new Rect();
        aircraftcarrierBox.set(16, 16+30, 16 + 128, 16 + 26+30);
        aircraftcarrier = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.aircraftcarrier), 128, 26, false);

        battleshipBox = new Rect();
        battleshipBox.set(16, 160+30, 16 + 128, 160 + 43+30);
        battleship = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.battleship), 128, 43, false);

        cruiserBox = new Rect();
        cruiserBox.set(16, 304+30, 16 + 128, 304 + 64+30);
        cruiser = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cruiser), 128, 64, false);
        
        destroyerBox = new Rect();
        destroyerBox.set(16, 448+30, 16 + 128, 448 + 32+30);
        destroyer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.destroyer), 128, 32, false);

        submarineBox = new Rect();
        submarineBox.set(16, 592+30, 16 + 128, 592 + 43+30);
        submarine = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.submarine), 128, 43, false);

        finishBox = new Rect(16, 830, 16 + 388, 830 + 195);
        finishButton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.shipplacing_button_on), 388, 195, false);

        fleetPointsBox = new Rect(600, 1080 - 195, 520 + 600, 1080);
        fleetPointsDisplay = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.points_title), 520, 195, false);

        fontColor = new Paint();
        fontColor.setTextSize(55);
        fontColor.setColor(Color.WHITE);

        costFontColor = new Paint();
        costFontColor.setTextSize(40);
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
        canvas.drawText("" + board.getP1().getAvailablePoints(), 600 + 195, 1035, fontColor);
        canvas.drawText("" + board.getP2().getAvailablePoints(), 600 + 435, 1035, fontColor);
        canvas.drawText("4 points", 180, 65, costFontColor);
        canvas.drawText("2 points", 180, 225, costFontColor);
        canvas.drawText("1 point", 180, 370, costFontColor);
        canvas.drawText("3 points", 180, 505, costFontColor);
        canvas.drawText("2 points", 180, 655, costFontColor);

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
                    } else if(board.getPlayerTurn() == 1) {
                        board.setPlayerTurn(0);
                        initialSeq = 2;
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
