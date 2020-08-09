/*
 * Copyright 2018 Sander Verdonschot <sander.verdonschot at gmail.com>.
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import com.github.mangara.puzzles.data.Nonogram;

public class NonogramPrinter {

    public static final int SQUARE_SIZE = 28;
    public static final int OUTER_PADDING = 5;
    public static final int VERTICAL_PADDING = 5;
    public static final int HORIZONTAL_PADDING = 10;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0);
    private static final Color FOREGROUND_COLOR = Color.BLACK;
    private static final String DEFAULT_FONT = "Roboto";
    
    private static Font font = null;
    
    static {
        // Load the correct font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        boolean found = false;

        for (Font f : fonts) {
            if (f.getName().equals(DEFAULT_FONT)) {
                font = f.deriveFont(22f);
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.err.printf("Font \"%s\" was not found.%n", DEFAULT_FONT);
        }
    }

    public static void printNonogram(Nonogram puzzle, Path outputFile) throws IOException {
        BufferedImage image = drawNonogram(puzzle);
        ImageIO.write(image, "png", outputFile.toFile());
    }
    
    public static BufferedImage drawNonogram(Nonogram puzzle) {
        List<List<Integer>> top = puzzle.getTopNumbers();
        List<List<Integer>> side = puzzle.getSideNumbers();
        int gridWidth = puzzle.getWidth();
        int gridHeight = puzzle.getHeight();

        // Compute side and top margins for text
        BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG = temp.createGraphics();
        tempG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int textHeight = (int) Math.ceil(computeMaxTextDimension(top, tempG.getFontRenderContext()).getHeight());
        int textWidth = (int) Math.ceil(computeMaxTextDimension(side, tempG.getFontRenderContext()).getWidth());

        if (textHeight > SQUARE_SIZE || textWidth > SQUARE_SIZE) {
            System.err.println("Text doesn't fit! Square: " + SQUARE_SIZE + " Max height: " + textHeight + " Max width: " + textWidth);
        }

        int topMargin = getMaxNumbers(top) * (textHeight + VERTICAL_PADDING) + VERTICAL_PADDING + OUTER_PADDING;
        int leftMargin = getMaxNumbers(side) * (textWidth + HORIZONTAL_PADDING) + HORIZONTAL_PADDING + OUTER_PADDING;

        // Compute full image size
        int gridTop = topMargin;
        int gridBottom = gridTop + gridHeight * SQUARE_SIZE;
        int gridLeft = leftMargin;
        int gridRight = gridLeft + gridWidth * SQUARE_SIZE;

        BufferedImage result = new BufferedImage(gridRight + OUTER_PADDING, gridBottom + OUTER_PADDING, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fill background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, gridRight, gridBottom);

        // Draw grid lines
        g.setColor(FOREGROUND_COLOR);

        for (int i = 0; i < gridWidth + 1; i++) {
            g.drawLine(gridLeft + i * SQUARE_SIZE, OUTER_PADDING, gridLeft + i * SQUARE_SIZE, gridBottom);
        }

        for (int i = 0; i < gridHeight + 1; i++) {
            g.drawLine(OUTER_PADDING, gridTop + i * SQUARE_SIZE, gridRight, gridTop + i * SQUARE_SIZE);
        }

        // Draw top numbers
        for (int i = 0; i < gridWidth; i++) {
            List<Integer> column = top.get(i);
            double bottom = gridTop - VERTICAL_PADDING;
            double left = gridLeft + i * SQUARE_SIZE;

            for (int j = 0; j < column.size(); j++) {
                Integer n = column.get(column.size() - 1 - j);
                TextLayout text = new TextLayout(n.toString(), font, g.getFontRenderContext());
                double h = text.getBounds().getHeight();
                double w = text.getBounds().getWidth();

                g.drawString(n.toString(), (int) Math.round(left + (SQUARE_SIZE - w) / 2), (int) Math.round(bottom - (textHeight - h) / 2));

                bottom -= textHeight + VERTICAL_PADDING;
            }
        }

        // Draw side numbers
        for (int i = 0; i < gridHeight; i++) {
            List<Integer> row = side.get(i);
            double left = gridLeft - HORIZONTAL_PADDING - textWidth;
            double bottom = gridTop + (i + 1) * SQUARE_SIZE;

            for (int j = 0; j < row.size(); j++) {
                Integer n = row.get(row.size() - 1 - j);
                TextLayout text = new TextLayout(n.toString(), font, g.getFontRenderContext());
                double h = text.getBounds().getHeight();
                double w = text.getBounds().getWidth();

                g.drawString(n.toString(), (int) Math.round(left + (textWidth - w) / 2), (int) Math.round(bottom - (SQUARE_SIZE - h) / 2));

                left -= textWidth + HORIZONTAL_PADDING;
            }
        }

        return result;
    }

    private static Rectangle2D computeMaxTextDimension(List<List<Integer>> numbers, FontRenderContext frc) {
        List<String> numbersAsText = numbers.stream()
                .flatMap(Collection::stream)
                .map(i -> i.toString())
                .collect(Collectors.toList());
        
        return FontDimensions.computeMaxTextDimension(numbersAsText, frc, font);
    }

    private static int getMaxNumbers(List<List<Integer>> numbers) {
        int max = 0;

        for (List<Integer> list : numbers) {
            max = Math.max(list.size(), max);
        }

        return max;
    }
}
