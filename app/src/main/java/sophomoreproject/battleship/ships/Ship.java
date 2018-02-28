package sophomoreproject.battleship.ships;

/**
 * Created by isaac on 1/31/2018.
 */

public class Ship {

    /**
     * @param direction = true corresponds to positive directions (right or up)
     *                  depending on if horizontal is true or false
     *                  if direction is false, then it corresponds to facing down or left
     */
    private int shipSize;
    private String name;
    private boolean isHorizontal;
    private boolean direction;
    private int rowCoord;
    private int columnCoord;
    private int SC;
    private int nMove;
    private int damage;
    private int fdamage;
    private int bdamage;
    private int nShots;
    private int DC;
    private int FDC;
    private int BDC;
    private int Hitpoints;

    public boolean getHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean getDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShipSize() {
        return shipSize;
    }

    public void setShipSize(int shipSize) {
        this.shipSize = shipSize;
    }


    public int getRowCoord() {
        return rowCoord;
    }

    public void setRowCoord(int rowCoord) {
        this.rowCoord = rowCoord;
    }

    public int getColumnCoord() {
        return columnCoord;
    }

    public void setColumnCoord(int columnCoord) {
        this.columnCoord = columnCoord;
    }




    public int getSC() {
        return SC;
    }
    public void setSC(int SC) {
        this.SC = SC;
    }
    //records the ship count
    public int getnMove() {
        return nMove;
    }
    public void setnMove(int nMove) {
        this.nMove = nMove;
    }
    //records max number of available moves
    public int getdamage() {
        return damage;
    }
    public void setdamage(int damage) {this.damage = damage; }
    //main damage
    public int getfdamage() {
        return fdamage;
    }
    public void setfdamage(int fdamage) {this.fdamage = fdamage; }
    //damage from front of ship
    public int getbdamage() {
        return bdamage;
    }
    public void setbdamage(int bdamage) {this.bdamage = bdamage; }
    //damage from back of ship
    public int getHitpoints() {
        return Hitpoints;
    }
    public void setHitpoints(int Hitpoints) {
        this.Hitpoints = Hitpoints;
    }
    //ships hitpoints
    public int getnShots() {
        return nShots;
    }
    public void setnShots(int nShots) {
        this.nShots = nShots;
    }
    //number of shots a ship can shoot per turn
    public void setDC(int DC) {this.DC = DC; }
    public int getDC() {
        return DC;
    }
    //damage count
    public void setFDC(int FDC) {this.FDC = FDC; }
    public int getFDC() {
        return FDC;
    }
    //front damage count
    public void setBDC(int BDC) {this.BDC = BDC; }
    public int getBDC() {
        return BDC;
    }
    //back damage count




}
