/*
 * Copyright 2019 Sander Verdonschot.
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
package com.github.mangara.puzzles.solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.mangara.puzzles.data.Nonogram;

public class BruteForceSolver {

    public static boolean recordPartials = false;
    private static List<boolean[][]> partialSolutionRecord;

    private static void clearRecord() {
        if (!recordPartials) {
            return;
        }

        partialSolutionRecord = new ArrayList<>();
    }

    private static void recordPartialSolution(boolean[][] partialSolution, int upToRow) {
        if (!recordPartials) {
            return;
        }

        boolean[][] copy = new boolean[partialSolution.length][partialSolution[0].length];

        for (int i = 0; i < partialSolution.length; i++) {
            copy[i] = new boolean[partialSolution[i].length];
            System.arraycopy(partialSolution[i], 0, copy[i], 0, upToRow);
        }

        partialSolutionRecord.add(copy);
    }
    
    public static List<boolean[][]> getPartialSolutionRecord() {
        return partialSolutionRecord;
    }

    public static boolean[][] findAnySolution(Nonogram puzzle) {
        clearRecord();
        boolean[][] solution = new boolean[puzzle.getWidth()][puzzle.getHeight()];
        boolean solved = findBruteForceSolution(puzzle, solution, 0);
        return solved ? solution : null;
    }

    public static boolean hasUniqueSolution(Nonogram puzzle) {
        clearRecord();
        return findUniqueBruteForceSolution(puzzle, false, new boolean[puzzle.getWidth()][puzzle.getHeight()], 0);
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

        recordPartialSolution(partialSolution, rowToSolve);

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
            } else {
                recordPartialSolution(partialSolution, rowToSolve);
            }

            System.out.println("Row " + rowToSolve + " not solved.");
        }

        return false;
    }

    private static boolean findUniqueBruteForceSolution(Nonogram puzzle, boolean solved, boolean[][] partialSolution, int rowToSolve) {
        System.out.println("Solving row " + rowToSolve);

        // Is each column of the partial solution valid?
        for (int i = 0; i < puzzle.getWidth(); i++) {
            System.out.println("Testing column " + i);
            if (!NonogramSolverHelper.isValidPartial(partialSolution[i], rowToSolve, puzzle.getTopNumbers().get(i))) {
                System.out.println("Wrong");
                return solved;
            }
        }

        // Are we done?
        if (rowToSolve == puzzle.getHeight()) {
            System.out.println("Done");
            return !solved;
        }

        List<Integer> rowNumbers = puzzle.getSideNumbers().get(rowToSolve);

        boolean mySolved = solved;

        // Try each possible solution for this row
        for (boolean[] possibleRowSolution : NonogramSolverHelper.getAllSolutions(rowNumbers, puzzle.getWidth())) {
            System.out.println("Trying row " + rowToSolve + " solution " + Arrays.toString(possibleRowSolution));

            for (int i = 0; i < puzzle.getWidth(); i++) {
                partialSolution[i][rowToSolve] = possibleRowSolution[i];
            }

            boolean result = findUniqueBruteForceSolution(puzzle, mySolved, partialSolution, rowToSolve + 1);

            if (mySolved) {
                if (result) {
                    // Still okay, no new solution found
                } else {
                    // Second solution found
                    System.out.println("Non-unique!");
                    return false;
                }
            } else {
                if (result) {
                    // First solution found
                    System.out.println("Solved!");
                    mySolved = true;
                } else {
                    // Still okay, no solution yet
                }
            }

            System.out.println("Row " + rowToSolve + " not solved.");
        }

        return mySolved;
    }
}
