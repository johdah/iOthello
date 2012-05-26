package org.iothello.logic.players;

import java.awt.Point;

import org.iothello.logic.GameGrid;

/**
 * An AI that implements the Monte Carlo algorithm for reinforcement learning.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Computer_MonteCarlo extends Player {
	private boolean debug = false;
	
	/**
	 * 
	 */
	@Override
	public Point getMove(GameGrid gamegrid) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}