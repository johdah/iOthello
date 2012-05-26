package org.iothello.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import org.iothello.logic.GameGrid;

/**
 * En Jlabel som representerar spelarens markör(eller frånvaro av markör), samt innehåller en mouselistener för att kunna registrera drag.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class MarkerEdit extends Marker {
	private int value = 0;
    private GameGrid gamegrid;
        
    public MarkerEdit(GameBoard board, GameGrid gamegrid) {
        super(board);
        this.gamegrid = gamegrid;
    }
    /*
     * Om show valid moves=enabled visas en speciell ikon istället då musen är ovanför.
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
