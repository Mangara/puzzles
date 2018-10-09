/*
 * Copyright 2018 Sander Verdonschot.
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
package org.bitbucket.mangara.puzzles.solvers;

import java.util.List;
import org.bitbucket.mangara.puzzles.data.Nonogram;

public class NonogramSolver {

    private enum Algorithm {
        BRUTE_FORCE
    }

    public static boolean hasUniqueSolution(Nonogram puzzle) {
        return true; // TODO
    }

    public static boolean[][] findAnySolution(Nonogram puzzle) {
        return findAnySolution(puzzle, Algorithm.BRUTE_FORCE);
    }

    private static boolean[][] findAnySolution(Nonogram puzzle, Algorithm algo) {
        switch (algo) {
            case BRUTE_FORCE:
                return findBruteForceSolution(puzzle);
            default:
                throw new IllegalArgumentException("Unrecognized algorithm: " + algo);
        }
    }

    private static boolean[][] findBruteForceSolution(Nonogram puzzle) {
        boolean[][] solution = new boolean[puzzle.getWidth()][puzzle.getHeight()]; // TODO: check if we should swap width and height
        boolean solved = findBruteForceSolution(puzzle, solution, 0);
        return solved ? solution : null;
    }

    private static boolean findBruteForceSolution(Nonogram puzzle, boolean[][] partialSolution, int rowToSolve) {
        // Are we done?
        if (rowToSolve == puzzle.getHeight()) {
            return true;
        }

        // Is each column of the partial solution valid?
        for (int i = 0; i < puzzle.getWidth(); i++) {
            if (!isValidPartial(partialSolution[i], rowToSolve, puzzle.getTopNumbers().get(i))) { // TODO: Make sure that partialSolution[i] is a column, with row 0 at the top
                return false;
            }
        }

        List<Integer> rowNumbers = puzzle.getSideNumbers().get(rowToSolve);

        // Try each possible solution for this row
        for (boolean[] possibleRowSolution : getAllSolutions(rowNumbers, puzzle.getWidth())) {
            for (int i = 0; i < puzzle.getWidth(); i++) {
                partialSolution[i][rowToSolve] = possibleRowSolution[i];
            }

            boolean solved = findBruteForceSolution(puzzle, partialSolution, rowToSolve + 1);

            if (solved) {
                return true;
            }
        }

        return false;
    }

    private static boolean isValidPartial(boolean[] partialColumn, int firstUnknown, List<Integer> numbers) {
        // true if there exists an assignment of booleans to partialColumn[firstUnknown...] such that it matches the given numbers

        // Does the completed part match?
        // Do the remaining numbers fit?
    }
}
