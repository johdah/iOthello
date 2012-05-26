package org.iothello.logic.players;

import java.awt.Point;

import org.iothello.logic.GameGrid;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
abstract public class Player {
	private String name;
    private int ID;
    private boolean validMoves = true, forfeit = false;
    private int points = 0;

    abstract Point getMove(GameGrid gamegrid);

    public boolean forfeited() {
        return forfeit;
    }
    
    public void forfeit() {
        forfeit = true;
    }
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setValidMoves(boolean b) {
        this.validMoves = b;
    }

    public boolean getValidMoves() {
        return validMoves;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    public int getPoints() {
        return points;
    }
}
