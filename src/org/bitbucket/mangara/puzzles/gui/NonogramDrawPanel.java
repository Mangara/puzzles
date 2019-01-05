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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.bitbucket.mangara.puzzles.data.Nonogram;
import org.bitbucket.mangara.puzzles.generators.NonogramGenerator;
import org.bitbucket.mangara.puzzles.io.NonogramPrinter;

public class NonogramDrawPanel extends JPanel implements MouseInputListener {

    private static final Color FILLED_COLOUR = new Color(16, 16, 16);
    private static final Color EMPTY_COLOUR = Color.white;
    private static final Color UNKNOWN_COLOUR = Color.lightGray;

    private enum PuzzleState {
        UNKNOWN, EMPTY, FILLED;
    }

    private boolean building = true;
    private boolean[][] nonogramResult = new boolean[12][8];
    private PuzzleState[][] puzzle = new PuzzleState[12][8];
    private int gridLeftX, gridTopY;

    public NonogramDrawPanel() {
        setPreferredSize(new Dimension(1200, 800));
        addMouseListener(this);
        addMouseMotionListener(this);

        nonogramResult[2][1] = true;
        nonogramResult[3][1] = true;
        nonogramResult[2][3] = true;
        
        for (PuzzleState[] row : puzzle) {
            Arrays.fill(row, PuzzleState.UNKNOWN);
        }
    }

    public int getNonogramWidth() {
        return nonogramResult.length;
    }

    public int getNonogramHeight() {
        return nonogramResult[0].length;
    }
    
    public Nonogram getPuzzle() {
        return NonogramGenerator.generateNonogram(nonogramResult);
    }
    
    public void setSolution(boolean[][] solution) {
        building = false;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                puzzle[i][j] = solution[i][j] ? PuzzleState.FILLED : PuzzleState.EMPTY;
            }
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
        puzzle = new PuzzleState[width][height];
        for (PuzzleState[] column : puzzle) {
            Arrays.fill(column, PuzzleState.UNKNOWN);
        }
        repaint();
    }

    public void saveNonogram(File outputFile) throws IOException {
        Nonogram puzzle = NonogramGenerator.generateNonogram(nonogramResult);
        NonogramPrinter.printNonogram(puzzle, outputFile.toPath());
    }

    public void clear() {
        if (building) {
            for (boolean[] col : nonogramResult) {
                Arrays.fill(col, false);
            }
        } else {
            for (PuzzleState[] col : puzzle) {
                Arrays.fill(col, PuzzleState.UNKNOWN);
            }
        }
        repaint();
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

    private PuzzleState paintState = null;

    @Override
    public void mousePressed(MouseEvent e) {
        int i = getGridCol(e.getX());
        int j = getGridRow(e.getY());

        if (i < 0 || i > getNonogramWidth() || j < 0 || j > getNonogramHeight()) {
            return;
        }

        if (building) {
            paintState = nonogramResult[i][j] ? PuzzleState.EMPTY : PuzzleState.FILLED;
            nonogramResult[i][j] = (paintState == PuzzleState.FILLED);
        } else {
            boolean rightClick = SwingUtilities.isRightMouseButton(e);

            switch (puzzle[i][j]) {
                case EMPTY:
                    paintState = rightClick ? PuzzleState.FILLED : PuzzleState.UNKNOWN;
                    break;
                case UNKNOWN:
                    paintState = rightClick ? PuzzleState.EMPTY : PuzzleState.FILLED;
                    break;
                case FILLED:
                    paintState = rightClick ? PuzzleState.UNKNOWN : PuzzleState.EMPTY;
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
            nonogramResult[i][j] = (paintState == PuzzleState.FILLED);
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
}
