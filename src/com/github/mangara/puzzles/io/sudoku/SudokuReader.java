/*
 * Copyright 2023 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SudokuReader {

    /**
     * Checks whether the given file is likely to be a Sudoku.
     *
     * Only reads the first line, checking for the text "type=sudoku".
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isSudoku(Path file) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line = in.readLine();

            if (line == null || !line.contains("=")) {
                return false;
            }

            String[] parts = line.split("=");

            return parts.length == 2 && parts[0].trim().equals("type") && parts[1].trim().equals("sudoku");
        }
    }

    /**
     * Reads the Sudoku from the given file.Assumes that the file contains a
     * Sudoku.
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public static Sudoku readSudoku(Path file) throws IOException {
        int[][] digits = readDigits(file);
        return new Sudoku(digits);
    }

    private static int[][] readDigits(Path file) throws IOException {
        int[][] digits = new int[9][9];

        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            int row = 0;

            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (line.isBlank() || line.contains("=")) {
                    continue;
                }

                parseRow(line, row, digits);
                row++;
            }
        }

        return digits;
    }

    private static void parseRow(String line, int row, int[][] digits) throws IOException {
        if (line.length() != 9) {
            throw new IOException("Invalid Sudoku: expected a row of 9 digits, found \"" + line + "\".");
        }

        for (int col = 0; col < 9; col++) {
            digits[row][col] = digitFromChar(line.charAt(col));
        }
    }

    private static int digitFromChar(char c) {
        if (c == '.') {
            return SudokuSolutionState.BLANK;
        } else {
            return c - '0';
        }
    }

}
