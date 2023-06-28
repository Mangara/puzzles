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
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import com.github.mangara.puzzles.data.nonogram.Nonogram;
import com.github.mangara.puzzles.data.nonogram.NonogramSolutionState;
import com.github.mangara.puzzles.io.nonogram.NonogramPrinter;
import java.util.Collections;

public class SimpleNonogramDrawPanel extends JPanel implements NonogramSolutionDrawer {

    private static final Color FILLED_COLOUR = new Color(16, 16, 16);
    private static final Color EMPTY_COLOUR = Color.white;
    private static final Color UNKNOWN_COLOUR = Color.lightGray;
    
    private Nonogram puzzle = new Nonogram(Collections.nCopies(8, Collections.singletonList(0)), Collections.nCopies(12, Collections.singletonList(0)));
    private NonogramSolutionState[][] puzzleState = new NonogramSolutionState[12][8];
    private int gridLeftX, gridTopY;

    public SimpleNonogramDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        
        for (NonogramSolutionState[] row : puzzleState) {
            Arrays.fill(row, NonogramSolutionState.UNKNOWN);
        }
    }
    
    public Nonogram getPuzzle() {
        return puzzle;
    }
    
    public void setPuzzle(Nonogram newPuzzle) {
        this.puzzle = newPuzzle;
        
        int width = newPuzzle.getWidth();
        int height = newPuzzle.getHeight();
        puzzleState = new NonogramSolutionState[width][height];
        for (NonogramSolutionState[] column : puzzleState) {
            Arrays.fill(column, NonogramSolutionState.UNKNOWN);
        }
        repaint();
    }
    
    @Override
    public void setSolution(NonogramSolutionState[][] solution) {
        for (int i = 0; i < puzzleState.length; i++) {
            System.arraycopy(solution[i], 0, puzzleState[i], 0, puzzleState[i].length);
        }
        repaint();
    }

    public void clear() {
        for (NonogramSolutionState[] col : puzzleState) {
            Arrays.fill(col, NonogramSolutionState.UNKNOWN);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Fill squares
        gridLeftX = getWidth() - NonogramPrinter.OUTER_PADDING - puzzle.getWidth() * NonogramPrinter.SQUARE_SIZE;
        gridTopY = getHeight() - NonogramPrinter.OUTER_PADDING - puzzle.getHeight() * NonogramPrinter.SQUARE_SIZE;

        for (int i = 0; i < puzzleState.length; i++) {
            for (int j = 0; j < puzzleState[i].length; j++) {
                int gridX = gridLeftX + i * NonogramPrinter.SQUARE_SIZE;
                int gridY = gridTopY + j * NonogramPrinter.SQUARE_SIZE;

                g.setColor(getColorForSquare(i, j));

                g.fillRect(gridX, gridY, NonogramPrinter.SQUARE_SIZE, NonogramPrinter.SQUARE_SIZE);
            }
        }

        // Draw grid and numbers
        BufferedImage drawing = NonogramPrinter.drawNonogram(puzzle);

        int x = getWidth() - drawing.getWidth();
        int y = getHeight() - drawing.getHeight();
        g.drawImage(drawing, x, y, this);
    }

    private Color getColorForSquare(int i, int j) {
        switch (puzzleState[i][j]) {
            case EMPTY:
                return EMPTY_COLOUR;
            case FILLED:
                return FILLED_COLOUR;
            default:
                return UNKNOWN_COLOUR;
        }
    }

    private int getGridRow(int y) {
        return (y - gridTopY) / NonogramPrinter.SQUARE_SIZE;
    }

    private int getGridCol(int x) {
        return (x - gridLeftX) / NonogramPrinter.SQUARE_SIZE;
    }
}
