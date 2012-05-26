package org.iothello.logic.players;

import java.awt.Point;

import org.iothello.logic.GameGrid;
import org.iothello.logic.MoveQueue;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Human extends Player {
    MoveQueue moveq = new MoveQueue();

    @Override
    public Point getMove(GameGrid gamegrid) {
        Point move = null;
        try {
            move = moveq.getMove(gamegrid.getValidMoves(this.getID()));
        } catch (InterruptedException ex) {
            System.out.println("Something went wrong. Error: "+ex);
        }
        return move;
    }
}
