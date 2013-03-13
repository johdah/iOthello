package org.iothello.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * An Jlabel that represents the players marker (or absence), and a MouseListener to register moves.
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

    @SuppressWarnings("UnusedDeclaration")
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

    /**
     * If show valid moves is true, show an icon instead of the mouse above.
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
