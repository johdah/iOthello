package org.iothello.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
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
import org.iothello.logic.players.Player;

/**
 * The game main window
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class GameFrame extends JFrame {
	private static final long serialVersionUID = -533543048838510024L;
	private static GameBoard panel;
    @SuppressWarnings("UnusedDeclaration")
    private static List<Point> validMoves = null;
    private boolean showValid = false;
    private boolean edit = false;
    public int editPlayer = -1;

    @SuppressWarnings("UnusedDeclaration")
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
        moveq.setFrame(this);
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
    
    @SuppressWarnings("UnusedDeclaration")
    public void resetFrame() {
        panel = new GameBoard();
    }
    
    /**
     * Send GameGrid to GameBoard
     * @param gamegrid GameGrid
     * @param player int
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
    }

    public void setFrameLabels(Player player1, Player player2, Player currentPlayer) {
        points.setText("<html>" + player1.getName() + ": " + player1.getPoints() + " <br>" + player2.getName() + ": " + player2.getPoints());
        if (currentPlayer.getID() == 1) {
            playerTurn.setText("<html><h3> " + currentPlayer.getName() + "</h3> Color: Black");
        }
        if (currentPlayer.getID() == 2) {
            playerTurn.setText("<html><h3> " + currentPlayer.getName() + "</h3> Color: White");
        }
    }

    public void setSpeed(int speed) {
        compSpeed = speed;
       
        menubar.setSpeedCheck(false);
    }

    public boolean endGame() {
        return endGame;

    }

    class OthelloMenuBar extends JMenuBar {
		private static final long serialVersionUID = 5195246075130274563L;

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
                for (String themeName : folders) {
                    JRadioButtonMenuItem jrdTheme = new JRadioButtonMenuItem(themeName);
                    jrdTheme.addActionListener(new changeTheme());
                    themes.add(jrdTheme);
                    themeselect.add(jrdTheme);
                }
            }

        }
        
        class setEdit implements ActionListener {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                showValid = !showValid;
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
        @Override
        public void actionPerformed(ActionEvent e) {
            moveq.releaseLock();
            endGame = true;
        }
    }

    class menuAbout implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Laboration XX\n\n   o Deltagare 1\n   o Deltagare 2\n\n", "About Othello",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void showWinnerDialog(Player p) {
        new WinnerDialog(this, p);
    }

    public void showDrawnDialog() {
        new DrawDialog(this);
    }

    public void setEndGame(boolean end) {
        endGame = end;

    }

    public static long getSpeed() {
        return compSpeed;
    }
}
