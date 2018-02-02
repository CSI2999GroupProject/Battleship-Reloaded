package sophomoreproject.battleship;

import sophomoreproject.battleship.ships.Ship;
import sophomoreproject.battleship.ships.ShipInterface;

/**
 * Created by isaac on 1/31/2018.
 */
public class GameBoard implements ShipInterface {

    private Ship[][] board;
    private int boardRows;
    private int boardColumns;
    private static final int DEFAULT_ROWS = 16;
    private static final int DEFAULT_COLUMNS = 24;

    public GameBoard() {
        boardRows = DEFAULT_ROWS;
        boardColumns = DEFAULT_COLUMNS;
        board = new Ship[boardRows][boardColumns];
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

    /**
     * A method to add a ship to the board
     */
    @Override
    public void addShip(Ship aShip) {
        //Your implementation here
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
