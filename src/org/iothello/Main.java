package org.iothello;

import org.iothello.logic.Othello;
import java.io.IOException;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Othello othello = new Othello();
		try {
			othello.startOthello();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}