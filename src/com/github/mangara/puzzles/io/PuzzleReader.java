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

import com.github.mangara.puzzles.io.logiquiz.LogiquizReader;
import com.github.mangara.puzzles.io.nonogram.NonogramReader;
import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.io.sudoku.SudokuReader;
import java.io.IOException;
import java.nio.file.Path;

public class PuzzleReader {
    
    public static Puzzle read(Path inputFile) throws IOException {
        if (SudokuReader.isSudoku(inputFile)) {
            return SudokuReader.readSudoku(inputFile);
        } else if (NonogramReader.isNonogram(inputFile)) { // Nonograms are easier to recognize, so check those first
            return NonogramReader.readNonogram(inputFile);
        } else if (LogiquizReader.isLogiquiz(inputFile)) {
            return LogiquizReader.readLogiquiz(inputFile);
        } else {
            throw new IOException("Puzzle could not be read.");
        }
    }
}
