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
package com.github.mangara.puzzles.gui;

import com.github.mangara.puzzles.data.CreatePuzzleSettings;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.github.mangara.puzzles.data.Nonogram;
import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.NonogramSolutionState;
import com.github.mangara.puzzles.generators.PuzzleFactory;
import com.github.mangara.puzzles.io.PuzzlePrinter;
import com.github.mangara.puzzles.io.PuzzleReader;
import com.github.mangara.puzzles.io.PuzzleWriter;
import com.github.mangara.puzzles.solvers.nonogram.IterativeSolver;
import com.github.mangara.puzzles.solvers.nonogram.NonogramSolver;

public class MainFrame extends javax.swing.JFrame {

    private static final String TXT_EXTENSION = "txt";
    private static final String PNG_EXTENSION = "png";
    
    private final CenterPuzzlePanel puzzlePanel;
    
    private final NewPuzzleDialog newDialog;
    
    private final JFileChooser saveFileChooser;
    private final JFileChooser exportFileChooser;
    private final FileNameExtensionFilter saveFileFilter = new FileNameExtensionFilter("Text files", TXT_EXTENSION);
    private final FileNameExtensionFilter exportFileFilter = new FileNameExtensionFilter("PNG Images", PNG_EXTENSION);
    
    /**
     * Creates new form PuzzleFrame
     */
    public MainFrame() {
        initComponents();
        
        puzzlePanel = new CenterPuzzlePanel(this);
        getContentPane().add(puzzlePanel, java.awt.BorderLayout.CENTER);
        
        newDialog = new NewPuzzleDialog(this);
        
        exportFileChooser = new JFileChooser(System.getProperty("user.dir"));
        exportFileChooser.addChoosableFileFilter(exportFileFilter);
        exportFileChooser.setFileFilter(exportFileFilter);
        
        saveFileChooser = new JFileChooser(System.getProperty("user.dir"));
        saveFileChooser.addChoosableFileFilter(saveFileFilter);
        saveFileChooser.setFileFilter(saveFileFilter);
        
        pack();
    }

    public void newPuzzle(CreatePuzzleSettings settings) {
        Puzzle puzzle = PuzzleFactory.create(settings);
        puzzlePanel.setPuzzle(puzzle);
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
        bottomPanel = new javax.swing.JPanel();
        clearButton = new javax.swing.JButton();
        buildingModeRadioButton = new javax.swing.JRadioButton();
        solvingModeRadioButton = new javax.swing.JRadioButton();
        modeLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        solveMenu = new javax.swing.JMenu();
        solveMenuItem = new javax.swing.JMenuItem();
        checkMenuItem = new javax.swing.JMenuItem();
        bruteForceStepsMenuItem = new javax.swing.JMenuItem();
        iterativeStepsMenuItem = new javax.swing.JMenuItem();

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

        javax.swing.GroupLayout bottomPanelLayout = new javax.swing.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomPanelLayout.createSequentialGroup()
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
        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearButton)
                    .addComponent(buildingModeRadioButton)
                    .addComponent(solvingModeRadioButton)
                    .addComponent(modeLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        newMenuItem.setText("New...");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openMenuItem.setText("Open...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setText("Save...");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        exportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        exportMenuItem.setText("Export...");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportMenuItem);

        menuBar.add(fileMenu);

        solveMenu.setText("Solve");

        solveMenuItem.setText("Find solution");
        solveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solveMenuItemActionPerformed(evt);
            }
        });
        solveMenu.add(solveMenuItem);

        checkMenuItem.setText("Uniquely solvable?");
        checkMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkMenuItemActionPerformed(evt);
            }
        });
        solveMenu.add(checkMenuItem);

        bruteForceStepsMenuItem.setText("Brute Force steps");
        bruteForceStepsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bruteForceStepsMenuItemActionPerformed(evt);
            }
        });
        solveMenu.add(bruteForceStepsMenuItem);

        iterativeStepsMenuItem.setText("Iterative steps");
        iterativeStepsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iterativeStepsMenuItemActionPerformed(evt);
            }
        });
        solveMenu.add(iterativeStepsMenuItem);

        menuBar.add(solveMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        puzzlePanel.clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void buildingModeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buildingModeRadioButtonActionPerformed
        puzzlePanel.setBuilding(true);
    }//GEN-LAST:event_buildingModeRadioButtonActionPerformed

    private void solvingModeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solvingModeRadioButtonActionPerformed
        puzzlePanel.setBuilding(false);
    }//GEN-LAST:event_solvingModeRadioButtonActionPerformed

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
        newDialog.setVisible(true);
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        int saved = exportFileChooser.showSaveDialog(this);

        if (saved == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = exportFileChooser.getSelectedFile();

                // Add an extension if that wasn't done already and save the current grid
                if (!selectedFile.getName().contains("." + PNG_EXTENSION)) {
                    selectedFile = new File(selectedFile.getParent(), selectedFile.getName() + "." + PNG_EXTENSION);
                }

                Puzzle puzzle = puzzlePanel.getPuzzle();
                PuzzlePrinter.print(puzzle, selectedFile.toPath());
            } catch (IOException ioe) {
                // Nice error
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "An error occurred while saving the puzzle:\n"
                        + ioe.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void solveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solveMenuItemActionPerformed
//        Nonogram puzzle = drawPanel.getPuzzle();
//        System.out.println("Puzzle: " + puzzle);
//        boolean[][] solution = NonogramSolver.findAnySolution(puzzle);
//        System.out.println("Solution: " + Arrays.deepToString(solution));
//        drawPanel.setSolution(solution);
    }//GEN-LAST:event_solveMenuItemActionPerformed

    private void checkMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkMenuItemActionPerformed
