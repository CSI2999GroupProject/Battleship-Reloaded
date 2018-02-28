package sophomoreproject.battleship;

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by isaac on 1/31/2018.
 */

public interface GameBoardInterface {

    void addShip(Ship aShip, int xPos, int yPos);
    void move();
    void rotateLeft();
    void rotateRight();
    boolean checkIndexBoundaries(Ship aShip);
    int nullCountOfShipSize(int shipSize, int startX, int startY, boolean isHorizontal, boolean direction);

}
