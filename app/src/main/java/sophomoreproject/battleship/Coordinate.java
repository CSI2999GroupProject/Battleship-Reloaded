package sophomoreproject.battleship;

/**
 * Created by isaac on 3/11/2018.
 *
 * A small object that stores coordinate points
 */

public class Coordinate {
    private int xPos;
    private int yPos;

    public Coordinate(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public Coordinate() {
        this.xPos = 0;
        this.yPos = 0;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
