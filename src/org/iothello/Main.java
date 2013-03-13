package org.iothello;

import org.iothello.logic.Othello;
import java.io.IOException;

/**
 * Initialize the game
 *
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Main {
    /**
     * @param args Arguments from console
     */
	public static void main(String[] args) {
		Othello othello = new Othello();
		try {
			othello.startOthello();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}  
	}
}