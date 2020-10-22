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
package com.github.mangara.puzzles.gui;

import com.github.mangara.puzzles.data.CreateLogiquizSettings;
import com.github.mangara.puzzles.data.Logiquiz;
import com.github.mangara.puzzles.data.LogiquizSolutionState;
import com.github.mangara.puzzles.generators.LogiquizGenerator;
import com.github.mangara.puzzles.gui.PuzzlePanel.InteractionMode;
import com.github.mangara.puzzles.io.LogiquizPrinter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.JPanel;

public class LogiquizDrawPanel extends JPanel implements MouseListener {

    private InteractionMode interactionMode;
    private Logiquiz puzzle;
    private LogiquizSolutionState[][] solution;

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
        int totalGridSize = (puzzle.getGroupCount() - 1) * puzzle.getGroupSize();
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
        g.setColor(Color.GRAY);
        g.fillRect(gridX, gridY, LogiquizPrinter.SQUARE_SIZE, LogiquizPrinter.SQUARE_SIZE);
    }

    private void drawNegativeSolutionSquare(Graphics g, int gridX, int gridY) {
        g.setColor(Color.RED);
        g.fillRect(gridX, gridY, LogiquizPrinter.SQUARE_SIZE, LogiquizPrinter.SQUARE_SIZE);
    }

    private void drawPositiveSolutionSquare(Graphics g, int gridX, int gridY) {
        g.setColor(Color.GREEN);
        g.fillRect(gridX, gridY, LogiquizPrinter.SQUARE_SIZE, LogiquizPrinter.SQUARE_SIZE);
    }

    private void clearSolution() {
        int totalGridSize = (puzzle.getGroupCount() - 1) * puzzle.getGroupSize();
        solution = new LogiquizSolutionState[totalGridSize][totalGridSize];

        for (LogiquizSolutionState[] row : solution) {
            Arrays.fill(row, LogiquizSolutionState.UNKNOWN);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO
        solution[0][0] = (solution[0][0] == LogiquizSolutionState.UNKNOWN) ? LogiquizSolutionState.POSITIVE : LogiquizSolutionState.UNKNOWN;
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

}
