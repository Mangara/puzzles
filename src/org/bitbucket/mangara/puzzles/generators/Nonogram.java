/*
 * Copyright 2016 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
package org.bitbucket.mangara.puzzles.generators;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.bitbucket.mangara.puzzles.tools.FontDimensions;

/**
 * @author Sander Verdonschot <sander.verdonschot at gmail.com>
 */
public class Nonogram {

    private static final String inputFile = "in.txt";
    private static final String outputFile = "out.png";
    private static final String fontName = "Roboto";

    public static final int SQUARE_SIZE = 28;
    public static final int OUTER_PADDING = 5;
    public static final int VERTICAL_PADDING = 5;
    public static final int HORIZONTAL_PADDING = 5;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0);
    private static final Color FOREGROUND_COLOR = Color.BLACK;

    private static final Set<Character> filledChars;

    static {
        filledChars = new HashSet<>();
        filledChars.add('X');
    }

    private static Font font = null;

    static {
        // Load the correct font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();

        for (Font f : fonts) {
            if (f.getName().equals(fontName)) {
                font = f.deriveFont(22f);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // Create the nonogram
        boolean[][] drawing = readFile(inputFile);
        List<List<Integer>> side = computeSideNumbers(drawing);
        List<List<Integer>> top = computeTopNumbers(drawing);
        BufferedImage image = drawNonogram(side, top);
        ImageIO.write(image, "png", new File(outputFile));
    }

    private static boolean[][] readFile(String inputFile) throws IOException {
        List<boolean[]> result = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                boolean[] drawingLine = new boolean[line.length()];

                for (int i = 0; i < line.length(); i++) {
                    drawingLine[i] = filledChars.contains(line.charAt(i));
                }

                result.add(drawingLine);
            }
        }

        return result.toArray(new boolean[result.size()][result.get(0).length]);
    }

    public static List<List<Integer>> computeSideNumbers(boolean[][] drawing) {
        List<List<Integer>> result = new ArrayList<>();

        for (boolean[] row : drawing) {
            List<Integer> resultRow = new ArrayList<>();
            int filledCount = 0;

            for (int i = 0; i < row.length; i++) {
                if (row[i]) {
                    filledCount++;
                } else if (filledCount > 0) {
                    resultRow.add(filledCount);
                    filledCount = 0;
                }
            }

            if (filledCount > 0) {
                resultRow.add(filledCount);
            } else if (resultRow.isEmpty()) {
                resultRow.add(0);
            }

            result.add(resultRow);
        }

        return result;
    }

    public static List<List<Integer>> computeTopNumbers(boolean[][] drawing) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < drawing[0].length; i++) {
            List<Integer> resultColumn = new ArrayList<>();
            int filledCount = 0;

            for (int j = 0; j < drawing.length; j++) {
                if (drawing[j][i]) {
                    filledCount++;
                } else if (filledCount > 0) {
                    resultColumn.add(filledCount);
                    filledCount = 0;
                }
            }

            if (filledCount > 0) {
                resultColumn.add(filledCount);
            } else if (resultColumn.isEmpty()) {
                resultColumn.add(0);
            }

            result.add(resultColumn);
        }

        return result;
    }

    public static BufferedImage drawNonogram(List<List<Integer>> side, List<List<Integer>> top) {
        int gridWidth = top.size();
        int gridHeight = side.size();

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
