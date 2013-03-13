package org.iothello.logic.players.td_old;

import org.iothello.logic.GameGrid;

public class TDGrid implements TDWorld {
	GameGrid grid;
	
	public TDGrid(GameGrid grid) {
		this.grid = grid;
	}

	@Override
	public int[] getDimension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getNextState(int action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getReward() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean validAction(int action) {
		return false;
	}

	@Override
	public boolean endState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] resetState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getInitValues() {
		// TODO Auto-generated method stub
		return 0;
	}
}