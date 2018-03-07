package sophomoreproject.battleship;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

/**
 * Created by Jacob on 2/22/2018.
 */

public interface Panel
{
    /**
     * A method to draw the panel onto the canvas
     * @param canvas the canvas being drawn onto
     */
    void draw(Canvas canvas);

    /**
     * A method to update anything onto the board
     */
    void update();

    /**
     * A method to detect whether or not a point is inside the panel somewhere
     * @param point the point being tested
     * @return true if point is inside the panel, and false otherwise
     */
    boolean contains(Point point);

    /**
     * A method to handle the panel getting touched at any position
     * @param event the MotionEvent that occurred inside the panel
     */
    void onTouchEvent(MotionEvent event);
}
