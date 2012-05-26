package org.iothello.logic.players;

import java.awt.Point;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.iothello.logic.GameGrid;
import org.iothello.logic.MoveQueue;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class NetPlayerLocal extends Player {

    private Socket socket;
    private DataOutputStream out;
  

    public NetPlayerLocal(String oppIP, int oppPort) throws IOException {
        try {
            //väntar lite på att motståndaren ska hinna skapa serversocket
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            //  Logger.getLogger(NetSetup.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            this.socket = new Socket(oppIP, oppPort);
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch(ConnectException e) {
            System.out.println(e);
        }

    }
    MoveQueue moveq = new MoveQueue();

    @Override
    public Point getMove(GameGrid gamegrid) {
        Point move = null;
        try {
            move = moveq.getMove(gamegrid.getValidMoves(this.getID()));
        } catch (InterruptedException ex) {
            System.out.println("Something went wrong. Error: " + ex);
        }
        try {
            out.writeInt(move.x);
            out.writeInt(move.y);
        } catch (IOException ex) {
            Logger.getLogger(NetPlayerLocal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return move;
    }
    
    public void closeConnection() {
       try {
           socket.close();
       } catch (IOException e) {
       }
    }
}
