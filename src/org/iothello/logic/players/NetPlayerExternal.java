package org.iothello.logic.players;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.iothello.logic.GameGrid;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class NetPlayerExternal extends Player {
	private ServerSocket serversocket;
	private Socket socket;
	private DataInputStream in;

	    private Point move;

	    public NetPlayerExternal(int myPort) throws UnknownHostException, IOException {
	       
	        serversocket=new ServerSocket(myPort);
	        
	        socket=serversocket.accept();
	       
	        in=new DataInputStream(socket.getInputStream());
	    
	        
	    }
	    
	     @Override
		public Point getMove(GameGrid gamegrid) {
	        try {
	            
	            int x = in.readInt();
	            int y = in.readInt();
	          
	            move=new Point(x,y);
	            
	          
	        } catch(SocketException ex) {
	        
	            // Other side disconnected
	            System.out.println(ex);
	            this.forfeit();
	            JOptionPane.showMessageDialog(null, this.getName() + " disconnected!", "Forfeit!", JOptionPane.WARNING_MESSAGE);
	                
	        } catch (IOException ex) {
	            Logger.getLogger(NetPlayerExternal.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
	        return move;
	    }
	     
	     public void closeConnection() {
	        try {
	            in.close();
	            socket.close();
	            serversocket.close();
	        } catch (IOException e) {
	        }
	    }
}
