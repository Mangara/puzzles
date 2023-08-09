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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class NakedSingle implements SolveStrategy {

    @Override
    public Optional<SolveStep> findStep(SolvingSudoku sudoku) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int digit = checkNakedSingle(sudoku.state[row][col]);
                if (digit != BLANK) {
                    return Optional.of(new Step(new Cell(row, col), digit));
                }
            }
        }
        
        return Optional.empty();
    }

    private int checkNakedSingle(SudokuSolutionState state) {
        if (state.digit != BLANK) {
            return BLANK;
        }
        
        int singleOption = BLANK;
        
        for (int d = 1; d <= 9; d++) {
            if (state.isPossible(d)) {
                if (singleOption == BLANK) {
                    // First option
                    singleOption = d;
                } else {
                    // Second option
                    return BLANK;
                }
            }
        }
        
        return singleOption;
    }
    
    public class Step implements SolveStep {

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
            return Collections.emptyList();
        }

        @Override
        public String description() {
            return String.format("%s is a naked single and can only be %d.", cell.toString(), digit);
        }

        @Override
        public void apply(SolvingSudoku sudoku) {
            sudoku.placeDigit(cell, digit);
        }
        
    }
    
}
