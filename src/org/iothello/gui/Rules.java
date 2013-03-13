package org.iothello.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * Downloads and presents the Othello rules from a website, if not found show an error message
 *
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class Rules implements ActionListener {
    private JFrame rules = new JFrame();

    public Rules() {
        rules.setTitle("Rules!");

        JEditorPane jep = null;
        try {
            jep = new JEditorPane("http://www.britishothello.org.uk/rules.html");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Oops, I could not find the rules.", "Error",
                    JOptionPane.PLAIN_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(jep, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        rules.pack();
        rules.setSize(850, 600);
        rules.setVisible(false);
        rules.add(scroll);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        rules.setVisible(true);
    }
}
