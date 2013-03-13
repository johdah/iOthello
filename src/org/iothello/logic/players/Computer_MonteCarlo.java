package org.iothello.logic.players;

import java.awt.Point;
import java.util.List;
import java.util.Random;

import org.iothello.logic.GameGrid;

/**
 * MCTSPlayer is an implementation of Player that makes moves using UCT (A Monte
 * 	Carlo Tree Search that uses an Upper Confidence Bounds formula).
 *
 * @author Johan Dahlberg <info@johandahlberg.com>
 * TODO: Not finished
 */
public class Computer_MonteCarlo extends Player {
	public final int STATUS_ONGOING = 1, STATUS_PLAYER1WIN = 2, STATUS_DRAW = 3, STATUS_PLAYER2WIN = 4;
	private boolean debug = false;

	//protected Game g;
    protected GameGrid grid;
	private MonteCarlo_Node curNode;
	private MonteCarlo_Node tree;
	private int thinkTime;

	/**
	 * Instantiates the player.
	 *
	 * @param thinkTime How many milliseconds this player is allowed to think per
	 *  turn (Longer think time yields better simulations.
	 */
	public Computer_MonteCarlo(int thinkTime) {
		this.thinkTime = thinkTime;
	}

	/**
	 * Gets the current state of the game.
	 *
	 * @return the current state of the game.
	 */
	public GameGrid getGameGrid() {
		return grid;
	}

	/**
     * Simulates possible games until allowed think time runs out, and thn makes
     * a move.
     */
	@Override
	public Point getMove(GameGrid gamegrid) {
		this.grid = gamegrid;
		tree = new MonteCarlo_Node(grid, null, this, this.getID() + 1);
		curNode = tree;
		curNode.expand();
		
		// Begin
		long endTime = System.currentTimeMillis() + thinkTime;
		while (System.currentTimeMillis() < endTime) {
			runTrial(curNode, true);
		}
	    
		//if (grid.gameStatus(curState) == Game.status.ONGOING) {
			MonteCarlo_Node best = curNode.bestMove();
			//grid = best.getState();
			curNode = best;
	    //}
		
		return curNode.bestMove().getPoint();
	}

	/**
	 * Gets a random move from a given state.
	 *
	 * @param node a game state from which a random child state is desired.
	 * @return a random child state of the passed state.
	 */
	private MonteCarlo_Node getRandomMoveFrom(MonteCarlo_Node node) {
		List<Point> moves = node.getGrid().getValidMoves(node.getCurPlayer());
		Random rand = new Random(System.currentTimeMillis());
		int r = rand.nextInt(moves.size());
		
		MonteCarlo_Node newNode = new MonteCarlo_Node(node.getGrid(), moves.get(r), this, node.getCurPlayer());
		
		return newNode;
	}
	
	/**
	 * Returns true if this player loses, false otherwise.
	 *
	 * @param status the status of the game to be checked.
	 * @return true if this player loses, false otherwise.
	 */
	protected boolean ILose(int status) {
		return (status == this.STATUS_PLAYER1WIN && this.getID() == 2)
			|| (status == STATUS_PLAYER2WIN && this.getID() == 1);
	}

	/**
	 * Is debug enabled
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Returns true if this player wins, false otherwise.
	 *
	 * @param status the status of the game to be checked.
	 * @return true if this player wins, false otherwise.
	 */
	protected boolean IWin(int status) {
		return (status == STATUS_PLAYER1WIN && this.getID() == 1)
			|| (status == STATUS_PLAYER2WIN && this.getID() == 2);
	}

	/**
	 * Plays a single simulated game, and encompasses the four stages of an MCTS
	 *  simulation (selection, expansion, simulation, and backpropogation).
	 *  Selection: Pick a node to simulate from by recursively applying UCB.
	 *  Expansion: Add a new set of nodes to the link tree as children of the
	 *  selected node. Simulation: Pick one of those nodes and simulate a game
	 *  from it. Backpropogation: Rank all nodes selected during the selection
	 *  step based on simulation outcome.
	 *
	 * @param node The node to begin running the trial from.
	 * @param myTurn Whether it is this players turn or not.
	 * @return The status of the trial.
	 */
	   private int runTrial(MonteCarlo_Node node, boolean myTurn)
	   {
	      int returnStatus;
	      node.visit();
	      if (!node.isLeaf()) {
	         //selection
	         returnStatus = runTrial(node.bestSelection(myTurn), !myTurn);
	      } else {
	         //expansion
	         node.expand();
	         
	         if (!node.isLeaf()) {
	            node = node.getRandomChild();
	            node.visit();
	         }
	         
	         //simulation
	         returnStatus = simulateFrom(node);
	      }
	      
	      //backpropogation
	      if (IWin(returnStatus)) {
	         node.setScore(node.getScore() + 1);
	      }
	      if (ILose(returnStatus)) {
	         node.setScore(node.getScore() - 1);
	      }
	      
	      return returnStatus;
	   }

	/**
	 * Enable/disable debug mode
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	/**
	 * Performs a simulation or "rollout" for the "simulation" phase of the
	 *  runTrial function. This can be written to contain game-specific heuristics
	 *  or "finishing move" detection if desired.
	 *
	 * @param node the state to be simulated from.
	 * @return the resulting status of the simulation.
	 */
	protected int simulateFrom(MonteCarlo_Node node) {
		int player = node.getCurPlayer();
		if(player == 0) {
			if(node.getGrid().getValidMoves(0) == null) {
				if(node.getGrid().getValidMoves(1) == null) {
					if(node.getGrid().getWhitePoints() > node.getGrid().getBlackPoints()) {
						return STATUS_PLAYER1WIN;
					} else if(node.getGrid().getWhitePoints() < node.getGrid().getBlackPoints()) {
						return STATUS_PLAYER2WIN;
					} else {
						return STATUS_DRAW;
					}
				}
			}
		}
         
		return simulateFrom(getRandomMoveFrom(node));
	}
	
	public void updateGameState(GameGrid gg)
	{
		grid = gg;
		if (curNode.isLeaf()) {
			curNode.expand();
		}
		
		curNode = curNode.findChildNode(gg);
	}
}