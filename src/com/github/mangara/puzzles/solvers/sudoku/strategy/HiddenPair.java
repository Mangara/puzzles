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
import static com.github.mangara.puzzles.data.sudoku.SudokuSolutionState.BLANK;
import com.github.mangara.puzzles.solvers.sudoku.Region;
import com.github.mangara.puzzles.solvers.sudoku.SolveStep;
import com.github.mangara.puzzles.solvers.sudoku.SolveStrategy;
import com.github.mangara.puzzles.solvers.sudoku.SolvingSudoku;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * If there are two digits that, in one region, can only appear in the same two
 * cells, we can eliminate all other options from those two cells.
 */
public class HiddenPair implements SolveStrategy {

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
        boolean debug = false;
        
        if (debug) {
            System.out.printf("[HiddenPair] Checking %s for hidden pairs.%n", region);
        }
        
        // Pick all cells that are not filled in
        List<Cell> blank = region.cells.stream().filter(c -> sudoku.state[c.row][c.col].digit == BLANK).toList();

        if (blank.size() <= 2) {
            if (debug) {
                System.out.printf("[HiddenPair] Fewer than 3 blank cells. Skipping.%n");
            }
            return Optional.empty();
        }

        // Find the blank cells that each digit can go in
        List<Integer> twoOptionDigits = new ArrayList<>();
        List<List<Cell>> options = new ArrayList<>();
        
        for (int digit = 1; digit <= 9; digit++) {
            List<Cell> digitOptions = new ArrayList<>();
            
            for (Cell cell : blank) {
                if (sudoku.getState(cell).isPossible(digit)) {
                    digitOptions.add(cell);
                }
            }
            
            if (debug) {
                System.out.printf("[HiddenPair] Options for %d: %s.%n", digit, digitOptions);
            }
            
            if (digitOptions.size() == 2) {
                twoOptionDigits.add(digit);
                options.add(digitOptions);
            }
        }
        
        if (twoOptionDigits.size() < 2) {
            if (debug) {
                System.out.printf("[HiddenPair] Fewer than 2 digits with 2 options. Skipping.%n");
            }
            return Optional.empty();
        }
        
        // Check each pair of digits that has two options to see if they have the same options
        for (int i = 0; i < twoOptionDigits.size(); i++) {
            int digit1 = twoOptionDigits.get(i);
            List<Cell> digit1Options = options.get(i);
            
            for (int j = i + 1; j < twoOptionDigits.size(); j++) {
                int digit2 = twoOptionDigits.get(j);
                List<Cell> digit2Options = options.get(j);
                
                if (digit2Options.equals(digit1Options)) {
                    if (otherOptions(sudoku, digit1Options, digit1, digit2)) {
                        if (debug) {
                            System.out.printf("[HiddenPair] Found a hidden pair: %s.%n", digit1Options);
                        }
                        return Optional.of(new Step(digit1Options, region, digit1, digit2));
                    } else {
                        if (debug) {
                            System.out.printf("[HiddenPair] Found a naked pair: %s. Moving on.%n", digit1Options);
                        }
                    }
                } else {
                    if (debug) {
                        System.out.printf("[HiddenPair] %d does not have the same options as %d. Skipping.%n", digit2, digit1);
                    }
                }
            }
        }

        return Optional.empty();
    }

    private boolean otherOptions(SolvingSudoku sudoku, List<Cell> cells, int digit1, int digit2) {
        for (int d = 1; d <= 9; d++) {
            if (d == digit1 || d == digit2) {
                continue;
            }
            
            for (Cell cell : cells) {
                if (sudoku.getState(cell).isPossible(d)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public class Step implements SolveStep {

        private final List<Cell> pair;
        private final Region region;
        private final int digit1;
        private final int digit2;

        public Step(List<Cell> pair, Region region, int digit1, int digit2) {
            this.pair = pair;
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
            List<Cell> result = new ArrayList<>(region.cells);
            result.removeAll(pair);
            return result;
        }

        @Override
        public String description() {
            return String.format("%d and %d must be in %s and %s in %s, eliminating the other options from these cells.", digit1, digit2, pair.get(0).toString(), pair.get(1).toString(), region.toString());
        }

        @Override
        public void apply(SolvingSudoku sudoku) {
            for (int d = 1; d <= 9; d++) {
                if (d == digit1 || d == digit2) {
                    continue;
                }
                
                sudoku.removePossible(pair.get(0), d);
                sudoku.removePossible(pair.get(1), d);
            }
        }

    }

}
