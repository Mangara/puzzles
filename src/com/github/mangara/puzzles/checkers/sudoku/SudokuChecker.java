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
package com.github.mangara.puzzles.checkers.sudoku;

import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;

public class SudokuChecker {

    /**
     * Checks whether the given digits satisfy the rules of Sudoku. Does not
     * check whether it has a unique solution.
     *
     * @param puzzle
     * @return
     */
    public static boolean isValidPuzzle(Sudoku puzzle) {
        return isValidPartialSolution(puzzle, partialSolutionStateFromPuzzle(puzzle));
    }

    public static boolean isValidSolution(Sudoku puzzle, int[][] solution) {
        return isValidSolution(puzzle, solutionStateFromSolution(solution));
    }

    public static boolean isValidSolution(Sudoku puzzle, SudokuSolutionState[][] solution) {
        return fullySolved(solution) && isValidPartialSolution(puzzle, solution);
    }

    public static boolean isValidPartialSolution(Sudoku puzzle, SudokuSolutionState[][] solution) {
        return matchesGiven(puzzle, solution) && validRows(solution) && validColumns(solution) && validBoxes(solution);
    }

    private static boolean fullySolved(SudokuSolutionState[][] solution) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int digit = solution[row][col].number;
                if (digit < 1 || digit > 9) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean matchesGiven(Sudoku puzzle, SudokuSolutionState[][] solution) {
        int[][] given = puzzle.getGivenDigits();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int givenDigit = given[row][col];

                if (givenDigit != BLANK && solution[row][col].number != givenDigit) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean validRows(SudokuSolutionState[][] solution) {
        for (int row = 0; row < 9; row++) {
            int[] digitCount = new int[10];

            for (int col = 0; col < 9; col++) {
                int digit = solution[row][col].number;

                if (digit != BLANK) {
                    digitCount[digit]++;
                    if (digitCount[digit] > 1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean validColumns(SudokuSolutionState[][] solution) {
        for (int col = 0; col < 9; col++) {
            int[] digitCount = new int[10];

            for (int row = 0; row < 9; row++) {
                int digit = solution[row][col].number;

                if (digit != BLANK) {
                    digitCount[digit]++;
                    if (digitCount[digit] > 1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static boolean validBoxes(SudokuSolutionState[][] solution) {
        for (int box = 0; box < 9; box++) {
            int[] digitCount = new int[10];
            int boxRow = 3 * (box / 3);
            int boxCol = 3 * (box % 3);

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    int digit = solution[boxRow + row][boxCol + col].number;

                    if (digit != BLANK) {
                        digitCount[digit]++;
                        if (digitCount[digit] > 1) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private static SudokuSolutionState[][] solutionStateFromSolution(int[][] solution) {
        SudokuSolutionState[][] result = new SudokuSolutionState[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                result[row][col] = new SudokuSolutionState(solution[row][col]);
            }
        }

        return result;
    }

    private static SudokuSolutionState[][] partialSolutionStateFromPuzzle(Sudoku puzzle) {
        SudokuSolutionState[][] result = new SudokuSolutionState[9][9];
        int[][] digits = puzzle.getGivenDigits();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                result[row][col] = new SudokuSolutionState(digits[row][col]);
            }
        }

        return result;
    }
}
