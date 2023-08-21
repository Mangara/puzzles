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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NakedPair implements SolveStrategy {

    @Override
    public Optional<SolveStep> findStep(SolvingSudoku sudoku) {
        Optional<SolveStep> step;
        
        for (int i = 0; i < 9; i++) {
            step = checkRegion(sudoku, Region.row(i));
            if (step.isPresent()) {
                return step;
            }

            step = checkRegion(sudoku, Region.col(i));
            if (step.isPresent()) {
                return step;
            }

            step = checkRegion(sudoku, Region.box(i));
            if (step.isPresent()) {
                return step;
            }
        }
        
        return Optional.empty();
    }
    
    private Optional<SolveStep> checkRegion(SolvingSudoku sudoku, Region region) {
        // Pick all cells that are not filled in
        List<Cell> blank = region.cells.stream().filter(c -> sudoku.state[c.row][c.col].digit == BLANK).toList();
        
        if (blank.size() <= 2) {
            System.out.printf("[ForcedSet] Only %d blank cells in %s. Skipping.%n", blank.size(), region.toString());
            return Optional.empty();
        }
        
        // Check for pairs
        List<Cell> twoOptions = blank.stream().filter(c -> sudoku.state[c.row][c.col].getPossibleCount() == 2).toList();
        
        for (int i = 0; i < twoOptions.size(); i++) {
            Cell cell1 = twoOptions.get(i);
            List<Integer> options = sudoku.state[cell1.row][cell1.col].allPossible();
            int d1 = options.get(0);
            int d2 = options.get(1);
                    
            for (int j = i + 1; j < twoOptions.size(); j++) {
                Cell cell2 = twoOptions.get(j);
                List<Integer> options2 = sudoku.state[cell2.row][cell2.col].allPossible();
                
                if (options2.get(0) == d1 && options2.get(1) == d2) {
                    // We found a pair, let's see how many options we can eliminate from other cells
                    List<Cell> targets = blank.stream().filter(c -> {
                        SudokuSolutionState state = sudoku.state[c.row][c.col];
                        return (state.isPossible(d1) || state.isPossible(d2)) && !c.equals(cell1) && !c.equals(cell2);
                    }).toList();
                    
                    if (!targets.isEmpty()) { // This pair is useful!
                        return Optional.of(new Step(Arrays.asList(cell1, cell2), targets, region, d1, d2));
                    }
                }
            }
        }
        
        return Optional.empty();
    }

    
    public class Step implements SolveStep {

        private final List<Cell> pair;
        private final List<Cell> targets;
        private final Region region;
        private final int digit1;
        private final int digit2;

        public Step(List<Cell> pair, List<Cell> targets, Region region, int digit1, int digit2) {
            this.pair = pair;
            this.targets = targets;
            this.region = region;
            this.digit1 = digit1;
            this.digit2 = digit2;
        }
        
        @Override
        public List<Cell> primaryCells() {
            return pair;
        }

        @Override
        public List<Cell> secondaryCells() {
            return targets;
        }

        @Override
        public String description() {
            return String.format("%s and %s form a naked pair, eliminating %d and %d from the other cells in %s.", pair.get(0).toString(), pair.get(1).toString(), digit1, digit2, region.toString());
        }

        @Override
        public void apply(SolvingSudoku sudoku) {
            for (Cell target : targets) {
                sudoku.removePossible(target, digit1);
                sudoku.removePossible(target, digit2);
            }
        }
        
    }
    
}
