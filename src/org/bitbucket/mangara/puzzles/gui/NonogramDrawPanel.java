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
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import org.bitbucket.mangara.puzzles.generators.NonogramGenerator;

public class NonogramDrawPanel extends JPanel implements MouseInputListener {

    private static final Color FILLED_COLOUR = new Color(16, 16, 16);
    private static final Color EMPTY_COLOUR = Color.white;
    private static final Color UNKNOWN_COLOUR = Color.lightGray;

    private enum PuzzleState {
        UNKNOWN, EMPTY, FILLED;
    }

    private boolean building = true;
    private boolean[][] nonogramResult = new boolean[8][12];
    private PuzzleState[][] puzzle = new PuzzleState[8][12];
    private int gridLeftX, gridTopY;

    public NonogramDrawPanel() {
        setPreferredSize(new Dimension(1200, 800));
        addMouseListener(this);
        addMouseMotionListener(this);

        nonogramResult[1][2] = true;
        nonogramResult[1][3] = true;
        nonogramResult[3][2] = true;
        
        for (PuzzleState[] row : puzzle) {
            Arrays.fill(row, PuzzleState.UNKNOWN);
        }
    }

    public int getNonogramWidth() {
        return nonogramResult[0].length;
    }

    public int getNonogramHeight() {
        return nonogramResult.length;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
        repaint();
    }

    public void newNonogram(int width, int height) {
        nonogramResult = new boolean[height][width];
        puzzle = new PuzzleState[height][width];
        for (PuzzleState[] row : puzzle) {
            Arrays.fill(row, PuzzleState.UNKNOWN);
        }
        repaint();
    }

    public void saveNonogram(File outputFile) throws IOException {
        List<List<Integer>> side = NonogramGenerator.computeSideNumbers(nonogramResult);
        List<List<Integer>> top = NonogramGenerator.computeTopNumbers(nonogramResult);
        BufferedImage image = NonogramGenerator.drawNonogram(side, top);
        ImageIO.write(image, "png", outputFile);
    }

    public void clear() {
        if (building) {
            for (boolean[] row : nonogramResult) {
                Arrays.fill(row, false);
            }
        } else {
            for (PuzzleState[] row : puzzle) {
                Arrays.fill(row, PuzzleState.UNKNOWN);
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Fill squares
        gridLeftX = getWidth() - NonogramGenerator.OUTER_PADDING - getNonogramWidth() * NonogramGenerator.SQUARE_SIZE;
        gridTopY = getHeight() - NonogramGenerator.OUTER_PADDING - getNonogramHeight() * NonogramGenerator.SQUARE_SIZE;

        for (int i = 0; i < nonogramResult.length; i++) {
            for (int j = 0; j < nonogramResult[i].length; j++) {
                int gridX = gridLeftX + j * NonogramGenerator.SQUARE_SIZE;
                int gridY = gridTopY + i * NonogramGenerator.SQUARE_SIZE;

                g.setColor(getColorForSquare(i, j));

                g.fillRect(gridX, gridY, NonogramGenerator.SQUARE_SIZE, NonogramGenerator.SQUARE_SIZE);
            }
        }

        // Draw grid and numbers
        List<List<Integer>> sideNumbers = NonogramGenerator.computeSideNumbers(nonogramResult);
        List<List<Integer>> topNumbers = NonogramGenerator.computeTopNumbers(nonogramResult);
        BufferedImage drawing = NonogramGenerator.drawNonogram(sideNumbers, topNumbers);

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
        int i = getGridRow(e.getY());
        int j = getGridCol(e.getX());

        if (i < 0 || i > getNonogramHeight() || j < 0 || j > getNonogramWidth()) {
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
        int i = getGridRow(e.getY());
        int j = getGridCol(e.getX());

        if (i < 0 || i > getNonogramHeight() || j < 0 || j > getNonogramWidth()) {
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
        return (y - gridTopY) / NonogramGenerator.SQUARE_SIZE;
    }

    private int getGridCol(int x) {
        return (x - gridLeftX) / NonogramGenerator.SQUARE_SIZE;
    }
}
