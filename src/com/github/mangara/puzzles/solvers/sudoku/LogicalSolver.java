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
package com.github.mangara.puzzles.solvers.sudoku;

import com.github.mangara.puzzles.data.Pair;
import com.github.mangara.puzzles.data.sudoku.Sudoku;
import com.github.mangara.puzzles.data.sudoku.SudokuSolutionState;
import com.github.mangara.puzzles.solvers.sudoku.strategy.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LogicalSolver {

    private static List<SolveStrategy> strategies = Arrays.asList(
            new OnlyInBox(), new OnlyInRow(), new OnlyInColumn(), new NakedSingle(),
            new NakedPair(), new AllSeen()
    );

    public static Pair<SolvingSudoku, List<SolveStep>> solve(Sudoku sudoku) {
        return solve(new SolvingSudoku(sudoku));
    }

    public static Pair<SolvingSudoku, List<SolveStep>> solve(SudokuSolutionState[][] sudoku) {
        return solve(new SolvingSudoku(sudoku));
    }

    public static Pair<SolvingSudoku, List<SolveStep>> solve(SolvingSudoku sudoku) {
        List<SolveStep> steps = new ArrayList<>();

        boolean progress;
        do {
            progress = false;

            for (SolveStrategy strategy : strategies) {
                Optional<SolveStep> maybeStep = strategy.findStep(sudoku);

                if (maybeStep.isPresent()) {
                    SolveStep step = maybeStep.get();
                    step.apply(sudoku);
                    steps.add(step);
                    progress = true;
                    break;
                }
            }
        } while (progress);

        return new Pair<>(sudoku, steps);
    }
}
