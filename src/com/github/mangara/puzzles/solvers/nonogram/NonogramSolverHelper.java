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
package com.github.mangara.puzzles.solvers.nonogram;

import com.github.mangara.puzzles.data.nonogram.NonogramSolutionState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NonogramSolverHelper {

    public static int getSpaceRequired(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return 0;
        }

        return numbers.stream()
                .mapToInt((Integer i) -> i + 1) // Segment + 1 empty pixel
                .sum() - 1; // Last segment does not need an empty pixel
    }

    public static int getFirstSegmentLength(boolean[] column, int start, int firstUnknown) {
        int segmentStart = -1;

        for (int i = start; i < firstUnknown; i++) {
            if (column[i] && segmentStart < 0) {
                segmentStart = i;
            } else if (!column[i] && segmentStart >= 0) {
                return i - segmentStart;
            }
        }

        return segmentStart < 0 ? 0 : firstUnknown - segmentStart;
    }

    /**
     * Returns the last index of the first segment in the given column.
     *
     * @param column
     * @param start
     * @return
     */
    private static int getFirstSegmentEnd(boolean[] column, int start, int firstUnknown) {
        boolean inSegment = false;

        for (int i = start; i < firstUnknown; i++) {
            if (column[i] && !inSegment) {
                inSegment = true;
            } else if (!column[i] && inSegment) {
                return i - 1;
            }
        }

        return inSegment ? firstUnknown - 1 : -1;
    }

    /**
     *
     * @param partial
     * @param firstUnknown
     * @param numbers
     * @return true if there exists an assignment of booleans to
     * partial[firstUnknown...] such that it matches the given numbers, false
     * otherwise
     */
    public static boolean isValidPartial(boolean[] partial, int firstUnknown, List<Integer> numbers) {
        OneSidedPartial osp = new OneSidedPartial(partial, firstUnknown);
        int firstFree = firstUnknown;

        for (int i = 0; i < osp.blocks.size(); i++) {
            if (i >= numbers.size()) {
                return false;
            }

            if (Objects.equals(osp.blocks.get(i), numbers.get(i))) {
                continue;
            }

            if (osp.blocks.get(i) > numbers.get(i) // block too long
                    || i < osp.blocks.size() - 1 // partial has more blocks
                    || !osp.blockInProgress) { // block is finished
                return false;
            }

            // Unequal blocks, it's the last block in the partial, and it's unfinished
            if (i == numbers.size() - 1) {
                firstFree = firstUnknown + numbers.get(i) - osp.blocks.get(i);
                return firstFree <= partial.length;
            } else {
                firstFree = firstUnknown + numbers.get(i) - osp.blocks.get(i) + 1;
            }
        }

        List<Integer> numbersLeft = numbers.subList(osp.blocks.size(), numbers.size());
        int spaceRequired = getSpaceRequired(numbersLeft);
        int spaceLeft = partial.length - firstFree;

        return spaceLeft >= spaceRequired;
    }

    public static List<boolean[]> getAllSolutions(List<Integer> numbers, int width) {
        List<boolean[]> result = new ArrayList<>();

        if (width == 0) {
            return result;
        }

        if (numbers.isEmpty()) {
            result.add(new boolean[width]);
            return result;
        }

        int n = numbers.get(0);

        if (numbers.size() == 1) {
            for (int i = 0; i <= width - n; i++) {
                boolean[] solution = new boolean[width];
                Arrays.fill(solution, i, i + n, true);
                result.add(solution);
            }
            return result;
        }

        List<Integer> rest = numbers.subList(1, numbers.size());
        int spaceRequiredForRest = getSpaceRequired(rest);
        int lastStart = width - n - 1 - spaceRequiredForRest;

        for (int i = 0; i <= lastStart; i++) {
            List<boolean[]> allSolutionsForRest = getAllSolutions(rest, width - n - 1 - i);
            for (boolean[] partial : allSolutionsForRest) {
                boolean[] solution = new boolean[width];
                Arrays.fill(solution, i, i + n, true);
                System.arraycopy(partial, 0, solution, i + n + 1, width - n - 1 - i);
                result.add(solution);
            }
        }

        return result;
    }

    public static NonogramSolutionState[] readRow(NonogramSolutionState[][] solution, int row) {
        NonogramSolutionState[] rowValues = new NonogramSolutionState[solution.length];
        for (int i = 0; i < solution.length; i++) {
            rowValues[i] = solution[i][row];
        }
        return rowValues;
    }

    public static NonogramSolutionState[] readColumn(NonogramSolutionState[][] solution, int col) {
        return solution[col];
    }

    public static void writeRow(NonogramSolutionState[][] solution, int row, NonogramSolutionState[] values) {
        for (int i = 0; i < solution.length; i++) {
            solution[i][row] = values[i];
        }
    }

    public static void writeColumn(NonogramSolutionState[][] solution, int col, NonogramSolutionState[] values) {
        System.arraycopy(values, 0, solution[col], 0, solution[col].length);
    }

    public static boolean isSolved(NonogramSolutionState[][] solution) {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (solution[i][j] == NonogramSolutionState.UNKNOWN) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param solution
     * @return A boolean array of the same size that is true when the solution
     * is FILLED and false otherwise.
     */
    static boolean[][] convertToBooleanArray(NonogramSolutionState[][] solution) {
        boolean[][] result = new boolean[solution.length][solution[0].length];

        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (solution[i][j] == NonogramSolutionState.FILLED) {
                    result[i][j] = true;
                }
            }
        }

        return result;
    }

    /**
     *
     * @param numbers
     * @param knownValues
     * @return
     */
    public static NonogramSolutionState[] intersectAllMatchingSolutions(List<Integer> numbers, NonogramSolutionState[] knownValues) {
        List<NonogramSolutionState[]> allMatchingSolutions = generateAllMatchingSolutions(numbers, knownValues);
        
//        System.out.println("Numbers: " + numbers + " Known values: " + solutionToString(knownValues));
//        System.out.println("All matching solutions:");
//        for (SolutionState[] solution : allMatchingSolutions) {
//            System.out.println("  " + solutionToString(solution));
//        }
        
        NonogramSolutionState[] result = intersection(allMatchingSolutions, knownValues.length);
        
//        System.out.println("Intersection: " + solutionToString(result));
        
        return result;
    }
    
    public static String solutionToString(NonogramSolutionState[] solution) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < solution.length; i++) {
            switch (solution[i]) {
                case EMPTY:
                    sb.append('-');
                    break;
                case FILLED:
                    sb.append('X');
                    break;
                case UNKNOWN:
                    sb.append('?');
                    break;
            }
        }
        return sb.toString();
    }
    
    private static List<NonogramSolutionState[]> generateAllMatchingSolutions(List<Integer> numbers, NonogramSolutionState[] knownValues) {
        return generateAllMatchingSolutions(numbers, knownValues, 0, new NonogramSolutionState[knownValues.length], 0);
    }
    
    private static List<NonogramSolutionState[]> generateAllMatchingSolutions(List<Integer> numbers, NonogramSolutionState[] knownValues, int segment, NonogramSolutionState[] currentSolution, int position) {
        int width = knownValues.length;
        
        if (segment == numbers.size()) {
            // We have a complete solution
            for (int i = position; i < width; i++) {
                if (knownValues[i] == NonogramSolutionState.FILLED) {
                    // No match
                    return Collections.EMPTY_LIST;
                }
                
                currentSolution[i] = NonogramSolutionState.EMPTY;
            }
            
            NonogramSolutionState[] result = new NonogramSolutionState[width];
            System.arraycopy(currentSolution, 0, result, 0, width);
            
            return Collections.singletonList(result);
        }
        
        int segmentLength = numbers.get(segment);
        int restSpaceNeeded = (segment == numbers.size() - 1 ? 0 : 1 + getSpaceRequired(numbers.subList(segment + 1, numbers.size())));
        int lastSpot = width - restSpaceNeeded - segmentLength;
        
        List<NonogramSolutionState[]> solutions = new ArrayList<>();
        
        for (int spot = position; spot <= lastSpot; spot++) {
            // Place this segment at this spot
            for (int i = position; i < spot + segmentLength; i++) {
                if (i < spot) {
                    currentSolution[i] = NonogramSolutionState.EMPTY;
                } else {
                    currentSolution[i] = NonogramSolutionState.FILLED;
                }
            }
            
            int newPosition = spot + segmentLength;
            
            if (segment < numbers.size() - 1) {
                currentSolution[newPosition] = NonogramSolutionState.EMPTY;
                newPosition++;
            }
            
            // Does it match?
            boolean match = true;
            for (int i = position; i < newPosition; i++) {
                if (knownValues[i] != NonogramSolutionState.UNKNOWN && knownValues[i] != currentSolution[i]) {
                    match = false;
                    break;
                }
            }
            
            if (match) {
                solutions.addAll(generateAllMatchingSolutions(numbers, knownValues, segment + 1, currentSolution, newPosition));
            }
        }
        
        return solutions;
    }
    
    private static NonogramSolutionState[] intersection(List<NonogramSolutionState[]> solutions, int width) {
        if (solutions.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        NonogramSolutionState[] result = solutions.get(0);
        int known = width;
        
        for (int i = 1; i < solutions.size(); i++) {
            NonogramSolutionState[] solution = solutions.get(i);
            
            for (int j = 0; j < width; j++) {
                if (result[j] != NonogramSolutionState.UNKNOWN && result[j] != solution[j]) {
                    result[j] = NonogramSolutionState.UNKNOWN;
                    known--;
                }
            }
            
            if (known == 0) {
                break;
            }
        }
        
        return result;
    }

    static class OneSidedPartial {

        final List<Integer> blocks;
        final boolean blockInProgress;
        final int length;
        final int firstUnknown;

        OneSidedPartial(boolean[] partial, int firstUnknown) {
            this.firstUnknown = firstUnknown;
            this.length = partial.length;
            this.blockInProgress = (firstUnknown > 0 ? partial[firstUnknown - 1] : true);
            this.blocks = extractBlocks(partial, firstUnknown);
        }

        private List<Integer> extractBlocks(boolean[] partial, int firstUnknown) {
            List<Integer> result = new ArrayList<>();

            int currentBlockLength = 0;

            for (int i = 0; i < firstUnknown; i++) {
                if (partial[i]) {
                    currentBlockLength++;
                } else {
                    if (currentBlockLength > 0) {
                        result.add(currentBlockLength);
                    }
                    currentBlockLength = 0;
                }
            }

            if (currentBlockLength > 0) {
                result.add(currentBlockLength);
            }

            return result;
        }
    }
}
