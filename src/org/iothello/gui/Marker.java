package org.iothello.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * En Jlabel som representerar spelarens markör(eller frånvaro av markör), samt innehåller en mouselistener för att kunna registrera drag.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Marker extends JLabel implements MouseListener {
	private static final long serialVersionUID = -3826593595197073273L;
	protected Point pos;
    protected ImageIcon temp;
    protected int value = 0;
    protected GameBoard gameboard;

    public Marker(GameBoard gameboard) {
        this.gameboard = gameboard;
    }

    public void setPos(int x, int y) {
        pos = new Point(x, y);
    }

    public Point getPos() {
        return pos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameboard.setMove(pos.x, pos.y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /*
     * Om show valid moves=enabled visas en speciell ikon istället då musen är ovanför.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (value == 3) {
            temp = (ImageIcon) getIcon();
            setIcon(GameBoard.getMouse_Over_Marker());
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (value == 3) {
            setIcon(temp);
        }
    }

    public void setValue(int i) {
        value = i;
    }
}
