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

import sophomoreproject.battleship.ships.Ship;

/**
 * Created by Jacob on 3/5/2018.
 */

public class ShipPanel implements Panel
{
    private final int BUTTON_TOTAL = 3;                                     //The total number of buttons on the panel
    private Rect panel;                                             //The back panel of the selection screen
    private Rect[] buttonBoxes = new Rect[BUTTON_TOTAL];            //Rects representing the dimensions and positions of the buttons
    private Bitmap[] buttonImages = new Bitmap[BUTTON_TOTAL];       //The images of the buttons (must match buttonBoxes perfectly)
    private Paint panelPaint = new Paint(), testPaint = new Paint();
    private int lastButtonClicked = -1;

    private Ship ship;
    private GamePanel gp;

    public ShipPanel(Context context, Ship ship, GamePanel gp)
    {
        this.ship = ship;
        this.gp = gp;

        final int SCREEN_WIDTH = context.getResources().getSystem().getDisplayMetrics().widthPixels;
        final int SCREEN_HEIGHT = context.getResources().getSystem().getDisplayMetrics().heightPixels;

        panel = new Rect(0, 0, SCREEN_WIDTH/8, SCREEN_HEIGHT);

        for(int i = 0; i < BUTTON_TOTAL; i++)
        {
            buttonBoxes[i] = new Rect(panel.left + panel.width()/6, panel.width()/6 + i*panel.width()*5/6, panel.width()*5/6, panel.width()*5/6 + i*panel.width()*5/6);
        }

        buttonImages[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fire_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.move_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);
        buttonImages[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.turn_left_button), buttonBoxes[0].width(), buttonBoxes[0].width(), false);

        panelPaint.setColor(Color.GRAY);
        testPaint.setColor(Color.RED);
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawRect(panel, panelPaint);
        for(int i = 0; i < BUTTON_TOTAL; i++)
        {
            canvas.drawBitmap(buttonImages[i], buttonBoxes[i].left, buttonBoxes[i].top, null);
        }
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean contains(Point point)
    {
        return panel.contains(point.x, point.y);
    }

    @Override
    public void onTouchEvent(MotionEvent event)
    {
        //Find which button got pressed
        int i = 0;
        while(i < BUTTON_TOTAL)
            if(buttonBoxes[i].contains((int)event.getX(), (int)event.getY()))
                break;
            else
                i++;

        if(i != BUTTON_TOTAL) //Event happened on one of the buttons
        {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    lastButtonClicked = i;
                    break;
                case MotionEvent.ACTION_UP:
                    if(lastButtonClicked == i) //The button the player let go of was the same button they last pressed
                    {
                        switch(i)
                        {
                            case 0: //Fire button pressed
                                System.out.println("Fire!");
                                break;
                            case 1: //Move button pressed

                                int pmove=ship.getpmove();//not sure where its at but when player turn switches pmove needs to be reset to 0
                                ship.setpmove(pmove++);
                            if(pmove!=ship.getnMove()){
                                GameBoard.move(ship,ship.getRowCoord(),ship.getColumnCoord(), pmove);
                                System.out.println("Move!");
                            }else{
                                System.out.println("You have moved this ship its maximum number of spaces!");
                            }
                                break;
                            case 2: //Rotate left button pressed this is the rotate that we currently have

                                GameBoard.rotateLeft(ship,ship.getRowCoord(),ship.getColumnCoord());
                                System.out.println("Rotate Left!");
                                break;
                            case 3: //Rotate Right button pressed this is currently in not here but it needs to be
                                GameBoard.rotateRight(ship,ship.getRowCoord(),ship.getColumnCoord());
                                System.out.println("Rotate Left!");
                                break;
                            default:
                                System.out.println("Something unexpected happened.");
                        }

                        gp.panels.remove(this);
                    }
                    lastButtonClicked = -1;
                    break;
            }
        }
    }

}
