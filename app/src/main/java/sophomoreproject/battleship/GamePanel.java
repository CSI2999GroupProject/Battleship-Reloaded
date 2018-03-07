package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import sophomoreproject.battleship.ships.Ship;

/**
 * Created by Jacob Austin on 2/7/2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    public ArrayList<Ship> boardObjects = new ArrayList<Ship>();
    public ArrayList<Panel> panels = new ArrayList<Panel>();
    private GameBoard board;
    private FleetBuildPanel fbp;
    private HudPanel hudPanel;
    private ShipPanel sp;

    private Point masterPoint; //The top-left corner of the map. Can be moved about the screen.
    private Point historicPoint = new Point(); //A point that represents the origin of a finger moving across the screen.
    private Point locator; //A dynamic point that corresponds to the last point the finger was that the game has updated to.
    private int seq; //The sequence that the game is in. Sequence 0 is when the players are placing ships on board, sequence 1 when they are playing.

    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        board = new GameBoard(context, this);
        fbp = new FleetBuildPanel(context, board);
        hudPanel = new HudPanel(context, board);
        sp = new ShipPanel(context, null, this);
        masterPoint = new Point(0, 0); //Starts the game with the map's top-left corner being on the screen's top-left corner
        locator = new Point(0,0);
        seq = 0;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
        while(true)
        while(retry)
        {
            try {
                thread.setRunning(false);
                thread.join();
            }
            catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    public void update()
    {

        board.update(masterPoint);

        for(Ship s : boardObjects)
        {
            s.update(masterPoint);
        }

        for(Panel p : panels)
        {
            p.update();
        }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    public void update()
    {

    }

    @Override
    public void draw(Canvas canvas)
    {
        if(fbp.getInitialSeq() == 0) {
            panels.add(fbp);
            fbp.setInitialSeq(1);
        }
        if(fbp.getInitialSeq() == 2) {
            if(panels.contains(fbp)) {
                panels.remove(fbp);
            }
            panels.add(hudPanel);
            board.setReady(true);
            fbp.setInitialSeq(3);
        }
        super.draw(canvas);

        board.draw(canvas);

        for(Ship s : boardObjects)
        {
            s.draw(canvas);
        }

        for(Panel p : panels)
        {
            p.draw(canvas);
        }
    }

    /**
     * A method that will delegate a touch event to the respective panel or ship that was clicked on.
     *
     * @param event the event of touching the screen
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        locator.set((int)event.getX(), (int)event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            historicPoint.set((int)event.getX(), (int)event.getY());
        boolean foundTarget = false;

        for(Panel panel : panels)
        {
            if(panel.contains(historicPoint) && !(historicPoint.x == 0 && historicPoint.y == 0))
            {
                panel.onTouchEvent(event);
                foundTarget = true;
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
            historicPoint.set(0, 0);

        if(!foundTarget) //If user didn't click in the bounds of an open panel, delegate it to the game board
        {
            board.onTouchEvent(event);
        }

        return true;
    }
}
