package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

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
    private int ShipCost;
    private int nMove;
    private int damage;
    private int fdamage;
    private int bdamage;
    private int nShots;
    private int DamageCost;
    private int FDamageCost;
    private int BDamageCost;
    private int Hitpoints;
    private int range;
    private int Frange;
    private int pmove;
    Bitmap shipImage;
    private Point masterPoint;

    public static final int CRUISER_SIZE = 2;



    public Ship()
    {

    }

    public Ship(Context context, int row, int column)
    {
        if(row > 23 || row < 0 || column > 15 || column < 0)
        {
            throw new IllegalArgumentException();
        }

        this.rowCoord = row;
        this.columnCoord = column;
    }

    public void applyRotate()
    {
        Matrix m = new Matrix();

        int degree = 0;
        if(!this.getHorizontal())
            degree -= 90;
        if(!this.getDirection())
            degree -= 180;

        m.postRotate(degree);
        System.out.println("Rotation of Ship image: " + degree + " degrees");
        shipImage = Bitmap.createBitmap(shipImage, 0, 0, shipImage.getWidth(), shipImage.getHeight(), m, false);
    }

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




    public int getShipCost() {
        return ShipCost;
    }
    public void setShipCost(int ShipCost) {
        this.ShipCost = ShipCost;
    }
    //records the ship cost

    public int getnMove() { return nMove;}
    public void setnMove(int nMove) {
        this.nMove = nMove;
    }
    //the max number of moves a single ship can physically move per turn

    public int getdamage() {
        return damage;
    }
    public void setdamage(int damage) {this.damage = damage; }
    /*special damage
    *Battleship: damage out sides
    * AirCraft carrier: damage at any spot of choice
    * cruiser: main gun damage
    * destroyer: damage out sides
    */

    public int getfdamage() {
        return fdamage;
    }
    public void setfdamage(int fdamage) {this.fdamage = fdamage; }
    /*damage from front of ships
     *only used on destroyer and battleship
     */

    public int getbdamage() {
        return bdamage;
    }
    public void setbdamage(int bdamage) {this.bdamage = bdamage; }
    /*damage from back of ship
     *only used on destroyer and battleship
     */

    public int getHitpoints() {
        return Hitpoints;
    }
    public void setHitpoints(int Hitpoints) {
        this.Hitpoints = Hitpoints;
    }
    //ships hitpoints



    /**
     *Don't worry about nshots, and the diffrent damage costs they are part of my part in making the game rules
     */
    public int getRange(){return range;}
    public void setRange(int range){this.range=range;}
    //range ships can fire

    public int getFRange(){return Frange;}
    public void setFRange(int Frange){this.Frange=Frange;}
    //range ships can fire

    public int getnShots() { return nShots;}
    public void setnShots(int nShots) {
        this.nShots = nShots;
    }
    //number of shots a ship specific damage can shoot per turn

    public int getpmove(){return pmove;}
    public void setpmove(int pmove){this.pmove=pmove;}

    public int getDamageCost() {
        return DamageCost;
    }
    public void setDamageCost(int DamageCost) {this.DamageCost = DamageCost; }
    //damage cost

    public int getFDamageCost() {
        return FDamageCost;
    }
    public void setFDamageCost(int FDamageCoust) {this.FDamageCost = FDamageCost; }
    //front damage cost

    public int getBDamageCost() {
        return BDamageCost;
    }
    public void setBDamageCost(int BDamageCost) {this.BDamageCost = BDamageCost; }
    //back damage cost





    /**
     * A method to update the ship's position on the game panel.
     *
     * @param point the point on the screen corresponding to the top-left corner of the map (masterPoint)
     */
    public void update(Point point)
    {
        masterPoint = point;
    }

    /**
     * A method to draw the ship on the correct place on screen.
     *
     * @param canvas the main canvas of the game
     */
    public void draw(Canvas canvas)
    {
        if(isHorizontal)
            if(direction)
                canvas.drawBitmap(shipImage, masterPoint.x + 128*(columnCoord-getShipSize() + 1), masterPoint.y + 128*rowCoord, null);
            else
                canvas.drawBitmap(shipImage, masterPoint.x + 128*columnCoord, masterPoint.y + 128*rowCoord, null);
        else
        if(direction)
            canvas.drawBitmap(shipImage, masterPoint.x + 128*columnCoord, masterPoint.y + 128*rowCoord, null);
        else
            canvas.drawBitmap(shipImage, masterPoint.x + 128*columnCoord, masterPoint.y + 128*(rowCoord-getShipSize() + 1), null);
    }
}
