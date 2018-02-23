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

import sophomoreproject.battleship.ships.Cruiser;
import sophomoreproject.battleship.ships.Ship;

/**
 * Created by Jacob on 2/20/2018.
 */

public class FleetBuildPanel implements Panel
{
    private Context context;
    private GameBoard board;
    private ArrayList<Rect> buttons = new ArrayList<>();
    private Rect panel, cruiserBox;
    private Paint panelPaint;
    private Drawable cruiser;
    private Drawable selected;
    private String selectedShip;

    public FleetBuildPanel(Context context, GameBoard board)
    {
        this.context = context;
        this.board = board;

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
        if(selected != null)
            selected.draw(canvas);
    }

    public boolean contains(Point point)
    {
        return panel.contains(point.x, point.y);
    }

    public void onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(cruiserBox.contains(x, y))
                {
                    selected = cruiser.mutate().getConstantState().newDrawable();
                    selected.mutate();
                    selected.setBounds(x-64, y-64, x + 64 + 128*(2-1), y + 64);
                    selectedShip = "Cruiser";
                }
                if(selected != null)
                    selected.setAlpha(128);
                break;
            case MotionEvent.ACTION_MOVE:
                if(selected != null)
                    selected.setBounds(x-64, y-64, x + 64 + 128*(2-1), y + 64);
                break;
            case MotionEvent.ACTION_UP:
                selected = null;
                int row = (x - board.getMasterPoint().x)/128;
                int column = (y - board.getMasterPoint().y)/128;
                switch(selectedShip)
                {
                    case "Cruiser":
                        try
                        {
                            Cruiser ship = new Cruiser(context, row, column);
                            board.addShip(ship, row, column);
                            System.out.println("Added a cruiser at (" + row + ", " + column + ")");
                        }
                        catch(IllegalArgumentException e)
                        {
                            System.out.println("Failed to add a cruiser at (" + row + ", " + column + ")");
                        }
                }
        }
    }
}
