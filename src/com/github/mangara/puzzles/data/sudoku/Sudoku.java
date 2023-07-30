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

import com.github.mangara.puzzles.data.Puzzle;
import com.github.mangara.puzzles.data.PuzzleType;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;

public class Sudoku implements Puzzle {

    // A 9 x 9 size with default regions is assumed
    private final int[][] givenDigits;

    public Sudoku(int[][] givenDigits) {
        validateGivenDigits(givenDigits);
        this.givenDigits = copy(givenDigits);
    }

    @Override
    public PuzzleType getType() {
        return PuzzleType.SUDOKU;
    }

    public int[][] getGivenDigits() {
        return copy(givenDigits);
    }

    private int[][] copy(int[][] original) {
        int[][] result = new int[original.length][original[0].length];

        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }

        return result;
    }

    private void validateGivenDigits(int[][] givenDigits) {
        validateSize(givenDigits);
        validateDigits(givenDigits);
    }

    private void validateSize(int[][] givenDigits) {
        if (givenDigits.length != 9) {
            throw new IllegalArgumentException("There must be exactly 9 rows of given digits");
        }

        for (int i = 0; i < 9; i++) {
            if (givenDigits[i].length != 9) {
                throw new IllegalArgumentException("There must be exactly 9 columns of given digits");
            }
        }
    }

    private void validateDigits(int[][] givenDigits) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (givenDigits[row][col] != BLANK && (givenDigits[row][col] < 1 || givenDigits[row][col] > 9)) {
                    throw new IllegalArgumentException("Given digits must be between 1 and 9, inclusive");
                }
            }
        }
    }    
}
