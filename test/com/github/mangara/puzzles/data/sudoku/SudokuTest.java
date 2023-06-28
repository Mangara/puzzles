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
package com.github.mangara.puzzles.data.sudoku;

import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class SudokuTest {
    
    public SudokuTest() {
    }

    @Test
    public void testValidateSize() {
        new Sudoku(blankGrid()); // 9x9 succeeds
        assertThrows(IllegalArgumentException.class, () -> { new Sudoku(new int[8][9]); });
        assertThrows(IllegalArgumentException.class, () -> { new Sudoku(new int[9][8]); });
    }
    
    @Test
    public void testValidateDigits() {
        int[][] grid = blankGrid();
        
        grid[0][0] = 1;
        grid[2][5] = 7;
        
        new Sudoku(grid); // succeeds
        
        grid[1][4] = -2;
        assertThrows(IllegalArgumentException.class, () -> { new Sudoku(grid); });
        
        grid[1][4] = 12;
        assertThrows(IllegalArgumentException.class, () -> { new Sudoku(grid); });
    }
    
    private int[][] blankGrid() {
        int[][] result = new int[9][9];
        
        for (int i = 0; i < 9; i++) {
            Arrays.fill(result[i], SudokuSolutionState.BLANK);
        }
        
        return result;
    }
}
