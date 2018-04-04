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


        /**
         * Created by isaac on 3/4/2018.
         */

        public class HudPanel implements Panel {

            private Context context;
            private GameBoard board;
            private Player player;
            private Rect hud, endTurnBox, playerPoints, maxPlayerPoints;
            private Paint hudPaint, textStuff, blackPaint, barPaint, pointsText;
            private Bitmap endTurn;
            private Bitmap lastButtonPress;
            private Point lastMotion = new Point(0, 0);
            private boolean win = false;

            public HudPanel(Context context, GameBoard board) {
                final int SCREEN_WIDTH = context.getResources().getSystem().getDisplayMetrics().widthPixels;

                this.context = context;
                this.board = board;

                hud = new Rect(SCREEN_WIDTH / 8, 0, SCREEN_WIDTH, 230);
                hudPaint = new Paint();
                hudPaint.setColor(Color.DKGRAY);

                maxPlayerPoints = new Rect(hud.width() / 6, hud.top + hud.height() * 4 / 6, hud.width() * 5 / 6, hud.top + hud.height() * 5 / 6);
                playerPoints = new Rect();
                blackPaint = new Paint();
                blackPaint.setColor(Color.BLACK);
                barPaint = new Paint();
                barPaint.setColor(Color.BLUE);
                pointsText = new Paint();
                pointsText.setColor(Color.WHITE);
                pointsText.setTextSize(maxPlayerPoints.height());
                pointsText.setTextAlign(Paint.Align.CENTER);

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
                canvas.drawText(playerTurnText(), 600, 75, textStuff);

                canvas.drawRect(maxPlayerPoints, blackPaint);
                canvas.drawRect(playerPoints, barPaint);
                canvas.drawText(playerPointText(), maxPlayerPoints.centerX(), maxPlayerPoints.bottom, pointsText);
            }

            @Override
            public void update() {
                double pointsPercent = (double) board.getPoints() / (double) Player.POINTS_PER_TURN;

                playerPoints.set(maxPlayerPoints.left, maxPlayerPoints.top, maxPlayerPoints.left + (int) (pointsPercent * (maxPlayerPoints.right - maxPlayerPoints.left)), maxPlayerPoints.bottom);
            }

            @Override
            public boolean contains(Point point) {
                return hud.contains(point.x, point.y);
            }

            @Override
            public void onTouchEvent(MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                lastMotion.set(x, y);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (endTurnBox.contains(x, y)) {
                            lastButtonPress = endTurn;
                        }

                        break;


                    case MotionEvent.ACTION_UP:

                        if (lastButtonPress == endTurn && endTurnBox.contains(x, y)) {
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

                public String playerTurnText () {
                    String str = "";
                    if (board.getPlayerTurn() == 0) {
                        str = "It's Player 1's turn";
                    } else {
                        str = "It's Player 2's turn";
                    }
                    return str;

                }

                public String playerPointText () {
                    return "Points:" + board.getPoints() + "/" + Player.POINTS_PER_TURN;
                }

        }

