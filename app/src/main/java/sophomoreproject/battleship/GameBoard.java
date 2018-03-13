package sophomoreproject.battleship;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
    private Rect waterBox;
    private Drawable waterImage;
    private boolean isScrolling = false;
    public boolean ready = false; //ready should be set to true when it is the user's turn, and they aren't placing ships anymore. Otherwise be false
    private Point locator = new Point(0, 0);
    private Point masterPoint = new Point(0, 0);
    private Context context;
    private GamePanel gp;
    private int playerTurn;
    private Player p1, p2;


    public GameBoard(Context context, GamePanel gp)
    {
        this.context = context;
        this.gp = gp;
        boardRows = DEFAULT_ROWS;
        boardColumns = DEFAULT_COLUMNS;
        board = new Ship[boardRows][boardColumns];
        shipSet = new HashSet<>();
        waterBox = new Rect();
        waterBox.set(0, 0, 128*24, 128*16);
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

    Point getMasterPoint()
    {
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


    /**
     * A method that runs identically to the addShip method, but costs points to place.
     * Intended to be used only when placing the ships from the FleetBuildPanel.
     */
    public void addShipWithCost(Ship aShip, int xPos, int yPos)
    {
        if(playerTurn == 0) {
            if(hasEnoughPoints(aShip, p1)) {
                p1.setAvailablePoints(p1.getAvailablePoints() - aShip.getShipCost());
            } else {
                throw new IllegalStateException("player 1 doesn't have enough points");
            }
        }
        if(playerTurn == 1) {
            if(hasEnoughPoints(aShip, p2)) {
                p2.setAvailablePoints(p2.getAvailablePoints() - aShip.getShipCost());
            }else {
                throw new IllegalStateException("player 2 doesn't have enough points");
            }
        }

        addShip(aShip, xPos, yPos);
    }

    /**
     * A method that runs identically to the removeShip method, except it refunds points to the player.
     * Intended to be used only when removing ships when first creating a fleet using FleetBuildPanel.
     */
    public void removeShipWithCost(Ship aShip)
    {
        if(playerTurn == 0)
            p1.setAvailablePoints(p1.getAvailablePoints() + aShip.getShipCost());
        else
            p2.setAvailablePoints(p2.getAvailablePoints() + aShip.getShipCost());

        removeShip(aShip);
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
        if(playerTurn == 0 && xPos > 11) {
            throw new IllegalArgumentException("It's player 1's turn and you're trying to place into player 2's territory");
        } else if(playerTurn == 1 && xPos < 12) {
            throw new IllegalArgumentException("It's player 2's turn and you're trying to place into player 1's territory");
        }
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
                        if(board[yPos][xPos - i] == null) {
                            board[yPos][xPos - i] = aShip;
                        }
                    }
                } else if (!direction) {
                    for (int i = 0; i < shipSize; i++) {
                        if(board[yPos][xPos + i] == null) {
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

        if (isHorizontal && direction)          //Facing East
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if(xPos + numOfMoves < 24) {
                    if (board[yPos][xPos + i] != null) {
                        if (playerTurn == 0 && xPos + i < 12) {
                            point = new Point(xPos + i, yPos);
                            coordinateList.add(point);
                        } else if (playerTurn == 1 && xPos + i < 24) {
                            point = new Point(xPos + i, yPos);
                            coordinateList.add(point);
                        }
                    }
                }
            }
        }
        else if (isHorizontal)                  //West
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if(xPos - numOfMoves >= 0) {
                    if (board[yPos][xPos - i] != null) {
                        if (playerTurn == 0 && xPos - i > 0) {
                            point = new Point(xPos - i, yPos);
                            coordinateList.add(point);
                        } else if (playerTurn == 1 && xPos - i > 11) {
                            point = new Point(xPos - i, yPos);
                            coordinateList.add(point);
                        }
                    }
                }
            }
        }
        else if (direction)                     //North
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if (yPos - i > 0) {
                    if(board[yPos - i][xPos] != null) {
                        point = new Point(xPos, yPos - i);
                        coordinateList.add(point);
                    }
                }
            }
        }
        else                                    //South
        {
            for (int i = 1; i <= numOfMoves; i++) {
                if (yPos + i < 16) {
                    if(board[yPos + i][xPos] != null) {
                        point = new Point(xPos, yPos + i);
                        coordinateList.add(point);
                    }
                }
            }
        }

        return coordinateList;
    }

    /**
     * Need to find out how the shooting mechanic works. NOT DONE
     *
     *
     *
     * possibleFireLoc(Ship aShip)
     * @param aShip The ship that is going to shoot
     * @return an ArrayList of Coordinates, every coordinate where you can shoot to.
     *          Note: It does not store coordinates on your side of the map, as you can't shoot
     *          your own ships.
     */
    public ArrayList<Point> possibleFireLoc(Ship aShip) {
        ArrayList<Point> coordinateList = new ArrayList<>();
        Point point;
        int fireRange = aShip.getFrange();
        int shipSize = aShip.getShipSize();
        int xPos = aShip.getColumnCoord();
        int yPos = aShip.getRowCoord();

        if(playerTurn == 0 && xPos + fireRange > 11) {
            for(int i = 0; i < fireRange; i++) {

            }
        } else if (playerTurn == 1 && xPos - fireRange < 12) {
            for(int i = 0; i < fireRange; i++) {

            }
        }


        return coordinateList;
    }

    /**
     * Need to find out in what respect the ship rotates. NOT DONE
     *
     *
     * checkRotate(Ship aShip),
     * @param aShip The ship that will be rotated
     * @return coordinateList, an Array of Point in which its size depends on the shipSize
     */
    public Point[] checkRotate(Ship aShip) {
        int shipSize = aShip.getShipSize();
        Point[] pointsArray = new Point[2*shipSize];






        return pointsArray;
    }
    /**
     * A method to move the ships in the board
     */
    public void move(Ship aShip, int xPos, int yPos, int pmove) {
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        removeShip(aShip);

        if (isHorizontal && direction)          //Facing East
        {
            addShip(aShip, xPos + pmove, yPos);
        }
        else if (isHorizontal)                  //West
        {
            addShip(aShip, xPos - pmove, yPos);
        }
        else if (direction)                     //North
        {
            addShip(aShip, xPos, yPos - pmove);
        }
        else                                    //South
        {
            addShip(aShip, xPos, yPos + pmove);
        }
    }

    @Override
    public void rotateLeft(Ship aShip, int xPos, int yPos) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();

        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        removeShip(aShip);

        if (isHorizontal)                //Facing West or East
        {
            aShip.setHorizontal(false);
        } else if (direction)             //Facing North
        {
            aShip.setHorizontal(true);
            aShip.setDirection(false);
        } else                            //Facing South
        {
            aShip.setHorizontal(true);
            aShip.setDirection(true);
        }

        aShip.applyRotateL(-90);

        addShip(aShip, shipX, shipY);

        System.out.println("Ships isHorizontal = " + aShip.getHorizontal() + " and direction = " + aShip.getDirection());
    }

    /**
     * A method to move the ships in the board.
     *
     *
     */
    @Override
    public void rotateRight(Ship aShip, int xPos, int yPos) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();

        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        removeShip(aShip);

        if (!isHorizontal)                //Facing West or East
        {
            aShip.setHorizontal(true);
        } else if (!direction)             //Facing North
        {
            aShip.setHorizontal(false);
            aShip.setDirection(true);
        } else                            //Facing South
        {
            aShip.setHorizontal(false);
            aShip.setDirection(false);
        }

        aShip.applyRotateL(90);

        addShip(aShip, shipX, shipY);

        System.out.println("Ships isHorizontal = " + aShip.getHorizontal() + " and direction = " + aShip.getDirection());
    }

    /**
     * A method to see if a player has enough points to place a ship on the board
     * @param aShip the ship to be placed so we can get it's cost
     * @param player player 1 or player 2
     * @return true if they have enough, false if they don't
     */
    public boolean hasEnoughPoints(Ship aShip, Player player) {
        int playerPoints = player.getAvailablePoints();
        int shipCost = aShip.getShipCost();

        if(playerPoints - shipCost < 0) {
            return false;
        }

        return true;
    }

    /**A method to see if your ship would be placed within the enemy's area
     *
     * @param aShip the ship to be placed
     * @param x the x position of the head of the ship
     * @return true if it WOULD NOT go in enemy territory (across red line),
     *          false if it WOULD go in enemy territory
     */
    public boolean checkPlacementInEnemy(Ship aShip, int x) {
        int shipSize = aShip.getShipSize();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if(playerTurn == 0 && !direction && isHorizontal) {
            if(shipSize + x > 12) {
                return false;
            }
        } else if(playerTurn == 1 && direction && isHorizontal) {
            if(x - shipSize < 11 ) {
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
        }
        else if (!direction && !isHorizontal && shipY + (shipSize - 1) > boardColumns) {
            return false;
        }
        else if (direction && isHorizontal && shipX - (shipSize - 1) < 0) {
            return false;
        }
        else if (!direction && isHorizontal && shipX + (shipSize - 1) > boardRows) {
            return false;
        }
        return true;
    }

    /**
     * A method that checks the board to see if there is a ship or not where you would want to place a ship
     * @param shipSize The size of the ship
     * @param startX The column position of the front of the ship
     * @param startY The row position of the front of the ship
     * @param isHorizontal The way the ship is facing
     * @param direction Left or Right if it's horizontal, Up or Down if it's vertical
     * @return counter, the amount of nulls in the board of where the ship would take a spot.
     */
    @Override
    public int nullCountOfShipSize(int shipSize, int startX, int startY, boolean isHorizontal, boolean direction) {
        int counter = 0;
        if(isHorizontal) {
            if (direction) {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY][startX - i] == null) {
                        counter++;
                    }
                }
            } else if(!direction) {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY][startX + i] == null) {
                        counter++;
                    }
                }
            }
        } else if(!isHorizontal) {
            if(direction) {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY + i][startX] == null) {
                        counter++;
                    }
                }
            } else if(!direction) {
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
     * A method to update the map's position on the board
     *
     * @param point where the user has dragged the top-left corner of the map
     */
    public void update(Point point)
    {
        waterBox.set(point.x, point.y, point.x + 128*24, point.y + 128*16);
        masterPoint = point;
        waterImage.setBounds(waterBox);

        for(Ship ship : shipSet)
        {
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

        for(Ship ship : shipSet)
        {
            ship.draw(canvas);
        }
    }

    @Override
    public void update() {

    }

    public boolean contains(Point point)
    {
        return waterBox.contains(point.x, point.y);
    }

    /**
     * Gets the amount of ships in the shipSet
     * @return int count is the amount of ships
     */
    public int shipCount() {
        int count = 0;
        if (shipSet.isEmpty()) {
            return 0;
        }
        Iterator<Ship> shipIterator = shipSet.iterator();
        while(shipIterator.hasNext()) {
            count++;
        }
        return count;
    }

    /**
     * Note: If you change the variables of an object, they do not get updated automatically.
     *       HashSets are unordered, so it's easy enough to remove the object from the set and add it back
     *       with its updated variables.
     *
     * Use this method when you modify a ship that is currently on the board
     * @param targetShip the Ship from the shipSet to be updated
     */
    public void updateShipInSet(Ship targetShip) {
        if(shipSet.contains(targetShip)) {
            shipSet.remove(targetShip);
            shipSet.add(targetShip);
        }
    }

    public void onTouchEvent(MotionEvent event)
    {
        final int SCROLL_TOLERANCE = 10;

        switch(event.getAction()) {
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
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!isScrolling && ready) //User only clicked a ship, didn't swipe screen over one
                {
                    Ship selected = board[((int)event.getY() - masterPoint.y)/128][((int)event.getX() - masterPoint.x)/128];

                    for (Object next : gp.panels) {
                        if (next instanceof ShipPanel)
                            gp.panels.remove(next);
                    }

                    if(selected != null)
                    {
                        System.out.println("Selected a " + selected.getName());

                        ShipPanel sp = new ShipPanel(context, selected, gp);
                        gp.panels.add(sp);
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
     * @param AttackedShip the ship from shipSet that is being attacked
     * @param Hits the amount of damage the ship from the shipSet is about to take
     * */

    public boolean HitShips(Ship AttackedShip, int Hits){
        AttackedShip.setHitpoints(AttackedShip.getHitpoints()-Hits);
        if(AttackedShip.getHitpoints()<=0){
            shipSet.remove(AttackedShip);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Use this method to calculate the number of ships lost in order to determine if the player
     * lost
     * @param PlayerShips the number of ships from shipSet that you lost
     * */
    public boolean hasLost(HashSet<Ship> PlayerShips){
        return PlayerShips.isEmpty();
    }
    /**
     * Use this method to calculate the number of the opponent's ships the player destroyed in
     * order to determine if the player win
     * @param OpponentsShips the number of ships from shipSet that you destroyed
     * */

    public boolean hasWon(HashSet<Ship> OpponentsShips){
        return OpponentsShips.isEmpty();
    }
}
