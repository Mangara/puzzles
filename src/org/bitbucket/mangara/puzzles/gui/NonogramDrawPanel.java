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
import javax.swing.event.MouseInputListener;
import org.bitbucket.mangara.puzzles.generators.Nonogram;
import static org.bitbucket.mangara.puzzles.generators.Nonogram.computeSideNumbers;
import static org.bitbucket.mangara.puzzles.generators.Nonogram.computeTopNumbers;
import static org.bitbucket.mangara.puzzles.generators.Nonogram.drawNonogram;

public class NonogramDrawPanel extends JPanel implements MouseInputListener {

    private boolean building = true;
    private boolean[][] nonogramResult = new boolean[8][12];
    private boolean[][] puzzle = new boolean[8][12];
    private int gridLeftX, gridTopY;

    public NonogramDrawPanel() {
        setPreferredSize(new Dimension(1200, 800));
        addMouseListener(this);
        addMouseMotionListener(this);
        
        nonogramResult[1][2] = true;
        nonogramResult[1][3] = true;
        nonogramResult[3][2] = true;
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
        puzzle = new boolean[height][width];
        repaint();
    }
    
    public void saveNonogram(File outputFile) throws IOException {
        List<List<Integer>> side = Nonogram.computeSideNumbers(nonogramResult);
        List<List<Integer>> top = Nonogram.computeTopNumbers(nonogramResult);
        BufferedImage image = Nonogram.drawNonogram(side, top);
        ImageIO.write(image, "png", outputFile);
    }

    public void clear() {
        if (building) {
            for (boolean[] row : nonogramResult) {
                Arrays.fill(row, false);
            }
        } else {
            for (boolean[] row : puzzle) {
                Arrays.fill(row, false);
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        List<List<Integer>> sideNumbers = Nonogram.computeSideNumbers(nonogramResult);
        List<List<Integer>> topNumbers = Nonogram.computeTopNumbers(nonogramResult);
        BufferedImage drawing = Nonogram.drawNonogram(sideNumbers, topNumbers);

        int x = getWidth() - drawing.getWidth();
        int y = getHeight() - drawing.getHeight();

        g.drawImage(drawing, x, y, this);
        
        g.setColor(Color.black);
        gridLeftX = getWidth() - Nonogram.OUTER_PADDING - getNonogramWidth() * Nonogram.SQUARE_SIZE;
        gridTopY = getHeight() - Nonogram.OUTER_PADDING - getNonogramHeight() * Nonogram.SQUARE_SIZE;
        fillSquares(g, building ? nonogramResult : puzzle);
    }
    
    private void fillSquares(Graphics g, boolean[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j]) {
                    int x = gridLeftX + j * Nonogram.SQUARE_SIZE;
                    int y = gridTopY + i * Nonogram.SQUARE_SIZE;
                    g.fillRect(x, y, Nonogram.SQUARE_SIZE, Nonogram.SQUARE_SIZE);
                }
            }
        }
    }

    private boolean fillOnDrag = true;
    
    @Override
    public void mousePressed(MouseEvent e) {
        int i = getGridRow(e.getY());
        int j = getGridCol(e.getX());
        
        if (i < 0 || i > getNonogramHeight() || j < 0 || j > getNonogramWidth()) {
            return;
        }
        
        boolean[][] grid = building ? nonogramResult : puzzle;
        
        fillOnDrag = !grid[i][j];
        grid[i][j] = fillOnDrag;
        
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int i = getGridRow(e.getY());
        int j = getGridCol(e.getX());
        
        if (i < 0 || i > getNonogramHeight() || j < 0 || j > getNonogramWidth()) {
            return;
        }
        
        boolean[][] grid = building ? nonogramResult : puzzle;
        grid[i][j] = fillOnDrag;
        
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
        return (y - gridTopY) / Nonogram.SQUARE_SIZE;
    }
    
    private int getGridCol(int x) {
        return (x - gridLeftX) / Nonogram.SQUARE_SIZE;
    }
}
