package org.iothello.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.iothello.gui.dialogs.HelperDialog;
import org.iothello.logic.GameGrid; // For the definitions(EMPTY_MARKER, BLACK_MARKER, WHITE_MARKER)
import org.iothello.logic.MoveQueue;

/**
 * Containing the game grid
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class GameBoard extends JPanel {
	private static Icon Mouse_Over_Marker;
    private static int player;
    private static String theme = "standard";
    private ImageIcon Black_marker;
    private ImageIcon White_marker;
    private ImageIcon Yinyang_marker;
    private ImageIcon Transparent_marker;
    private ImageIcon Valid_black_marker;
    private ImageIcon Valid_white_marker;
    private Image BoardBackground;
    private static boolean edit = false;
    private MoveQueue mq = new MoveQueue();
    public int hoover_i=-1, hoover_j=-1;
    
    
    public GameBoard() {
        BoardBackground = (new ImageIcon("gfx/themes/" + theme + "/board.jpg").getImage());
        setTheme();
        Dimension size = new Dimension(BoardBackground.getWidth(null), BoardBackground.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(new GridLayout(8, 8));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(BoardBackground, 0, 0, null);
    }
    
    public void editGame(boolean pause) {
        this.edit = pause;
    }
    
    /*
     * Uppdaterar brädet genom att tömma gridlayouten och fylla på med market enligt aktuell speluppställning.
     */
    public void updateBoard(GameGrid gamegrid, boolean showValid, int player) {
        GameBoard.player = player;
        this.removeAll();
        int grid[][] = gamegrid.getGameGrid();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Marker marker = new Marker(this);
                marker.setPos(j, i);
                marker.setValue(grid[j][i]);

                if (gamegrid.getGameGrid()[j][i] == GameGrid.EMPTY_MARKER) {
                    marker.setIcon(Transparent_marker);
                }
                if (gamegrid.getGameGrid()[j][i] == GameGrid.WHITE_MARKER) {
                    marker.setIcon(White_marker);
                }
                if (gamegrid.getGameGrid()[j][i] == GameGrid.BLACK_MARKER) {
                    marker.setIcon(Black_marker);
                }
                Point checkpoint = new Point(j, i);
                if (showValid && gamegrid.getValidMoves(player).contains(checkpoint)) {
                    if (player == 1) {
                        marker.setIcon(Valid_black_marker);
                    }
                    if (player == 2) {
                        marker.setIcon(Valid_white_marker);
                    }
                    marker.setValue(3);
                }
                

                
                
                marker.addMouseListener(marker);
                this.add(marker);
            }
        }
        
    }

    public void editBoard(GameGrid gamegrid) {
        GameBoard.player = player;
        this.removeAll();
        int grid[][] = gamegrid.getGameGrid();
        for (int i = 0; i < 8; i++) {
            
            for (int j = 0; j < 8; j++) {

                MarkerEdit marker = new MarkerEdit(this, gamegrid);
                marker.setPos(j, i);
                marker.setValue(grid[j][i]);

                if (gamegrid.getGameGrid()[j][i] == GameGrid.EMPTY_MARKER) {
                    marker.setIcon(Transparent_marker);
                }
                if (gamegrid.getGameGrid()[j][i] == GameGrid.WHITE_MARKER) {
                    marker.setIcon(White_marker);
                }
                if (gamegrid.getGameGrid()[j][i] == GameGrid.BLACK_MARKER) {
                    marker.setIcon(Black_marker);
                }
                if (i == hoover_i && j == hoover_j) {
                    marker.setIcon(Yinyang_marker);
                }

                marker.addMouseListener(marker);
                
                this.add(marker);
            }
        }
        
    }
    
    public void changeTheme(String theme) {
        GameBoard.theme = theme;
        setTheme();
    }
    
    /*
     * Skickar tillbaka den ImageIcon som ska visar när muspekaren hoovrar över en godkänd ruta. 
     */
    public static Icon getMouse_Over_Marker() {
        if (player == 1) {
            Mouse_Over_Marker = new ImageIcon("gfx/themes/" + theme + "/Black_over.png");
        }
        if (player == 2) {
            Mouse_Over_Marker = new ImageIcon("gfx/themes/" + theme + "/White_over.png");
        }
        if(HelperDialog.getInstance().isStep()){
            Mouse_Over_Marker = new ImageIcon("gfx/themes/" + theme + "/yinyang_over.png");
        }
        return Mouse_Over_Marker;
    }
    
    /*
     * Byter bilderna som visas, tema.
     */
    private void setTheme() {
        Black_marker = new ImageIcon("gfx/themes/" + theme + "/Black_marker.png");
        White_marker = new ImageIcon("gfx/themes/" + theme + "/White_marker.png");
        Yinyang_marker = new ImageIcon("gfx/themes/" + theme + "/yinyang_over.png");
        Transparent_marker = new ImageIcon("gfx/themes/" + theme + "/Empty.png");
        Valid_black_marker = new ImageIcon("gfx/themes/" + theme + "/Black_valid.png");
        Valid_white_marker = new ImageIcon("gfx/themes/" + theme + "/White_valid.png");
        BoardBackground = (new ImageIcon("gfx/themes/" + theme + "/board.jpg").getImage());
    }
    
    /* 
     * Sätter ett drag i MoveQueue
     */
    public void setMove(int x, int y) {
        mq.setMove(x, y);
    }
}
