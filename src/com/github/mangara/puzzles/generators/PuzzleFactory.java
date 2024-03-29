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
package com.github.mangara.puzzles.generators;

import com.github.mangara.puzzles.generators.logiquiz.LogiquizGenerator;
import com.github.mangara.puzzles.generators.sudoku.SudokuGenerator;
import com.github.mangara.puzzles.generators.nonogram.NonogramGenerator;
import com.github.mangara.puzzles.data.logiquiz.CreateLogiquizSettings;
import com.github.mangara.puzzles.data.nonogram.CreateNonogramSettings;
import com.github.mangara.puzzles.data.CreatePuzzleSettings;
import com.github.mangara.puzzles.data.sudoku.CreateSudokuSettings;
import com.github.mangara.puzzles.data.Puzzle;

public class PuzzleFactory {

    public static Puzzle create(CreatePuzzleSettings settings) {
        switch (settings.getType()) {
            case NONOGRAM:
                return NonogramGenerator.create((CreateNonogramSettings) settings);
            case LOGIQUIZ:
                return LogiquizGenerator.create((CreateLogiquizSettings) settings);
            case SUDOKU:
                return SudokuGenerator.create((CreateSudokuSettings) settings);
            default:
                throw new IllegalArgumentException("Unknown puzzle type: " + settings.getType());
        }
    }
}
