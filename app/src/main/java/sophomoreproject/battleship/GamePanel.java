package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;

import sophomoreproject.battleship.ships.Cruiser;
import sophomoreproject.battleship.ships.Ship;

/**
 * Created by Jacob Austin on 2/7/2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private ArrayList<Ship> boardObjects = new ArrayList();
    private ArrayList<Panel> panels = new ArrayList();
    private GameBoard board;
    private Cruiser test; //Test is just an experimental fixed cruiser
    private FleetBuildPanel fbp;
    private Point masterPoint; //The top-left corner of the map
    private Point historicPoint = new Point();
    private Point locator; //A dynamic point that corresponds to the last point the finger was that the game has updated to.
    private int seq; //The sequence that the game is in. Sequence 0 is when the players are placing ships on board, sequence 1 when they are playing.

    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        board = new GameBoard(context);
        test = new Cruiser(context, 2, 4);
        fbp = new FleetBuildPanel(context);
        masterPoint = new Point(0, 0); //Starts the game with the map's top-left corner being on the screen's top-left corner
        locator = new Point(0,0);
        seq = 0;

        boardObjects.add(test);
        panels.add(fbp);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        boolean retry = true;
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
    }

    @Override
    public void draw(Canvas canvas)
    {
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
     * A method that will move the game board and its contents whenever the user swipes across the screen
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
        else if (event.getAction() == MotionEvent.ACTION_UP)
            historicPoint.set(0, 0);
        boolean foundTarget = false;

        for(Panel panel : panels)
        {
            if(panel.contains(historicPoint) && !(historicPoint.x == 0 && historicPoint.y == 0))
            {
                panel.onTouchEvent(event);
                foundTarget = true;
            }
        }

        if(!foundTarget)
        {
            board.onTouchEvent(event);
        }

        return true;
    }
}
