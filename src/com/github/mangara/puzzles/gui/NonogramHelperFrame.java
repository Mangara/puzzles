/*
 * Copyright 2022 Sander Verdonschot <sander.verdonschot at gmail.com>.
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

import com.github.mangara.puzzles.data.Nonogram;
import com.github.mangara.puzzles.data.NonogramSolutionState;
import com.github.mangara.puzzles.solvers.IterativeSolver;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NonogramHelperFrame extends javax.swing.JFrame implements DocumentListener {

    private final SimpleNonogramDrawPanel drawPanel;
    private final SolutionStepsDialog stepsDialog;

    /**
     * Creates new form NonogramHelperFrame
     */
    public NonogramHelperFrame() {
        initComponents();

        sideNumbersTextArea.getDocument().addDocumentListener(this);
        topNumbersTextArea.getDocument().addDocumentListener(this);

        drawPanel = new SimpleNonogramDrawPanel();
        add(drawPanel, java.awt.BorderLayout.CENTER);
        updateNonogram();

        stepsDialog = new SolutionStepsDialog(this, false, drawPanel);

        pack();
    }

    private void updateNonogram() {
        List<List<Integer>> sideNumbers = parseNumberList(sideNumbersTextArea.getText());
        List<List<Integer>> topNumbers = parseNumberList(topNumbersTextArea.getText());

        if (sideNumbers == null || topNumbers == null || sideNumbers.isEmpty() || topNumbers.isEmpty()) {
            // Invalid
            return;
        }

        Nonogram puzzle = new Nonogram(sideNumbers, topNumbers);
        drawPanel.setPuzzle(puzzle);
    }

    private List<List<Integer>> parseNumberList(String text) {
        return text.lines()
                .filter((line) -> !line.startsWith("#"))
                .map((line) -> line.strip())
                .filter((line) -> !line.isEmpty())
                .map((line) -> parseNumberLine(line))
                .collect(Collectors.toList());
    }

    private List<Integer> parseNumberLine(String line) {
        return Arrays.stream(line.split("\\s"))
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
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
        sideNumbersLabel = new javax.swing.JLabel();
        topNumbersLabel = new javax.swing.JLabel();
        sideNumbersScrollPane = new javax.swing.JScrollPane();
        sideNumbersTextArea = new javax.swing.JTextArea();
        topNumbersScrollPane = new javax.swing.JScrollPane();
        topNumbersTextArea = new javax.swing.JTextArea();
        solveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nonogram Helper");

        sideNumbersLabel.setText("Side numbers");

        topNumbersLabel.setText("Top numbers");

        sideNumbersTextArea.setColumns(20);
        sideNumbersTextArea.setRows(5);
        sideNumbersTextArea.setText("# Top to bottom, left to right\n2 1\n3\n0\n2\n");
        sideNumbersScrollPane.setViewportView(sideNumbersTextArea);

        topNumbersTextArea.setColumns(20);
        topNumbersTextArea.setRows(5);
        topNumbersTextArea.setText("# Left to right, top to bottom\n1\n2 1\n1 1\n2\n");
        topNumbersScrollPane.setViewportView(topNumbersTextArea);

        solveButton.setText("Solve!");
        solveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sideNumbersLabel)
                    .addComponent(topNumbersLabel)
                    .addComponent(sideNumbersScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .addComponent(topNumbersScrollPane)
                    .addComponent(solveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sideNumbersLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sideNumbersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(topNumbersLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topNumbersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(solveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {sideNumbersScrollPane, topNumbersScrollPane});

        getContentPane().add(sidePanel, java.awt.BorderLayout.LINE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void solveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solveButtonActionPerformed
        IterativeSolver solver = new IterativeSolver(true);
        solver.findAnySolution(drawPanel.getPuzzle());
        List<NonogramSolutionState[][]> steps = solver.getPartialSolutionRecord();
        stepsDialog.setSteps(steps);
        stepsDialog.setVisible(true);
    }//GEN-LAST:event_solveButtonActionPerformed

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
            java.util.logging.Logger.getLogger(NonogramHelperFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NonogramHelperFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NonogramHelperFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NonogramHelperFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NonogramHelperFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel sideNumbersLabel;
    private javax.swing.JScrollPane sideNumbersScrollPane;
    private javax.swing.JTextArea sideNumbersTextArea;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JButton solveButton;
    private javax.swing.JLabel topNumbersLabel;
    private javax.swing.JScrollPane topNumbersScrollPane;
    private javax.swing.JTextArea topNumbersTextArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateNonogram();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateNonogram();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateNonogram();
    }
}
