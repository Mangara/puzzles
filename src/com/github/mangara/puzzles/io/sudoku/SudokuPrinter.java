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
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import com.github.mangara.puzzles.io.FontDimensions;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
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
    
    // Derived values
    private static final int SIZE = 9 * SQUARE_SIZE + 2 * OUTER_PADDING;
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
        BufferedImage result = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = initGraphics(result);

        fillBackground(g);

        if (drawGrid) {
            drawGrid(g, color);
        }
        
        drawDigits(g, puzzle.getGivenDigits(), color);
        
        return result;
    }

    public static BufferedImage drawSolvingSudoku(SolvingSudoku puzzle, boolean drawGrid, Color baseColor, Color guessColor, Color possibleColor) {
        BufferedImage result = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = initGraphics(result);
        
        fillBackground(g);
        
        if (drawGrid) {
            drawGrid(g, baseColor);
        }
        
        drawDigits(g, puzzle.getGivenDigits(), baseColor);
        drawDigits(g, puzzle.getGuessedDigits(), guessColor);
        
        drawPossibleDigits(g, puzzle.state, possibleColor);
        
        // TODO: draw pencilmarks
        
        return result;
    }

    private static Graphics2D initGraphics(BufferedImage result) {
        Graphics2D g = result.createGraphics();
        
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        return g;
    }
    
    private static void fillBackground(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, SIZE, SIZE);
    }
    
    private static void drawGrid(Graphics2D g, Color color) {
        g.setColor(color);
        
        // Draw grid lines
        for (int i = 0; i < 10; i++) {
            g.drawLine(OUTER_PADDING + i * SQUARE_SIZE, OUTER_PADDING, OUTER_PADDING + i * SQUARE_SIZE, SIZE - OUTER_PADDING);
        }

        for (int i = 0; i < 10; i++) {
            g.drawLine(OUTER_PADDING, OUTER_PADDING + i * SQUARE_SIZE, SIZE - OUTER_PADDING, OUTER_PADDING + i * SQUARE_SIZE);
        }

        // Emphasize box grid lines
        g.setStroke(new BasicStroke(3));

        for (int i = 0; i < 4; i++) {
            g.drawLine(OUTER_PADDING + 3 * i * SQUARE_SIZE, OUTER_PADDING, OUTER_PADDING + 3 * i * SQUARE_SIZE, SIZE - OUTER_PADDING);
        }

        for (int i = 0; i < 4; i++) {
            g.drawLine(OUTER_PADDING, OUTER_PADDING + 3 * i * SQUARE_SIZE, SIZE - OUTER_PADDING, OUTER_PADDING + 3 * i * SQUARE_SIZE);
        }
    }
    
    private static void drawDigits(Graphics2D g, int[][] digits, Color color) {
        g.setFont(font);
        FontRenderContext frc = g.getFontRenderContext();
        
        g.setColor(color);
        
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
    }

    private static void drawPossibleDigits(Graphics2D g, SudokuSolutionState[][] fullState, Color color) {
        FontRenderContext frc = g.getFontRenderContext();
        
        g.setColor(color);
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                SudokuSolutionState state = fullState[row][col];
                
                if (state.digit == BLANK) {
                    String text = possibleString(state);
                    if (text.isBlank()) {
                        // Sudoku is broken, but trying to draw this would crash
                        continue;
                    }
                    
                    float fontSize = DEFAULT_FONT_SIZE * 0.6f;
                    Font f = font.deriveFont(fontSize);
                    
                    int width = FontDimensions.getWidth(text, frc, font);
                    
                    while (width > SQUARE_SIZE - 3) {
                        fontSize -= 1.0f;
                        f = font.deriveFont(fontSize);
                        
                        width = FontDimensions.getWidth(text, frc, f);
                    }
                    
                    int height = FontDimensions.getHeight(text, frc, font);

                    int x = OUTER_PADDING + col * SQUARE_SIZE + (SQUARE_SIZE - width) / 2;
                    int y = OUTER_PADDING + row * SQUARE_SIZE + SQUARE_SIZE - (SQUARE_SIZE - height) / 2;

                    g.setFont(f);
                    g.drawString(text, x, y);
                }
            }
        }
    }

    private static String possibleString(SudokuSolutionState state) {
        StringBuilder sb = new StringBuilder();
        
        for (int d = 1; d <= 9; d++) {
            if (state.isPossible(d)) {
                sb.append(Integer.toString(d));
            }
        }
        
        return sb.toString();
    }
}
