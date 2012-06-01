package org.iothello.logic.players;

import java.awt.Point;

import org.iothello.logic.GameGrid;
import org.iothello.logic.players.td.TDGrid;
import org.iothello.logic.players.td.TDPolicy;
import org.iothello.logic.players.td.TDLearner;

public class Computer_TD extends Player {
	private boolean debug = false;
	
	public TDLearner learner;
	public int epochswaiting = 0, epochsdone = 0, totaldone = 0;
	long delay;
	int UPDATE_EPOCHS = 100;
	
	public boolean newInfo;
	
	public Computer_TD(GameGrid grid) {
		this(grid, 0);
	}
	
	public Computer_TD(GameGrid grid, long waitperiod) {
		// create TDLearner
		learner = new TDLearner(new TDGrid(grid), debug);
		delay = waitperiod;		
	}

	@Override
	public Point getMove(GameGrid gamegrid) {
	 	if (epochswaiting > 0) {
			if(isDebug()) System.out.println("Running " + epochswaiting + " epochs");
			learner.running = true;
			while(epochswaiting > 0) {
				epochswaiting--;
				epochsdone++;
				learner.runEpoch();
				
				// Inform gui we are finished
				//if (epochswaiting % UPDATE_EPOCHS == 0) 
			}
			
			totaldone += epochsdone;
			epochsdone = 0;
			learner.running = false;
			
			newInfo = true;
			// Inform gui we are finished
		}
		return null;
	}

	/**
	 * Is debug enabled
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}
	
	public synchronized TDPolicy resetLearner() {
		totaldone = 0;
		epochsdone = 0;
		epochswaiting = 0;

		return learner.newPolicy();		
	}

	/**
	 * Enable/disable debug mode
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public void setEpisodes(int episodes) { 
		if(isDebug()) System.out.println("Setting " + episodes + " episodes");
		this.epochswaiting += episodes;
	}
	
	public void stopLearner() {
		if(isDebug()) System.out.println("Stopping learner.");
		newInfo = false;
		epochswaiting = 0;
		totaldone += epochsdone;
		epochsdone = 0;

		learner.running = false;
	}
}