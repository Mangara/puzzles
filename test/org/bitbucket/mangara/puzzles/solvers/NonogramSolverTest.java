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

import org.bitbucket.mangara.puzzles.data.Nonogram;
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
        Nonogram puzzle = null;
        boolean expResult = false;
        boolean result = NonogramSolver.hasUniqueSolution(puzzle);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAnySolution method, of class NonogramSolver.
     */
    @Test
    public void testFindAnySolution() {
        System.out.println("findAnySolution");
        Nonogram puzzle = null;
        boolean[][] expResult = null;
        boolean[][] result = NonogramSolver.findAnySolution(puzzle);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
