package org.iothello.logic.players;

import org.iothello.logic.GameGrid;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Johan Dahlberg
 * Date: 2013-03-13
 * Time: 18:35
 * @author Johan Dahlberg <info@johandahlberg.com>
 * @author Andreas Westberg
 */
public class Computer_TD extends Player {
    private boolean debug;

    @Override
    public Point getMove(GameGrid gamegrid) {
        return null;
    }

    /**
     * Enable/disable debug mode
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
