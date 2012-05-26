package org.iothello.logic.players;

import java.awt.Point;
import java.util.List;
import org.iothello.gui.GameFrame;
import org.iothello.gui.dialogs.HelperDialog;
import org.iothello.logic.GameGrid;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Computer_AI_Minmax extends Player {
	private int maxdepth; // max depth to search
	private boolean runAlphaTest = true; // Should the alpha-beta pruning run
	private boolean debug = false;
	private int searchedNodesFM;
	private int searchedNodesTot;
	private double methodDST = 10; // How much should the Disc-Square Table affect the utility
	private double methodMobility = 3; // How much should the Mobility affect the utility
	
	/**
	 * The table that represents the position score in the 
	 *  Disc-Square Table
	 */
	private int posValues[][] = {
	        {100, -20, 10, 5, 5, 10, -20, 100},
	        {-20, -50, -2, -2, -2, -2, -50, -20},
	        {10, -2, -1, -1, -1, -1, -2, 10},
	        {5, -2, -1, -1, -1, -1, -2, 10},
	        {5, -2, -1, -1, -1, -1, -2, 10},
	        {10, -2, -1, -1, -1, -1, -2, 10},
	        {-20, -50, -2, -2, -2, -2, -50, -20},
	        {100, -20, 10, 5, 5, 10, -20, 100}};
	
	/**
	 * Setup the min-max algorithm
	 * @param max
	 * @param runAlphaTest 
	 */
	public Computer_AI_Minmax(int max, boolean runAlphaTest){
	    // Depth must be even to avoid horizont blindness.
	    this.maxdepth = max;
	    this.runAlphaTest = runAlphaTest;
	    this.searchedNodesFM = 0;
	    this.searchedNodesTot = 0;
	}
	
	/**
	 * Start the min-max algorithm
	 * 
	 * @param gamegrid
	 * @return Point minmax_Decision_
	 */
	private Point minmax_Decision(GameGrid gamegrid){
	    if(this.isDebug()){
	        int tot = this.searchedNodesTot;
	        this.setRunAlphaTest(false);
	        Point temp = null;
	        temp = minmax_Decision_(gamegrid);
	        this.setRunAlphaTest(true);            
	        this.searchedNodesTot = tot;
	    }
	    
	    return minmax_Decision_(gamegrid);        
	}
	
	/**
	 * Start the min-max algorithm
	 * 
	 * @param gamegrid
	 * @return Point result
	 */
	private Point minmax_Decision_(GameGrid gamegrid){
	    int currentdepth = 0;
	    this.searchedNodesFM = 0;
	    Point result = null;
	    double resultValue = Double.NEGATIVE_INFINITY;
	    
	    if(terminalState(currentdepth)){            
	        System.out.print("Kan vara smart att inte ha maxdjupet 0");
	    }
	    
	    // Get all valid moves
	    List lofMoves = gamegrid.getValidMoves(this.getID());
	    
	    // Loop through all possible grids
	    for(int i = 0; i < lofMoves.size(); i++){
	        Point p = (Point) lofMoves.get(i);
	        GameGrid g = gamegrid.copyGrid();
	        g.updateGrid(p.x,  p.y, this.getID());
	        
	        // Get min value for this grid
	        double value = minValue(g, currentdepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	        
	        if(value > resultValue || resultValue == Double.NEGATIVE_INFINITY) {
	            result = p;
	            resultValue = value;
	        }
	    }
	    
	    this.searchedNodesTot = searchedNodesTot + searchedNodesFM;       
	    if(isDebug()) {
	        if(this.isRunAlphaTest()){
	            HelperDialog.getInstance().logAdd("With alpha beta:");
	        }else{
	            HelperDialog.getInstance().logAdd("Without alpha beta:");
	        }
	            HelperDialog.getInstance().logAdd("Searched nodes to make this move: " + this.searchedNodesFM);
	            HelperDialog.getInstance().logAdd("Total nodes + this move: " + this.searchedNodesTot);
	//		                HelperDialog.getInstance().logAdd("Value of move: " + posValues[result.x][result.y]);
	        
	    }
	    this.searchedNodesFM = 0;
	    return result;
	}
	
	/**
	 * Return the max value
	 * 
	 * @param gamegrid
	 * @param currentdepth
	 * @param alpha
	 * @param beta
	 * @return double maxvalue
	 */
	private double maxValue(GameGrid gamegrid, int currentdepth, Double alpha, Double beta){
	    int currentID;
	    currentdepth++;
	    this.searchedNodesFM++;
	    double value = Double.NEGATIVE_INFINITY;
	    
	    if(terminalState(currentdepth)){
	        return getUtilityValue(gamegrid);
	    }
	        
	    List lofMoves = gamegrid.getValidMoves(this.getID());
	    
	    // Generates all possible grids. 
	    for(int i =0; i<lofMoves.size(); i++){
	        Point p = (Point) lofMoves.get(i);
	        GameGrid g = gamegrid.copyGrid();
	        g.updateGrid(p.x,  p.y, this.getID());
	
	        // Get the max value for this grid
	        value = Math.max(value, minValue(g, currentdepth, alpha, beta));
	        
	        // If runAlphaTest is true, and value is greater or equal to beta, we can prune the rest
	        if(value >= beta && runAlphaTest) {
	            return value;
	        }
	        
	        // Set alpha to the current max
	        alpha = Math.max(alpha, value);       
	    }
	
	    return value;
	}
	
	/**
	 * Return the minvalue
	 * 
	 * @param gamegrid
	 * @param currentdepth
	 * @param alpha
	 * @param beta
	 * @return double minvalue
	 */
	private double minValue(GameGrid gamegrid, int currentdepth, Double alpha, Double beta){
	    
	    int currentID;
	    
	    currentdepth++;
	    this.searchedNodesFM++;
	    double value = Double.POSITIVE_INFINITY;
	    
	    if(terminalState(currentdepth)){
	        return getUtilityValue(gamegrid);
	    }
	    
	    if(this.getID() == GameGrid.WHITE_MARKER)
	        currentID = GameGrid.BLACK_MARKER;
	    else
	        currentID = GameGrid.WHITE_MARKER;
	
	    List lofMoves = gamegrid.getValidMoves(currentID);
	    
	    // Generates all possible grids. 
	
	    for(int i =0; i<lofMoves.size(); i++){
	        Point p = (Point) lofMoves.get(i);
	        GameGrid g = gamegrid.copyGrid();
	        g.updateGrid(p.x,  p.y, myEnemyColor());
	        
	        // Get the min value for this gamegrid
	        value = Math.min(value, maxValue(g, currentdepth, alpha, beta));
	        
	        // If runAlphaTest is true and value is less or equal to alpha, we can prune the rest
	        if(value <= alpha && runAlphaTest) {
	            return value;
	        }
	        
	        // Set beta to the current minimum
	        beta = Math.min(beta, value);
	    }
	
	    return value;
	}
	
	/**
	 * If this is the maximum depth we return true
	 * 
	 * @param currentdepth
	 * @return bool Reached the terminal state
	 */
	private boolean terminalState(int currentdepth){
	    if(currentdepth >= this.maxdepth){
	        return true;
	    }
	    
	    return false;
	}
	
	/**
	 * Use methodDST and methodMobility to calculate points by the choosen
	 *  heuristic.
	 * 
	 * @param grid
	 * @return 
	 */
	public int getUtilityValue(GameGrid grid) {
	    double dst, mobility, result;
	    
	    dst = getUtilityValueDiscSquareTable(grid);
	    mobility = getUtilityValueMobility(grid);
	    
	    result = ((dst * methodDST) + (mobility * methodMobility)) / (methodDST + methodMobility);
	    
	    return (int) result;
	}
	
	/**
	 * Get the mobility value for the current grid
	 * 
	 * @param grid
	 * @return int mobility value
	 */
	public int getUtilityValueMobility(GameGrid grid) {
	    List lofMoves = null;
	    
	    if(getID() == GameGrid.WHITE_MARKER) {
	        lofMoves = grid.getValidMoves(GameGrid.BLACK_MARKER);
	    } else {
	        lofMoves = grid.getValidMoves(GameGrid.WHITE_MARKER);
	    }
	    
	    return 64 - lofMoves.size();
	}
	
	/**
	 * Get the DST value by comparing the current grid to the
	 *  Disc-Square Table
	 * 
	 * @param grid
	 * @return int DST value
	 */
	public int getUtilityValueDiscSquareTable(GameGrid grid){
	    int score = 0;
	    int[][] board; // [grid.GRID_ROWS][grid.GRID_COLUMNS]
	    
	    board = grid.getGameGrid();
	    
	    for(int y = 0; y < GameGrid.GRID_ROWS; y++) {
	        for(int x = 0; x < GameGrid.GRID_COLUMNS; x++) {
	            if(board[y][x] == getID()) {
	                score += posValues[y][x];
	            }
	        }
	    }
	    
	    return score;
	}
	
	/**
	 * Get the opponents ID
	 * 
	 * @return int enemyColor
	 */
	private int myEnemyColor(){
	    if(this.getID() == 1){
	        return 2;
	    } else {
	        return 1;
	    }
	}
	
	/**
	 * Get move
	 * 
	 * @param gamegrid
	 * @return Point
	 */
	@Override
	Point getMove(GameGrid gamegrid) {
	    try {
	        Thread.sleep(GameFrame.getSpeed());
	    } catch (InterruptedException ex) {
	        System.out.println("Sleep error in "+this);
	    }
	 
	    GameGrid g = new GameGrid();
	    g = gamegrid.copyGrid();       
	    
	    Point move = (Point) minmax_Decision(g);
	    
	    if(move == null) {
	        
	        System.out.println("Omg wtf??");
	    }
	    
	    if(isDebug()) {
	        HelperDialog.getInstance().logAdd("P"+this.getID() + " AI Move X=" + move.x + " Y="+move.y + "\n");
	    }
	    
	    return move;
	}
	
	/**
	 * Is the AI in debug mode
	 * 
	 * @return boolean
	 */
	public boolean isDebug() {
	    return debug;
	}
	
	/**
	 * Set if the AI should be in debug mode
	 * 
	 * @param boolean debug
	 */
	public void setDebug(boolean debug) {
	    this.debug = debug;
	}
	
	/**
	 * Get how much the DST affect the utility
	 * 
	 * @return double methodDST
	 */
	public double getMethodDST() {
	    return methodDST;
	}
	
	/**
	 * Set how much DST should affect the Utility
	 * 
	 * @param double methodDST
	 */
	public void setMethodDST(double methodDST) {
	    this.methodDST = methodDST;
	}
	
	/**
	 * Get how much the mobility affect the utility
	 * 
	 * @return double methodMobility
	 */
	public double getMethodMobility() {
	    return methodMobility;
	}
	
	/**
	 * Set how much the mobility should affect the utility
	 * 
	 * @param double methodMobility 
	 */
	public void setMethodMobility(double methodMobility) {
	    this.methodMobility = methodMobility;
	}
	
	/**
	 * Return true if alpha-beta pruning is set to run
	 * 
	 * @return boolean runAlphaTest
	 */
	public boolean isRunAlphaTest() {
	    return runAlphaTest;
	}
	
	/**
	 * Set if alpha-beta pruning should run
	 * 
	 * @param boolean runAlphaTest 
	 */
	public void setRunAlphaTest(boolean runAlphaTest) {
	    this.runAlphaTest = runAlphaTest;
	}
}
