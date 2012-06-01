package org.iothello.logic.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.iothello.logic.GameGrid;

/**
 * A class that represends a node in the MC tree
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class MonteCarlo_Node {
	private GameGrid grid;

	private Point point;
	private List<MonteCarlo_Node> children;
	private Random rand;
	private float score;
	private int timesVisisted;
	private Player player;
	private int curPlayer;

	/**
	 * Instantiates an MCTSNode and initializes ranking variables.
	 *
	 * @param grid The grid that we will be working on
	 * @param p 
	 */
	public MonteCarlo_Node(GameGrid grid, Point p, Player player, int curPlayer) {
		this.grid = grid.copyGrid();
		if(p != null) this.grid.updateGrid(p.x, p.y, curPlayer);
		this.player = player;
		this.setPoint(p);
		timesVisisted = 0;
		score = 0;
		children = null;
		rand = new Random(System.currentTimeMillis());
	}

	/**
	 * Chooses the best available move (node) following this node.
	 *
	 * @return the best available move (node) following this node.
	 */
	public MonteCarlo_Node bestMove() {
		float max = -Float.MAX_VALUE;
		int maxIndex = rand.nextInt(children.size());
		float randomizer;
		
		for (int i = 0; i < children.size(); i++) {
			MonteCarlo_Node node = children.get(i);
			float nodeScore = (float) node.getScore() / (float) node.getTimesVisited();
			randomizer = Float.MIN_VALUE * rand.nextInt(children.size() * children.size());
			
			if (nodeScore + randomizer > max) {
				max = nodeScore + randomizer;
	            maxIndex = i;
			}
		}
		
		return children.get(maxIndex);
	}

	/**
	 * Uses an Upper Confidence Bounds formula to select the best node for the
	 *  "selecction" phase of a single MCTS game simulation. The UCB formula is
	 *  used to balance the value of exploring relatively unexplored nodes against
	 *  the value of exploring nodes that are highly ranked thus far. Function
	 *  assumes alternating turns between two opposing players in a \ zero-sum
	 *  game.
	 *
	 * @param myTurn Whether or not it is the turn of the {@link Computer_MonteCarlo} 
	 *  that contains this node.
	 * @return The best seletion from this node.
	 */
	public MonteCarlo_Node bestSelection(boolean myTurn) {
		int turn;
		if (myTurn) {
			turn = 1;
		} else {
			turn = -1;
		}
		
		//the randomizer is a tiny random number added for tie-breaking
		float bias, randomizer;
      	float max = -Float.MAX_VALUE * turn;
      	int maxIndex = 0;
      	float C = 1;
      	
      	for (int i = 0; i < children.size(); i++) {
	         MonteCarlo_Node node = children.get(i);
	         float nodeScore = (float) node.getScore() / ((float) (node.getTimesVisited() + Float.MIN_VALUE));
	         bias = 2 * C * (float) (Math.sqrt(Math.log((float) this.getTimesVisited()) / ((float) node.getTimesVisited() + Float.MIN_VALUE)));
	         randomizer = Float.MIN_VALUE * rand.nextInt(children.size() * children.size());
	         float biasedScore = nodeScore + randomizer + (bias * turn);
	         
	         if (biasedScore * turn > max * turn) {
	            max = biasedScore;
	            maxIndex = i;
	         }
      	}
      	
      	return children.get(maxIndex);
	}

	/**
	 * Expands the tree by adding a set of child nodes to this node.
	 *
	 * @param possibleMoves The list of move to be added as child nodes to this
	 * node.
	 */
	public void expand() {
		children = new ArrayList<MonteCarlo_Node>();
		for (Point p : grid.getValidMoves(curPlayer)) {
			children.add(new MonteCarlo_Node(grid, p, player, (curPlayer == 1) ? 2 : 1));
		}
	}
	
	/**
	 * Get current player
	 * @return
	 */
	public int getCurPlayer() {
		return curPlayer;
	}
	
	/**
	 * Get grid
	 * @return grid
	 */
	public GameGrid getGrid() {
		return grid;
	}

	/**
	 * Gets the child nodes of this node.
	 *
	 * @return the child nodes of this node.
	 */
	public List<MonteCarlo_Node> getNextMoves() {
		return children;
	}

	/**
	 * Get point
	 * @return point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * Gets the GameState for this node.
	 *
	 * @return the GameState for this node.
	 */
	public GameGrid getState() {
		return grid;
	}

	/**
	 * Returns the number of times this node has been visited.
	 *
	 * @return The number of times this node has been visited.
	 */
	public int getTimesVisited() {
		return timesVisisted;
	}

	/**
	 * Finds a child of the current Node that has a given GameState.
	 *
	 * @param s is the state to be searched for.
	 * @return matching node if found, null otherwise.
	 */
	public MonteCarlo_Node findChildNode(GameGrid gg) {
		for (MonteCarlo_Node x : children) {
			if (x.getState().equals(gg)) {
				return x;
			}
		}
		
		return null;
	}

	/**
	 * Returns a random child node of this node.
	 *
	 * @return a random child node of this node.
	 */
	public MonteCarlo_Node getRandomChild() {
		int random = rand.nextInt(children.size());
		return children.get(random);
	}

	/**
	 * Gets the score for this node.
	 *
	 * @return the score for this node.
	 */
	public float getScore() {
		return score;
	}

	/**
	 * Returns whether this node is a leaf (has no child nodes).
	 *
	 * @return whether this node is a leaf (has no child nodes).
	 */
	public boolean isLeaf() {
		return children == null || children.isEmpty();
	}

	/**
	 * Set player
	 * @param curPlayer
	 */
	public void setCurPlayer(int curPlayer) {
		this.curPlayer = curPlayer;
	}

	/**
	 * Set grid
	 * @param grid
	 */
	public void setGrid(GameGrid grid) {
		this.grid = grid;
	}

	/**
	 * Set point
	 * @param point
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	
	/**
	 * Sets the score of this node.
	 *
	 * @param score the new score to be set for this node.
	 */
	public void setScore(float score) {
		this.score = score;
	}

	/**
	 * Returns the string representation of the GameState of this node.
	 *
	 * @return the string representation of the GameState of this node.
	 */
	@Override
	public String toString() {
		return grid.toString();
	}

	/**
	 * Increases the number of visits recorded to this node by one.
	 */
	public void visit() {
		timesVisisted++;
	}
}
