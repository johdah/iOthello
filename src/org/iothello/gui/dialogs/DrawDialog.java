package org.iothello.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.iothello.gui.CustomJPanel;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class DrawDialog extends JDialog implements ActionListener {
 
    public DrawDialog(JFrame owner) {
        super(owner, true);

        CustomJPanel setupPanel = new CustomJPanel("gfx/drawn.png");
        JButton jbtOk = new JButton("Ok");
        jbtOk.addActionListener(this);
        
        add(setupPanel, BorderLayout.CENTER);
        add(jbtOk, BorderLayout.SOUTH);

        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;   
        setLocation(width / 2 - this.getWidth() / 2, height / 2 - this.getHeight() / 2);
        
        setTitle("Drawn");
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        dispose();
    }
}
