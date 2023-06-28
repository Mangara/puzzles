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

import com.github.mangara.puzzles.solvers.nonogram.NonogramSolver;
import java.util.Arrays;
import com.github.mangara.puzzles.data.Nonogram;
import com.github.mangara.puzzles.generators.NonogramGenerator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sander
 */
public class NonogramSolverTest {

    public static Nonogram[] puzzles = new Nonogram[]{
        new Nonogram(Arrays.asList(Arrays.asList(1), Arrays.asList(1)), Arrays.asList(Arrays.asList(1), Arrays.asList(1))), // Non-unique solution
        new Nonogram(Arrays.asList(Arrays.asList(2), Arrays.asList(1)), Arrays.asList(Arrays.asList(2), Arrays.asList(1))), // Unique solution
        new Nonogram(Arrays.asList(Arrays.asList(2), Arrays.asList(1)), Arrays.asList(Arrays.asList(1), Arrays.asList(1))), // No solution
    };

    public NonogramSolverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of hasUniqueSolution method, of class NonogramSolver.
     */
    @Test
    public void testHasUniqueSolution() {
        System.out.println("hasUniqueSolution");

        boolean[] expected = new boolean[]{
            false,
            true,
            false
        };

        for (NonogramSolver.Algorithm alg : NonogramSolver.Algorithm.values()) {
            System.out.println(alg);

            for (int i = 0; i < puzzles.length; i++) {
                boolean result = NonogramSolver.hasUniqueSolution(puzzles[i], alg);
                assertEquals(expected[i], result);
            }
        }
    }

    /**
     * Test of findAnySolution method, of class NonogramSolver.
     */
    @Test
    public void testFindAnySolution() {
        System.out.println("findAnySolution");

        boolean[] hasSolution = new boolean[]{
            true,
            true,
            false
        };

        for (NonogramSolver.Algorithm alg : NonogramSolver.Algorithm.values()) {
            System.out.println(alg);
            
            for (int i = 0; i < puzzles.length; i++) {
                boolean[][] solution = NonogramSolver.findAnySolution(puzzles[i], alg);

                if (hasSolution[i]) {
                    testSolution(solution, puzzles[i]);
                } else {
                    assertNull(solution);
                }
            }
        }
    }

    private void testSolution(boolean[][] solution, Nonogram puzzle) {
        assertNotNull(solution);
        Nonogram puzzleFromSolution = NonogramGenerator.generateNonogram(solution);
        assertEquals(puzzle.getSideNumbers(), puzzleFromSolution.getSideNumbers());
        assertEquals(puzzle.getTopNumbers(), puzzleFromSolution.getTopNumbers());
    }
}
