package sophomoreproject.battleship;
/**
 * Created by ZJ on 1/24/2018.
 */
public class GameBoard {
    /**
     * @para position marks the location of the ships
     * @para score keeps tracks of the points of the players
     * @para rows there will be 16 rows
     * @para col there will be 24 columns
     */
    private int[][] position;
    public int score = 0;
    public static final int UnGuessed=0;
    public static final int Hit=1;
    public static final int Missed=2;
    public int rows = 16;
    public int col = 24;
    private int status;
    /*
    public GameBoard() throws IllegalAccessException {
        if(rows > 16){
            throw new IllegalAccessException("ERROR!");
        }
        status=0;
        position = new Location[rows][col];
        for(int rows = 0; rows<position.length; rows++){
            for(int col = 0; col < position[rows].length; col++){
                Location mark = position[rows][col];
            }
        }
    }
    */
    public boolean checkHit(){
        if(status==Hit)
            return true;
        else
            return false;
    }
    public boolean checkMiss(){
        if(status==Missed)
            return true;
        else
            return false;
    }
    /**
     * if hit score will go up one
     * @param rows the number of rows on the gameboard
     * @param col the number of col on the gameboard
     */
    public void Hit(int rows, int col){
        position=new int[rows][col];
        markHit(position);
        score++;
    }
    /**
     * if misses score will not change
     * @param rows the number of rows on the gameboard
     * @param col the number of col on the gameboard
     */
    public void Miss(int rows, int col){
        position = new int[rows][col];
        markMiss(position);
    }
    /**
     * sets up rows
     * */
    public void setRows(int row)
    {
        this.rows=row;
    }
    /**
     * return rows
     * */
    public int getRows(){
        return rows;
    }
    /**
     * set up columns
     * */
    public void setCol(int col){
        this.col=col;
    }
    /**
     * return columns
     * */
    public int getCol(){
        return col;
    }
    public void setStatus(int row, int col){
        position=new int[row][col];
    }
    public void setStatus(int status){
        if(status == Hit && status == Missed){

        }
    }
    public int[][] getStatus(){
        return position;
    }
    public void markHit(int[][] position){
        setStatus(Hit);
    }
    public void markMiss(int[][] position){
        setStatus(Missed);
    }
    /**
     * return whether the position is going to be guessed or not
     * */
    public boolean GuessedOrNot(int[][] position){
        return !isUnguessed(position);
    }

    public boolean isUnguessed(int[][] position){
        if(status==Hit || status==Missed;)
            return true;
        else
            return false;
    }
}
