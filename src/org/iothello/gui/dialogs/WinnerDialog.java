package org.iothello.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.iothello.gui.CustomJPanel;
import org.iothello.logic.players.Player;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class WinnerDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 317964133027806835L;

	public WinnerDialog(JFrame owner, Player player) {
        super(owner, true);
        String winnerName = player.getName();
        CustomJPanel setupPanel = new CustomJPanel("gfx/winner.jpg");
        JLabel winner = new JLabel("<html><b>Winner: " + winnerName+"</b></html>");
        winner.setHorizontalAlignment(JLabel.CENTER);
        
        JButton jbtOk = new JButton("Ok");
        jbtOk.addActionListener(this);
        
        JPanel jpnWrapper = new JPanel(new GridLayout(2, 1));
        jpnWrapper.add(winner);
        jpnWrapper.add(jbtOk);
        
        
        add(setupPanel, BorderLayout.CENTER);
        add(jpnWrapper, BorderLayout.SOUTH);
        
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        setLocation(width / 2 - this.getWidth() / 2, height / 2 - this.getHeight() / 2);
        setTitle("Winner: " + winnerName);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        dispose();
    }
}