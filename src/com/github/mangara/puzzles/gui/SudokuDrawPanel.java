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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import com.github.mangara.puzzles.data.NonogramSolutionState;
import static com.github.mangara.puzzles.data.NonogramSolutionState.EMPTY;
import static com.github.mangara.puzzles.data.NonogramSolutionState.FILLED;
import static com.github.mangara.puzzles.data.NonogramSolutionState.UNKNOWN;
import com.github.mangara.puzzles.data.Sudoku;
import com.github.mangara.puzzles.data.SudokuSolutionState;
import static com.github.mangara.puzzles.data.SudokuSolutionState.BLANK;
import com.github.mangara.puzzles.gui.events.NonogramChangeListener;
import com.github.mangara.puzzles.gui.events.NonogramChangedEvent;
import com.github.mangara.puzzles.io.NonogramPrinter;
import com.github.mangara.puzzles.io.SudokuPrinter;

public class SudokuDrawPanel extends JPanel implements MouseInputListener {

//    private final List<NonogramChangeListener> changeListeners = new ArrayList<>(1);
    
    private boolean building = true;
    private int[][] puzzle;
    private SudokuSolutionState[][] solution;
    private int gridLeftX, gridTopY;

    public SudokuDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
        
        solution = new SudokuSolutionState[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                solution[row][col] = new SudokuSolutionState();
            }
        }
    }
    
    public Sudoku getPuzzle() {
        return new Sudoku(puzzle);
    }
    
    public void setPuzzle(Sudoku sudoku) {
        building = true;
        puzzle = sudoku.getGivenDigits();
        resetSolution();
        
        repaint();
        fireGlobalChangedEvent();
    }
    
    public void setSolution(SudokuSolutionState[][] newSolution) {
        building = false;
        for (int row = 0; row < 9; row++) {
            System.arraycopy(newSolution[row], 0, solution[row], 0, 9);
        }
        repaint();
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
        repaint();
    }

    public void clear() {
        if (building) {
            for (int[] col : puzzle) {
                Arrays.fill(col, BLANK);
            }
        } else {
            resetSolution();
        }
        repaint();
        fireGlobalChangedEvent();
    }
    
    private void resetSolution() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                solution[row][col] = new SudokuSolutionState();

                if (puzzle[row][col] != BLANK) {
                    solution[row][col].number = puzzle[row][col];
                }
            }
        }
    }
    
//    public void addChangeListener(NonogramChangeListener listener) {
//        changeListeners.add(listener);
//    }

    private static final Color GUESS_COLOR = Color.DARK_GRAY;
    private static final Color GIVEN_COLOR = Color.BLACK;
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Highlight squares seen from selected square
        
        // Highlight selected square
        
        // Draw guessed numbers
        Sudoku guesses = new Sudoku(getGuesses());
        BufferedImage guessesDrawing = SudokuPrinter.drawSudoku(guesses, false, GUESS_COLOR);

        int guessesX = getWidth() - guessesDrawing.getWidth();
        int guessesY = getHeight() - guessesDrawing.getHeight();
        g.drawImage(guessesDrawing, guessesX, guessesY, this);
        
        // Draw grid and given numbers
        Sudoku given = new Sudoku(puzzle);
        BufferedImage givenDrawing = SudokuPrinter.drawSudoku(given, true, GIVEN_COLOR);

        int givenX = getWidth() - givenDrawing.getWidth();
        int givenY = getHeight() - givenDrawing.getHeight();
        g.drawImage(givenDrawing, givenX, givenY, this);
    }
    
    private int[][] getGuesses() {
        int[][] guesses = new int[9][9];
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] == BLANK) {
                    guesses[row][col] = solution[row][col].number;
                } else {
                    guesses[row][col] = BLANK;
                }
            }
        }
        
        return guesses;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int i = getGridCol(e.getX());
        int j = getGridRow(e.getY());

        if (i < 0 || i > 9 || j < 0 || j > 9) {
            return;
        }

        if (building) {
//            boolean oldState = nonogramResult[i][j];
//            nonogramResult[i][j] = (paintState == NonogramSolutionState.FILLED);
//            fireChangedEvent(i, j, oldState, nonogramResult[i][j]);
        } else {
//            boolean rightClick = SwingUtilities.isRightMouseButton(e);
//
//            switch (puzzle[i][j]) {
//                case EMPTY:
//                    paintState = rightClick ? NonogramSolutionState.FILLED : NonogramSolutionState.UNKNOWN;
//                    break;
//                case UNKNOWN:
//                    paintState = rightClick ? NonogramSolutionState.EMPTY : NonogramSolutionState.FILLED;
//                    break;
//                case FILLED:
//                    paintState = rightClick ? NonogramSolutionState.UNKNOWN : NonogramSolutionState.EMPTY;
//                    break;
//            }
//
//            puzzle[i][j] = paintState;
        }

        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private int getGridRow(int y) {
        return (y - gridTopY) / NonogramPrinter.SQUARE_SIZE;
    }

    private int getGridCol(int x) {
        return (x - gridLeftX) / NonogramPrinter.SQUARE_SIZE;
    }

    private void fireGlobalChangedEvent() {
//        fireChangedEvent(NonogramChangedEvent.GLOBAL, NonogramChangedEvent.GLOBAL, false, true);
    }
    
    private void fireChangedEvent(int i, int j, boolean oldState, boolean newState) {
//        if (oldState == newState) {
//            return;
//        }
//        
//        NonogramChangedEvent e = new NonogramChangedEvent(i, j, oldState, newState);
//        for (NonogramChangeListener listener : changeListeners) {
//            listener.nonogramChanged(e);
//        }
    }
}
