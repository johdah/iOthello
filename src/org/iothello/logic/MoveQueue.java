package org.iothello.logic;

import org.iothello.gui.GameFrame;
import java.awt.Point;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Containing the players move
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class MoveQueue {
	private static boolean noMove = true;
    private static Point move = new Point();
    private static final Lock lock = new ReentrantLock();
    private static final Condition noMoveCon = lock.newCondition();
    private static GameFrame frame;

    public Point getMove(List<Point> moves) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (noMove) {
                    //väntar på input
                    noMoveCon.await();
                }
            } finally {
                lock.unlock();
            }
            noMove = true;
            //kollar om spelet är avslutat eller om draget är godkänt
            if (frame.endGame() || moves.contains(move)) {
                break;
            }
        }
        return move;
    }
    
    /*
     * Sätter draget och släpper lås-condition
     */
    public void setMove(int x, int y) {
        lock.lock();
        try {
            move.setLocation(x, y);
            noMove = false;
            noMoveCon.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void setFrame(GameFrame frame) {
        MoveQueue.frame = frame;
    }
    
    /*
     * Körs om man klickar på new game
     */
    public void releaseLock() {
        lock.lock();
        try {
            noMove = false;
            noMoveCon.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
