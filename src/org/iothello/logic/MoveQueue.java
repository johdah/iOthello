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
                    //v�ntar p� input
                    noMoveCon.await();
                }
            } finally {
                lock.unlock();
            }
            noMove = true;
            //kollar om spelet �r avslutat eller om draget �r godk�nt
            if (frame.endGame() || moves.contains(move)) {
                break;
            }
        }
        return move;
    }
    
    /*
     * S�tter draget och sl�pper l�s-condition
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
     * K�rs om man klickar p� new game
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
