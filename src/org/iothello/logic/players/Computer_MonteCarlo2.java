package org.iothello.logic.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.iothello.logic.GameGrid;

/**
 * An AI that implements the Monte Carlo algorithm for reinforcement learning.
 * 
 * Monte Carlo is a method for making optimal decisions in artificial intelligence (AI) problems,
 *  typically move planning in combinatorial games. It combines the generality of random simulation 
 *  with the precision of tree search.
 *  
 * The basic MCTS algorithm is simple: a search tree is built, node by node, according to the 
 *  outcomes of simulated playouts. The process can be broken down into the following steps.
 * 1. Selection: Starting at root node R, recursively select optimal child nodes (explained below) until a leaf node L is reached.
 * 2. Expansion: If L is a not a terminal node (i.e. it does not end the game) then create one or more child nodes and select one C.
 * 3. Simulation: Run a simulated playout from C until a result is achieved.
 * 4. Backpropagation: Update the current move sequence with the simulation result.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Computer_MonteCarlo2 extends Player {
	private boolean debug = false;
	
	private GameGrid grid;
	private Point point;
	static Random rand = new Random();
    static int nActions = 5;
    static double epsilon = 1e-6;
    private int gameStatus = 0;

    List<Computer_MonteCarlo2> children;
    double nVisits, totValue;
    
    public Computer_MonteCarlo2() {}
    
    public Computer_MonteCarlo2(GameGrid oldGrid, Point p) {
		this.grid = oldGrid.copyGrid();
		this.setPoint(p);
	}

	/**
     * Return the trees arity
     * 
     * @return arity
     */
    public int arity() {
        return (isLeaf()) ? 0 : children.size();
    }
    
    /**
     * Todo: I think it's done
     * 
     * Expand the tree
     */
    public void expand() {
        children = new ArrayList<Computer_MonteCarlo2>();
        
    	for(Point p : grid.getValidMoves(this.getID())) {
            children.add(new Computer_MonteCarlo2(grid, p));
        }
    }
	
	public int getGameStatus() {
		return gameStatus;
	}

	/**
	 * TODO: Not finished
	 */
	@Override
	public Point getMove(GameGrid gamegrid) {
		this.grid = gamegrid.copyGrid();
		Computer_MonteCarlo2.rand = new Random(System.currentTimeMillis());
		this.setID(this.getID());
		
		long endTime = System.currentTimeMillis() + 10000;
	    while (System.currentTimeMillis() < endTime) {
	         runTrial(this, true);
	    }
	     
	    //if (g.gameStatus(curState) == Game.status.ONGOING) {
	         Computer_MonteCarlo2 best = this.select();
	         return best.getPoint();
	    //}
	}

	/**
	 * Get point
	 * @return point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * Is debug enabled
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * @return isLeaf
	 */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }
    
    /**
	 * TODO: Not finished
	 * 
     * Ultimately a roll out will end in some value assume for now that it ends
     *  in a win or a loss and just return this at random.
     * 
     * @param treenode
     * @return win or loss
     */
    public double rollOut(Computer_MonteCarlo2 treenode) {
    	// TODO: -1 if loss, 0 if draw or +1 if win
        return rand.nextInt(2);
    }

	private void runTrial(Computer_MonteCarlo2 node, boolean myTurn) {
		selectAction();
		/*Game.status returnStatus;
	      node.visit();
	      if (!node.isLeaf()) {
	         //selection
	         returnStatus = runTrial(node.bestSelection(myTurn), !myTurn);
	      } else {
	         //expansion
	         node.expand(g.getPossibleMoves(node.getState()));
	         if (!node.isLeaf()) {
	            node = node.getRandomChild();
	            node.visit();
	         }
	         //simulation
	         returnStatus = simulateFrom(node.getState());
	      }
	      //backpropogation
	      if (IWin(returnStatus)) {
	         node.setScore(node.getScore() + 1);
	      }
	      if (ILose(returnStatus)) {
	         node.setScore(node.getScore() - 1);
	      }
	      return returnStatus;*/
	}
    
    /**
	 * TODO: Not finished
	 * 
     * Select
     * @return selected node
     */
    private Computer_MonteCarlo2 select() {
    	Computer_MonteCarlo2 selected = null;
        double bestValue = Double.MIN_VALUE;
        
        for (Computer_MonteCarlo2 c : children) {
            double uctValue = c.totValue / (c.nVisits + epsilon) +
                       Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
                           rand.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        
        return selected;
    }
    
    /**
	 * TODO: Not finished
     * Select action
     */
    public void selectAction() {
        List<Computer_MonteCarlo2> visited = new LinkedList<Computer_MonteCarlo2>();
        Computer_MonteCarlo2 cur = this;
        visited.add(this);
        
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
        }
        
        cur.expand();
        Computer_MonteCarlo2 newNode = cur.select();
        visited.add(newNode);
        double value = rollOut(newNode);
        for (Computer_MonteCarlo2 node : visited) {
            // would need extra logic for n-player game
            node.updateStats(value);
        }
    }

	/**
	 * Enable/disable debug mode
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
	}

	/**
	 * Set point
	 * @param point
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	
	/**
	 * Update total value
	 * @param value
	 */
	public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }
}