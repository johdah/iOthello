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
    private Player currentPlayer;
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
            frame.setEndGame(false); //återställer endgame
        }

        currentPlayer = playerTwo;

        List<Point> validMoves;
        //While som rullar på tills spelet är slut.
        while (true) {
            // I edit mode så ska vi bara uppdatera brädet
            while(HelperDialog.getInstance().isStep() && !HelperDialog.getInstance().isNext()) {
                 try {
                    frame.updateBoard(gamegrid, currentPlayer.getID());
                
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
            }
            
            //Byter spelare.
            if(frame.editPlayer == -1) {
                if (currentPlayer == playerOne) {
                    currentPlayer = playerTwo;
                } else {
                    currentPlayer = playerOne;
                }
            } else {
                if(frame.editPlayer == 0)
                    currentPlayer = playerOne;
                else
                    currentPlayer = playerTwo;
                
                frame.editPlayer = -1;
            }
            
            //Räknar ut aktuell poängställning.
            gamegrid.calculatePoints();
            playerOne.setPoints(gamegrid.getBlackPoints());
            playerTwo.setPoints(gamegrid.getWhitePoints());

            //Om testmode(ai vs ai) är aktiverat görs inga grafiska uppdateringar
            if (!testMode) {
                //uppdetara JLabels
                frame.setFrameLabels(playerOne, playerTwo, currentPlayer);
            }

            validMoves = gamegrid.getValidMoves(currentPlayer.getID());
            //Om testmode(ai vs ai) är aktiverat görs inga grafiska uppdateringar
            if (!testMode && currentPlayer.getValidMoves()) {
                //uppdaterar brädet
                frame.updateBoard(gamegrid, currentPlayer.getID());
            }
            
            //Kollar om aktuell spelare har några godkända drag och sätter bool efter det.
            if (validMoves.isEmpty()) {
                currentPlayer.setValidMoves(false);
            } else {
                currentPlayer.setValidMoves(true);
            }
            
            //Om spelare har godkända drag så körs getMove på spelaren.
            if (currentPlayer.getValidMoves()) {
                move.setLocation(currentPlayer.getMove(gamegrid));
            }
            
            //Ser efter om Nytt spel knappen har tryckts(fram.endGame=true) och avbryter i sådana fall whileloopen och därmed aktuell spelsession.
            if (!testMode && frame.endGame()) {
                break;//kollar om nytt spel knappen har trycks
            }

            //Om spelaren hade godkända drag så uppdateras gamegriden med det.
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

            //Om ingen av spelaren har godkända drag så är spelet slut. Beroende på poängställning visas olika dialoger.
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