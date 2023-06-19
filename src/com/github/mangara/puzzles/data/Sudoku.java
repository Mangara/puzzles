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
package com.github.mangara.puzzles.data;

public class Sudoku implements Puzzle {
    
    public static final int BLANK = 0;

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
        validateRows(givenDigits);
        validateColumns(givenDigits);
        validateBoxes(givenDigits);
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
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (givenDigits[i][j] != BLANK && (givenDigits[i][j] < 1 || givenDigits[i][j] > 9)) {
                    throw new IllegalArgumentException("Given digits must be between 1 and 9, inclusive");
                }
            }
        }
    }

    private void validateRows(int[][] givenDigits) {
        for (int i = 0; i < 9; i++) {
            int[] digitCount = new int[10];
            
            for (int j = 0; j < 9; j++) {
                int digit = givenDigits[i][j];
                
                if (digit != BLANK) {
                    digitCount[digit]++;
                    if (digitCount[digit] > 1) {
                        throw new IllegalArgumentException("Rows must not contain the same digit twice");
                    }
                }
            }
        }
    }
    
    private void validateColumns(int[][] givenDigits) {
        for (int col = 0; col < 9; col++) {
            int[] digitCount = new int[10];
            
            for (int row = 0; row < 9; row++) {
                int digit = givenDigits[row][col];
                
                if (digit != BLANK) {
                    digitCount[digit]++;
                    if (digitCount[digit] > 1) {
                        throw new IllegalArgumentException("Columns must not contain the same digit twice");
                    }
                }
            }
        }
    }
    
    private void validateBoxes(int[][] givenDigits) {
        for (int box = 0; box < 9; box++) {
            int[] digitCount = new int[10];
            int boxRow = 3 * (box / 3);
            int boxCol = 3 * (box % 3);
            
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    int digit = givenDigits[boxRow + row][boxCol + col];

                    if (digit != BLANK) {
                        digitCount[digit]++;
                        if (digitCount[digit] > 1) {
                            throw new IllegalArgumentException("Boxes must not contain the same digit twice");
                        }
                    }
                }
            }
        }
    }
}
