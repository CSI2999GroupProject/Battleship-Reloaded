package sophomoreproject.battleship;

import java.util.HashSet;
import java.util.Iterator;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by isaac on 1/31/2018.
 */
public class GameBoard implements GameBoardInterface {

    private Ship[][] board;
    private int boardRows;
    private int boardColumns;
    private static final int DEFAULT_ROWS = 16;
    private static final int DEFAULT_COLUMNS = 24;
    private HashSet<Ship> shipSet;
    private int lostShips;
    public GameBoard() {
        boardRows = DEFAULT_ROWS;
        boardColumns = DEFAULT_COLUMNS;
        board = new Ship[boardRows][boardColumns];
        shipSet = new HashSet<Ship>();
        lostShips = 0;
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
    public int getLostShips(){
        return lostShips;
    }
    public void setLostShips(int lostShips){
        this.lostShips = lostShips;
    }
    /**
     * A method to add a ship to the board. If the shipSize is greater than 1, then depending on the direction of the Ship,
     * the Ship[][] will add multiple of the same object in the corresponding spot.
     *
     * After it adds the ship to board, it adds the front of the ship to the HashSet shipSet and sets the Ship's coordinates.
     *
     */
    @Override
    public void addShip(Ship aShip, int xPos, int yPos) {
        int shipSize = aShip.getShipSize();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if (xPos < 0 || yPos < 0) {
            throw new IllegalArgumentException("There is no negative position on the board");
        }
        if (xPos > boardColumns || yPos > boardRows) {
            throw new IllegalArgumentException("Can't place a ship beyond the board's boundaries");
        }
        if (!checkIndexBoundaries(aShip)) {
            throw new IllegalStateException("Can't place a ship beyond the board's boundaries");
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
            } else if (!isHorizontal) {
                if (direction) {
                    for (int i = 0; i < shipSize; i++) {
                        if (board[yPos - i][xPos] == null) {
                            board[yPos - i][xPos] = aShip;
                        }
                    }
                } else {
                    for (int i = 0; i < shipSize; i++) {
                        if (board[yPos + i][xPos] == null) {
                            board[yPos + i][xPos] = aShip;
                        }
                    }
                }
            }
        } else {
            board[yPos][xPos] = aShip;
        }
        shipSet.add(aShip);
    }

