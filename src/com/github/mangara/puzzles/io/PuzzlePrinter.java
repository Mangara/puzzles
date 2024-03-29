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

import com.github.mangara.puzzles.io.logiquiz.LogiquizPrinter;
import com.github.mangara.puzzles.io.sudoku.SudokuPrinter;
import com.github.mangara.puzzles.io.nonogram.NonogramPrinter;
import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import com.github.mangara.puzzles.data.nonogram.Nonogram;
import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import java.io.IOException;
import java.nio.file.Path;

public class PuzzlePrinter {

    public static void print(Puzzle puzzle, Path outputFile) throws IOException {
        switch (puzzle.getType()) {
            case NONOGRAM:
                NonogramPrinter.printNonogram((Nonogram) puzzle, outputFile);
                break;
            case LOGIQUIZ:
                LogiquizPrinter.printLogiquiz((Logiquiz) puzzle, outputFile);
                break;
            case SUDOKU:
                SudokuPrinter.printSudoku((Sudoku) puzzle, outputFile);
                break;
            default:
                throw new IllegalArgumentException("Unknown puzzle type: " + puzzle.getType());
        }
    }
}
