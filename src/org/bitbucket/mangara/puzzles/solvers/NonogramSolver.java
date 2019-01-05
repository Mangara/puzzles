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

import java.util.Arrays;
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
        boolean[][] solution = new boolean[puzzle.getWidth()][puzzle.getHeight()];
        boolean solved = findBruteForceSolution(puzzle, solution, 0);
        return solved ? solution : null;
    }

    private static boolean findBruteForceSolution(Nonogram puzzle, boolean[][] partialSolution, int rowToSolve) {
        System.out.println("Solving row " + rowToSolve);
        
        // Is each column of the partial solution valid?
        for (int i = 0; i < puzzle.getWidth(); i++) {
            System.out.println("Testing column " + i);
            if (!NonogramSolverHelper.isValidPartial(partialSolution[i], rowToSolve, puzzle.getTopNumbers().get(i))) {
                System.out.println("Wrong");
                return false;
            }
        }
        
        // Are we done?
        if (rowToSolve == puzzle.getHeight()) {
            System.out.println("Done");
            return true;
        }

        List<Integer> rowNumbers = puzzle.getSideNumbers().get(rowToSolve);

        // Try each possible solution for this row
        for (boolean[] possibleRowSolution : NonogramSolverHelper.getAllSolutions(rowNumbers, puzzle.getWidth())) {
            System.out.println("Trying row " + rowToSolve + " solution " + Arrays.toString(possibleRowSolution));
            
            for (int i = 0; i < puzzle.getWidth(); i++) {
                partialSolution[i][rowToSolve] = possibleRowSolution[i];
            }

            boolean solved = findBruteForceSolution(puzzle, partialSolution, rowToSolve + 1);

            if (solved) {
                System.out.println("Solved!");
                return true;
            }
            System.out.println("Row " + rowToSolve + " not solved.");
        }

        return false;
    }    
}
