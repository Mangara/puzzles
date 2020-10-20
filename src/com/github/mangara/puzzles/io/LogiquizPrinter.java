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
package com.github.mangara.puzzles.io;

import com.github.mangara.puzzles.data.Logiquiz;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;

public class LogiquizPrinter {

    private static final int SQUARE_SIZE = 28;
    private static final int PADDING = 5;
    private static final float FONT_SIZE = 18f;
    private static final String DEFAULT_FONT = "Roboto";
    
    private static Font font = null;
    
    static {
        // Load the correct font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        boolean found = false;

        for (Font f : fonts) {
            if (f.getName().equals(DEFAULT_FONT)) {
                font = f.deriveFont(FONT_SIZE);
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.err.printf("Font \"%s\" was not found.%n", DEFAULT_FONT);
        }
    }
    
    public static void printLogiquiz(Logiquiz puzzle, Path outputFile) throws IOException {
        BufferedImage image = drawLogiquiz(puzzle);
        ImageIO.write(image, "png", outputFile.toFile());
    }

    public static BufferedImage drawLogiquiz(Logiquiz puzzle) {
        List<List<String>> groups = puzzle.getGroups();
        int groupSize = groups.get(0).size();
        List<List<String>> leftGroups = groups.subList(0, groups.size() - 1);
        List<List<String>> topGroups = new ArrayList<>(groups.subList(1, groups.size()));
        Collections.reverse(topGroups);

        // Compute side and top margins for text
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG = temp.createGraphics();
        tempG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int textHeight = (int) Math.ceil(FontDimensions.computeMaxTextDimension(topGroups.stream().flatMap(Collection::stream)::iterator, tempG.getFontRenderContext(), font).getWidth());
        int textWidth = (int) Math.ceil(FontDimensions.computeMaxTextDimension(leftGroups.stream().flatMap(Collection::stream)::iterator, tempG.getFontRenderContext(), font).getWidth());

        // Compute full image size
        int gridTop = textHeight + 2 * PADDING;
        int gridBottom = gridTop + leftGroups.size() * groupSize * SQUARE_SIZE;
        int gridLeft = textWidth + 2 * PADDING;
        int gridRight = gridLeft + topGroups.size() * groupSize * SQUARE_SIZE;

        BufferedImage result = new BufferedImage(gridRight + 2 * PADDING, gridBottom + 2 * PADDING, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fill background
        g.setColor(Color.white);
        g.fillRect(0, 0, result.getWidth(), result.getHeight());

        // Draw grid lines
        g.setColor(Color.black);

        g.setStroke(new BasicStroke(2));
        g.drawLine(PADDING, gridTop, gridRight, gridTop); // Hor
        g.drawLine(gridLeft, PADDING, gridLeft, gridBottom); // Vert
        
        for (int i = 0; i < leftGroups.size(); i++) {
            int length = (topGroups.size() - i) * groupSize;
            
            for (int j = 0; j < groupSize; j++) {
                int n = i * groupSize + j + 1;
                
                if (j == groupSize - 1) {
                    g.setStroke(new BasicStroke(2));
                } else {
                    g.setStroke(new BasicStroke(1));
                }
                
                g.drawLine(PADDING, gridTop + n * SQUARE_SIZE, gridLeft + length * SQUARE_SIZE, gridTop + n * SQUARE_SIZE); // Hor
                g.drawLine(gridLeft + n * SQUARE_SIZE, PADDING, gridLeft + n * SQUARE_SIZE, gridTop + length * SQUARE_SIZE); // Vert
            }
        }

        // Draw side text
        for (int i = 0; i < leftGroups.size(); i++) {
            for (int j = 0; j < groupSize; j++) {
                String word = leftGroups.get(i).get(j);
                double bottom = gridTop + (i * groupSize + j + 1) * SQUARE_SIZE;
                
                TextLayout text = new TextLayout(word, font, g.getFontRenderContext());
                double h = text.getBounds().getHeight();
                double w = text.getBounds().getWidth();
                
                g.drawString(word, (int) Math.round(gridLeft - PADDING - w), (int) Math.round(bottom - (SQUARE_SIZE - h) / 2));
            }
        }
        
        // Draw top text rotated
        g.translate(gridLeft, gridTop - PADDING);
        g.rotate(-Math.PI / 2);
        
        for (int i = 0; i < topGroups.size(); i++) {
            for (int j = 0; j < groupSize; j++) {
                String word = topGroups.get(i).get(j);
                double bottom = (i * groupSize + j + 1) * SQUARE_SIZE;
                
                TextLayout text = new TextLayout(word, font, g.getFontRenderContext());
                double h = text.getBounds().getHeight();
                
                g.drawString(word, 0, (int) Math.round(bottom - (SQUARE_SIZE - h) / 2));
            }
        }

        return result;
    }
}
