package sophomoreproject.battleship;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

/**
 * Created by Jacob on 2/22/2018.
 */

public interface Panel
{
    void draw(Canvas canvas);
    void update();
    boolean contains(Point point);
    void onTouchEvent(MotionEvent event);
}
