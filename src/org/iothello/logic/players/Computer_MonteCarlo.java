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
public class Computer_MonteCarlo extends Player {
	private boolean debug = false;
	
	static Random r = new Random();
    static int nActions = 5;
    static double epsilon = 1e-6;

    Computer_MonteCarlo[] children;
    double nVisits, totValue;
    
    /**
     * Return the trees arity
     * 
     * @return arity
     */
    public int arity() {
        return (isLeaf()) ? 0 : children.length;
    }
    
    /**
     * Expand the tree
     */
    public void expand() {
        children = new Computer_MonteCarlo[nActions];
        
        for (int i = 0; i < nActions; i++) {
            children[i] = new Computer_MonteCarlo();
        }
    }
	
	/**
	 * TODO: Not finished
	 */
	@Override
	public Point getMove(GameGrid gamegrid) {
		return null;
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
        return children == null;
    }
    
    /**
     * Ultimately a roll out will end in some value assume for now that it ends
     *  in a win or a loss and just return this at random.
     * 
     * @param treenode
     * @return win or loss
     */
    public double rollOut(Computer_MonteCarlo treenode) {
    	// TODO: -1 if loss, 0 if draw or +1 if win
        return r.nextInt(2);
    }
    
    /**
     * Select
     * @return selected node
     */
    private Computer_MonteCarlo select() {
    	Computer_MonteCarlo selected = null;
        double bestValue = Double.MIN_VALUE;
        
        for (Computer_MonteCarlo c : children) {
            double uctValue = c.totValue / (c.nVisits + epsilon) +
                       Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
                           r.nextDouble() * epsilon;
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }
    
    /**
     * Select action
     */
    public void selectAction() {
        List<Computer_MonteCarlo> visited = new LinkedList<Computer_MonteCarlo>();
        Computer_MonteCarlo cur = this;
        visited.add(this);
        
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
        }
        
        cur.expand();
        Computer_MonteCarlo newNode = cur.select();
        visited.add(newNode);
        double value = rollOut(newNode);
        for (Computer_MonteCarlo node : visited) {
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
	
	/**
	 * Update total value
	 * @param value
	 */
	public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }
}