    /**
     * A method to move the ships in the board.
     *
     *
     */
    @Override
    public void move(Ship aShip, int xPos, int yPos, int pmove) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();
        int nMove = aShip.getnMove();
        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if (!checkIndexBoundaries(aShip)) {
            throw new IllegalStateException("Can't place a ship beyond the board's boundaries");
        }else if (nullCountOfShipSize(shipSize, xPos, yPos, isHorizontal, direction) != shipSize) {
            throw new IllegalStateException("There is already a ship there");
        }else if(pmove > nMove) {
            throw new IllegalStateException("This ship cannot move that many spaces");
        }else{
            if (shipSize > 1) {
                if (isHorizontal) {
                    if (direction) {

                        xPos=xPos+pmove;
                        for (int i = 0; i < shipSize; i++) {
                                board[yPos][xPos - i] = aShip;
                        }
                    } else if (!direction) {

                        xPos=xPos-pmove;
                        for (int i = 0; i < shipSize; i++) {
                                board[yPos][xPos + i] = aShip;
                        }
                    }
                } else if (!isHorizontal) {
                    if (direction) {

                        yPos=yPos+pmove;
                        for (int i = 0; i < shipSize; i++) {
                                board[yPos - i][xPos] = aShip;
                        }
                    } else {

                        yPos=yPos-pmove;
                        for (int i = 0; i < shipSize; i++) {
                                board[yPos + i][xPos] = aShip;
                        }
                    }
                }
            }
            updateShipInSet(aShip);
        }
    }

    @Override
    public void rotateLeft(Ship aShip, int xPos, int yPos) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();

        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();

        if (!checkIndexBoundaries(aShip)) {
            throw new IllegalStateException("Can't place a ship beyond the board's boundaries");
        }else if (nullCountOfShipSize(shipSize, xPos, yPos, isHorizontal, direction) != shipSize) {
            throw new IllegalStateException("There is already a ship there");
        }else{
            if (shipSize > 1) {
                if (isHorizontal) {
                    if (direction) {

                        aShip.setHorizontal(false);
                        for (int i = 0; i < shipSize; i++) {
                            board[yPos-i][xPos] = aShip;
                        }
                    } else if (!direction) {

                        aShip.setHorizontal(false);

                        for (int i = 0; i < shipSize; i++) {
                            board[yPos+i][xPos] = aShip;
                        }
                    }
                } else if (!isHorizontal) {
                    if (direction) {

                        aShip.setHorizontal(true);
                        aShip.setDirection(false);
                        for (int i = 0; i < shipSize; i++) {
                            board[yPos][xPos-i] = aShip;
                        }
                    } else {

                        aShip.setHorizontal(true);
                        aShip.setDirection(true);
                        for (int i = 0; i < shipSize; i++) {
                            board[yPos][xPos+i] = aShip;
                        }
                    }
                }
            }
            updateShipInSet(aShip);
        }
    }

    @Override
    public void rotateRight(Ship aShip, int xPos, int yPos) {
        int shipX = aShip.getColumnCoord();
        int shipY = aShip.getRowCoord();
        int shipSize = aShip.getShipSize();

        boolean isHorizontal = aShip.getHorizontal();
        boolean direction = aShip.getDirection();


        if (!checkIndexBoundaries(aShip)) {
            throw new IllegalStateException("Can't place a ship beyond the board's boundaries");
        }else if (nullCountOfShipSize(shipSize, xPos, yPos, isHorizontal, direction) != shipSize) {
            throw new IllegalStateException("There is already a ship there");
        }else{
            if (shipSize > 1) {
                if (isHorizontal) {
                    if (direction) {

                        aShip.setHorizontal(false);
                        aShip.setDirection(false);
                        for (int i = 0; i < shipSize; i++) {
                            board[yPos+i][xPos] = aShip;
                        }
                    } else if (!direction) {

                        aShip.setHorizontal(false);
                        aShip.setDirection(true);

                        for (int i = 0; i < shipSize; i++) {
                            board[yPos-i][xPos] = aShip;
                        }
                    }
                } else if (!isHorizontal) {
                    if (direction) {

                        aShip.setHorizontal(true);

                        for (int i = 0; i < shipSize; i++) {
                            board[yPos][xPos+i] = aShip;
                        }
                    } else {

                        aShip.setHorizontal(true);

                        for (int i = 0; i < shipSize; i++) {
                            board[yPos][xPos-i] = aShip;
                        }
                    }
                }
            }
            updateShipInSet(aShip);
        }
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

        if (shipY - (shipSize - 1) < 0) {
            return false;
        }
        if (shipY + (shipSize - 1) > boardRows) {
            return false;
        }
        if (shipX - (shipSize - 1) < 0) {
            return false;
        }
        if (shipX + (shipSize - 1) > boardColumns) {
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
                    if (board[startY - i][startX] == null) {
                        counter++;
                    }
                }
            } else if(!direction) {
                for (int i = 0; i < shipSize; i++) {
                    if (board[startY + i][startX] == null) {
                        counter++;
                    }
                }
            }
        }
        return counter;
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
    /**
     * Use this method when a ship is Hit or Missed
     * @param AttackedShip the ship from shipSet that is being attacked
     * @param Hits the amount of damage the ship from the shipSet is about to take
     * */
    public boolean SunkenShip(Ship AttackedShip, int Hits){
        AttackedShip.setHitpoints(AttackedShip.getHitpoints()-Hits);
        if(AttackedShip.getHitpoints()<=0){
            shipSet.remove(AttackedShip);
            lostShips++;
            setLostShips(lostShips);
            return true;
        }else{
            return false;
        }
    }
    /**
     * Use this method to calculate the number of ships lost in order to determine if the player
     * lost
     * @param lostShips the number of ships from shipSet lost
     * */
    public boolean hasLost(int lostShips){
        if(lostShips==5){
            return true;
        }else{
            return false;
        }
    }
}
