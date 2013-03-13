package org.iothello.logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Point;
import java.util.List;
import org.iothello.gui.GameFrame;
import org.iothello.gui.dialogs.HelperDialog;
import org.iothello.logic.players.Player;

public class GameManager {
	private Player playerOne; 
    private Player playerTwo;
    private int turns = 0; // TODO: Should this exist?

	public GameManager(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public int gameplay(GameFrame frame, GameGrid gamegrid, boolean testMode) {
        Point move = new Point();
        // TODO: Should probably be created here
        //GameGrid gamegrid = new GameGrid();
        
        if (!testMode) {
            frame.setEndGame(false); //�terst�ller endgame
        }

        Player currentPlayer = playerTwo;

        List<Point> validMoves;

        // Gameloop
        while (true) {
            // In edit mode, we should only update the board
            while(HelperDialog.getInstance().isStep() && !HelperDialog.getInstance().isNext()) {
                try {
                    frame.updateBoard(gamegrid, currentPlayer.getID());
                
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // Switching players
            if(frame.editPlayer == -1) {
                if (currentPlayer == playerOne) currentPlayer = playerTwo;
                else currentPlayer = playerOne;
            } else {
                if(frame.editPlayer == 0)
                    currentPlayer = playerOne;
                else
                    currentPlayer = playerTwo;
                
                frame.editPlayer = -1;
            }

            // Count the current points
            gamegrid.calculatePoints();
            playerOne.setPoints(gamegrid.getBlackPoints());
            playerTwo.setPoints(gamegrid.getWhitePoints());

            // If testmode is activated, no Draw is done
            if (!testMode) {
                frame.setFrameLabels(playerOne, playerTwo, currentPlayer);
            }

            validMoves = gamegrid.getValidMoves(currentPlayer.getID());
            // If testmode is activated, no Draw is done
            if (!testMode && currentPlayer.getValidMoves()) {
                frame.updateBoard(gamegrid, currentPlayer.getID());
            }

            if (validMoves.isEmpty()) {
                currentPlayer.setValidMoves(false);
            } else {
                currentPlayer.setValidMoves(true);
            }
            
            if (currentPlayer.getValidMoves()) {
                move.setLocation(currentPlayer.getMove(gamegrid));
            }
            
            if (!testMode && frame.endGame()) {
                break;
            }

            if (currentPlayer.getValidMoves()) {
                gamegrid.updateGrid(move.x, move.y, currentPlayer.getID());
            }

            // The other player has forfeit
            if(playerOne.forfeited()) {
                // Punishment, half the score!
                playerOne.setPoints(playerOne.getPoints() / 2);
                
                frame.showWinnerDialog(playerTwo);
                return playerTwo.getID();
            }
            
            if(playerTwo.forfeited()) {
                // Punishment, half the score!
                playerTwo.setPoints(playerTwo.getPoints() / 2);
                
                frame.showWinnerDialog(playerOne);
                return playerOne.getID();
            }

            // If no players has accepted moves the game is over. The dialog is choosen based on the result
            if (!playerOne.getValidMoves() && !playerTwo.getValidMoves()) {
                if (playerOne.getPoints() > playerTwo.getPoints()) {
                    if (!testMode) {
                        frame.showWinnerDialog(playerOne);
                    }
                    return playerOne.getID();
                }
                
                if (playerOne.getPoints() < playerTwo.getPoints()) {
                    if (!testMode) {
                        frame.showWinnerDialog(playerTwo);
                    }
                    return playerTwo.getID();
                }
                
                if (playerOne.getPoints() == playerTwo.getPoints()) {
                    if (!testMode) {
                        frame.showDrawnDialog();
                    }
                    return 0;
                }
                
                break;
            }
            
            setTurns(turns + 1);
        }
        
        return 3;
    }

    /**
     * Get turns
     * 
     * @return
     */
    public int getTurns() {
		return turns;
	}

    /**
     * Set turns
     * 
     * @param turns
     */
	public void setTurns(int turns) {
		this.turns = turns;
	}
}