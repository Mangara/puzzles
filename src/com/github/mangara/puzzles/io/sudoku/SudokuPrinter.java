/*
 * Copyright 2023 Sander Verdonschot.
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

package com.github.mangara.puzzles.io.sudoku;

import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.io.FontDimensions;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class SudokuPrinter {

    // TODO: increase resolution
    public static final int SQUARE_SIZE = 48;
    public static final int OUTER_PADDING = 10;
    
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 0);
    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final String DEFAULT_FONT = "Roboto";
    private static final float DEFAULT_FONT_SIZE = 36f;
    
    private static Font font = null;
    
    static {
        // Load the correct font
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        boolean found = false;

        for (Font f : fonts) {
            if (f.getName().equals(DEFAULT_FONT)) {
                font = f.deriveFont(DEFAULT_FONT_SIZE);
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.err.printf("Font \"%s\" was not found.%n", DEFAULT_FONT);
            font = new Font("SansSerif", Font.PLAIN, Math.round(DEFAULT_FONT_SIZE));
        }
    }
    
    public static void printSudoku(Sudoku puzzle, Path outputFile) throws IOException {
        BufferedImage image = drawSudoku(puzzle);
        ImageIO.write(image, "png", outputFile.toFile());
    }
    
    public static BufferedImage drawSudoku(Sudoku puzzle) {
        return drawSudoku(puzzle, true, DEFAULT_COLOR);
    }
    
    public static BufferedImage drawSudoku(Sudoku puzzle, boolean drawGrid, Color color) {
        int size = 9 * SQUARE_SIZE + 2 * OUTER_PADDING;
        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fill background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, size, size);

        g.setColor(color);
        
        if (drawGrid) {
            // Draw grid lines
            for (int i = 0; i < 10; i++) {
                g.drawLine(OUTER_PADDING + i * SQUARE_SIZE, OUTER_PADDING, OUTER_PADDING + i * SQUARE_SIZE, size - OUTER_PADDING);
            }

            for (int i = 0; i < 10; i++) {
                g.drawLine(OUTER_PADDING, OUTER_PADDING + i * SQUARE_SIZE, size - OUTER_PADDING, OUTER_PADDING + i * SQUARE_SIZE);
            }

            // Emphasize box grid lines
            g.setStroke(new BasicStroke(3));

            for (int i = 0; i < 4; i++) {
                g.drawLine(OUTER_PADDING + 3 * i * SQUARE_SIZE, OUTER_PADDING, OUTER_PADDING + 3 * i * SQUARE_SIZE, size - OUTER_PADDING);
            }

            for (int i = 0; i < 4; i++) {
                g.drawLine(OUTER_PADDING, OUTER_PADDING + 3 * i * SQUARE_SIZE, size - OUTER_PADDING, OUTER_PADDING + 3 * i * SQUARE_SIZE);
            }
        }
        
        // Draw numbers
        FontRenderContext frc = g.getFontRenderContext();
        int[][] digits = puzzle.getGivenDigits();
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int digit = digits[row][col];
                
                if (digit != BLANK) {
                    String text = Integer.toString(digit);
                    int width = FontDimensions.getWidth(text, frc, font);
                    int height = FontDimensions.getHeight(text, frc, font);
                    
                    int x = OUTER_PADDING + col * SQUARE_SIZE + (SQUARE_SIZE - width) / 2;
                    int y = OUTER_PADDING + row * SQUARE_SIZE + SQUARE_SIZE - (SQUARE_SIZE - height) / 2;
                    
                    g.drawString(text, x, y);
                }
            }
        }
        
        return result;
    }

}
