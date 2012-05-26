package org.iothello.logic.players;

import java.awt.Point;
import java.util.List;
import org.iothello.gui.GameFrame;
import org.iothello.logic.GameGrid;

/**
 * AI som väljer ut det drag av de godkända som har högst värde enligt matris.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Computer_1 extends Player {

    @Override
	public Point getMove(GameGrid gamegrid) {
          try {
            Thread.sleep(GameFrame.getSpeed());
        } catch (InterruptedException ex) {
            System.out.println("Sleep error in "+this);
        }
      
        return getBestMove(gamegrid);
    }

    Point getBestMove(GameGrid grid) {
        int x, y;
        int posValues[][] = {
            {50, -1, 5, 2, 2, 5, -1, 50},
            {-1, -10, 1, 1, 1, 1, -10, -1},
            {5, 1, 1, 1, 1, 1, 1, 5},
            {2, 1, 1, 0, 0, 1, 1, 2},
            {2, 1, 1, 0, 0, 1, 1, 2},
            {5, 1, 1, 1, 1, 1, 1, 5},
            {-1, -10, 1, 1, 1, 1, -10, -1},
            {50, -1, 5, 2, 2, 5, -1, 50},};

        List validMoves = grid.getValidMoves(this.getID());
        int currBest = -100, cbX = 0, cbY = 0;
        for (int i = 0; i < validMoves.size(); i++) {
            Point move1 = (Point) validMoves.get(i);

            x = (int) move1.getX();
            y = (int) move1.getY();

            if (posValues[x][y] > currBest) {
                cbX = x;
                cbY = y;
                currBest = posValues[x][y];

            }
        }

        return new Point(cbX, cbY);
    }
}
