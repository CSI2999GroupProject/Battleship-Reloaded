package sophomoreproject.battleship;

import android.content.Context;
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
    private Cruiser test;
    private Point startPoint;

    public GamePanel(Context context)
    {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

        test = new Cruiser(context);
        startPoint = new Point(100, 100);
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return super.onTouchEvent(event);
    }

    public void update()
    {
        test.update(startPoint);
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        test.draw(canvas);
    }
}
