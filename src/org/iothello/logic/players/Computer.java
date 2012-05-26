package org.iothello.logic.players;

import java.awt.Point;
import java.util.Random;
import org.iothello.gui.GameFrame;
import org.iothello.logic.GameGrid;

/**
 * AI spelare som väljer ut ett de godkända dragen helt slumpmässigt.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Computer extends Player {

    @Override
    public Point getMove( GameGrid gamegrid) {
        try {
            Thread.sleep(GameFrame.getSpeed());
        } catch (InterruptedException ex) {
            System.out.println("Sleep error in "+this);
        }
      
        Point move = new Point();

        Random r = new Random();
        int i = r.nextInt(gamegrid.getValidMoves(this.getID()).size());
        Point move1 = (Point) gamegrid.getValidMoves(this.getID()).get(i);

        move.setLocation(move1.getX(), move1.getY());
        return move;

    }
}
