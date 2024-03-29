/*
 * Copyright 2020 Sander Verdonschot.
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
package com.github.mangara.puzzles.generators.sudoku;

import com.github.mangara.puzzles.data.sudoku.CreateSudokuSettings;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import java.util.Arrays;

public class SudokuGenerator {
    
    public static Sudoku create(CreateSudokuSettings settings) {
        return new Sudoku(blankGrid());
    }
    
    private static int[][] blankGrid() {
        int[][] result = new int[9][9];
        
        for (int i = 0; i < 9; i++) {
            Arrays.fill(result[i], SudokuSolutionState.BLANK);
        }
        
        return result;
    }
}
