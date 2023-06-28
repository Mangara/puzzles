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

import com.github.mangara.puzzles.data.nonogram.Nonogram;

public class NonogramSolver {

    public enum Algorithm {
        BRUTE_FORCE, ITERATIVE
    }

    public static boolean hasUniqueSolution(Nonogram puzzle) {
        return hasUniqueSolution(puzzle, Algorithm.ITERATIVE);
    }
    
    public static boolean hasUniqueSolution(Nonogram puzzle, Algorithm algo) {
        switch (algo) {
            case BRUTE_FORCE:
                return BruteForceSolver.hasUniqueSolution(puzzle);
            case ITERATIVE:
                return (new IterativeSolver()).hasUniqueSolution(puzzle);
            default:
                throw new IllegalArgumentException("Unrecognized algorithm: " + algo);
        }
    }

    public static boolean[][] findAnySolution(Nonogram puzzle) {
        return findAnySolution(puzzle, Algorithm.BRUTE_FORCE);
    }

    public static boolean[][] findAnySolution(Nonogram puzzle, Algorithm algo) {
        switch (algo) {
            case BRUTE_FORCE:
                return BruteForceSolver.findAnySolution(puzzle);
            case ITERATIVE:
                return (new IterativeSolver()).findAnySolution(puzzle);
            default:
                throw new IllegalArgumentException("Unrecognized algorithm: " + algo);
        }
    }
}
