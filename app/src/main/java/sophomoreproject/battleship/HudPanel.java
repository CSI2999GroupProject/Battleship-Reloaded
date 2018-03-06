package sophomoreproject.battleship;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by isaac on 3/4/2018.
 */

public class HudPanel implements Panel {

    private Context context;
    private GameBoard board;
    private Rect hud, endTurnBox;
    private Paint hudPaint, textStuff;
    private Bitmap endTurn;
    private Bitmap lastButtonPress;
    private Point lastMotion = new Point(0, 0);

    public HudPanel(Context context, GameBoard board) {
        this.context = context;
        this.board = board;

        hud = new Rect(0, 0, 1800, 230);
        hudPaint = new Paint();
        hudPaint.setColor(Color.DKGRAY);

        textStuff = new Paint();
        textStuff.setColor(Color.RED);
        textStuff.setTextSize(72);

        endTurnBox = new Rect(1800 - 420, 15, 1800 - 20, 215);
        endTurn = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.endturn_button_on), 388, 195, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(hud, hudPaint);
        canvas.drawBitmap(endTurn, endTurnBox.left, endTurnBox.top, null);

        canvas.drawText(playerTurnText(), 200, 75, textStuff);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean contains(Point point) {
        return hud.contains(point.x, point.y);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        lastMotion.set(x, y);

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if(endTurnBox.contains(x, y)) {
                    lastButtonPress = endTurn;
                }

                break;

            case MotionEvent.ACTION_UP:

                if(lastButtonPress == endTurn && endTurnBox.contains(x, y)) {
                    switch(board.getPlayerTurn()) {
                        case 0:
                            board.setPlayerTurn(1);
                            break;
                        case 1:
                            board.setPlayerTurn(0);
                            break;
                    }
                }
                break;
        }
    }

    public String playerTurnText() {
        String str = "";
        if(board.getPlayerTurn() == 0) {
            str = "It's Player 1's turn";
        } else {
            str = "It's Player 2's turn";
        }
        return str;
    }
}
