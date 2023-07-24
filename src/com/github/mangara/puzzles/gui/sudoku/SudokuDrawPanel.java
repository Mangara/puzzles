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
package com.github.mangara.puzzles.gui.sudoku;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import com.github.mangara.puzzles.io.sudoku.SudokuPrinter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class SudokuDrawPanel extends JPanel implements MouseInputListener, KeyListener {

    private final List<SudokuChangeListener> changeListeners = new ArrayList<>(1);

    private static final int NO_SELECTION = -1;

    private boolean building = true;
    private int[][] puzzle;
    private SudokuSolutionState[][] solution;
    private int selectedRow = NO_SELECTION, selectedCol = NO_SELECTION;
    private int leftX = 0, topY = 0;

    public SudokuDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

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
        resetSolution();
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

    public void addChangeListener(SudokuChangeListener listener) {
        changeListeners.add(listener);
    }

    private static final Color GUESS_COLOR = Color.BLUE;
    private static final Color GIVEN_COLOR = Color.BLACK;

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        int sudokuSize = 9 * SudokuPrinter.SQUARE_SIZE + 2 * SudokuPrinter.OUTER_PADDING;
        leftX = getWidth() - sudokuSize;
        topY = getHeight() - sudokuSize;

        // Highlight squares seen from selected square
        // Highlight selected square
        if (selectedRow != NO_SELECTION && selectedCol != NO_SELECTION) {
            int selectedX = leftX + SudokuPrinter.OUTER_PADDING + selectedCol * SudokuPrinter.SQUARE_SIZE;
            int selectedY = topY + SudokuPrinter.OUTER_PADDING + selectedRow * SudokuPrinter.SQUARE_SIZE;
            g.setColor(Color.red);
            g.fillRect(selectedX, selectedY, SudokuPrinter.SQUARE_SIZE, SudokuPrinter.SQUARE_SIZE);
        }

        if (!building) {
            // Draw guessed numbers
            Sudoku guesses = new Sudoku(getGuesses());
            BufferedImage guessesDrawing = SudokuPrinter.drawSudoku(guesses, false, GUESS_COLOR);
            g.drawImage(guessesDrawing, leftX, topY, this);
        }

        // Draw grid and given numbers
        Sudoku given = new Sudoku(puzzle);
        BufferedImage givenDrawing = SudokuPrinter.drawSudoku(given, true, GIVEN_COLOR);
        g.drawImage(givenDrawing, leftX, topY, this);
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
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();

        int row = getGridRow(e.getY());
        int col = getGridCol(e.getX());

        if (col < 0 || col > 9 || row < 0 || row > 9) {
            selectedRow = NO_SELECTION;
            selectedCol = NO_SELECTION;
            repaint();
            return;
        }

        selectedRow = row;
        selectedCol = col;

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedCol == NO_SELECTION || selectedRow == NO_SELECTION) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_DELETE:
                if (building) {
                    int oldDigit = puzzle[selectedRow][selectedCol];
                    puzzle[selectedRow][selectedCol] = BLANK;
                    fireSelectedSquareChangedEvent(oldDigit, BLANK);
                } else {
                    solution[selectedRow][selectedCol].number = BLANK;
                }
                break;

            case KeyEvent.VK_UP:
                selectedRow = Math.max(0, selectedRow - 1);
                break;

            case KeyEvent.VK_DOWN:
                selectedRow = Math.min(8, selectedRow + 1);
                break;

            case KeyEvent.VK_LEFT:
                selectedCol = Math.max(0, selectedCol - 1);
                break;

            case KeyEvent.VK_RIGHT:
                selectedCol = Math.min(8, selectedCol + 1);
                break;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (selectedCol == NO_SELECTION || selectedRow == NO_SELECTION) {
            return;
        }

        char key = e.getKeyChar();

        if (key < '1' || key > '9') {
            return;
        }

        int digit = key - '1' + 1;

        if (building) {
            int oldDigit = puzzle[selectedRow][selectedCol];
            puzzle[selectedRow][selectedCol] = digit;
            fireSelectedSquareChangedEvent(oldDigit, digit);
        } else {
            solution[selectedRow][selectedCol].number = digit;
        }

        repaint();
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

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private int getGridRow(int y) {
        return (y - topY - SudokuPrinter.OUTER_PADDING) / SudokuPrinter.SQUARE_SIZE;
    }

    private int getGridCol(int x) {
        return (x - leftX - SudokuPrinter.OUTER_PADDING) / SudokuPrinter.SQUARE_SIZE;
    }

    private void fireGlobalChangedEvent() {
        fireChangedEvent(SudokuChangedEvent.GLOBAL);
    }

    private void fireSelectedSquareChangedEvent(int oldDigit, int newDigit) {
        if (oldDigit == newDigit) {
            return;
        }

        SudokuChangedEvent e = new SudokuChangedEvent(selectedRow, selectedCol, oldDigit, newDigit);
        fireChangedEvent(e);
    }

    private void fireChangedEvent(SudokuChangedEvent e) {
        for (SudokuChangeListener listener : changeListeners) {
            listener.sudokuChanged(e);
        }
    }
}
