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

import java.util.HashSet;

import sophomoreproject.battleship.ships.Ship;


/**
 * Created by isaac on 3/4/2018.
 */

public class HudPanel implements Panel {
    
    private Context context;
    private GameBoard board;
    private Player player;
    private Rect hud, endTurnBox, playerPoints, maxPlayerPoints, fleetHealthBackground, fleetHealthP1, fleetSizeBackground, fleetSize;
    private Paint hudPaint, textStuff, blackPaint, bluePaint, redPaint, infoText;
    private Bitmap endTurn;
    private Bitmap lastButtonPress;
    private Point lastMotion = new Point(0, 0);
    private boolean win = false;

    public HudPanel(Context context, GameBoard board) {
        this.context = context;
        this.board = board;

        hud = new Rect(board.SCREEN_WIDTH/8, 0, board.SCREEN_WIDTH, board.SCREEN_HEIGHT/5);
        
        hudPaint = new Paint(); hudPaint.setColor(Color.DKGRAY);
        blackPaint = new Paint(); blackPaint.setColor(Color.BLACK);
        bluePaint = new Paint(); bluePaint.setColor(Color.BLUE);
        redPaint = new Paint(); redPaint.setColor(Color.RED);

        fleetHealthBackground = new Rect(hud.left + hud.width()/24, hud.top + hud.height()*3/12, hud.left + hud.width()/4, hud.top + hud.height()*5/12);
        fleetHealthP1 = new Rect();

        fleetSizeBackground = new Rect(fleetHealthBackground.left, fleetHealthBackground.top + hud.height()*3/6, fleetHealthBackground.right, fleetHealthBackground.bottom + hud.height()*3/6);
        fleetSize = new Rect();

        maxPlayerPoints = new Rect(fleetHealthBackground.right + hud.width()/24, hud.bottom - hud.height()/3, hud.right - hud.bottom*13/8 - hud.width()/24, hud.bottom - hud.height()/6);
        playerPoints = new Rect();

        endTurnBox = new Rect(hud.right - hud.bottom*13/8, hud.bottom/8, hud.right - hud.bottom/8, hud.bottom*7/8);
        endTurn = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.endturn_button_on), endTurnBox.width(), endTurnBox.height(), false);

        textStuff = new Paint();
        infoText = new Paint(); infoText.setColor(Color.WHITE); infoText.setTextSize(maxPlayerPoints.height()); infoText.setTextAlign(Paint.Align.CENTER);
        textStuff.setColor(Color.RED);
        textStuff.setTextSize(endTurnBox.height()/2);
        textStuff.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(hud, hudPaint);

        canvas.drawBitmap(endTurn, endTurnBox.left, endTurnBox.top, null);
        canvas.drawText(playerTurnText(), hud.centerX(), hud.bottom/3, textStuff);

        int[] fleetHealth = getTotalFleetHealth();
        int healthPercent = (100 * fleetHealth[0])/(fleetHealth[0] + fleetHealth[1]);
        canvas.drawRect(fleetHealthBackground, bluePaint);
        canvas.drawRect(fleetHealthP1, redPaint);
        Paint text = new Paint(infoText);
        canvas.drawText("Fleet Condition:", fleetHealthBackground.centerX(), fleetHealthBackground.top, text);
        canvas.drawText(healthPercent + "%", fleetHealthP1.centerX(), fleetHealthBackground.bottom, text);
        canvas.drawText((100-healthPercent) + "%", (fleetHealthP1.right + fleetHealthBackground.right)/2, fleetHealthBackground.bottom, infoText);

        canvas.drawRect(fleetSizeBackground, bluePaint);
        canvas.drawRect(fleetSize, redPaint);
        canvas.drawText("Fleet Size:", fleetSizeBackground.centerX(), fleetSizeBackground.top, text);
        canvas.drawText(board.getP1().getPlayerSet().size() + "", fleetSize.exactCenterX(), fleetSizeBackground.bottom, text);
        canvas.drawText(board.getP2().getPlayerSet().size() + "", (fleetSize.right + fleetSizeBackground.right)/2, fleetSizeBackground.bottom, text);

        canvas.drawRect(maxPlayerPoints, blackPaint);
        canvas.drawRect(playerPoints, bluePaint);
        canvas.drawText(playerPointText(), maxPlayerPoints.centerX(), maxPlayerPoints.bottom, infoText);
    }

    @Override
    public void update()
    {
        double pointsRatio = (double)board.getPoints() / (double)Player.POINTS_PER_TURN;
        playerPoints.set(maxPlayerPoints.left, maxPlayerPoints.top, maxPlayerPoints.left + (int)(pointsRatio*(maxPlayerPoints.right - maxPlayerPoints.left)), maxPlayerPoints.bottom);

        int[] fleetHealth = getTotalFleetHealth();
        double healthRatio = (double)fleetHealth[0]/(double)(fleetHealth[0] + fleetHealth[1]); //Finds what percent of the sum of points on the board belong to P1 ships
        fleetHealthP1.set(fleetHealthBackground.left, fleetHealthBackground.top, fleetHealthBackground.left + (int)(healthRatio*(fleetHealthBackground.right - fleetHealthBackground.left)), fleetHealthBackground.bottom);
        
        double P1Size = (double)board.getP1().getPlayerSet().size();
        double P2Size = (double)board.getP2().getPlayerSet().size();
        double sizeRatio = P1Size / (P1Size + P2Size);
        fleetSize.set(fleetSizeBackground.left, fleetSizeBackground.top, fleetSizeBackground.left + (int)(sizeRatio*(fleetSizeBackground.right - fleetSizeBackground.left)), fleetSizeBackground.bottom);
    }

    /**
     * A method that will calculate the total fleet healths of both players.
     * @return Int array of length 2. Index 0 of return array is P1, index 1 is P2.
     */
    private int[] getTotalFleetHealth()
    {
        int[] answer = {0, 0};

        for(Ship ship : board.getP1().getPlayerSet())
        {
            answer[0] += ship.getHitpoints();
        }

        for(Ship ship : board.getP2().getPlayerSet())
        {
            answer[1] += ship.getHitpoints();
        }
        return answer;
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
                    switch (board.getPlayerTurn()) {
                        case 0:
                            board.setPlayerTurn(1);
                            board.setPoints(Player.POINTS_PER_TURN);
                            board.getP2().resetPMove();
                            board.getP1().resetPMove();
                            board.getMasterPoint().x = -128 * board.xPosOfShip(board.getP2()) + board.VIEW_RANGE - 128;
                            break;
                        case 1:
                            board.setPlayerTurn(0);
                            board.setPoints(Player.POINTS_PER_TURN);
                            board.getP1().resetPMove();
                            board.getP2().resetPMove();
                            board.getMasterPoint().x = -128 * board.xPosOfShip(board.getP1()) - board.VIEW_RANGE + board.SCREEN_WIDTH;
                            break;
                    }
                    board.purgeOldPanels();
                    break;
                }
        }
    }

    public String playerTurnText() {
        String str = "";
            if (board.getPlayerTurn() == 0) {
                str = "It's Player 1's turn";
            } else {
                str = "It's Player 2's turn";
            }
            return str;

    }

    public String playerPointText() {
        return "Points:" + board.getPoints() + "/" + Player.POINTS_PER_TURN;
    }
}
