package sophomoreproject.battleship;

import java.util.HashSet;

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

    public GameBoard() {
        boardRows = DEFAULT_ROWS;
        boardColumns = DEFAULT_COLUMNS;
        board = new Ship[boardRows][boardColumns];
        shipSet = new HashSet<Ship>();
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
     * A method to add a ship to the board. If the shipSize is greater than 1, then depending on the direction of the Ship,
     * the Ship[][] will add multiple of the same object in the corresponding spot.
     *
     * After it adds the ship to board, it adds the front of the ship to the HashSet shipSet and sets the Ship's coordinates.
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


}
