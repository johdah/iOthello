package org.iothello.gui.dialogs;

import javax.swing.JDialog;

import java.util.ArrayList;

/**
 * 
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class HighscoreViewer extends JDialog {

    /** Creates new form highscoreViewer */
    public HighscoreViewer(java.awt.Frame parent, boolean modal, ArrayList highscore, ArrayList ranking) {
        super(parent, modal);
        initComponents();
        insertHighscore(highscore);
        insertRanking(ranking);
        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtpHighScore = new javax.swing.JTabbedPane();
        jspScrollHighscore = new javax.swing.JScrollPane();
        jtHighscore = new javax.swing.JTable();
        jspScrollRanking = new javax.swing.JScrollPane();
        jtPlayerRanking = new javax.swing.JTable();
        jbtClose = new javax.swing.JButton();
        jlHighscores = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Highscores");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);
        setResizable(false);

        jtHighscore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Player name", "Top score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jspScrollHighscore.setViewportView(jtHighscore);

        jtpHighScore.addTab("Highscore", jspScrollHighscore);

        jtPlayerRanking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Player name", "Wins", "Draws", "Losses", "Total points"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jspScrollRanking.setViewportView(jtPlayerRanking);
        jtPlayerRanking.getColumnModel().getColumn(0).setMinWidth(100);
        jtPlayerRanking.getColumnModel().getColumn(0).setPreferredWidth(100);
        jtPlayerRanking.getColumnModel().getColumn(1).setResizable(false);
        jtPlayerRanking.getColumnModel().getColumn(1).setPreferredWidth(55);
        jtPlayerRanking.getColumnModel().getColumn(2).setResizable(false);
        jtPlayerRanking.getColumnModel().getColumn(2).setPreferredWidth(55);
        jtPlayerRanking.getColumnModel().getColumn(3).setPreferredWidth(55);
        jtPlayerRanking.getColumnModel().getColumn(4).setPreferredWidth(75);

        jtpHighScore.addTab("Player ranking", jspScrollRanking);

        jbtClose.setText("Close");
        jbtClose.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jbtClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCloseActionPerformed(evt);
            }
        });

        jlHighscores.setFont(new java.awt.Font("Corbel", 1, 36));
        jlHighscores.setText("Highscores");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jlHighscores, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jtpHighScore, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbtClose, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jlHighscores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtpHighScore, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbtClose, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_jbtCloseActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbtClose;
    private javax.swing.JLabel jlHighscores;
    private javax.swing.JScrollPane jspScrollHighscore;
    private javax.swing.JScrollPane jspScrollRanking;
    private javax.swing.JTable jtHighscore;
    private javax.swing.JTable jtPlayerRanking;
    private javax.swing.JTabbedPane jtpHighScore;
    // End of variables declaration//GEN-END:variables
    private int x, y = 0;

    // Takes in a arraylist that holds a copy of the top highscores from the
    // database and then inserts that copy into the JTable called jtHighscore.
    public void insertHighscore(ArrayList highscoreList) {
        x = 0;
        
        for (int i = 0; i < highscoreList.size(); i += 2) {
            jtHighscore.setValueAt(highscoreList.get(i), x, y);
            jtHighscore.setValueAt(highscoreList.get(i + 1), x, y + 1);
            x++;
        }
    }

    // Takes in a arraylist that holds a copy of the current ranking from the
    // database and then inserts that copy into the JTable called jtPlayerRanking.
    public void insertRanking(ArrayList rankingList) {
        x = 0;
        
        for (int i = 0; i < rankingList.size(); i += 5) {
            jtPlayerRanking.setValueAt(rankingList.get(i), x, y);
            jtPlayerRanking.setValueAt(rankingList.get(i + 1), x, y + 1);
            jtPlayerRanking.setValueAt(rankingList.get(i + 2), x, y + 2);
            jtPlayerRanking.setValueAt(rankingList.get(i + 3), x, y + 3);
            jtPlayerRanking.setValueAt(rankingList.get(i + 4), x, y + 4);
            x++;
        }

    }
}