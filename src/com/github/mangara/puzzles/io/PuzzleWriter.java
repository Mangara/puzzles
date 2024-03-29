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

import com.github.mangara.puzzles.io.sudoku.SudokuWriter;
import com.github.mangara.puzzles.io.logiquiz.LogiquizWriter;
import com.github.mangara.puzzles.io.nonogram.NonogramWriter;
import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.nonogram.SolvedNonogram;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import java.io.IOException;
import java.nio.file.Path;

public class PuzzleWriter {
    
    public static void write(Puzzle puzzle, Path outputFile) throws IOException {
        switch (puzzle.getType()) {
            case NONOGRAM:
                if (!(puzzle instanceof SolvedNonogram)) {
                    throw new IllegalArgumentException("Nonograms must have a solution to write.");
                }
                SolvedNonogram nonogram = (SolvedNonogram) puzzle;
                NonogramWriter.save(nonogram, outputFile);
                break;
            case LOGIQUIZ:
                if (!(puzzle instanceof Logiquiz)) {
                    throw new IllegalArgumentException("Incorrect Logiquiz type.");
                }
                Logiquiz logiquiz = (Logiquiz) puzzle;
                LogiquizWriter.save(logiquiz, outputFile);
                break;
            case SUDOKU:
                if (!(puzzle instanceof Sudoku)) {
                    throw new IllegalArgumentException("Incorrect Logiquiz type.");
                }
                Sudoku sudoku = (Sudoku) puzzle;
                SudokuWriter.save(sudoku, outputFile);
                break;
            default:
                throw new IllegalArgumentException("Unexpected puzzle type: " + puzzle.getType());
        }
    }
}
