/*
 * Copyright 2020 Sander Verdonschot.
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
package com.github.mangara.puzzles.gui.logiquiz;

import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.PuzzleType;
import com.github.mangara.puzzles.gui.PuzzlePanel;

public class LogiquizPanel extends javax.swing.JPanel implements PuzzlePanel {

    private final LogiquizDrawPanel drawPanel;
    private final EditGroupsDialog editGroupsDialog;
    
    /**
     * Creates new form LogiquizPanel
     */
    public LogiquizPanel(java.awt.Frame frame) {
        initComponents();
        
        drawPanel = new LogiquizDrawPanel();
        add(drawPanel, java.awt.BorderLayout.CENTER);
        
        editGroupsDialog = new EditGroupsDialog(frame, drawPanel);
    }

    @Override
    public void setPuzzle(Puzzle puzzle) {
        if (puzzle.getType() != PuzzleType.LOGIQUIZ) {
            throw new IllegalArgumentException("Puzzle must be a Logiquiz");
        }
        
        Logiquiz logiquiz = (Logiquiz) puzzle;
        drawPanel.setPuzzle(logiquiz);
    }

    @Override
    public Puzzle getPuzzle() {
        return drawPanel.getPuzzle();
    }

    @Override
    public void clear() {
        drawPanel.clear();
    }

    @Override
    public void setMode(InteractionMode mode) {
        drawPanel.setInteractionMode(mode);
    }
    
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator2 = new javax.swing.JSeparator();
        cluesPanel = new javax.swing.JPanel();
        editGroupsButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        editGroupsButton.setText("Edit groups");
        editGroupsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editGroupsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cluesPanelLayout = new javax.swing.GroupLayout(cluesPanel);
        cluesPanel.setLayout(cluesPanelLayout);
        cluesPanelLayout.setHorizontalGroup(
            cluesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cluesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editGroupsButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cluesPanelLayout.setVerticalGroup(
            cluesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cluesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editGroupsButton)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        add(cluesPanel, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void editGroupsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editGroupsButtonActionPerformed
        editGroupsDialog.copyGroupsFrom(drawPanel.getPuzzle());
        editGroupsDialog.setVisible(true);
    }//GEN-LAST:event_editGroupsButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cluesPanel;
    private javax.swing.JButton editGroupsButton;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

}