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
import com.github.mangara.puzzles.solvers.sudoku.Region;
import com.github.mangara.puzzles.solvers.sudoku.SolveStep;
import com.github.mangara.puzzles.solvers.sudoku.SolveStrategy;
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AllSeen implements SolveStrategy {

    @Override
    public Optional<SolveStep> findStep(SolvingSudoku sudoku) {
        Optional<SolveStep> step;
        
        for (int digit = 1; digit <= 9; digit++) {
            for (int i = 0; i < 9; i++) {
                step = checkRegion(sudoku, digit, Region.row(i));
                if (step.isPresent()) {
                    return step;
                }

                step = checkRegion(sudoku, digit, Region.col(i));
                if (step.isPresent()) {
                    return step;
                }

                step = checkRegion(sudoku, digit, Region.box(i));
                if (step.isPresent()) {
                    return step;
                }
            }
        }
        
        return Optional.empty();
    }
    
    private Optional<SolveStep> checkRegion(SolvingSudoku sudoku, int digit, Region region) {
        // Find all cells in row that can be the given digit
        List<Cell> options = new ArrayList<>();
        
        for (Cell cell : region.cells) {
            SudokuSolutionState state = sudoku.state[cell.row][cell.col];
            
            if (state.digit == BLANK) {
                if (state.isPossible(digit)) {
                    options.add(cell);
                }
            } else if (state.digit == digit) { // If the digit is already placed in this row, return
                return Optional.empty();
            }
        }
        
        // Find the intersection of cells seen from these
        Set<Cell> visibleFromAll = sudoku.visibleFromAll(options);
        
        if (visibleFromAll.isEmpty()) {
            return Optional.empty();
        }
        
        // If any of these can still be digit, create a step to eliminate that
        List<Cell> targets = new ArrayList<>();
        
        for (Cell cell : visibleFromAll) {
            SudokuSolutionState state = sudoku.state[cell.row][cell.col];
            if (state.digit == BLANK && state.isPossible(digit)) {
                targets.add(cell);
            }
        }
        
        if (targets.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.of(new Step(options, targets, digit, region));
    }

    
    public class Step implements SolveStep {

        private final List<Cell> sources;
        private final List<Cell> targets;
        private final int digit;
        private final Region region;

        public Step(List<Cell> sources, List<Cell> targets, int digit, Region region) {
            this.sources = sources;
            this.targets = targets;
            this.digit = digit;
            this.region = region;
        }
        
        @Override
        public List<Cell> primaryCells() {
            return sources;
        }

        @Override
        public List<Cell> secondaryCells() {
            return targets;
        }

        @Override
        public String description() {
            return String.format("All options for %d in %s see %s.", digit, region.toString(), Cell.toString(targets));
        }

        @Override
        public void apply(SolvingSudoku sudoku) {
            for (Cell target : targets) {
                sudoku.removePossible(target, digit);
            }
        }
        
    }
    
}
