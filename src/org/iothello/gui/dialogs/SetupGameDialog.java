package org.iothello.gui.dialogs;

import org.iothello.gui.LobbyLogin;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.iothello.gui.CustomJPanel;
import org.iothello.logic.players.Computer_AI_Minmax;
import org.iothello.logic.players.Computer;
import org.iothello.logic.players.Computer_MonteCarlo;
import org.iothello.logic.GameGrid;
import org.iothello.logic.players.Human;
import org.iothello.logic.players.Player;

/**
 * En JDialog som tar in information om de deltagande spelarna.
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class SetupGameDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -7749933146161564166L;
	private int compSpeed = 0;
    private int times = 10;
    private Player player1;
    private Player player2;
    private String[] petStrings = {"Human", "Computer(random)","AI Easy(2)", "AI Medium(4)", "AI Hard(6)", "AI Extreme(8)", "AI Monte Carlo"};
    private JComboBox<?> jcmPlayer1 = new JComboBox(petStrings);
    private JComboBox<?> jcmPlayer2 = new JComboBox(petStrings);
    private JTextField jtxName1 = new JTextField("Player 1");
    private JTextField jtxName2 = new JTextField("Player 2");
    private boolean testMode = false;
    private boolean debug = false;
    private JButton jbtTest = new JButton("Test");
    private JButton jbtStart = new JButton("Start");
    private JCheckBox jchDebug = new JCheckBox("Debug");
    private JTextField jtxDepth1 = new JTextField("");
    private JTextField jtxDepth2 = new JTextField("");
    private JTextField jtxSpeed = new JTextField("0");
    private JTextField jtxTimes = new JTextField("10");
    private JLabel p1Avatar = new JLabel("");
    private JLabel p2Avatar = new JLabel("");

    private JLabel labelDepth1 = new JLabel("P1 Depth:");
    private JLabel labelDepth2 = new JLabel("P2 Depth:");
    private JLabel labelSpeed = new JLabel("Speed:");
    private JLabel labelTimes = new JLabel("Runtimes for test:");
    
    public int getGameMode;
    public LobbyLogin ll;
    
    public SetupGameDialog(JFrame owner) {
        super(owner, true);
        
        CustomJPanel setupPanel = new CustomJPanel("gfx/dialogbakgrund.png");
        JPanel player1Input = new JPanel(new GridLayout(5, 1, 7, 7));
        JPanel player2Input = new JPanel(new GridLayout(5, 1, 7, 7));
        JPanel jpnButtons = new JPanel(new GridLayout(5, 1, 7, 7));
        player1Input.setOpaque(false);
        player2Input.setOpaque(false);
        jbtTest.setEnabled(false);
        jchDebug.setOpaque(false);
        jchDebug.setForeground(Color.white);
        jpnButtons.setOpaque(false);
        setTitle("Othello");
        setResizable(false);
        jtxName1.setToolTipText("P1 Name");
        jtxName2.setToolTipText("P2 Name");
        jtxDepth1.setToolTipText("P1 Depth");
        jtxDepth2.setToolTipText("P2 Depth");
        jtxSpeed.setToolTipText("Speed");
        jtxTimes.setToolTipText("Times");
        
        jbtStart.addActionListener(this);
        jbtTest.addActionListener(this);
        jchDebug.addActionListener(this);
        
        labelDepth1.setForeground(Color.white);
        labelDepth2.setForeground(Color.white);
        labelSpeed.setForeground(Color.white);
        labelTimes.setForeground(Color.white);
        
        
        //jbtTest.setEnabled(false);
        JPanel jpnSetup = new JPanel(new GridLayout(3, 3, 10, 0));

        jpnSetup.add(new JLabel(""));
        jpnSetup.add(new JLabel(""));
        jpnSetup.add(new JLabel(""));

        
        player1Input.add(jtxName1);
        player1Input.add(jcmPlayer1);
        player1Input.add(labelDepth1);
        player1Input.add(jtxDepth1);
        player1Input.add(labelTimes);

        

        player2Input.add(jtxName2);
        player2Input.add(jcmPlayer2);
        player2Input.add(labelDepth2);
        player2Input.add(jtxDepth2);
        player2Input.add(jchDebug);


        jpnButtons.add(jbtStart);
        jpnButtons.add(jbtTest);
        jpnButtons.add(labelSpeed);
        jpnButtons.add(jtxSpeed);
        jpnButtons.add(jtxTimes);
        
        
        
        p1Avatar = new JLabel("");
        p1Avatar.setVerticalAlignment(3);
        p1Avatar.setHorizontalAlignment(4);
        p1Avatar.setIcon(new ImageIcon("gfx/apaL.png"));
        
        p2Avatar = new JLabel("");
        p2Avatar.setVerticalAlignment(3);
        p2Avatar.setHorizontalAlignment(2);
        p2Avatar.setIcon(new ImageIcon("gfx/apaR.png"));
        
        jpnSetup.add(p1Avatar);
        jpnSetup.add(new JLabel(""));
        jpnSetup.add(p2Avatar);
        
        jpnSetup.add(player1Input);
        jpnSetup.add(jpnButtons);
        jpnSetup.add(player2Input);
        
        jcmPlayer1.addActionListener(this);
        jcmPlayer2.addActionListener(this);
        
        addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        jpnSetup.setOpaque(false);
        
        setupPanel.add(jpnSetup);
        
        getContentPane().add(setupPanel);
        pack();
        
        this.setLocationRelativeTo(null);
     
    }
    /*
     * Sätter spelarna utifrån comboboxarna.
     */
    private void setPlayers() {   
        boolean p1comp = false;
        boolean p2comp = false;
        
        if (jcmPlayer1.getSelectedIndex() == 0) {
            player1 = new Human();
            player1.setName(jtxName1.getText());
            player1.setID(GameGrid.BLACK_MARKER);
        } else if (jcmPlayer1.getSelectedIndex() == 1) {
            player1 = new Computer();
            player1.setName("P1 Computer A");
            player1.setID(GameGrid.BLACK_MARKER);
        }
        /*else if (jcmPlayer1.getSelectedIndex() == 2) {
            player1 = new Computer_1();
            player1.setName("P1 Computer B");
            player1.setID(GameGrid.BLACK_MARKER);
        }
         */
        else if (jcmPlayer1.getSelectedIndex() == 2) { 
            player1 = new Computer_AI_Minmax(getDepth1(2), true);
            player1.setName("P1 AI Easy");
            player1.setID(GameGrid.BLACK_MARKER);
            p1comp = true;
        } else if (jcmPlayer1.getSelectedIndex() == 3) {            
            player1 = new Computer_AI_Minmax(getDepth1(4), true);
            player1.setName("P1 AI Medium");
            player1.setID(GameGrid.BLACK_MARKER);
            p1comp = true;
        } else if (jcmPlayer1.getSelectedIndex() == 4) {
            player1 = new Computer_AI_Minmax(getDepth1(6), true);
            player1.setName("P1 AI Hard");
            player1.setID(GameGrid.BLACK_MARKER);
            p1comp = true;
        } else if (jcmPlayer1.getSelectedIndex() == 5) {
            player1 = new Computer_AI_Minmax(getDepth1(8), true);
            player1.setName("P1 AI Extreme");
            player1.setID(GameGrid.BLACK_MARKER);
            p1comp = true;
        } else if (jcmPlayer1.getSelectedIndex() == 6) {
            player1 = new Computer_MonteCarlo(1000);
            player1.setName("P1 AI Monte Carlo");
            player1.setID(GameGrid.BLACK_MARKER);
            
            // Activate debug if debug mode is selected
            if(jchDebug.isSelected()) ((Computer_MonteCarlo) player1).setDebug(true);
            
            p1comp = true;
        }
        
        if (jcmPlayer2.getSelectedIndex() == 0) {
            player2 = new Human();
            player2.setName(jtxName2.getText());
            player2.setID(GameGrid.WHITE_MARKER);
        } else if (jcmPlayer2.getSelectedIndex() == 1) {
            player2 = new Computer();
            player2.setName("P2 Computer A");
            player2.setID(GameGrid.WHITE_MARKER);
        }
        /*else if (jcmPlayer2.getSelectedIndex() == 2) {
            player2 = new Computer_1();
            player2.setName("P2 Computer B");
            player2.setID(GameGrid.WHITE_MARKER);
        }*/
        else if (jcmPlayer2.getSelectedIndex() == 2) {
            Computer_AI_Minmax tmpPlayer = new Computer_AI_Minmax(getDepth2(2), true);
            tmpPlayer.setMethodMobility(6);
            player2 = tmpPlayer;
            player2.setName("P1 AI Easy");
            player2.setID(GameGrid.WHITE_MARKER);
            p2comp = true;
        } else if (jcmPlayer2.getSelectedIndex() == 3) {
            player2 = new Computer_AI_Minmax(getDepth2(4), true);
            player2.setName("P2 AI Medium");
            player2.setID(GameGrid.WHITE_MARKER);
            p2comp = true;
        } else if (jcmPlayer2.getSelectedIndex() == 4) {
            player2 = new Computer_AI_Minmax(getDepth2(6), true);            
            player2.setName("P2 AI Hard");
            player2.setID(GameGrid.WHITE_MARKER);
            p2comp = true;
        } else if (jcmPlayer2.getSelectedIndex() == 5) {
            player2 = new Computer_AI_Minmax(getDepth2(8), true);
            player2.setName("P2 AI Extreme");
            player2.setID(GameGrid.WHITE_MARKER);
            p2comp = true;
        } else if (jcmPlayer2.getSelectedIndex() == 6) {
            player2 = new Computer_MonteCarlo(1000);
            player2.setName("P2 AI Monte Carlo");
            player2.setID(GameGrid.WHITE_MARKER);
            
            // Activate debug if debug mode is selected
            if(jchDebug.isSelected()) ((Computer_MonteCarlo) player2).setDebug(true);
            
            p2comp = true;
        }
        
        if(p1comp && player1 instanceof Computer_AI_Minmax){
            Computer_AI_Minmax cmp1 = (Computer_AI_Minmax) player1;
            
            if(jchDebug.isSelected())
                cmp1.setDebug(true);
            
            player1 = cmp1;
        }
        if(p2comp && player2 instanceof Computer_AI_Minmax){
            Computer_AI_Minmax cmp2 = (Computer_AI_Minmax) player2;
            
            if(jchDebug.isSelected())
                cmp2.setDebug(true);
            
            player2 = cmp2;
        }
        
        
    } 
    
    private void setGame(){
        setPlayers();
        
        this.setDebug(jchDebug.isSelected());
        HelperDialog.getInstance().logClear();
        
        if(this.isDebug()) {
            HelperDialog.getInstance().setVisible(true);


        }
        
        this.setCompSpeed(Integer.parseInt(jtxSpeed.getText()));
        this.setTimes(Integer.parseInt(jtxTimes.getText()));
    }
    
    private int getDepth1(int standard){
        int result = standard;
        if(!jtxDepth1.getText().equals("")){
            result = Integer.parseInt(jtxDepth1.getText());
        }        
        return result;
    }
    
    private int getDepth2(int standard){
        int result = standard;
        if(!jtxDepth2.getText().equals("")){
            result = Integer.parseInt(jtxDepth2.getText());
        }        
        return result;
    }
    
    
    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public boolean getTestMode() {
        return testMode;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        if ("Start".equals(e.getActionCommand())) {
            setGame();
            
            
            System.out.println("kör rungame");
            getGameMode = 0;
            this.setVisible(false);
        }
        
        if ("Test".equals(e.getActionCommand())) {
        
             
            setGame();
            getGameMode=2;
            testMode = true;

            this.setVisible(false);
            
        }
        
        /*
         * Byter grafik på avatarerna.
         */
        if ("comboBoxChanged".equals(e.getActionCommand())) {
            
            if (jcmPlayer1.getSelectedIndex() != 0 && jcmPlayer2.getSelectedIndex() != 0) {
                jbtTest.setEnabled(true);
            } else {
                jbtTest.setEnabled(false);
            }
            
            if (jcmPlayer1.getSelectedIndex() != 0) {
                p1Avatar.setIcon(new ImageIcon("gfx/robotL.png"));
            }
            if (jcmPlayer1.getSelectedIndex() == 0) {
                p1Avatar.setIcon(new ImageIcon("gfx/apaL.png"));
            }
            if (jcmPlayer2.getSelectedIndex() != 0) {
                p2Avatar.setIcon(new ImageIcon("gfx/robotR.png"));
            }
            if (jcmPlayer2.getSelectedIndex() == 0) {
                p2Avatar.setIcon(new ImageIcon("gfx/apaR.png"));
            }
            
        }
    }
    /*
     * Slår på/av testläget(ai vs ai)
     */
    public void setTestMode(boolean b) {
        this.testMode = b;
    }
    
    public int getCompSpeed() {
        return compSpeed;
    }
    public void setCompSpeed(int compSpeed) {
        this.compSpeed = compSpeed;
    }
    
    public int getTimes() {
        return times;
    }
    public void setTimes(int times) {
        this.times = times;
    }
    
    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}