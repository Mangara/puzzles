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
import com.github.mangara.puzzles.generators.LogiquizGenerator;
import com.github.mangara.puzzles.gui.PuzzlePanel.InteractionMode;
import com.github.mangara.puzzles.io.LogiquizPrinter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class LogiquizDrawPanel extends JPanel implements MouseListener {

    private InteractionMode interactionMode;
    private Logiquiz puzzle;
    
    public LogiquizDrawPanel() {
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        
        puzzle = LogiquizGenerator.create(new CreateLogiquizSettings(3, 2));
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
        repaint();
    }
    
    public void clear() {
        CreateLogiquizSettings settings = new CreateLogiquizSettings(puzzle.getGroupCount(), puzzle.getGroupSize());
        puzzle = LogiquizGenerator.create(settings);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw grid and numbers
        BufferedImage drawing = LogiquizPrinter.drawLogiquiz(puzzle);

        int x = getWidth() - drawing.getWidth();
        int y = getHeight() - drawing.getHeight();
        g.drawImage(drawing, x, y, this);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
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
