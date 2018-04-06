package sophomoreproject.battleship;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    private HashSet<Point> mineSet;
    private Rect waterBox;
    private Drawable waterImage;
    private boolean isScrolling = false;
    public boolean ready = false; //ready should be set to true when it is the user's turn, and they aren't placing ships anymore. Otherwise be false
    private Point locator = new Point(0, 0);
    private Point masterPoint;
    private Context context;
    private GamePanel gp;
    private int playerTurn;
    private Player p1, p2;
    private TextView winningText = null;
    private Layout winningScreen = null;
    public final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels; //width of the screen, not board
    public final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int VIEW_RANGE = 128*3;


    public GameBoard(Context context, GamePanel gp)
    {
        this.context = context;
        this.gp = gp;
        boardRows = DEFAULT_ROWS;
        boardColumns = DEFAULT_COLUMNS;
        board = new Ship[boardRows][boardColumns];
        shipSet = new HashSet<>();
        mineSet = new HashSet<>();
        masterPoint = new Point(0, 0); //Makes sure that player 1's fleet build panel camera doesn't start showing part of P2's board
        waterBox = new Rect();
        waterBox.set(0, 0, 128 * 24, 128 * 16);
        waterImage = context.getResources().getDrawable(R.drawable.water_old);
        waterImage.setBounds(waterBox);
        p1 = new Player();
        p2 = new Player();
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

    Point getMasterPoint() {
        return masterPoint;
    }

    public HashSet<Ship> getShipSet() {
        return shipSet;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getPlayerTurn() { return playerTurn; }

    public void setPlayerTurn(int playerTurn) { this.playerTurn = playerTurn; }

    public Ship[][] getShipBoard() {
        return board;
    }

    public HashSet<Point> getMineSet() { return mineSet; }

    /**
     * A method that runs identically to the addShip method, but costs points to place.
     * Intended to be used only when placing the ships from the FleetBuildPanel.
     */
    public void addShipWithCost(Ship aShip, int xPos, int yPos)
    {

        if(playerTurn == 0 && xPos > 11) {
            throw new IllegalArgumentException("It's player 1's turn and you're trying to place into player 2's territory");
        } else if(playerTurn == 1 && xPos < 12) {
            throw new IllegalArgumentException("It's player 2's turn and you're trying to place into player 1's territory");
        }

        if(playerTurn == 0) {
            if(hasEnoughPoints(aShip, p1)) {
                p1.setAvailablePoints(p1.getAvailablePoints() - aShip.getShipCost());
                aShip.setPlayer(1);
            } else {
                throw new IllegalStateException("player 1 doesn't have enough points");
            }
        }
        if(playerTurn == 1) {
            if(hasEnoughPoints(aShip, p2)) {
                p2.setAvailablePoints(p2.getAvailablePoints() - aShip.getShipCost());
                aShip.setPlayer(2);
            }else {
                throw new IllegalStateException("player 2 doesn't have enough points");
            }
        }

        try
        {
            addShip(aShip, xPos, yPos);
        }
        catch(Exception e) //Ship couldn't be added, refund the points
        {
            if(playerTurn == 0)
                p1.setAvailablePoints(p1.getAvailablePoints() + aShip.getShipCost());
            else
                p2.setAvailablePoints(p2.getAvailablePoints() + aShip.getShipCost());
        }
    }

public int getPoints() {
    if (playerTurn == 0) {
            return p1.getAvailablePoints();
        } else {
            return p2.getAvailablePoints();
        }
    }

    public void setPoints(int points){
        if(playerTurn == 0) {
            p1.setAvailablePoints(points);

        }else{
            p2.setAvailablePoints(points);
        }
    }
    /**
     * A method that runs identically to the removeShip method, except it refunds points to the player.
     * Intended to be used only when removing ships when first creating a fleet using FleetBuildPanel.
     */
    public void removeShipWithCost(Ship aShip)
    {
        removeShip(aShip);

        if(playerTurn == 0)
            p1.setAvailablePoints(p1.getAvailablePoints() + aShip.getShipCost());
        else
            p2.setAvailablePoints(p2.getAvailablePoints() + aShip.getShipCost());
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
    public void addShip(Ship aShip, int xPos, int yPos) throws IndexOutOfBoundsException {
        int shipSize = aShip.getShipSize();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        /*if (xPos < 0 || yPos < 0) {
            throw new IllegalArgumentException("There is no negative position on the board");
        }
        if (xPos > boardColumns || yPos > boardRows) {
            throw new IllegalArgumentException("Can't place a ship beyond the board's boundaries A");
        }
        if (!checkIndexBoundaries(aShip)) {
            throw new IllegalStateException("Can't place a ship beyond the board's boundaries B");
        }*/
        if(!checkPlacementInEnemy(aShip, xPos)) {
            throw new IllegalArgumentException("checkPlacementInEnemy returns false");
        }
        if (nullCountOfShipSize(shipSize, xPos, yPos, isHorizontal, direction) != shipSize) {
            throw new IllegalStateException("There is already a ship there");
        }

        aShip.setRowCoord(yPos);
        aShip.setColumnCoord(xPos);

        if (shipSize > 1) {
            if (isHorizontal) {
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {
                        if (board[yPos][xPos - i] == null) {
                            board[yPos][xPos - i] = aShip;
                        }
                    }
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (board[yPos][xPos + i] == null) {
                            board[yPos][xPos + i] = aShip;
                        }
                    }
                }
            } else {
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {
                        if (board[yPos + i][xPos] == null) {
                            board[yPos + i][xPos] = aShip;
                        }

                    }
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (board[yPos - i][xPos] == null) {
                            board[yPos - i][xPos] = aShip;
                        }
                    }
                }
            }
        } else {
            board[yPos][xPos] = aShip;
        }
        shipSet.add(aShip);
        if(playerTurn == 0) {
            p1.getPlayerSet().add(aShip);
        } else {
            p2.getPlayerSet().add(aShip);
        }
    }

    /**
     * possibleMoveLoc(Ship aShip)
     * @param aShip the ship that is tapped to see where it can move to.
     * @return an ArrayList of Coordinates, the Coordinates have a x and y int as its only private variables.
     *          The x and y ints in this function are the possible spots the ship can move to.
     */
    public ArrayList<Point> possibleMoveLoc(Ship aShip) {
        ArrayList<Point> coordinateList = new ArrayList<>();
        Point point;
        int numOfMoves = aShip.getnMove();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();
        int xPos = aShip.getColumnCoord();
        int yPos = aShip.getRowCoord();

        if (isHorizontal && direction) {       //Facing East
            for (int i = 1; i <= numOfMoves; i++) {
                if(xPos + i < 24) {
                    if(board[yPos][xPos + i] == null){
                        coordinateList.add(new Point(xPos + i, yPos));
                    } else if (board[yPos][xPos + i] != null) {
                        break;
                    }
                } else {
                    break;
                }


            }
        }
        else if (isHorizontal && !direction)                  //West
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if (xPos - i >= 0) {
                    if (board[yPos][xPos - i] == null) {
                        point = new Point(xPos - i, yPos);
                        coordinateList.add(point);
                    } else if (board[yPos][xPos - i] != null) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        else if (direction)                     //North
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if (yPos - i >= 0) {
                    if(board[yPos - i][xPos] == null) {
                        point = new Point(xPos, yPos - i);
                        coordinateList.add(point);
                    } else if(board[yPos - i][xPos] != null) {
                        break;
                    }
                }
                else
                    break;
            }
        }
        else                                    //South
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if (yPos + i < 16) {
                    if(board[yPos + i][xPos] == null) {
                        point = new Point(xPos, yPos + i);
                        coordinateList.add(point);
                    } else if(board[yPos + i][xPos] != null) {
                        break;
                    }
                }
                else
                    break;
            }
        }
        return coordinateList;
    }

    /**
     * possibleFireLoc(Ship aShip)
     * @param aShip The ship that is going to shoot
     * @return an ArrayList of Coordinates, every coordinate where you can shoot to.
     *          Note: It does not store coordinates on your side of the map, as you can't shoot
     *          your own ships.
     */
    public ArrayList<Point> possibleFireLoc(Ship aShip) {
        ArrayList<Point> coordinateList = new ArrayList<>();
        int fireRange = aShip.getFrange();
        int xPos = aShip.getColumnCoord();
        int yPos = aShip.getRowCoord();
        int minX, maxX;

        if(playerTurn == 0)
        {
            minX = Math.max(xPos - fireRange, 0); //Don't check to the left of the board
            maxX = Math.min( Math.min(xPos + fireRange, lastViewableColumn() ), 23); //Farthest to the right you can shoot is the smallest of the range, the view, and the board.
            System.out.println("Farthest check is column " + lastViewableColumn());
        }
        else
        {
            minX = Math.max( Math.max(xPos - fireRange, lastViewableColumn() ), 0); //Farthest to the left you can shoot is the largest of the range, the view, and the board.
            maxX = Math.min(xPos + fireRange, 23); //Don't check to the right of the board
            System.out.println("Farthest check is column " + lastViewableColumn());
        }

        for(int i = minX; i <= maxX; i++) //Search within a horizontal range of fireRange from front of ship, unless it's off the board
        {
            for(int j = Math.max(yPos - fireRange, 0); j <= Math.min(yPos + fireRange, 15); j++) //Same thing as the previous for() statement, but vertical.
            {
                if(board[j][i] != null && board[j][i].getPlayer() != getPlayerTurn()+1)
                {
                    coordinateList.add(new Point(i, j));
                }
            }
        }

        /*for(int i = 0; i <= fireRange; i++) {
            for(int j = 0; j <= fireRange; j++) {

                pointBottomRight = new Point(xPos + i, yPos + j);
                if(pointBottomRight.y <= 15 && pointBottomRight.y >= 0 && pointBottomRight.x >= 0 && pointBottomRight.x <= 23) {
                    coordinateList.add(pointBottomRight);
                }
                pointBottomLeft = new Point(xPos - i, yPos + j);
                if(pointBottomLeft.y <= 15 && pointBottomLeft.y >= 0 && pointBottomLeft.x >= 0 && pointBottomLeft.x <= 23) {
                    coordinateList.add(pointBottomLeft);
                }
                pointUpperRight = new Point(xPos + i, yPos - j);
                if(pointUpperRight.y <= 15 && pointUpperRight.y >= 0 && pointUpperRight.x >= 0 && pointUpperRight.x <= 23) {
                    coordinateList.add(pointUpperRight);
                }
                pointUpperLeft = new Point(xPos - i, yPos - j);
                if(pointUpperLeft.y <= 15 && pointUpperLeft.y >= 0 && pointUpperLeft.x >= 0 && pointUpperLeft.x <= 23) {
                    coordinateList.add(pointUpperLeft);
                }
            }
        }*/

        return coordinateList;
    }

    /**
     * checkRotate(Ship aShip),
     * @param aShip The ship that will be rotated
     * @return coordinateList, an Array of Point in which its size depends on the shipSize.
     *          The contents of the array are the points in which the ship can possibly rotate to depending on isHorizontal boolean
     *
     *          The size is 2. so pointsArray[0] is left, and pointsArray[1] is right.
     *          By left and right, I mean if you are facing the same direction as the ship. AKA from the Ships point of view: left and right.
     *
     *
     */
    public Point[] checkRotate(Ship aShip) {
        int shipSize = aShip.getShipSize();
        int xPos = aShip.getColumnCoord();
        int yPos = aShip.getRowCoord();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();
        Point[] pointsArray = new Point[2];
        int count = 0;

        if(isHorizontal && direction) { //east
            for(int i = 1; i < shipSize; i++) {
                if(yPos - i >= 0) {
                    if (board[yPos - i][xPos] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && yPos - shipSize + 1 >= 0) {
                pointsArray[0] = new Point(xPos, yPos - 1);
            }
            count = 0;
            for(int i = 1; i < shipSize; i++) {
                if(yPos + i < 16){
                    if (board[yPos + i][xPos] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && yPos + shipSize - 1 < 16) {
                pointsArray[1] = new Point(xPos, yPos + 1);
            }
        } else if(!isHorizontal && !direction) { //south
            for(int i = 1; i < shipSize; i++) {
                if(xPos + i < 24) {
                    if (board[yPos][xPos + i] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && xPos + shipSize - 1 < 24) {
                pointsArray[0] = new Point(xPos + 1, yPos);
            }
            count = 0;
            for(int i = 1; i < shipSize; i++) {
                if(xPos - i >= 0) {
                    if (board[yPos][xPos - i] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && xPos - shipSize + 1 >= 0) {
                pointsArray[1] = new Point(xPos - 1, yPos);
            }
        } else if(isHorizontal && !direction) { //west
            for(int i = 1; i < shipSize; i++) {
                if(yPos + i < 16) {
                    if (board[yPos + i][xPos] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && yPos + shipSize - 1 < 16) {
                pointsArray[0] = new Point(xPos, yPos + 1);
            }
            count = 0;
            for(int i = 1; i < shipSize; i++) {
                if(yPos - i >= 0) {
                    if (board[yPos - i][xPos] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && yPos - shipSize + 1 >= 0) {
                pointsArray[1] = new Point(xPos, yPos - 1);
            }
        } else if(!isHorizontal && direction) { //north
            for(int i = 1; i < shipSize; i++) {
                if(xPos - i >= 0) {
                    if (board[yPos][xPos - i] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && xPos - shipSize + 1 >= 0) {
                pointsArray[0] = new Point(xPos - 1, yPos);
            }
            count = 0;
            for(int i = 1; i < shipSize; i++) {
                if(xPos + i < 24) {
                    if (board[yPos][xPos + i] != null) {
                        count++;
                    }
                }
            }
            if(count == 0 && xPos + shipSize - 1 < 24) {
                pointsArray[1] = new Point(xPos + 1, yPos);
            }
        }
        return pointsArray;
    }

    /**
     * this method removes the ship fro the board so that the other methods can adjust its location or sink it
     * @param aShip the ship that gets removed
     */
    public void removeShip(Ship aShip)
    {
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();
        int shipSize = aShip.getShipSize();
        int xPos = aShip.getColumnCoord();
        int yPos =aShip.getRowCoord();

        shipSet.remove(aShip);

        if(playerTurn == 0) {
            p1.getPlayerSet().remove(aShip);
        } else {
            p2.getPlayerSet().remove(aShip);
        }

        if (isHorizontal && direction)          //Facing East
        {
            for (int i = 0; i < shipSize; i++)
                board[yPos][xPos - i] = null;
        }
        else if (isHorizontal)                  //West
        {
            for (int i = 0; i < shipSize; i++)
                board[yPos][xPos + i] = null;
        }
        else if (direction)                     //North
        {
            for (int i = 0; i < shipSize; i++)
                board[yPos + i][xPos] = null;
        }
        else                                    //South
        {
            for (int i = 0; i < shipSize; i++)
                board[yPos - i][xPos] = null;
        }
    }
    /**
     * method that moves the ship on the board
     * @param aShip the ship that gets placed
     * @param xPos the position of the head of the ships X
     * @param yPos the position of the head of the ships Y
     * @param pmove the amount of spaces the ship will move
     */
    public void move(Ship aShip, int xPos, int yPos, int pmove) {
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        removeShip(aShip);

        if (isHorizontal && direction)          //Facing East
        {
            RaddShip(aShip, xPos + pmove, yPos);
            for(Point p : mineSet) {
                if(xPos + pmove == p.x && yPos == p.y) {
                    aShip.damageShip(500);
                }
            }
        }
        else if (isHorizontal)                  //West
        {
            RaddShip(aShip, xPos - pmove, yPos);
            for(Point p : mineSet) {
                if(xPos - pmove == p.x && yPos == p.y) {
                    aShip.damageShip(500);
                }
            }
        }
        else if (direction)                     //North
        {
            RaddShip(aShip, xPos, yPos - pmove);
            for(Point p : mineSet) {
                if(xPos == p.x && yPos - pmove == p.y) {
                    aShip.damageShip(500);
                }
            }
        }
        else                                    //South
        {
            RaddShip(aShip, xPos, yPos + pmove);
            for(Point p : mineSet) {
                if(xPos == p.x && yPos + pmove == p.y) {
                    aShip.damageShip(500);
                }
            }
        }
    }

    /**
     * adds the ships to the board after move and rotate
     * @param aShip the ship that gets placed
     * @param xPos the position of the head of the ships X
     * @param yPos the position of the head of the ships Y
     * @throws IndexOutOfBoundsException
     */
    public void RaddShip(Ship aShip, int xPos, int yPos) throws IndexOutOfBoundsException {
        int shipSize = aShip.getShipSize();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        aShip.setRowCoord(yPos);
        aShip.setColumnCoord(xPos);
        if (shipSize > 1){
            if (isHorizontal){
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {

                        board[yPos][xPos - i] = aShip;
                    }
                }else if (!direction){
                    for (int i = 0; i < shipSize; i++){
                        board[yPos][xPos + i] = aShip;
                    }
                }
            }else{
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {

                        board[yPos + i][xPos] = aShip;


                    }
                } else {
                    for (int i = 0; i < shipSize; i++) {

                        board[yPos - i][xPos] = aShip;

                    }
                }}
        } else {
            board[yPos][xPos] = aShip;
        }
        shipSet.add(aShip);
        if(playerTurn == 0) {
            p1.getPlayerSet().add(aShip);
        } else {
            p2.getPlayerSet().add(aShip);
        }

    }

    /**
     * method that rotates the ship left
     * @param aShip the ship that gets placed
     * @param xPos the position of the head of the ships X
     * @param yPos the position of the head of the ships Y
     */
    @Override
    public void rotateLeft(Ship aShip, int xPos, int yPos) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();

        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        removeShip(aShip);
        if (isHorizontal && direction)                //Facing  East
        {
            aShip.setHorizontal(false);
            aShip.setDirection(true);
            shipY=shipY-shipSize+1;
        }else if (isHorizontal)                //Facing West
        {
            aShip.setHorizontal(false);
            shipY=shipY+shipSize-1;
        } else if (direction)             //Facing North
        {
            aShip.setHorizontal(true);
            aShip.setDirection(false);
            shipX=shipX-shipSize+1;
        } else                            //Facing South
        {
            aShip.setHorizontal(true);
            aShip.setDirection(true);
            shipX=shipX+shipSize-1;
        }
        aShip.applyRotateL(-90);

        RaddShip(aShip, shipX, shipY);

    }

    /**
     * method that rotates the ship Right
     * @param aShip the ship that gets placed
     * @param xPos the position of the head of the ships X
     * @param yPos the position of the head of the ships Y
     */
    @Override
    public void rotateRight(Ship aShip, int xPos, int yPos) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();

        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        removeShip(aShip);

        if (isHorizontal && direction)                //Facing West or East
        {
            aShip.setHorizontal(false);
            aShip.setDirection(false);
            shipY=shipY+shipSize-1;
        }else if (isHorizontal)                //Facing West or East
        {
            aShip.setHorizontal(false);
            aShip.setDirection(true);
            shipY=shipY-shipSize+1;
        } else if (direction)             //Facing North
        {
            aShip.setHorizontal(true);

            shipX=shipX+shipSize-1;
        } else                            //Facing South
        {
            aShip.setHorizontal(true);

            shipX=shipX-shipSize+1;
        }

        aShip.applyRotateL(90);

        RaddShip(aShip, shipX, shipY);
    }
    /**
     * A method to see if a player has enough points to place a ship on the board
     *
     * @param aShip  the ship to be placed so we can get it's cost
     * @param player player 1 or player 2
     * @return true if they have enough, false if they don't
     */
    public boolean hasEnoughPoints(Ship aShip, Player player) {
        int playerPoints = player.getAvailablePoints();
        int shipCost = aShip.getShipCost();

        if (playerPoints - shipCost < 0) {
            return false;
        }

        return true;
    }

    /**
     * A method to see if your ship would be placed within the enemy's area
     *
     * @param aShip the ship to be placed
     * @param x     the x position of the head of the ship
     * @return true if it WOULD NOT go in enemy territory (across red line),
     * false if it WOULD go in enemy territory
     */
    public boolean checkPlacementInEnemy(Ship aShip, int x) {
        int shipSize = aShip.getShipSize();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if (playerTurn == 0 && !direction && isHorizontal) {
            if (shipSize + x > 12) {
                return false;
            }
        } else if (playerTurn == 1 && direction && isHorizontal) {
            if (x - shipSize < 11) {
                return false;
            }
        }
        return true;
    }
    /**
     * A boolean that checks if the area around the ship would go outside the index of the board.
     * @return true if it does not go outside the index of the board. false if it would.
     */

    @Override
    public boolean checkIndexBoundaries(Ship aShip) {
        int shipSize = aShip.getShipSize();
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if (direction && !isHorizontal && shipY < shipSize - 1) {
            return false;
        } else if (!direction && !isHorizontal && shipY + (shipSize - 1) > boardColumns) {
            return false;
        } else if (direction && isHorizontal && shipX - (shipSize - 1) < 0) {
            return false;
        } else if (!direction && isHorizontal && shipX + (shipSize - 1) > boardRows) {
            return false;
        }
        return true;
    }

    /**
     * A method that checks the board to see if there is a ship or not where you would want to place a ship
     *
     * @param shipSize     The size of the ship
     * @param startX       The column position of the front of the ship
     * @param startY       The row position of the front of the ship
     * @param isHorizontal The way the ship is facing
     * @param direction    Left or Right if it's horizontal, Up or Down if it's vertical
     * @return counter, the amount of nulls in the board of where the ship would take a spot.
     */
    @Override
    public int nullCountOfShipSize(int shipSize, int startX, int startY, boolean isHorizontal, boolean direction) {
        int counter = 0;
        if (isHorizontal) {
            if (direction) {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY][startX - i] == null) {
                        counter++;
                    }
                }
            } else {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY][startX + i] == null) {
                        counter++;
                    }
                }
            }
        } else {
            if(direction) {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY + i][startX] == null) {
                        counter++;
                    }
                }
            } else {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY - i][startX] == null) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    /**
     * xPosofShip(Player player) is an int method that goes the inputted player's playerSet and returns an int value
     *                           that represents the ship's x position that is closest to the boarder down the middle of the board.
     * @param player The player that you want to get the set from.
     *               If you want the rightmost x position, then player should be p1.
     *               If you want the leftmost x position, then player should be p2.
     * @return xPos, the rightmost x position of p1's ships OR the leftmost x position of p2's ships.
     */
    public int xPosOfShip(Player player) {
        int xPos = playerTurn*24; //Will make it so that the first player's default is at 0, and player 2's default is at 24.
        for(Ship ship : player.getPlayerSet()) {
            int shipX = ship.getColumnCoord();
            if(playerTurn == 0) {
                if(shipX > xPos) {
                    xPos = shipX;
                }
            }
            else
            {
                if(shipX < xPos)
                {
                    xPos = shipX;
                }
            }
        }
        return xPos;
    }

    public int lastViewableColumn()
    {
        if(playerTurn == 0)
            return xPosOfShip(p1) + VIEW_RANGE/128 - 1; //Technically, our columns aren't represented by the integers; The right side of them are. To fully see the column, we need to subtract an additional 1.
        else
            return xPosOfShip(p2) - VIEW_RANGE/128 ;
    }


    /**
     * A method to update the map's position on the board
     *
     * @param point where the user has dragged the top-left corner of the map
     */
    public void update(Point point) {
        waterBox.set(point.x, point.y, point.x + 128 * 24, point.y + 128 * 16);
        masterPoint = point;
        waterImage.setBounds(waterBox);

        for (Ship ship : shipSet) {
            ship.update(point);
        }
    }

    /**
     * A method to draw the map and its contents onto the screen during a cycle of the game loop
     *
     * @param canvas the main canvas of the game
     */
    public void draw(Canvas canvas) {
        waterImage.draw(canvas);

        for (Ship ship : shipSet) {
            ship.draw(canvas);
        }
    }

    @Override
    public void update() {

    }

    /**
     * Removes all ShipPanels and Markers off the screen.
     */
    public void purgeOldPanels()
    {
        ArrayList<Panel> panels = (ArrayList<Panel>)gp.panels.clone(); //Must create a clone to prevent the iterator from skipping items after it deletes something
        for (Panel next : panels)
        {
            if (next instanceof ShipPanel || next instanceof Marker)
                gp.panels.remove(next);
        }
    }

    public boolean contains(Point point)
    {
        return waterBox.contains(point.x, point.y);
    }

    /**
     * Gets the amount of ships in the shipSet
     *
     * @return int count is the amount of ships
     */
    public int shipCount() {
        int count = 0;
        if (shipSet.isEmpty()) {
            return 0;
        }
        Iterator<Ship> shipIterator = shipSet.iterator();
        while (shipIterator.hasNext()) {
            count++;
        }
        return count;
    }

    /**
     * Note: If you change the variables of an object, they do not get updated automatically.
     * HashSets are unordered, so it's easy enough to remove the object from the set and add it back
     * with its updated variables.
     * <p>
     * Use this method when you modify a ship that is currently on the board
     *
     * @param targetShip the Ship from the shipSet to be updated
     */
    public void updateShipInSet(Ship targetShip) {
        if (shipSet.contains(targetShip)) {
            shipSet.remove(targetShip);
            shipSet.add(targetShip);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        final int SCROLL_TOLERANCE = 10;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locator.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                //Only scroll if the user has moved their finger more than SCROLL_TOLERANCE pixels
                if (Math.abs(locator.x - event.getX()) > SCROLL_TOLERANCE
                        || Math.abs(locator.y - event.getY()) > SCROLL_TOLERANCE
                        || isScrolling) {
                    isScrolling = true;
                    masterPoint.set(masterPoint.x + (int) event.getX() - locator.x, masterPoint.y + (int) event.getY() - locator.y);

                    if (masterPoint.y > 256)
                        masterPoint.y = 256;
                    else if (masterPoint.y < -128*16 + SCREEN_HEIGHT)
                        masterPoint.y = -128*16 + SCREEN_HEIGHT;

                    if(ready)
                    {
                        if(playerTurn == 0)
                        {
                            if (masterPoint.x > SCREEN_WIDTH)
                                masterPoint.x = SCREEN_WIDTH;
                            else if (masterPoint.x < -128*xPosOfShip(p1) - VIEW_RANGE + SCREEN_WIDTH)
                                masterPoint.x = -128*xPosOfShip(p1) - VIEW_RANGE + SCREEN_WIDTH;
                        }
                        else
                        {
                            if (masterPoint.x > -128*xPosOfShip(p2) + VIEW_RANGE - 128)
                                masterPoint.x = -128*xPosOfShip(p2) + VIEW_RANGE - 128;
                            else if (masterPoint.x < -128*24)
                                masterPoint.x = -128*24;
                        }
                    }
                    else
                    {
                        if(playerTurn == 0)
                        {
                            if (masterPoint.x > SCREEN_WIDTH)
                                masterPoint.x = SCREEN_WIDTH;
                            else if (masterPoint.x < -128*12 + SCREEN_WIDTH)
                                masterPoint.x = -128*12 + SCREEN_WIDTH;
                        }
                        else
                        {
                            if (masterPoint.x > -128*12 + SCREEN_WIDTH/4)
                                masterPoint.x = -128*12 + SCREEN_WIDTH/4;
                            else if (masterPoint.x < -128*24)
                                masterPoint.x = -128*24;
                        }
                    }

                    /*
                    //Will not let the user scroll anymore if there are only MIN_GRID_SPACES left on the screen.
                    if (masterPoint.x > SCREEN_WIDTH - 128 * MIN_GRID_SPACES)
                        masterPoint.x = SCREEN_WIDTH - 128 * MIN_GRID_SPACES;
                    else if (masterPoint.x < -128 * (24 - MIN_GRID_SPACES))
                        masterPoint.x = -128 * (24 - MIN_GRID_SPACES);
                    if (masterPoint.y > SCREEN_HEIGHT - 128 * MIN_GRID_SPACES)
                        masterPoint.y = SCREEN_HEIGHT - 128 * MIN_GRID_SPACES;
                    else if (masterPoint.y < -128 * (16 - MIN_GRID_SPACES))
                        masterPoint.y = -128 * (16 - MIN_GRID_SPACES);
                    */

                    //update the locator to the current position of the finger
                    locator.set((int) event.getX(), (int) event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isScrolling && ready) //User only clicked a ship, didn't swipe screen over one
                {
                    Ship selected = board[((int) event.getY() - masterPoint.y) / 128][((int) event.getX() - masterPoint.x) / 128];

                    purgeOldPanels();

                    if(selected != null) //Player clicked on a ship, not empty space
                    {
                        if(selected.getPlayer() == this.playerTurn + 1) //Player selected their own ship, not opponent's. Isaac defined player 1 as 0 and player 2 as 1 for some reason so I needed to readjust this value by 1
                        {
                            ShipPanel sp = new ShipPanel(context, selected, gp);
                            gp.panels.add(sp);
                        }
                    }
                }
                else if (!isScrolling) //User clicks on a ship while still building a fleet
                {
                    Ship selected = board[((int)event.getY() - masterPoint.y)/128][((int)event.getX() - masterPoint.x)/128];

                    if(selected != null)
                    {
                        removeShipWithCost(selected);
                    }
                }
                isScrolling = false;
        }
    }

    /**
     * Use this method when a ship is Hit or Missed
     * //@param AttackedShip the ship from shipSet that is being attacked
     * @param Hits the amount of damage the ship from the shipSet is about to take
     * */


    public void Fire( int Hits,int x,int y){
        Ship AttackedShip=board[y][x];
        AttackedShip.applyDamage(Hits);
        sunkenship(AttackedShip);
    }

    private void sunkenship(Ship AttackedShip) {
        if(AttackedShip.getHitpoints()<=0){
            removeShip(AttackedShip);
        }
    }

    /**
     * Use this method to calculate the number of ships lost in order to determine if the player
     * lost
     *
     * @param PlayerShips the number of ships from shipSet that you lost
     */
    public boolean hasLost(HashSet<Ship> PlayerShips) {
        return PlayerShips.isEmpty();
    }

    /**
     * Use this method to calculate the number of the opponent's ships the player destroyed in
     * order to determine if the player win
     *
     * @param OpponentsShips the number of ships from shipSet that you destroyed
     */
    public boolean hasWon(HashSet<Ship> OpponentsShips) {
        return OpponentsShips.isEmpty();
    }

    /**
     * Use this method to calculate the number ships that the player still has in order
     * to determine if the player still has any ships left
     */
    public boolean hasShips(HashSet<Ship> PlayerShips) {
        if (PlayerShips.isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * Use this method to end the game and display the win screen if one of the players is
     * out of ships
     */

    public void endGame(int pl1,int pl2) {
        Intent intent = new Intent(context, WinScreen.class);
        Intent intent1 = new Intent(context,WinScreenP2.class);
        if(pl1==0){


            System.out.println("Player 2 wins");
            Toast.makeText(context, "player 2 won!!", Toast.LENGTH_LONG).show();
            context.startActivity(intent1);

            System.out.print("YO");
        }else if(pl2==0){
            System.out.println("Player 1 wins");
            Toast.makeText(context, "player 1 won!!", Toast.LENGTH_SHORT).show();
            context.startActivity(intent);

    /*public void endGame(Player player1, Player player2) {
        if (!hasShips(player1.getPlayerSet())) {
            setWinText(player2);
        }
        if (!hasShips(player2.getPlayerSet())) {
            setWinText(player1);
        }
    }*/

            System.out.println("YO");
        }else{

        }

    }

}