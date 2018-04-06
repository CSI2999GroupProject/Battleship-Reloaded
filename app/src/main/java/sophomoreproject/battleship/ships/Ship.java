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
    private int shipSize;//the size of the ships length
    private String name;
    private boolean isHorizontal;//like on a graph this tells if it is along side x or y axis
    private boolean direction;//like on a graph this tells if it is positive or negative
    private int rowCoord;//the y coordinate
    private int columnCoord;//the x coordinate
    private int ShipCost;//the amount it cost to place each ship
    private int nMove;//number of max movement spaces per ship
    private int damage;//damage output per ship
    private int nShots;//number of total shots each ship has
    private int pShots;//counter to hold how many shots each ship has used
    private int DamageCost;//the amount it costs to shoot each shot
    public  int maxHealth;
    private int Hitpoints;//the hitpoints given to each ship
    private int Frange;//the range each ship can fire
    private int pmove;//counter to keep track on how much a ship has moved
    private int player;

    Rect shipBox;
    Rect dshipBox;
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

    public int getpmove(){return pmove;}
    public void setpmove(int pmove){this.pmove =pmove;}
    //counter to keep track of moves

    public int getdamage() {
        return damage;
    }
    public void setdamage(int damage) {this.damage = damage; }
  //damage the ship deals

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
    public void damageShip(int amount) {
        setHitpoints(getHitpoints() - amount);
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

    public int getDamageCost() {
        return DamageCost;
    }
    public void setDamageCost(int DamageCost) {this.DamageCost = DamageCost; }
    //damage cost




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
