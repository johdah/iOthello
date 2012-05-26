package org.iothello.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import org.iothello.gui.dialogs.DrawDialog;
import org.iothello.gui.dialogs.HelperDialog;
import org.iothello.gui.dialogs.WinnerDialog;
import org.iothello.logic.GameGrid;
import org.iothello.logic.MoveQueue;
import org.iothello.logic.Othello;
import org.iothello.logic.players.Player;

/**
 * The game main window
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class GameFrame extends JFrame {
	private static GameBoard panel;
    private static List validMoves;
    private boolean showValid = false;
    private boolean edit = false;
    public int editPlayer = -1;

    public boolean isEdit() {
        return edit;
    }
    private static JLabel playerTurn, points;
    private static int compSpeed = 1000;
    private int player;
    private MoveQueue moveq = new MoveQueue();
    private boolean endGame = false;
    private GameGrid gamegrid;
    private OthelloMenuBar menubar = new OthelloMenuBar();

    public GameFrame() {
        moveq.setFrame(this);//fixa?
        setTitle("Othello");
        setResizable(false);
        this.setIconImage((new ImageIcon("gfx/icon.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new GameBoard();

        setJMenuBar(menubar);

        JPanel knappsats = new JPanel(new GridLayout(1, 4));
        JButton jbtNew = new JButton("New Game");
        JButton jbtEnd = new JButton("Exit");
        jbtEnd.addActionListener(new menuExit());
        jbtNew.addActionListener(new menuNewGame());

        playerTurn = new JLabel("<html><h3>Let's play!");
        points = new JLabel("<html>Player 1: 0 <br>Player 2: 0");
        knappsats.add(playerTurn);
        knappsats.add(points);
        //knappsats.add(jbtNew);
        knappsats.add(jbtEnd);

        getContentPane().add(knappsats, BorderLayout.SOUTH);

        getContentPane().add(panel, BorderLayout.CENTER);

        setVisible(true);
        pack();
//center frame 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        this.setLocation(width / 2 - this.getWidth() / 2, height / 2 - this.getHeight() / 2);

    }
    
    public void resetFrame() {
        panel = new GameBoard();
    }
    
    
    /*
     * Skickar vidare gamegrid till gameboard.
     */

    public void updateBoard(GameGrid gamegrid, int player) {

        this.player = player;
        this.gamegrid = gamegrid;
        GameFrame.validMoves = gamegrid.getValidMoves(player);

            
        if(HelperDialog.getInstance().isStep()) {
            panel.editBoard(gamegrid);
        } else{
            panel.updateBoard(gamegrid, showValid, player);
        } 
        
        pack();
/*
            while(HelperDialog.getInstance().isStep() && !HelperDialog.getInstance().isNext()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        */
    }

    
    /*
     * Uppdaterar JLabels.
     */

    public void setFrameLabels(Player player1, Player player2, Player currentPlayer) {
        points.setText("<html>" + player1.getName() + ": " + player1.getPoints() + " <br>" + player2.getName() + ": " + player2.getPoints());
        if (currentPlayer.getID() == 1) {
            playerTurn.setText("<html><h3> " + currentPlayer.getName() + "</h3> Color: Black");
        }
        if (currentPlayer.getID() == 2) {
            playerTurn.setText("<html><h3> " + currentPlayer.getName() + "</h3> Color: White");
        }
    }
    /*
     * Uppdaterar datorns "betänketid".
     */

    public void setSpeed(int speed) {
        compSpeed = speed;
       
        menubar.setSpeedCheck(false);
    }

    public boolean endGame() {
        return endGame;

    }
    /*
     * Menubar.
     */

    class OthelloMenuBar extends JMenuBar {

        private void setSpeedCheck(boolean b) {
            speedCheckBox.setState(b);
        }
        private JCheckBoxMenuItem speedCheckBox = new JCheckBoxMenuItem("Speedy computer");

        public OthelloMenuBar() {
            JMenu menu = new JMenu("Meny");
            JMenu settings = new JMenu("Settings");
            JMenu help = new JMenu("Help");

            add(menu);
            add(settings);
            add(help);

            Rules rulesWindow = new Rules();
            JMenuItem rules = new JMenuItem("Rules");
            rules.addActionListener(rulesWindow);
            help.add(rules);
            help.addSeparator();

            JMenuItem about = new JMenuItem("About");
            about.addActionListener(new menuAbout());
            help.add(about);

            JMenuItem newgame = new JMenuItem("New game", KeyEvent.VK_N);
            newgame.addActionListener(new menuNewGame());
            //menu.add(newgame);
            menu.addSeparator();

            JMenuItem exitgame = new JMenuItem("Exit", KeyEvent.VK_E);
            exitgame.addActionListener(new menuExit());
            menu.add(exitgame);

            JCheckBoxMenuItem valmoves = new JCheckBoxMenuItem("Show valid moves");
            valmoves.addActionListener(new changeShowValid());
            
            JCheckBoxMenuItem editgame = new JCheckBoxMenuItem("Edit board!");
            editgame.addActionListener(new setEdit());
            
            settings.add(valmoves);
            settings.addSeparator();
            //settings.add(editgame);


            speedCheckBox.addActionListener(new changeCompSpeed());
            settings.add(speedCheckBox);
            settings.addSeparator();

            JMenu themeselect = new JMenu("Select theme");
            settings.add(themeselect);
            ButtonGroup themes = new ButtonGroup();
            File dir = new File("gfx/themes/");
            String[] folders = dir.list();
            if (folders == null) {
                JOptionPane.showMessageDialog(null, "You are missing graphic compoments, try to reinstall :)", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                for (int i = 0; i < folders.length; i++) {
                    String themeName = folders[i];
                    JRadioButtonMenuItem jrdTheme = new JRadioButtonMenuItem(themeName);
                    jrdTheme.addActionListener(new changeTheme());
                    themes.add(jrdTheme);
                    themeselect.add(jrdTheme);
                }
            }

        }

        
        class setEdit implements ActionListener {
            /*
             * Slår på eller av funktionen att visa valid moves och sedan uppdaterars gameboard.
             */

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!edit) {
                    edit = true;
                    panel.editGame(edit);
                    editPlayer = -1;
                } else {
                    
                Object[] options = {"Black",
                    "White"};
                editPlayer = JOptionPane.showOptionDialog(null,
                    "Player to make next move? ",
                    "Next move?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
                    
                    edit = false;
                    panel.editGame(edit);

                }
                
                pack();
            }
        }
        
        class changeShowValid implements ActionListener {
            /*
             * Slår på eller av funktionen att visa valid moves och sedan uppdaterars gameboard.
             */

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!showValid) {
                    showValid = true;
                } else {
                    showValid = false;
                }
                panel.updateBoard(gamegrid, showValid, player);
                pack();
            }
        }

        class changeCompSpeed implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (compSpeed != 0) {
                    compSpeed = 0;
                } else {
                    compSpeed = 1000;
                }
            }
        }

        class changeTheme implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel.changeTheme(e.getActionCommand());
                panel.updateBoard(gamegrid, showValid, player);
                pack();
            }
        }
    }

    class menuExit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    class menuNewGame implements ActionListener {
//Sätter endGame boolen till true, samt släpper låset i MoveQueue ifall tråden står och väntar där.
        @Override
        public void actionPerformed(ActionEvent e) {
            moveq.releaseLock();
            endGame = true;
            
        }
    }

    class menuAbout implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Laboration 2 OOPJ - Gameplay & AI\n\n   o Bjorn Dahlstrand\n   o Niclas Gustafsson\n   o Fredrik Tornvall\n\nLaboration 3 OOPJ - Network & Database\n\n   o Bjorn Dahlstrand\n   o Anders Hansson\n   o Pierre Odengard\n\n", "About Othello",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void showWinnerDialog(Player p) {
        WinnerDialog wd = new WinnerDialog(this, p);
    }

    public void showDrawnDialog() {
        DrawDialog dd = new DrawDialog(this);
    }

    public void setEndGame(boolean end) {
        endGame = end;

    }

    public static long getSpeed() {
        return compSpeed;
    }
}
