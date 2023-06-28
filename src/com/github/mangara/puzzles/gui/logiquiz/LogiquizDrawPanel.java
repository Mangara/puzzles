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
package com.github.mangara.puzzles.gui.logiquiz;

import com.github.mangara.puzzles.data.logiquiz.CreateLogiquizSettings;
import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import com.github.mangara.puzzles.data.logiquiz.LogiquizSolutionState;
import com.github.mangara.puzzles.generators.logiquiz.LogiquizGenerator;
import com.github.mangara.puzzles.gui.PuzzlePanel.InteractionMode;
import com.github.mangara.puzzles.io.logiquiz.LogiquizPrinter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LogiquizDrawPanel extends JPanel implements MouseListener {

    private InteractionMode interactionMode;
    private Logiquiz puzzle;
    private LogiquizSolutionState[][] solution;
    private int totalGridSize;

    public LogiquizDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);

        puzzle = LogiquizGenerator.create(new CreateLogiquizSettings(3, 2));
        clearSolution();
    }

    public InteractionMode getInteractionMode() {
        return interactionMode;
    }

    public void setInteractionMode(InteractionMode interactionMode) {
        this.interactionMode = interactionMode;
    }

    public Logiquiz getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Logiquiz puzzle) {
        this.puzzle = puzzle;
        clearSolution();
        repaint();
    }

    public void clear() {
        CreateLogiquizSettings settings = new CreateLogiquizSettings(puzzle.getGroupCount(), puzzle.getGroupSize());
        puzzle = LogiquizGenerator.create(settings);
        clearSolution();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        clearDrawingArea(g);
        drawSolution(g);
        drawPuzzle(g);
    }

    private void clearDrawingArea(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawSolution(Graphics g) {
        int gridLeftX = getWidth() - 2 * LogiquizPrinter.PADDING - totalGridSize * LogiquizPrinter.SQUARE_SIZE;
        int gridTopY = getHeight() - 2 * LogiquizPrinter.PADDING - totalGridSize * LogiquizPrinter.SQUARE_SIZE;

        for (int i = 0; i < totalGridSize; i++) {
            for (int j = 0; j < totalGridSize; j++) {
                int gridX = gridLeftX + i * LogiquizPrinter.SQUARE_SIZE;
                int gridY = gridTopY + j * LogiquizPrinter.SQUARE_SIZE;

                drawSolutionSquare(g, gridX, gridY, solution[i][j]);
            }
        }
    }

    private void drawPuzzle(Graphics g) {
        BufferedImage drawing = LogiquizPrinter.drawLogiquiz(puzzle, false);

        int x = getWidth() - drawing.getWidth();
        int y = getHeight() - drawing.getHeight();
        g.drawImage(drawing, x, y, this);
    }

    private void drawSolutionSquare(Graphics g, int gridX, int gridY, LogiquizSolutionState state) {
        switch (state) {
            case UNKNOWN:
                drawUnknownSolutionSquare(g, gridX, gridY);
                break;
            case NEGATIVE:
                drawNegativeSolutionSquare(g, gridX, gridY);
                break;
            case POSITIVE:
                drawPositiveSolutionSquare(g, gridX, gridY);
                break;
        }
    }

    private void drawUnknownSolutionSquare(Graphics g, int gridX, int gridY) {
        // Draw nothing
    }

    private void drawNegativeSolutionSquare(Graphics g, int gridX, int gridY) {
        g.setColor(Color.RED);
        
        int BAR_WIDTH = 5;
        int leftX = gridX + 1 + BAR_WIDTH;
        int topY = gridY + 1 + ((LogiquizPrinter.SQUARE_SIZE - BAR_WIDTH) / 2);
        
        g.fillRect(leftX, topY, LogiquizPrinter.SQUARE_SIZE - 1 - 2 * BAR_WIDTH, BAR_WIDTH);
    }

    private void drawPositiveSolutionSquare(Graphics g, int gridX, int gridY) {
        g.setColor(Color.GREEN);
        
        int BAR_WIDTH = 5;
        
        // Horizontal bar
        int horLeftX = gridX + 1 + BAR_WIDTH;
        int horTopY = gridY + 1 + ((LogiquizPrinter.SQUARE_SIZE - BAR_WIDTH) / 2);
        
        g.fillRect(horLeftX, horTopY, LogiquizPrinter.SQUARE_SIZE - 1 - 2 * BAR_WIDTH, BAR_WIDTH);
        
        // Vertical bar
        int vertLeftX = gridX + 1 + ((LogiquizPrinter.SQUARE_SIZE - BAR_WIDTH) / 2);
        int vertTopY = gridY + 1 + BAR_WIDTH;
        
        g.fillRect(vertLeftX, vertTopY, BAR_WIDTH, LogiquizPrinter.SQUARE_SIZE - 1 - 2 * BAR_WIDTH);
    }

    private void clearSolution() {
        totalGridSize = (puzzle.getGroupCount() - 1) * puzzle.getGroupSize();
        solution = new LogiquizSolutionState[totalGridSize][totalGridSize];

        for (LogiquizSolutionState[] row : solution) {
            Arrays.fill(row, LogiquizSolutionState.UNKNOWN);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (interactionMode == InteractionMode.BUILDING) {
            return;
        }
        
        int i = getGridCol(e.getX());
        int j = getGridRow(e.getY());

        if (i < 0 || i > totalGridSize || j < 0 || j > totalGridSize) {
            return;
        }
        // TODO: Return for out of staircase clicks

        boolean rightClick = SwingUtilities.isRightMouseButton(e);
        LogiquizSolutionState paintState;

        switch (solution[i][j]) {
            case UNKNOWN:
                paintState = rightClick ? LogiquizSolutionState.POSITIVE : LogiquizSolutionState.NEGATIVE;
                break;
            case NEGATIVE:
                paintState = rightClick ? LogiquizSolutionState.UNKNOWN : LogiquizSolutionState.POSITIVE;
                break;
            case POSITIVE:
                paintState = rightClick ? LogiquizSolutionState.NEGATIVE : LogiquizSolutionState.UNKNOWN;
                break;
            default:
                throw new InternalError("Illegal logiquiz solution state: " + solution[i][j]);
        }

        solution[i][j] = paintState;

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
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

    private int getGridCol(int x) {
        int gridLeftX = getWidth() - 2 * LogiquizPrinter.PADDING - totalGridSize * LogiquizPrinter.SQUARE_SIZE;
        return (x - gridLeftX) / LogiquizPrinter.SQUARE_SIZE;
    }
    
    private int getGridRow(int y) {
        int gridTopY = getHeight() - 2 * LogiquizPrinter.PADDING - totalGridSize * LogiquizPrinter.SQUARE_SIZE;
        return (y - gridTopY) / LogiquizPrinter.SQUARE_SIZE;
    }
}
