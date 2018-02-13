package sophomoreproject.battleship;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sophomoreproject.battleship.ships.Cruiser;

/**
 * Created by Jacob Austin on 2/7/2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    private MainThread thread;
    private GameBoard board;
    private Cruiser test; //Test is just an experimental fixed cruiser
    private Point masterPoint; //The top-left corner of the map
    private Point locator; //A dynamic point that corresponds to the last point the finger was that the game has updated to.

    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        board = new GameBoard(context);

        test = new Cruiser(context, 2, 4);
        masterPoint = new Point(0, 0); //Starts the game with the map's top-left corner being on the screen's top-left corner
        locator = new Point(0,0);
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

    /**
     * A method that will move the game board and its contents whenever the user swipes across the screen
     *
     * @param event the event of touching the screen
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                locator.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                masterPoint.set(masterPoint.x + (int)event.getX() - locator.x, masterPoint.y + (int)event.getY() - locator.y);
                final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
                final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
                final int MIN_GRID_SPACES = 5;


                //Will not let the user scroll anymore if there are only MIN_GRID_SPACES left on the screen.
                if(masterPoint.x > SCREEN_WIDTH - 128*MIN_GRID_SPACES)
                    masterPoint.x = SCREEN_WIDTH - 128*MIN_GRID_SPACES;
                else if(masterPoint.x < -128*(24-MIN_GRID_SPACES))
                    masterPoint.x = -128*(24-MIN_GRID_SPACES);
                if(masterPoint.y > SCREEN_HEIGHT - 128*MIN_GRID_SPACES)
                    masterPoint.y = SCREEN_HEIGHT - 128*MIN_GRID_SPACES;
                else if(masterPoint.y < -128*(16-MIN_GRID_SPACES))
                    masterPoint.y = -128*(16-MIN_GRID_SPACES);

                locator.set((int)event.getX(), (int)event.getY());
        }

        return true;//super.onTouchEvent(event);
    }

    public void update()
    {
        board.update(masterPoint);
        test.update(masterPoint);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        board.draw(canvas);
        test.draw(canvas);
    }
}
