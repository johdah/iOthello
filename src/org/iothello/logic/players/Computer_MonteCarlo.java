package org.iothello.logic.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.iothello.logic.GameGrid;

/**
 * An AI that implements the Monte Carlo algorithm for reinforcement learning.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Computer_MonteCarlo extends Player {
	private boolean debug = false;
	
	/**
	 * TODO: Not finished
	 */
	@Override
	public Point getMove(GameGrid gamegrid) {
		/* Run a simulation
		 * 1. Pick a certain move.
		 * 2. Make random moves until the game is ﬁnished.
		 * 3. Calculate the winner and report back the result up the tree to the initial
		 *  move.
		 */
		
		/*Using many of these simulations, a simple MC algorithm would be to:

		1. Find out all possible moves to make.
		
		2. Divide the total number of simulations by how many possible moves there
		are.
		
		3. Run random simulations this many times for each move.
		
		4. Find the move that had the highest chance of winning. From this, infer
		that such a move is the best in this situation.*/
		return null;
	}

	/**
	 * Is debug enabled
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}
	
	private boolean isLeafNode(GameGrid grid) {
		if(!grid.getValidMoves(0).isEmpty()) return false;
		if(!grid.getValidMoves(1).isEmpty()) return false;
		return true;
	}
	
	private void playOneSequence(GameGrid grid) {
		int index = 0;
		List<GameGrid> nodes = new ArrayList<GameGrid>();
		nodes.add(grid);
		
		while(!isLeafNode(nodes.get(index))) {
			nodes.add(nodes.get(index));
			//i++;
		}
		//updateValues(node, -grid.get(i));
	}

	/**
	 * Enable/disable debug mode
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}