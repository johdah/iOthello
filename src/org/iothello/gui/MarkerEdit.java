package org.iothello.gui;

import java.awt.event.MouseEvent;
import org.iothello.logic.GameGrid;

/**
 * En Jlabel som representerar spelarens mark�r(eller fr�nvaro av mark�r), samt inneh�ller en mouselistener f�r att kunna registrera drag.
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
     * Om show valid moves=enabled visas en speciell ikon ist�llet d� musen �r ovanf�r.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        //temp = (ImageIcon) getIcon();
        //setIcon(GameBoard.getMouse_Over_Marker());
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
