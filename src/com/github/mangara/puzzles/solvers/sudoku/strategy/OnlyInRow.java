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
package com.github.mangara.puzzles.solvers.sudoku.strategy;

import com.github.mangara.puzzles.data.sudoku.Cell;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import com.github.mangara.puzzles.solvers.sudoku.SolveStep;
import com.github.mangara.puzzles.solvers.sudoku.SolveStrategy;
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OnlyInRow implements SolveStrategy {

    @Override
    public Optional<SolveStep> findStep(SolvingSudoku sudoku) {
        for (int row = 0; row < 9; row++) {
            Optional<SolveStep> possibleStep = findOnlyInRow(row, sudoku.state[row]);

            if (possibleStep.isPresent()) {
                return possibleStep;
            }
        }

        return Optional.empty();
    }

    private Optional<SolveStep> findOnlyInRow(int row, SudokuSolutionState[] rowState) {
        int[] onlyPlaceForDigit = new int[10];
        Arrays.fill(onlyPlaceForDigit, -2);

        for (int col = 0; col < 9; col++) {
            SudokuSolutionState state = rowState[col];

            if (state.digit != BLANK) {
                onlyPlaceForDigit[state.digit] = -1;
            } else {
                for (int d = 1; d <= 9; d++) {
                    if (state.isPossible(d)) {
                        switch (onlyPlaceForDigit[d]) {
                            case -2:
                                // First time we see an option for this digit
                                onlyPlaceForDigit[d] = col;
                                break;
                            case -1:
                                // We've ruled out this digit
                                break;
                            default:
                                // Second time we see this digit
                                onlyPlaceForDigit[d] = -1;
                        }
                    }
                }
            }
        }

        for (int d = 1; d <= 9; d++) {
            if (onlyPlaceForDigit[d] >= 0) {
                return Optional.of(new Step(new Cell(row, onlyPlaceForDigit[d]), d));
            }
        }

        return Optional.empty();
    }

    class Step implements SolveStep {

        private final Cell cell;
        private final int digit;

        public Step(Cell cell, int digit) {
            this.cell = cell;
            this.digit = digit;
        }

        @Override
        public List<Cell> primaryCells() {
            return Collections.singletonList(cell);
        }

        @Override
        public List<Cell> secondaryCells() {
            List<Cell> result = new ArrayList<>();

            for (int col = 0; col < 9; col++) {
                if (col == cell.col) {
                    continue;
                }

                result.add(new Cell(cell.row, col));
            }

            return result;
        }

        @Override
        public String description() {
            return String.format("%s is the only place for %d in row %d.", cell.toString(), digit, cell.displayRow());
        }

        @Override
        public void apply(SolvingSudoku sudoku) {
            sudoku.placeDigit(cell, digit);
        }
    }
}
