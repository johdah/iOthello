package org.iothello.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that keeps track of the current state of the game. The rules are
 *  also defined in this class along with the valid moves checks.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class GameGrid {
	public static final int EMPTY_MARKER = 0, WHITE_MARKER = 2, BLACK_MARKER = 1;
    public static final int GRID_ROWS = 8, GRID_COLUMNS = 8, NUMBER_OF_MARKERS = 32;
    private static final Vector2D NOT_VALID_MOVE = new Vector2D(-10,-10);
    
    /*
     * Defines returntypes for calcValidMoves method
     *  VALID_MOVES returns the position which the player can place markers
     *  while DIRECTIONS returns the directions that produced a valid move(used 
     *  by the switchColor method to determine the directions that should get flipped)
     */
    private static final int VALID_MOVES = 0, DIRECTIONS = 1;
    
    private int blackMarkers; 
    private int whiteMarkers;
    private int [][] board = new int[GRID_ROWS][GRID_COLUMNS];
    private List<Vector2D> validMoves = new ArrayList<>();
    private int blackPoints;
    private int whitePoints;

    /**
     *
     * @param v a list of Vector2D
     * @param b multiarray of ints
     * @param blackMarkers int
     * @param whiteMarkers int
     * @param blackPoints int
     * @param whitePoints int
     */
    public GameGrid(List<Vector2D> v, int [][] b, int blackMarkers, int whiteMarkers, int blackPoints, int whitePoints) {
        this.blackMarkers = blackMarkers;
        this.whiteMarkers = whiteMarkers;
        this.blackPoints = blackPoints;
        this.whitePoints = whitePoints;
        
        for(int i = 0; i < GRID_ROWS;i++){
            System.arraycopy(b[i], 0, this.board[i], 0, GRID_COLUMNS);
        }
        
        for(int i=0;i<this.validMoves.size();i++){
            this.validMoves.add(v.get(i));
        }
    }

    /**
     *
     */
    public GameGrid(){
        initGame();
    }

    /**
     *
     */
    public void initGame(){
        for(int i = 0; i < GRID_ROWS;i++){
            for(int j = 0; j < GRID_COLUMNS;j++){
                board[i][j] = EMPTY_MARKER;
            }
        }
        
        // Initializes the first four markers      
        board[3][3] = BLACK_MARKER;
        board[3][4] = WHITE_MARKER;
        
        board[4][3] = WHITE_MARKER;
        board[4][4] = BLACK_MARKER;
        
        blackMarkers = NUMBER_OF_MARKERS;
        whiteMarkers = NUMBER_OF_MARKERS;
    }

    /**
     *
     * @return gamegrid
     */
    public int[][] getGameGrid(){
        return board;
    }

    /**
     *
     * @param playerColor the color of the player
     * @return points
     */
    public List<Point> getValidMoves(int playerColor){
        //Remove all previous valid moves
        validMoves.clear();
        
        for(int i=0;i<GRID_ROWS;i++){
            for(int j = 0; j < GRID_COLUMNS;j++){
                if(board[i][j] == EMPTY_MARKER){
                    validMoves.addAll(calcValidMove(new Vector2D(i,j),playerColor,VALID_MOVES));
                }
            }
        }
        
        return vector2DToPoints(validMoves);
    }

    /**
     * Uses the switchColor method to flip the markers
     * in all valid directions produced by the calcValidMove
     *
     * @param x coordinate
     * @param y coordinate
     * @param color int color
     */
    public void updateGrid(int x, int y, int color){
        Vector2D pos = new Vector2D(x,y);
        List<Vector2D> updateList;

        updateList = calcValidMove(pos,color,DIRECTIONS);
        for (Vector2D anUpdateList : updateList) {
            Vector2D start = new Vector2D(pos);
            switchColor(start, anUpdateList, color);
        }
        board[x][y] = color;
    }

    /**
     *
     * @param x coordinate
     * @param y coordinate
     * @param color int color
     */
    public void setGrid(int x, int y, int color){
        if(board[x][y] == color)
            board[x][y] = 0;
        else
            board[x][y] = color;
    }

    /**
     * Returns a list of either all possible valid moves, or all directions that produce a valid move
     *  (used by the switchColor method).
     *
     * @param pos Vector2D
     * @param playerColor int
     * @param returnType the type that should be returned: VALID_MOVES or DIRECTIONS
     * @return list of Vector2D
     */
    public List<Vector2D> calcValidMove(Vector2D pos, int playerColor, int returnType){
        List<Vector2D> moves = new ArrayList<>();
        Vector2D direction = new Vector2D();
        Vector2D searchPos;

        // Two loops for the 8(9) directions
        for(int i = -1; i<=1;i++){
            for(int j = -1; j<=1; j++)
            {
                direction.setPosition(i,j);
                searchPos = findColor(pos,direction,playerColor,0);
                if(!searchPos.equals(NOT_VALID_MOVE)){
                    switch(returnType){
                        case(VALID_MOVES): moves.add(pos);break;
                        case(DIRECTIONS): moves.add(new Vector2D(direction));break;
                        default: //TODO: Exception?
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Recursively flips the markers in the given direction until
     * one of your own markers are crossed
     *
     * @param current Vector2D
     * @param direction Vector2D
     * @param color int
     */
    public void switchColor(Vector2D current, Vector2D direction, int color){
        current.add(direction);

        if(current.getX() >= GRID_COLUMNS || current.getY() >= GRID_ROWS || current.getX() < 0 || current.getY() < 0)
            return;
        
        if(board[current.getX()][current.getY()] == color)
            return;
        
        board[current.getX()][current.getY()] = color;
        switchColor(current,direction,color);
    }


    /**
     * Can be used to find valid moves or to find the directions for the switchColor method
     *
     * @param start Vector2D
     * @param direction Vector2D
     * @param playerColor int
     * @param count int
     * @return Vector2D
     */
    public Vector2D findColor(Vector2D start,Vector2D direction, int playerColor, int count){
        //Make a copy of the previous and go one step in the direction
        Vector2D current = new Vector2D(start); 
        current.add(direction);
        
        if(current.getX() >= GRID_COLUMNS || current.getY() >= GRID_ROWS || current.getX() < 0 || current.getY() < 0)
            return NOT_VALID_MOVE;
        
        //If your own marker is adjacent to the starting marker it is not a valid move
        if(count == 0 && board[current.getX()][current.getY()] == playerColor){
            return NOT_VALID_MOVE;
        }
        
        ///If there is an empty square in between it is not a valid move
        if(board[current.getX()][current.getY()] == EMPTY_MARKER){
            return NOT_VALID_MOVE;
        }
        
        //If you trap atleast one marker between two of yours it is a valid move
        if(count > 0 && board[current.getX()][current.getY()] == playerColor)
            return direction;
        return findColor(current,direction,playerColor, ++count);
    }

    /**
     *
     */
    public void calculatePoints(){
        whitePoints = 0;
        blackPoints = 0;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == WHITE_MARKER) {
                    whitePoints++;
                }
                if (board[i][j] == BLACK_MARKER) {
                    blackPoints++;
                }
            }
        }
    }

    /**
     *
     * @return number of black points
     */
    public int getBlackPoints(){
        return blackPoints;
    }

    /**
     *
     * @return number of white points
     */
    public int getWhitePoints(){
        return whitePoints;
    }
    
    /**
     * List<Vector2D> to List<Points>
     * 
     * @param input list of Vector2D
     * @return outList
     */
    private List<Point> vector2DToPoints(List<Vector2D> input) {
        List<Point> points = new ArrayList<>();
        Vector2D temp;
        Point point;

        while (!input.isEmpty()) {
            temp = input.remove(0);
            point = new Point(temp.getX(), temp.getY());
            points.add(point);
        }
        
        return points;
    }

    /**
     *
     * @param newGrid multidimen int array
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setGrid(int[][] newGrid) {
        board = newGrid;
    }

    /**
     *
     * @return new grid
     */
    public GameGrid copyGrid(){
        return new GameGrid(this.validMoves, this.board, this.blackMarkers, this.whiteMarkers, this.blackPoints, this.whitePoints);
    }
}
