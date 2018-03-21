package sophomoreproject.battleship.ships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by isaac on 1/31/2018.
 */

public class Ship {

    /**
     * A word on direction: true corresponds to positive directions (right or up)
     *                      depending on if horizontal is true or false
     *                      if direction is false, then it corresponds to facing down or left
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
    private int fDamage;
    private int bDamage;
    private int nShots;
    private int pShots;
    private int DamageCost;
    private int FDamageCost;
    private int BDamageCost;
    public  int maxHealth;
    private int Hitpoints;
    private int range;
    private int Frange;
    private int pmove;
    private int player;

    Rect shipBox;
    private Paint boxPaint = new Paint();
    Bitmap shipImage;
    private Point masterPoint;



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

    public void applyRotateL(int degree){
        Matrix m = new Matrix();
        m.postRotate(degree);
        System.out.println("Rotation of Ship image: " + degree + " degrees");
        shipImage = Bitmap.createBitmap(shipImage, 0, 0, shipImage.getWidth(), shipImage.getHeight(), m, false);
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

    public void setPlayer(int player)
    {
        this.player = player;

        if(player == 1)
        {
            boxPaint.setColor(Color.RED);
        }
        else
        {
            boxPaint.setColor(Color.BLUE);
        }

        boxPaint.setAlpha(128);
    }

    public int getPlayer()
    {
        return player;
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
        return fDamage;
    }
    public void setfdamage(int fdamage) {this.fDamage = fdamage; }
    /*damage from front of ships
     *only used on destroyer and battleship
     */

    public int getbdamage() {
        return bDamage;
    }
    public void setbdamage(int bdamage) {this.bDamage = bdamage; }
    /*damage from back of ship
     *only used on destroyer and battleship
     */

    /**
     * A method to get the current health of the ship.
     * @return
     */
    public int getHitpoints() {
        return Hitpoints;
    }

    /**
     * A method to be used when initializing a ship's health; DO NOT USE IF THE SHIP IS TAKING DAMAGE
     * @param Hitpoints the maximum health of the ship. Will set current health to max health.
     */
    public void setHitpoints(int Hitpoints) {
        this.Hitpoints = Hitpoints;
        maxHealth = Hitpoints;
    }

    /**
     *  A method to damage a ship. Note: Health may be negative after using this method.
     *  Wherever you call it, make sure you handle a negative health appropriately.
     * @param damage the amount of damage applied to the ship.
     * @return the remaining health of the ship. May be negative, so handle appropriately.
     */
    public int applyDamage(int damage)
    {
        Hitpoints -= damage;
        return Hitpoints;
    }
    //ships Hitpoints



    /**
     *Don't worry about nshots, and the diffrent damage costs they are part of my part in making the game rules
     */
    public int getRange(){return range;}
    public void setRange(int range){this.range=range;}
    //range ships can fire

    public int getFrange(){return Frange;}
    public void setFrange(int Frange){this.Frange =Frange;}
    //range ships can fire

    public int getnShots() { return nShots;}
    public void setnShots(int nShots) {
        this.nShots = nShots;
    }
    public int getpShots() { return pShots;}
    public void setpShots(int pShots) {
        this.pShots = pShots;
    }

    //number of shots a ship specific damage can shoot per turn

    public int getpmove(){return pmove;}
    public void setpmove(int pmove){this.pmove =pmove;}

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
        Point topLeft = new Point();

        if(isHorizontal)
            if(direction)
            {
                topLeft.set(masterPoint.x + 128*(columnCoord-getShipSize() + 1), masterPoint.y + 128*rowCoord);
            }
            else
            {
                topLeft.set(masterPoint.x + 128*columnCoord, masterPoint.y + 128*rowCoord);
            }
        else
            if(direction)
            {
                topLeft.set(masterPoint.x + 128*columnCoord, masterPoint.y + 128*rowCoord);
            }
            else
            {
                topLeft.set(masterPoint.x + 128*columnCoord, masterPoint.y + 128*(rowCoord-getShipSize() + 1));
            }

            shipBox.set(topLeft.x, topLeft.y, topLeft.x + shipImage.getWidth(), topLeft.y + shipImage.getHeight());
            canvas.drawRect(shipBox, boxPaint);
            canvas.drawBitmap(shipImage, topLeft.x, topLeft.y, null);
    }
}
