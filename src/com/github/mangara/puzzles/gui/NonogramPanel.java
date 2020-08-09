/*
 * Copyright 2020 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
package com.github.mangara.puzzles.gui;

import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.PuzzleType;
import com.github.mangara.puzzles.data.SolvedNonogram;

public class NonogramPanel extends javax.swing.JPanel implements PuzzlePanel {

    private final NonogramDrawPanel drawPanel;
    
    public NonogramPanel() {
        initComponents();
        
        drawPanel = new NonogramDrawPanel();
        add(drawPanel, java.awt.BorderLayout.CENTER);
    }

    @Override
    public void setPuzzle(Puzzle puzzle) {
        if (puzzle.getType() != PuzzleType.NONOGRAM) {
            throw new IllegalArgumentException("Puzzle must be a Nonogram");
        }
        if (!(puzzle instanceof SolvedNonogram)) {
            throw new IllegalArgumentException("Puzzle must be solved");
        }
        
        SolvedNonogram nonogram = (SolvedNonogram) puzzle;
        drawPanel.setPuzzle(nonogram.getDrawing());
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
        boolean building = mode == InteractionMode.BUILDING;
        drawPanel.setBuilding(building);
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

        sidePanel = new javax.swing.JPanel();
        solvableLabel = new javax.swing.JLabel();
        stepsButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        solvableLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        solvableLabel.setText("Solvable");

        stepsButton.setText("Show Steps");

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(solvableLabel)
                    .addComponent(stepsButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(solvableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stepsButton)
                .addContainerGap(241, Short.MAX_VALUE))
        );

        add(sidePanel, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel sidePanel;
    private javax.swing.JLabel solvableLabel;
    private javax.swing.JButton stepsButton;
    // End of variables declaration//GEN-END:variables

}