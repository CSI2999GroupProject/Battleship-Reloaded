package sophomoreproject.battleship;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.HashSet;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by isaac on 1/31/2018.
 */
public class GameBoard implements GameBoardInterface, Panel {

    private Ship[][] board;
    private int boardRows;
    private int boardColumns;
    private static final int DEFAULT_ROWS = 16;
    private static final int DEFAULT_COLUMNS = 24;
    private HashSet<Ship> shipSet;
    private Rect waterBox = new Rect();
    private Drawable waterImage;
    private boolean isScrolling = false;
    private Point locator = new Point(0, 0);
    private Point masterPoint = new Point(0, 0);

    public GameBoard(Context context)
    {
        boardRows = DEFAULT_ROWS;
        boardColumns = DEFAULT_COLUMNS;
        board = new Ship[boardRows][boardColumns];
        shipSet = new HashSet<Ship>();
        waterBox.set(0, 0, 128*24, 128*16);
        waterImage = context.getResources().getDrawable(R.drawable.water_old);
        waterImage.setBounds(waterBox);
    }

    public int getBoardRows() {
        return boardRows;
    }

    public void setBoardRows(int boardRows) {
        this.boardRows = boardRows;
    }

    public int getBoardColumns() {
        return boardColumns;
    }

    public void setBoardColumns(int boardColumns) {
        this.boardColumns = boardColumns;
    }

    public HashSet<Ship> getShipSet() {
        return shipSet;
    }

    /**
     * A method to add a cruiser to the board. If the shipSize is greater than 1, then depending on the direction of the Ship,
     * the Ship[][] will add multiple of the same object in the corresponding spot.
     *
     * After it adds the cruiser to board, it adds the front of the cruiser to the HashSet shipSet and sets the Ship's coordinates.
     *
     * Still needs checks on the boundaries of the array
     *
     * Will be updated when user input is available
     */
    @Override
    public void addShip(Ship aShip, int xPos, int yPos) {
        int shipSize = aShip.getShipSize();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if (shipSize > 1) {
            if (isHorizontal) {
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {
                        board[yPos][xPos - i] = aShip;
                    }
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        board[yPos][xPos + i] = aShip;
                    }
                }
            } else if (!isHorizontal) {
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {
                        board[yPos - i][xPos] = aShip;
                    }
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        board[yPos + i][xPos] = aShip;
                    }
                }
            }
        } else {
            board[yPos][xPos] = aShip;
        }
        shipSet.add(aShip);
        aShip.setRowCoord(yPos);
        aShip.setColumnCoord(xPos);
    }

    /**
     * A method to move the ships in the board
     */
    @Override
    public void move() {
        //Your implementation here
    }

    /**
     * A method to rotate the ships in the board
     */
    @Override
    public void rotate() {
        //Your implementation here
    }

    public void update()
    {

    }

    /**
     * A method to update the map's position on the board
     *
     * @param point where the user has dragged the top-left corner of the map
     */
    public void update(Point point)
    {
        waterBox.set(point.x, point.y, point.x + 128*24, point.y + 128*16);
        masterPoint = point;
        waterImage.setBounds(waterBox);
    }

    /**
     * A method to draw the map and its contents onto the screen during a cycle of the game loop
     *
     * @param canvas the main canvas of the game
     */
    public void draw(Canvas canvas) {
        waterImage.draw(canvas);
    }

    public boolean contains(Point point)
    {
        return waterBox.contains(point.x, point.y);
    }

    public void onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locator.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                final int SCROLL_TOLERANCE = 10;
                //Only scroll if the user has moved their finger more than SCROLL_TOLERANCE pixels
                if (Math.abs(locator.x - event.getX()) > SCROLL_TOLERANCE
                        || Math.abs(locator.y - event.getY()) > SCROLL_TOLERANCE
                        || isScrolling) {
                    isScrolling = true;
                    masterPoint.set(masterPoint.x + (int) event.getX() - locator.x, masterPoint.y + (int) event.getY() - locator.y);
                    final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
                    final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
                    final int MIN_GRID_SPACES = 5;


                    //Will not let the user scroll anymore if there are only MIN_GRID_SPACES left on the screen.
                    if (masterPoint.x > SCREEN_WIDTH - 128 * MIN_GRID_SPACES)
                        masterPoint.x = SCREEN_WIDTH - 128 * MIN_GRID_SPACES;
                    else if (masterPoint.x < -128 * (24 - MIN_GRID_SPACES))
                        masterPoint.x = -128 * (24 - MIN_GRID_SPACES);
                    if (masterPoint.y > SCREEN_HEIGHT - 128 * MIN_GRID_SPACES)
                        masterPoint.y = SCREEN_HEIGHT - 128 * MIN_GRID_SPACES;
                    else if (masterPoint.y < -128 * (16 - MIN_GRID_SPACES))
                        masterPoint.y = -128 * (16 - MIN_GRID_SPACES);

                    //update the locator to the current position of the finger
                    locator.set((int) event.getX(), (int) event.getY());
                    break;
                }
            case MotionEvent.ACTION_UP:
                isScrolling = false;
        }
    }
}
