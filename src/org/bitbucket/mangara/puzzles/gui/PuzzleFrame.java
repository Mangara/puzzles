/*
 * Copyright 2018 Sander Verdonschot.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitbucket.mangara.puzzles.gui;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PuzzleFrame extends javax.swing.JFrame {

    private final NewNonogramDialog newDialog;
    private final NonogramDrawPanel drawPanel;
    private final JFileChooser saveFileChooser;
    private final String pngExtension = "png";
    private final FileNameExtensionFilter myFilter = new FileNameExtensionFilter("PNG Images", pngExtension);
    
    /**
     * Creates new form PuzzleFrame
     */
    public PuzzleFrame() {
        initComponents();
        
        drawPanel = new NonogramDrawPanel();
        getContentPane().add(drawPanel, java.awt.BorderLayout.CENTER);
        
        newDialog = new NewNonogramDialog(this, true);
        
        saveFileChooser = new JFileChooser(System.getProperty("user.dir"));
        saveFileChooser.addChoosableFileFilter(myFilter);
        saveFileChooser.setFileFilter(myFilter);
        
        pack();
    }

    public void newPuzzle(int width, int height) {
        drawPanel.newNonogram(width, height);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modeButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        clearButton = new javax.swing.JButton();
        buildingModeRadioButton = new javax.swing.JRadioButton();
        solvingModeRadioButton = new javax.swing.JRadioButton();
        modeLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        importMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Puzzles");

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        modeButtonGroup.add(buildingModeRadioButton);
        buildingModeRadioButton.setSelected(true);
        buildingModeRadioButton.setText("Building");
        buildingModeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buildingModeRadioButtonActionPerformed(evt);
            }
        });

        modeButtonGroup.add(solvingModeRadioButton);
        solvingModeRadioButton.setText("Solving");
        solvingModeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solvingModeRadioButtonActionPerformed(evt);
            }
        });

        modeLabel.setText("Mode:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buildingModeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(solvingModeRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
                .addComponent(clearButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(buildingModeRadioButton)
                    .addComponent(solvingModeRadioButton)
                    .addComponent(modeLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("New...");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        importMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        importMenuItem.setText("Import...");
        fileMenu.add(importMenuItem);

        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        exportMenuItem.setText("Export...");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportMenuItem);

        menuBar.add(fileMenu);

        jMenu2.setText("Edit");
        menuBar.add(jMenu2);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        drawPanel.clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void buildingModeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buildingModeRadioButtonActionPerformed
        drawPanel.setBuilding(true);
    }//GEN-LAST:event_buildingModeRadioButtonActionPerformed

    private void solvingModeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solvingModeRadioButtonActionPerformed
        drawPanel.setBuilding(false);
    }//GEN-LAST:event_solvingModeRadioButtonActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        newDialog.setValues(drawPanel.getNonogramWidth(), drawPanel.getNonogramHeight());
        newDialog.setVisible(true);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        int saved = saveFileChooser.showSaveDialog(this);

        if (saved == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = saveFileChooser.getSelectedFile();

                // Add an extension if that wasn't done already and save the current grid
                if (!selectedFile.getName().contains("." + pngExtension)) {
                    selectedFile = new File(selectedFile.getParent(), selectedFile.getName() + "." + pngExtension);
                }

                drawPanel.saveNonogram(selectedFile);
            } catch (IOException ioe) {
                // Nice error
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "An error occurred while saving the data:\n"
                        + ioe.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_exportMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PuzzleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PuzzleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PuzzleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PuzzleFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PuzzleFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton buildingModeRadioButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem importMenuItem;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.ButtonGroup modeButtonGroup;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JRadioButton solvingModeRadioButton;
    // End of variables declaration//GEN-END:variables
}
