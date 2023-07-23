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
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SudokuWriter {

    public static void save(Sudoku sudoku, Path file) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file)) {
            writeType(out);
            writeDigits(sudoku.getGivenDigits(), out);
        }
    }
    
    private static void writeType(BufferedWriter out) throws IOException {
        out.write("type=sudoku");
        out.newLine();
    }

    private static void writeDigits(int[][] digits, BufferedWriter out) throws IOException {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                out.write(charFor(digits[row][col]));
            }
            out.newLine();
        }
    }

    private static String charFor(int digit) {
        if (digit == SudokuSolutionState.BLANK) {
            return ".";
        } else {
            return Integer.toString(digit);
        }
    }
}
