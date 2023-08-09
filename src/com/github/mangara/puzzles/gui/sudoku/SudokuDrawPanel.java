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

import com.github.mangara.puzzles.data.sudoku.Cell;
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
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuDrawPanel extends JPanel implements MouseInputListener, KeyListener {

    private final List<SudokuChangeListener> changeListeners = new ArrayList<>(1);

    private static final int NO_SELECTION = -1;
    private static final Color SELECTED_COLOR = new Color(194, 222, 252);
    private static final Color SELECTED_BORDER = new Color(96, 110, 124);
    private static final Color PRIMARY_HIGHLIGHT = new Color(194, 222, 252);
    private static final Color SECONDARY_HIGHLIGHT = new Color(252, 224, 194);

    private boolean building = true;
    private int[][] puzzle;
    private SolvingSudoku solution;
    private int selectedRow = NO_SELECTION, selectedCol = NO_SELECTION;
    private int leftX = 0, topY = 0;
    private List<Cell> primaryHighlightCells = Collections.emptyList();
    private List<Cell> secondaryHighlightCells = Collections.emptyList();

    public SudokuDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        solution = new SolvingSudoku();
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
        solution = new SolvingSudoku(newSolution);
        repaint();
    }
    
    public void setSolution(SolvingSudoku newSolution) {
        building = false;
        solution = newSolution;
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
        solution = new SolvingSudoku(puzzle);
    }
    
    public void setPrimaryHighlight(List<Cell> cells) {
        primaryHighlightCells = cells;
    }

    public void setSecondaryHighlight(List<Cell> cells) {
        secondaryHighlightCells = cells;
    }

    public void addChangeListener(SudokuChangeListener listener) {
        changeListeners.add(listener);
    }

    private static final Color GUESS_COLOR = Color.BLUE;
    private static final Color GIVEN_COLOR = Color.BLACK;
    private static final Color POSSIBLE_COLOR = Color.GRAY;

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        int sudokuSize = 9 * SudokuPrinter.SQUARE_SIZE + 2 * SudokuPrinter.OUTER_PADDING;
        leftX = getWidth() - sudokuSize;
        topY = getHeight() - sudokuSize;

        // Highlight squares seen from selected square
        // TODO
        
        // Highlights
        if (primaryHighlightCells != null) {
            g.setColor(PRIMARY_HIGHLIGHT);
            for (Cell cell : primaryHighlightCells) {
                fillCell(g, cell, leftX, topY);
            }
        }
        
        if (secondaryHighlightCells != null) {
            g.setColor(SECONDARY_HIGHLIGHT);
            for (Cell cell : secondaryHighlightCells) {
                fillCell(g, cell, leftX, topY);
            }
        }
        
        // Highlight selected square
//        if (selectedRow != NO_SELECTION && selectedCol != NO_SELECTION) {
//            g.setColor(SELECTED_COLOR);
//            fillCell(g, selectedRow, selectedCol, leftX, topY);
//        }

        if (building) {
            // Draw grid and given numbers
            Sudoku given = new Sudoku(puzzle);
            BufferedImage drawing = SudokuPrinter.drawSudoku(given, true, GIVEN_COLOR);
            g.drawImage(drawing, leftX, topY, this);
        } else {
            // Draw guessed numbers
            BufferedImage drawing = SudokuPrinter.drawSolvingSudoku(solution, true, GIVEN_COLOR, GUESS_COLOR, POSSIBLE_COLOR);
            g.drawImage(drawing, leftX, topY, this);
        }
        
        // Highlight selected square border
        if (selectedRow != NO_SELECTION && selectedCol != NO_SELECTION) {
            int selectedX = leftX + SudokuPrinter.OUTER_PADDING + selectedCol * SudokuPrinter.SQUARE_SIZE;
            int selectedY = topY + SudokuPrinter.OUTER_PADDING + selectedRow * SudokuPrinter.SQUARE_SIZE;
            g.setColor(SELECTED_BORDER);
            ((Graphics2D) g).setStroke(new BasicStroke(6f));
//            g.drawRect(selectedX, selectedY, SudokuPrinter.SQUARE_SIZE, SudokuPrinter.SQUARE_SIZE);
            g.drawRoundRect(selectedX, selectedY, SudokuPrinter.SQUARE_SIZE, SudokuPrinter.SQUARE_SIZE, 4, 4);
        }
    }
    
    private void fillCell(Graphics g, Cell cell, int leftX, int topY) {
        fillCell(g, cell.row, cell.col, leftX, topY);
    }
    
    private void fillCell(Graphics g, int row, int col, int leftX, int topY) {
        int selectedX = leftX + SudokuPrinter.OUTER_PADDING + col * SudokuPrinter.SQUARE_SIZE;
        int selectedY = topY + SudokuPrinter.OUTER_PADDING + row * SudokuPrinter.SQUARE_SIZE;
        g.fillRect(selectedX, selectedY, SudokuPrinter.SQUARE_SIZE, SudokuPrinter.SQUARE_SIZE);
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
                    solution.removeDigit(selectedRow, selectedCol);
                }
                break;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                selectedRow = Math.max(0, selectedRow - 1);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                selectedRow = Math.min(8, selectedRow + 1);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                selectedCol = Math.max(0, selectedCol - 1);
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
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
            
            if (oldDigit == digit) {
                // Remove the digit instead of overwriting with itself
                digit = BLANK;
            }
            
            puzzle[selectedRow][selectedCol] = digit;
            fireSelectedSquareChangedEvent(oldDigit, digit);
        } else {
            int oldDigit = solution.state[selectedRow][selectedCol].digit;
            
            if (oldDigit == digit) {
                // Remove the digit instead of overwriting with itself
                solution.removeDigit(selectedRow, selectedCol);
            } else {
                solution.placeDigit(selectedRow, selectedCol, digit);
            }
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
