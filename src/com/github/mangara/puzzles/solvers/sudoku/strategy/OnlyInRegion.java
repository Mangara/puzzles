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
import com.github.mangara.puzzles.solvers.sudoku.Region;
import com.github.mangara.puzzles.solvers.sudoku.SolveStep;
import com.github.mangara.puzzles.solvers.sudoku.SolveStrategy;
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OnlyInRegion implements SolveStrategy {
    
    @Override
    public Optional<SolveStep> findStep(SolvingSudoku sudoku) {
        Optional<SolveStep> step;
        
        // Try boxes first
        for (int i = 0; i < 9; i++) {
            for (int digit = 1; digit <= 9; digit++) {
                step = findOnlyInRegion(sudoku, Region.box(i), digit);
                if (step.isPresent()) {
                    return step;
                }
            }
        }
        
        // Then rows and columns
        for (int i = 0; i < 9; i++) {
            for (int digit = 1; digit <= 9; digit++) {
                step = findOnlyInRegion(sudoku, Region.row(i), digit);
                if (step.isPresent()) {
                    return step;
                }
                
                step = findOnlyInRegion(sudoku, Region.col(i), digit);
                if (step.isPresent()) {
                    return step;
                }
            }
        }

        return Optional.empty();
    }
    
    private Optional<SolveStep> findOnlyInRegion(SolvingSudoku sudoku, Region region, int digit) {
        Cell option = null;
        
        for (Cell cell : region.cells) {
            SudokuSolutionState state = sudoku.getState(cell);
            
            if (state.digit == digit) {
                return Optional.empty();
            }
            
            if (state.isPossible(digit)) {
                if (option == null) { // First time we see an option for this digit
                    option = cell;
                } else { // We've seen an option for this digit already
                    return Optional.empty();
                }
            }
        }
        
        if (option == null) {
            throw new InternalError(String.format("No options for %d in %s, but %d has not yet been placed.", digit, region.toString(), digit));
        }
        
        return Optional.of(new Step(option, region, digit));
    }

    class Step implements SolveStep {

        private final Cell cell;
        private final Region region;
        private final int digit;

        public Step(Cell cell, Region region, int digit) {
            this.cell = cell;
            this.region = region;
            this.digit = digit;
        }

        @Override
        public List<Cell> primaryCells() {
            return Collections.singletonList(cell);
        }

        @Override
        public List<Cell> secondaryCells() {
            List<Cell> result = new ArrayList<>(region.cells);
            result.remove(cell);
            return result;
        }

        @Override
        public String description() {
            return String.format("%s is the only place for %d in %s.", cell.toString(), digit, region.toString());
        }

        @Override
        public void apply(SolvingSudoku sudoku) {
            sudoku.placeDigit(cell, digit);
        }
    }
}
