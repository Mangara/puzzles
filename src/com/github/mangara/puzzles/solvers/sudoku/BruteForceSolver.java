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
package com.github.mangara.puzzles.solvers.sudoku;

import com.github.mangara.puzzles.data.sudoku.Sudoku;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import java.util.Arrays;

public class BruteForceSolver {

    public static boolean hasUniqueSolution(Sudoku puzzle) {
        return findUniqueBruteForceSolution(puzzle, false, puzzle.getGivenDigits(), 0);
    }

    public static int[][] findAnySolution(Sudoku puzzle) {
        int[][] solution = puzzle.getGivenDigits();
        return findBruteForceSolution(puzzle, solution, 0);
    }

    private static boolean findUniqueBruteForceSolution(Sudoku puzzle, boolean solved, int[][] partialSolution, int nextCell) {
        if (nextCell == 81) {
            System.out.println("Done");
            return !solved;
        }
        
        int row = nextCell / 9;
        int col = nextCell % 9;
        System.out.printf("Solving cell %d = (%d, %d)%n", nextCell, row, col);
        
        if (partialSolution[row][col] != BLANK) {
            // Move on to the next cell
            return findUniqueBruteForceSolution(puzzle, solved, partialSolution, nextCell + 1);
        }
        
        // Find possile digits for this cell
        boolean[] options = new boolean[9];
        Arrays.fill(options, true);
        
        int boxStartRow = 3 * (row / 3);
        int boxStartCol = 3 * (col / 3);
        
        for (int i = 0; i < 9; i++) {
            // Strike digits in the same column
            int digit = partialSolution[i][col];
            if (digit != BLANK) {
                options[digit - 1] = false;
            }
            
            // Strike digits in the same row
            digit = partialSolution[row][i];
            if (digit != BLANK) {
                options[digit - 1] = false;
            }
            
            // Strike digits in the same box
            digit = partialSolution[boxStartRow + i / 3][boxStartCol + i % 3];
            if (digit != BLANK) {
                options[digit - 1] = false;
            }
        }
        
        // Try each possible digit
        boolean mySolved = solved;
        
        for (int d = 1; d < 10; d++) {
            if (!options[d - 1]) {
                continue;
            }
            
            partialSolution[row][col] = d;
            
            System.out.printf("Testing (%d, %d) = %d%n", row, col, d);
            
            boolean result = findUniqueBruteForceSolution(puzzle, mySolved, partialSolution, nextCell + 1);
            
            if (mySolved) {
                if (result) {
                    // Still okay, no new solution found
                } else {
                    // Second solution found
                    System.out.println("Non-unique!");
                    return false;
                }
            } else {
                if (result) {
                    // First solution found
                    System.out.println("Solved!");
                    mySolved = true;
                } else {
                    // Still okay, no solution yet
                }
            }
        }
        
        return mySolved;
    }

    private static int[][] findBruteForceSolution(Sudoku puzzle, int[][] solution, int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
