package org.iothello.gui;

import java.awt.event.MouseEvent;
import org.iothello.logic.GameGrid;

/**
 * An Jlabel that represents the players marker (or absence), and a MouseListener to register moves.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class MarkerEdit extends Marker {
	private static final long serialVersionUID = -6379649564869328703L;
    private GameGrid gamegrid;
        
    public MarkerEdit(GameBoard board, GameGrid gamegrid) {
        super(board);
        this.gamegrid = gamegrid;
    }
    
    /**
     * If show valid moves is true, show an icon instead of the mouse above.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        gameboard.hoover_j = pos.x;
        gameboard.hoover_i = pos.y;

    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    	// Right-click
    	if(e.getButton() == MouseEvent.BUTTON3) {
            gameboard.setMove(pos.x, pos.y);
            gamegrid.setGrid(pos.x, pos.y, GameGrid.BLACK_MARKER);
    	} else { // Left-click
            gameboard.setMove(pos.x, pos.y);
            gamegrid.setGrid(pos.x, pos.y, GameGrid.WHITE_MARKER);
    	}    
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setIcon(temp);
    }

    @Override
    public void setValue(int i) {
        value = i;
    }
}
