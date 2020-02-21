/*
 * Copyright 2020 Sander Verdonschot.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bitbucket.mangara.puzzles.data.Nonogram;

public class IterativeSolver {
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
        SolutionState[][] solution = findSolution(puzzle);
        return (NonogramSolverHelper.isSolved(solution) ? NonogramSolverHelper.convertToBooleanArray(solution) : null);
    }
    
    public static boolean hasUniqueSolution(Nonogram puzzle) {
        clearRecord();
        return NonogramSolverHelper.isSolved(findSolution(puzzle));
    }
    
    private static SolutionState[][] findSolution(Nonogram puzzle) {
        SolutionState[][] solution = new SolutionState[puzzle.getWidth()][puzzle.getHeight()];
        boolean progress = true;
        
        while (progress) {
            progress = false;
            
            for (int row = 0; row < puzzle.getHeight(); row++) {
                System.out.println("Testing row " + row);
                List<Integer> rowNumbers = puzzle.getSideNumbers().get(row);
                SolutionState[] currentValues = NonogramSolverHelper.readRow(solution, row);
                SolutionState[] intersection = NonogramSolverHelper.intersectAllMatchingSolutions(rowNumbers, currentValues);
                progress = progress || !Arrays.equals(currentValues, intersection);
                NonogramSolverHelper.writeRow(solution, row, intersection);
            }
            
            for (int col = 0; col < puzzle.getWidth(); col++) {
                System.out.println("Testing column " + col);
                List<Integer> colNumbers = puzzle.getTopNumbers().get(col);
                SolutionState[] currentValues = NonogramSolverHelper.readColumn(solution, col);
                SolutionState[] intersection = NonogramSolverHelper.intersectAllMatchingSolutions(colNumbers, currentValues);
                progress = progress || !Arrays.equals(currentValues, intersection);
                NonogramSolverHelper.writeColumn(solution, col, intersection);
            }
        }
        
        return solution;
    }
}
