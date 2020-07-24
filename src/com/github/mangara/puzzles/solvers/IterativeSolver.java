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
package com.github.mangara.puzzles.solvers;

import com.github.mangara.puzzles.data.NonogramSolutionState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.mangara.puzzles.data.Nonogram;

public class IterativeSolver {
    private final boolean recordPartials;
    private List<NonogramSolutionState[][]> partialSolutionRecord;

    public IterativeSolver() {
        this(false);
    }

    public IterativeSolver(boolean recordPartials) {
        this.recordPartials = recordPartials;
    }

    private void clearRecord() {
        if (!recordPartials) {
            return;
        }

        partialSolutionRecord = new ArrayList<>();
    }

    private void recordPartialSolution(NonogramSolutionState[][] partialSolution) {
        if (!recordPartials) {
            return;
        }

        NonogramSolutionState[][] copy = new NonogramSolutionState[partialSolution.length][partialSolution[0].length];

        for (int i = 0; i < partialSolution.length; i++) {
            copy[i] = new NonogramSolutionState[partialSolution[i].length];
            System.arraycopy(partialSolution[i], 0, copy[i], 0, partialSolution[i].length);
        }

        partialSolutionRecord.add(copy);
    }
    
    public List<NonogramSolutionState[][]> getPartialSolutionRecord() {
        return partialSolutionRecord;
    }
    
    public boolean[][] findAnySolution(Nonogram puzzle) {
        clearRecord();
        NonogramSolutionState[][] solution = findSolution(puzzle);
        return (NonogramSolverHelper.isSolved(solution) ? NonogramSolverHelper.convertToBooleanArray(solution) : null);
    }
    
    public boolean hasUniqueSolution(Nonogram puzzle) {
        clearRecord();
        return NonogramSolverHelper.isSolved(findSolution(puzzle));
    }
    
    private NonogramSolutionState[][] findSolution(Nonogram puzzle) {
        NonogramSolutionState[][] solution = new NonogramSolutionState[puzzle.getWidth()][puzzle.getHeight()];
        for (int i = 0; i < puzzle.getWidth(); i++) {
            Arrays.fill(solution[i], NonogramSolutionState.UNKNOWN);
        }
        recordPartialSolution(solution);
        
        boolean progress = true;
        
        while (progress) {
            progress = false;
            
            for (int row = 0; row < puzzle.getHeight(); row++) {
                System.out.println("Testing row " + row);
                List<Integer> rowNumbers = puzzle.getSideNumbers().get(row);
                NonogramSolutionState[] currentValues = NonogramSolverHelper.readRow(solution, row);
                NonogramSolutionState[] intersection = NonogramSolverHelper.intersectAllMatchingSolutions(rowNumbers, currentValues);
                
                if (!Arrays.equals(currentValues, intersection)) {
                    progress = true;
                    NonogramSolverHelper.writeRow(solution, row, intersection);
                    recordPartialSolution(solution);
                }
            }
            
            for (int col = 0; col < puzzle.getWidth(); col++) {
                System.out.println("Testing column " + col);
                List<Integer> colNumbers = puzzle.getTopNumbers().get(col);
                NonogramSolutionState[] currentValues = NonogramSolverHelper.readColumn(solution, col);
                NonogramSolutionState[] intersection = NonogramSolverHelper.intersectAllMatchingSolutions(colNumbers, currentValues);
                
                if (!Arrays.equals(currentValues, intersection)) {
                    progress = true;
                    NonogramSolverHelper.writeColumn(solution, col, intersection);
                    recordPartialSolution(solution);
                }
            }
        }
        
        return solution;
    }
}
