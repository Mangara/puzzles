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
package com.github.mangara.puzzles.gui.sudoku;

import com.github.mangara.puzzles.checkers.sudoku.SudokuChecker;
import com.github.mangara.puzzles.data.Pair;
import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.PuzzleType;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import com.github.mangara.puzzles.gui.PuzzlePanel;
import com.github.mangara.puzzles.solvers.sudoku.BruteForceSolver;
import com.github.mangara.puzzles.solvers.sudoku.LogicalSolver;
import com.github.mangara.puzzles.solvers.sudoku.SolveStep;
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
import java.util.List;

public class SudokuPanel extends javax.swing.JPanel implements PuzzlePanel {

    private final SudokuDrawPanel drawPanel;
//    private final SolutionStepsDialog stepsDialog;

    public SudokuPanel(java.awt.Frame frame) {
        initComponents();

        drawPanel = new SudokuDrawPanel();
        drawPanel.addChangeListener((SudokuChangedEvent e) -> puzzleChanged(e));
        add(drawPanel, java.awt.BorderLayout.CENTER);
        
//        stepsDialog = new SolutionStepsDialog(frame, false, drawPanel);
    }

    @Override
    public void setPuzzle(Puzzle puzzle) {
        if (puzzle.getType() != PuzzleType.SUDOKU) {
            throw new IllegalArgumentException("Puzzle must be a Sudoku");
        }

        drawPanel.setPuzzle((Sudoku) puzzle);
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

    private void puzzleChanged(SudokuChangedEvent e) {
        Sudoku puzzle = drawPanel.getPuzzle();
        updateValidity(puzzle);
        updateUniqueness(puzzle);
    }

    private void updateValidity(Sudoku puzzle) {
        boolean isValid = SudokuChecker.isValidPuzzle(puzzle);
        String text = isValid ? "Valid: YES" : "Valid: NO";
        validLabel.setText(text);
    }
    
    private void updateUniqueness(Sudoku puzzle) {
        // count given digits, if too few, don't check
        int givenDigitCount = 0;
        int[][] digits = puzzle.getGivenDigits();
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (digits[row][col] != BLANK) {
                    givenDigitCount++;
                }
            }
        }
        
        String text;
        
        if (givenDigitCount < 78) {
            text = "Unique: ?";
        } else {
            boolean isUnique = BruteForceSolver.hasUniqueSolution(puzzle);
            text = isUnique ? "Unique: YES" : "Unique: NO";
        }
        
        uniqueLabel.setText(text);
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
        validLabel = new javax.swing.JLabel();
        stepsButton = new javax.swing.JButton();
        uniqueLabel = new javax.swing.JLabel();
        solveButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        validLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        validLabel.setText("Solvable");

        stepsButton.setText("Show Steps");
        stepsButton.setEnabled(false);
        stepsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stepsButtonActionPerformed(evt);
            }
        });

        uniqueLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        uniqueLabel.setText("Unique?");

        solveButton.setText("Solve");
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
                    .addComponent(validLabel)
                    .addComponent(stepsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(uniqueLabel)
                    .addComponent(solveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(validLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(uniqueLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 351, Short.MAX_VALUE)
                .addComponent(solveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stepsButton)
                .addContainerGap())
        );

        add(sidePanel, java.awt.BorderLayout.LINE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void stepsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepsButtonActionPerformed
//        IterativeSolver solver = new IterativeSolver(true);
//        Nonogram puzzle = drawPanel.getPuzzle();
//        solver.findAnySolution(puzzle);
//        List<NonogramSolutionState[][]> steps = solver.getPartialSolutionRecord();
//        drawPanel.setBuilding(false);
//        stepsDialog.setSteps(steps);
//        stepsDialog.setVisible(true);
    }//GEN-LAST:event_stepsButtonActionPerformed

    private void solveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solveButtonActionPerformed
        Pair<SolvingSudoku, List<SolveStep>> solution = LogicalSolver.solve(drawPanel.getPuzzle());
        
        System.out.println("Next steps:");
        for (SolveStep step : solution.getSecond()) {
            System.out.println(step.description());
        }
        System.out.println("     ------      ");
    }//GEN-LAST:event_solveButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel sidePanel;
    private javax.swing.JButton solveButton;
    private javax.swing.JButton stepsButton;
    private javax.swing.JLabel uniqueLabel;
    private javax.swing.JLabel validLabel;
    // End of variables declaration//GEN-END:variables

}
