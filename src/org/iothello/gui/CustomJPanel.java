package org.iothello.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Create a new JPanel
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class CustomJPanel extends JPanel {
	private static final long serialVersionUID = -158123242409744287L;
	private Image PanelBackground;
    
	public CustomJPanel(String file) {
	    PanelBackground = (new ImageIcon(file).getImage());
	    Dimension size = new Dimension(PanelBackground.getWidth(null), PanelBackground.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(new GridLayout(1, 1));
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    g.drawImage(PanelBackground, 0, 0, null);
	}
}