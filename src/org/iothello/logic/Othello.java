package org.iothello.logic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.iothello.gui.GameFrame;
import org.iothello.gui.dialogs.SetupGameDialog;
import org.iothello.logic.players.NetComputerLocal;
import org.iothello.logic.players.NetPlayerExternal;
import org.iothello.logic.players.NetPlayerLocal;

/**
 * Run one session at a time
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Othello {
	private GameFrame frame = new GameFrame();
    private GameManager gm;
    private SetupGameDialog sgd = new SetupGameDialog(frame);
    private static boolean noGame = true;
    private static final Lock lock = new ReentrantLock();
    private static final Condition noGameCon = lock.newCondition();

    public void startOthello() throws IOException, InterruptedException {
        sgd.setVisible(true);        
        
            if (sgd.getGameMode == 0) {
                runGame();
            }
            if (sgd.getGameMode == 1) {
                    runNetGame();
            }
            if (sgd.getGameMode == 2) {
                    runTest();
            }
            //sgd.dispose();
            //sgd = new SetupGameDialog(frame);
            //sgd.setVisible(true);
          
    }

    /*
     * Testläge som kör en ai mot en annan ai ett antal gånger. Räknar samman resultatet och redovisar det i en dialog.
     */
    private void testComp() {
        int times = sgd.getTimes();
        
        JLabel jlbThinking = new JLabel("");
        jlbThinking.setIcon(new ImageIcon("gfx/thinking.gif"));
        JProgressBar jprBar = new JProgressBar();
        JDialog jdlDialog = new JDialog();
        jdlDialog.setTitle("Thinking...");
        JLabel jlbInfo = new JLabel("Running " + times + " games to see which AI is best.");
        jdlDialog.add(jprBar, BorderLayout.SOUTH);
        jdlDialog.add(jlbThinking, BorderLayout.CENTER);
        jdlDialog.add(jlbInfo, BorderLayout.NORTH);
        jdlDialog.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        jdlDialog.setLocation(width / 2 - jdlDialog.getWidth() / 2, height / 2 - jdlDialog.getHeight() / 2);
        jdlDialog.setVisible(true);

        double p1 = 0, p2 = 0, d = 0;
        jprBar.setMaximum(times);
        jprBar.setMinimum(0);
        int result;
        for (int i = 0; i < times; i++) {
            jprBar.setValue(i);
            result = gm.gameplay(frame, true);
            if (result == 1) {
                p1++;
            }
            if (result == 2) {
                p2++;
            }
            if (result == 0) {
                d++;
            }
        }
        jdlDialog.dispose();
        JOptionPane.showMessageDialog(null, "<html>" + sgd.getPlayer1().getName() + ": " + p1 / (times) * 100 + "% <br>" + sgd.getPlayer2().getName() + ": " + p2 / (times) * 100 + "% <br>Draw: " + d / (p1 + p2 + d) * 100 + "%", "Resultat", JOptionPane.INFORMATION_MESSAGE);

    }

    public void runGame() {
        gm = new GameManager(sgd.getPlayer1(), sgd.getPlayer2());
        frame.setSpeed(sgd.getCompSpeed());
        frame.toFront();
        gm.gameplay(frame, false);
        //sgd.setVisible(true);
    }

    public void runNetGame() throws InterruptedException {
        noGame = true;
        lock.lock();
        
        try {
            while (noGame) {
                //väntar på att starta nätspel
                noGameCon.await();
            }
        } finally {
            lock.unlock();
        }

        frame.toFront();
        gm = new GameManager(sgd.ll.getPlayer1(), sgd.ll.getPlayer2());
        gm.gameplay(frame, false);

        // Utmanaren rapporterar resultat till server via gamelobby
        // Om utmanaren disconnectat så rapporterar den utmanade
        
        sgd.ll.setStatus(0);
        
        
        // eh, close connections and update score :)
        
        if (sgd.ll.getPlayer1() instanceof NetPlayerLocal) {
            NetPlayerLocal p1 = (NetPlayerLocal)sgd.ll.getPlayer1();
            p1.closeConnection();
            sgd.ll.updateScore();
        } else if (sgd.ll.getPlayer1() instanceof NetComputerLocal){
            NetComputerLocal p1 = (NetComputerLocal)sgd.ll.getPlayer1();
            p1.closeConnection();
            sgd.ll.updateScore();
        } else {
            NetPlayerExternal p1 = (NetPlayerExternal)sgd.ll.getPlayer1();
            p1.closeConnection();
            
            if(p1.forfeited())
                sgd.ll.updateScore();
        }
        
        if (sgd.ll.getPlayer2() instanceof NetPlayerLocal) {
            NetPlayerLocal p2 = (NetPlayerLocal)sgd.ll.getPlayer2();
            p2.closeConnection();
        } else if (sgd.ll.getPlayer2() instanceof NetComputerLocal){
            NetComputerLocal p2 = (NetComputerLocal)sgd.ll.getPlayer2();
            p2.closeConnection();
        } else {
            NetPlayerExternal p2 = (NetPlayerExternal)sgd.ll.getPlayer2();
            p2.closeConnection();
        }

        gm = null;

    }

    public void runTest() {
        gm = new GameManager(sgd.getPlayer1(), sgd.getPlayer2());
        frame.setSpeed(0);
        testComp();
        sgd.setTestMode(false);
    }

    public static void releaseLock() {
        lock.lock();
        try {
            noGame = false;
            noGameCon.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
