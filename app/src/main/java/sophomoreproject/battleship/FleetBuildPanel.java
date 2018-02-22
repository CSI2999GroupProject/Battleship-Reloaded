package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Jacob on 2/20/2018.
 */

public class FleetBuildPanel implements Panel
{
    private Context context;
    private ArrayList<Rect> buttons = new ArrayList<>();
    private Rect panel, cruiserBox;
    private Paint panelPaint;
    private Drawable cruiser;
    private Drawable selected;

    public FleetBuildPanel(Context context)
    {
        this.context = context;

        panel = new Rect();
        panel.set(0, 0, 160, 640);
        panelPaint = new Paint();
        panelPaint.setColor(Color.GRAY);

        cruiserBox = new Rect();
        cruiserBox.set(16, 16, 16 + 128, 16 + 64);
        cruiser = context.getResources().getDrawable(R.drawable.cruiser_old);
        cruiser.setBounds(cruiserBox);

        buttons.add(cruiserBox);
    }


    /**
     * A method to update the panel (currently unused)
     */
    public void update()
    {

    }

    /**
     * A method to draw the panel onto the screen
     *
     * @param canvas the main canvas of the screen
     */
    public void draw(Canvas canvas)
    {
        canvas.drawRect(panel, panelPaint);
        cruiser.draw(canvas);
    }

    public boolean contains(Point point)
    {
        return panel.contains(point.x, point.y);
    }

    public void onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                int x = (int)event.getX();
                int y = (int)event.getY();
                if(cruiserBox.contains(x, y))
                    selected = cruiser;
                break;
            case MotionEvent.ACTION_MOVE:

        }
    }
}
