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
package com.github.mangara.puzzles.gui.nonogram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import com.github.mangara.puzzles.data.nonogram.Nonogram;
import com.github.mangara.puzzles.data.nonogram.NonogramSolutionState;
import com.github.mangara.puzzles.data.nonogram.SolvedNonogram;
import com.github.mangara.puzzles.generators.nonogram.NonogramGenerator;
import com.github.mangara.puzzles.io.nonogram.NonogramPrinter;
import java.util.ArrayList;
import java.util.List;

public class NonogramDrawPanel extends JPanel implements MouseInputListener, NonogramSolutionDrawer{

    private static final Color FILLED_COLOUR = new Color(16, 16, 16);
    private static final Color EMPTY_COLOUR = Color.white;
    private static final Color UNKNOWN_COLOUR = Color.lightGray;

    private final List<NonogramChangeListener> changeListeners = new ArrayList<>(1);
    
    private boolean building = true;
    private boolean[][] nonogramResult = new boolean[12][8];
    private NonogramSolutionState[][] puzzle = new NonogramSolutionState[12][8];
    private int gridLeftX, gridTopY;

    public NonogramDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
        
        for (NonogramSolutionState[] row : puzzle) {
            Arrays.fill(row, NonogramSolutionState.UNKNOWN);
        }
    }

    public int getNonogramWidth() {
        return nonogramResult.length;
    }

    public int getNonogramHeight() {
        return nonogramResult[0].length;
    }
    
    public boolean[][] getDrawing() {
        return nonogramResult;
    }
    
    public SolvedNonogram getPuzzle() {
        return NonogramGenerator.generateNonogram(nonogramResult);
    }
    
    public void setPuzzle(boolean[][] drawing) {
        int width = drawing.length;
        int height = drawing[0].length;
        newNonogram(width, height);
        building = true;
        for (int i = 0; i < drawing.length; i++) {
            System.arraycopy(drawing[i], 0, nonogramResult[i], 0, drawing[i].length);
        }
        repaint();
        fireGlobalChangedEvent();
    }
    
    public void setSolution(boolean[][] solution) {
        building = false;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                puzzle[i][j] = solution[i][j] ? NonogramSolutionState.FILLED : NonogramSolutionState.EMPTY;
            }
        }
        repaint();
    }
    
    @Override
    public void setSolution(NonogramSolutionState[][] solution) {
        building = false;
        for (int i = 0; i < puzzle.length; i++) {
            System.arraycopy(solution[i], 0, puzzle[i], 0, puzzle[i].length);
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

    public void newNonogram(int width, int height) {
        nonogramResult = new boolean[width][height];
        puzzle = new NonogramSolutionState[width][height];
        for (NonogramSolutionState[] column : puzzle) {
            Arrays.fill(column, NonogramSolutionState.UNKNOWN);
        }
        repaint();
        fireGlobalChangedEvent();
    }

    public void clear() {
        if (building) {
            for (boolean[] col : nonogramResult) {
                Arrays.fill(col, false);
            }
        } else {
            for (NonogramSolutionState[] col : puzzle) {
                Arrays.fill(col, NonogramSolutionState.UNKNOWN);
            }
        }
        repaint();
        fireGlobalChangedEvent();
    }
    
    public void addChangeListener(NonogramChangeListener listener) {
        changeListeners.add(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Fill squares
        gridLeftX = getWidth() - NonogramPrinter.OUTER_PADDING - getNonogramWidth() * NonogramPrinter.SQUARE_SIZE;
        gridTopY = getHeight() - NonogramPrinter.OUTER_PADDING - getNonogramHeight() * NonogramPrinter.SQUARE_SIZE;

        for (int i = 0; i < nonogramResult.length; i++) {
            for (int j = 0; j < nonogramResult[i].length; j++) {
                int gridX = gridLeftX + i * NonogramPrinter.SQUARE_SIZE;
                int gridY = gridTopY + j * NonogramPrinter.SQUARE_SIZE;

                g.setColor(getColorForSquare(i, j));

                g.fillRect(gridX, gridY, NonogramPrinter.SQUARE_SIZE, NonogramPrinter.SQUARE_SIZE);
            }
        }

        // Draw grid and numbers
        Nonogram puzzle = NonogramGenerator.generateNonogram(nonogramResult);
        BufferedImage drawing = NonogramPrinter.drawNonogram(puzzle);

        int x = getWidth() - drawing.getWidth();
        int y = getHeight() - drawing.getHeight();
        g.drawImage(drawing, x, y, this);
    }

    private Color getColorForSquare(int i, int j) {
        if (building) {
            return nonogramResult[i][j] ? FILLED_COLOUR : EMPTY_COLOUR;
        } else {
            switch (puzzle[i][j]) {
                case EMPTY:
                    return EMPTY_COLOUR;
                case FILLED:
                    return FILLED_COLOUR;
                default:
                    return UNKNOWN_COLOUR;
            }
        }
    }

    private NonogramSolutionState paintState = null;

    @Override
    public void mousePressed(MouseEvent e) {
        int i = getGridCol(e.getX());
        int j = getGridRow(e.getY());

        if (i < 0 || i > getNonogramWidth() || j < 0 || j > getNonogramHeight()) {
            return;
        }

        if (building) {
            paintState = nonogramResult[i][j] ? NonogramSolutionState.EMPTY : NonogramSolutionState.FILLED;
            boolean oldState = nonogramResult[i][j];
            nonogramResult[i][j] = (paintState == NonogramSolutionState.FILLED);
            fireChangedEvent(i, j, oldState, nonogramResult[i][j]);
        } else {
            boolean rightClick = SwingUtilities.isRightMouseButton(e);

            switch (puzzle[i][j]) {
                case EMPTY:
                    paintState = rightClick ? NonogramSolutionState.FILLED : NonogramSolutionState.UNKNOWN;
                    break;
                case UNKNOWN:
                    paintState = rightClick ? NonogramSolutionState.EMPTY : NonogramSolutionState.FILLED;
                    break;
                case FILLED:
                    paintState = rightClick ? NonogramSolutionState.UNKNOWN : NonogramSolutionState.EMPTY;
                    break;
            }

            puzzle[i][j] = paintState;
        }

        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int i = getGridCol(e.getX());
        int j = getGridRow(e.getY());

        if (i < 0 || i > getNonogramWidth() || j < 0 || j > getNonogramHeight()) {
            return;
        }

        if (building) {
            boolean oldState = nonogramResult[i][j];
            nonogramResult[i][j] = (paintState == NonogramSolutionState.FILLED);
            fireChangedEvent(i, j, oldState, nonogramResult[i][j]);
        } else {
            puzzle[i][j] = paintState;
        }

        repaint();
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
        fireChangedEvent(NonogramChangedEvent.GLOBAL, NonogramChangedEvent.GLOBAL, false, true);
    }
    
    private void fireChangedEvent(int i, int j, boolean oldState, boolean newState) {
        if (oldState == newState) {
            return;
        }
        
        NonogramChangedEvent e = new NonogramChangedEvent(i, j, oldState, newState);
        for (NonogramChangeListener listener : changeListeners) {
            listener.nonogramChanged(e);
        }
    }
}
