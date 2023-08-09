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
package com.github.mangara.puzzles.solvers.sudoku;

import com.github.mangara.puzzles.data.sudoku.Cell;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import java.util.Arrays;

public class SolvingSudoku {
    public final SudokuSolutionState[][] state;

    public SolvingSudoku() {
        state = initState();
    }

    public SolvingSudoku(int[][] givenDigits) {
        state = initState();
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (givenDigits[row][col] != BLANK) {
                    placeDigit(row, col, givenDigits[row][col]);
                    state[row][col].given = true;
                }
            }
        }
    }
    
    public SolvingSudoku(Sudoku sudoku) {
        this(sudoku.getGivenDigits());
    }
    
    public SolvingSudoku(SolvingSudoku sudoku) {
        this.state = copyState(sudoku.state);
    }
    
    public SolvingSudoku(SudokuSolutionState[][] state) {
        this.state = copyState(state);
    }
    
    public int[][] getGivenDigits() {
        int[][] digits = new int[9][9];
        
        for (int row = 0; row < 9; row++) {
            Arrays.fill(digits[row], BLANK);
            
            for (int col = 0; col < 9; col++) {
                if (state[row][col].given) {
                    digits[row][col] = state[row][col].digit;
                }
            }
        }
        
        return digits;
    }
    
    public int[][] getGuessedDigits() {
        int[][] digits = new int[9][9];
        
        for (int row = 0; row < 9; row++) {
            Arrays.fill(digits[row], BLANK);
            
            for (int col = 0; col < 9; col++) {
                if (!state[row][col].given && state[row][col].digit != BLANK) {
                    digits[row][col] = state[row][col].digit;
                }
            }
        }
        
        return digits;
    }
    
    public void placeDigit(Cell cell, int digit) {
        placeDigit(cell.row, cell.col, digit);
    }
    
    public void placeDigit(int row, int col, int digit) {
        state[row][col].setDigit(digit);
        
        // Eliminate digit from row
        for (int c = 0; c < 9; c++) {
            state[row][c].removePossible(digit);
        }

        // Eliminate from column
        for (int r = 0; r < 9; r++) {
            state[r][col].removePossible(digit);
        }

        // Eliminate from box
        int boxRow = 3 * (row / 3);
        int boxCol = 3 * (col / 3);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                state[boxRow + r][boxCol + c].removePossible(digit);
            }
        }
    }
    
    public void removeDigit(int row, int col) {
        state[row][col].digit = BLANK;
        // TODO: update possible of (row, col) and all other affected places
    }
    
    private SudokuSolutionState[][] initState() {
        SudokuSolutionState[][] result = new SudokuSolutionState[9][9];
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                result[row][col] = new SudokuSolutionState();
            }
        }
        
        return result;
    }

    private static SudokuSolutionState[][] copyState(SudokuSolutionState[][] sudoku) {
        SudokuSolutionState[][] result = new SudokuSolutionState[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                result[row][col] = new SudokuSolutionState(sudoku[row][col]);
            }
        }

        return result;
    }
}