//        Nonogram puzzle = drawPanel.getPuzzle();
//        boolean unique = NonogramSolver.hasUniqueSolution(puzzle);
//        
//        String message = unique ? "Unique solution!" : "No unique solution.";
//        int type = unique ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
//        JOptionPane.showMessageDialog(this, message, "Unique solution?", type);
    }//GEN-LAST:event_checkMenuItemActionPerformed

    private void bruteForceStepsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bruteForceStepsMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bruteForceStepsMenuItemActionPerformed

    private void iterativeStepsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iterativeStepsMenuItemActionPerformed
//        IterativeSolver solver = new IterativeSolver(true);
//        Nonogram puzzle = drawPanel.getPuzzle();
//        solver.findAnySolution(puzzle);
//        List<NonogramSolutionState[][]> steps = solver.getPartialSolutionRecord();
//        SolutionStepsDialog stepsDialog = new SolutionStepsDialog(this, false, steps, drawPanel);
//        stepsDialog.setVisible(true);
    }//GEN-LAST:event_iterativeStepsMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        int saved = saveFileChooser.showOpenDialog(this);

        if (saved == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = saveFileChooser.getSelectedFile();
                
                Puzzle puzzle = PuzzleReader.read(selectedFile.toPath());
                puzzlePanel.setPuzzle(puzzle);
            } catch (IOException ex) {
                // Nice error
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "An error occurred while reading the data:\n"
                        + ex.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        int saved = saveFileChooser.showSaveDialog(this);

        if (saved == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = saveFileChooser.getSelectedFile();

                // Add an extension if that wasn't done already and save the current grid
                if (!selectedFile.getName().contains("." + TXT_EXTENSION)) {
                    selectedFile = new File(selectedFile.getParent(), selectedFile.getName() + "." + TXT_EXTENSION);
                }

                Puzzle puzzle = puzzlePanel.getPuzzle();
                PuzzleWriter.write(puzzle, selectedFile.toPath());
            } catch (IOException ioe) {
                // Nice error
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "An error occurred while saving the data:\n"
                        + ioe.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JMenuItem bruteForceStepsMenuItem;
    private javax.swing.JRadioButton buildingModeRadioButton;
    private javax.swing.JMenuItem checkMenuItem;
    private javax.swing.JButton clearButton;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem iterativeStepsMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.ButtonGroup modeButtonGroup;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu solveMenu;
    private javax.swing.JMenuItem solveMenuItem;
    private javax.swing.JRadioButton solvingModeRadioButton;
    // End of variables declaration//GEN-END:variables
}
