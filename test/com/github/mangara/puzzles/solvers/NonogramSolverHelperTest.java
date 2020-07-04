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
package com.github.mangara.puzzles.solvers;

import com.github.mangara.puzzles.solvers.NonogramSolverHelper;
import com.github.mangara.puzzles.data.SolutionState;
import java.util.Arrays;
import java.util.List;
import static com.github.mangara.puzzles.solvers.NonogramSolverHelper.getFirstSegmentLength;
import static com.github.mangara.puzzles.solvers.NonogramSolverHelper.getSpaceRequired;
import static com.github.mangara.puzzles.solvers.NonogramSolverHelper.isValidPartial;
import static com.github.mangara.puzzles.solvers.NonogramSolverHelper.getAllSolutions;
import static com.github.mangara.puzzles.solvers.NonogramSolverHelper.intersectAllMatchingSolutions;
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
public class NonogramSolverHelperTest {

    public NonogramSolverHelperTest() {
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
     * Test of getSpaceRequired method, of class NonogramSolverHelper.
     */
    @Test
    public void testGetSpaceRequired() {
        System.out.println("getSpaceRequired");
        assertEquals(0, getSpaceRequired(Arrays.asList()));
        assertEquals(3, getSpaceRequired(Arrays.asList(3)));
        assertEquals(5, getSpaceRequired(Arrays.asList(2, 2)));
        assertEquals(13, getSpaceRequired(Arrays.asList(1, 2, 3, 2, 1)));
    }

    /**
     * Test of getFirstSegmentLength method, of class NonogramSolverHelper.
     */
    @Test
    public void testGetFirstSegmentLength() {
        System.out.println("getFirstSegmentLength");
        assertEquals(0, getFirstSegmentLength(new boolean[0], 0, 0));
        assertEquals(0, getFirstSegmentLength(new boolean[10], 0, 10));
        assertEquals(0, getFirstSegmentLength(new boolean[7], 7, 7));
        assertEquals(1, getFirstSegmentLength(new boolean[]{true}, 0, 1));
        assertEquals(3, getFirstSegmentLength(new boolean[]{true, true, true}, 0, 3));
        assertEquals(2, getFirstSegmentLength(new boolean[]{true, true, true}, 1, 3));
        assertEquals(1, getFirstSegmentLength(new boolean[]{true, true, true}, 2, 3));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, true}, 3, 3));
        assertEquals(2, getFirstSegmentLength(new boolean[]{false, true, true}, 0, 3));
        assertEquals(2, getFirstSegmentLength(new boolean[]{false, true, true}, 1, 3));
        assertEquals(1, getFirstSegmentLength(new boolean[]{false, true, true}, 2, 3));
        assertEquals(2, getFirstSegmentLength(new boolean[]{true, true, false}, 0, 3));
        assertEquals(1, getFirstSegmentLength(new boolean[]{true, true, false}, 1, 3));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, false}, 2, 3));

        assertEquals(1, getFirstSegmentLength(new boolean[]{true, true, true}, 0, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, true}, 1, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, true}, 2, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, true}, 3, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{false, true, true}, 0, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{false, true, true}, 1, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{false, true, true}, 2, 1));
        assertEquals(1, getFirstSegmentLength(new boolean[]{true, true, false}, 0, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, false}, 1, 1));
        assertEquals(0, getFirstSegmentLength(new boolean[]{true, true, false}, 2, 1));
    }

    /**
     * Test of isValidPartial method, of class NonogramSolverHelper.
     */
    @Test
    public void testIsValidPartial() {
        System.out.println("isValidPartial");

        // Fully unknown
        assertEquals(true, isValidPartial(new boolean[0], 0, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[2], 0, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[2], 0, Arrays.asList(1)));
        assertEquals(true, isValidPartial(new boolean[2], 0, Arrays.asList(2)));
        assertEquals(false, isValidPartial(new boolean[2], 0, Arrays.asList(3)));
        assertEquals(false, isValidPartial(new boolean[2], 0, Arrays.asList(1, 1)));
        assertEquals(true, isValidPartial(new boolean[5], 0, Arrays.asList(1, 2)));
        assertEquals(true, isValidPartial(new boolean[5], 0, Arrays.asList(2, 2)));
        assertEquals(false, isValidPartial(new boolean[5], 0, Arrays.asList(3, 2)));
        assertEquals(true, isValidPartial(new boolean[5], 0, Arrays.asList(1, 1, 1)));

        // Fully known
        assertEquals(true, isValidPartial(new boolean[1], 1, Arrays.asList()));
        assertEquals(false, isValidPartial(new boolean[1], 1, Arrays.asList(1)));
        assertEquals(false, isValidPartial(new boolean[]{true}, 1, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{true}, 1, Arrays.asList(1)));

        assertEquals(true, isValidPartial(new boolean[]{false, false}, 2, Arrays.asList()));
        assertEquals(false, isValidPartial(new boolean[]{false, false}, 2, Arrays.asList(1)));
        assertEquals(false, isValidPartial(new boolean[]{false, false}, 2, Arrays.asList(2)));
        assertEquals(false, isValidPartial(new boolean[]{false, true}, 2, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{false, true}, 2, Arrays.asList(1)));
        assertEquals(false, isValidPartial(new boolean[]{false, true}, 2, Arrays.asList(2)));
        assertEquals(false, isValidPartial(new boolean[]{true, false}, 2, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{true, false}, 2, Arrays.asList(1)));
        assertEquals(false, isValidPartial(new boolean[]{true, false}, 2, Arrays.asList(2)));
        assertEquals(false, isValidPartial(new boolean[]{true, true}, 2, Arrays.asList()));
        assertEquals(false, isValidPartial(new boolean[]{true, true}, 2, Arrays.asList(1)));
        assertEquals(true, isValidPartial(new boolean[]{true, true}, 2, Arrays.asList(2)));

        assertEquals(true, isValidPartial(new boolean[]{false, true, false}, 3, Arrays.asList(1)));
        assertEquals(true, isValidPartial(new boolean[]{true, false, true}, 3, Arrays.asList(1, 1)));
        assertEquals(false, isValidPartial(new boolean[]{false, true, true}, 3, Arrays.asList(1, 1)));
        assertEquals(false, isValidPartial(new boolean[]{true, true, false}, 3, Arrays.asList(1, 1)));

        assertEquals(true, isValidPartial(new boolean[]{true, false, true, true}, 4, Arrays.asList(1, 2)));
        assertEquals(true, isValidPartial(new boolean[]{true, false, true, false}, 4, Arrays.asList(1, 1)));
        assertEquals(true, isValidPartial(new boolean[]{true, false, false, true}, 4, Arrays.asList(1, 1)));
        assertEquals(true, isValidPartial(new boolean[]{false, true, false, true}, 4, Arrays.asList(1, 1)));

        // Partly known
        assertEquals(true, isValidPartial(new boolean[]{false, false}, 1, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{false, false}, 1, Arrays.asList(1)));
        assertEquals(false, isValidPartial(new boolean[]{false, false}, 1, Arrays.asList(2)));
        assertEquals(true, isValidPartial(new boolean[]{false, true}, 1, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{false, true}, 1, Arrays.asList(1)));
        assertEquals(false, isValidPartial(new boolean[]{false, true}, 1, Arrays.asList(2)));
        assertEquals(false, isValidPartial(new boolean[]{true, false}, 1, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{true, false}, 1, Arrays.asList(1)));
        assertEquals(true, isValidPartial(new boolean[]{true, false}, 1, Arrays.asList(2)));
        assertEquals(false, isValidPartial(new boolean[]{true, true}, 1, Arrays.asList()));
        assertEquals(true, isValidPartial(new boolean[]{true, true}, 1, Arrays.asList(1)));
        assertEquals(true, isValidPartial(new boolean[]{true, true}, 1, Arrays.asList(2)));
        
        assertEquals(true, isValidPartial(new boolean[]{true, false, false, false}, 1, Arrays.asList(2, 1)));
        assertEquals(false, isValidPartial(new boolean[]{false, true, false, false}, 2, Arrays.asList(2, 1)));
        assertEquals(true, isValidPartial(new boolean[]{false, true, false, false, false}, 2, Arrays.asList(2, 1)));
    }

    @Test
    public void testOneSidedPartialLength() {
        System.out.println("oneSidedPartialLength");

        // Fully known
        assertEquals(0, new NonogramSolverHelper.OneSidedPartial(new boolean[0], 0).length);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[2], 0).length);
        assertEquals(5, new NonogramSolverHelper.OneSidedPartial(new boolean[5], 0).length);
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false}, 1).length);
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true}, 1).length);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 2).length);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true}, 2).length);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 2).length);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true}, 2).length);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false}, 3).length);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true}, 3).length);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true, false}, 3).length);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, true}, 3).length);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true}, 4).length);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, false}, 4).length);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, false, true}, 4).length);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false, true}, 4).length);

        // Partially known
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 1).length);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 1).length);

        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 0).length);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 1).length);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 2).length);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 3).length);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 4).length);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 5).length);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 6).length);
    }

    @Test
    public void testOneSidedPartialFirstUnknown() {
        System.out.println("oneSidedPartialFirstUnknown");

        assertEquals(0, new NonogramSolverHelper.OneSidedPartial(new boolean[0], 0).firstUnknown);
        assertEquals(0, new NonogramSolverHelper.OneSidedPartial(new boolean[2], 0).firstUnknown);
        assertEquals(0, new NonogramSolverHelper.OneSidedPartial(new boolean[5], 0).firstUnknown);

        // Fully known
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false}, 1).firstUnknown);
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true}, 1).firstUnknown);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 2).firstUnknown);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true}, 2).firstUnknown);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 2).firstUnknown);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true}, 2).firstUnknown);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false}, 3).firstUnknown);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true}, 3).firstUnknown);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true, false}, 3).firstUnknown);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, true}, 3).firstUnknown);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true}, 4).firstUnknown);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, false}, 4).firstUnknown);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, false, true}, 4).firstUnknown);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false, true}, 4).firstUnknown);

        // Partially known
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 1).firstUnknown);
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 1).firstUnknown);

        assertEquals(0, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 0).firstUnknown);
        assertEquals(1, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 1).firstUnknown);
        assertEquals(2, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 2).firstUnknown);
        assertEquals(3, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 3).firstUnknown);
        assertEquals(4, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 4).firstUnknown);
        assertEquals(5, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 5).firstUnknown);
        assertEquals(6, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 6).firstUnknown);
    }

    @Test
    public void testOneSidedPartialBlockInProgress() {
        System.out.println("oneSidedPartialBlockInProgress");

        // Fully unknown
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[0], 0).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[2], 0).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[5], 0).blockInProgress);

        // Fully known
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false}, 1).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true}, 1).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 2).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true}, 2).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 2).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true}, 2).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false}, 3).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true}, 3).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true, false}, 3).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, true}, 3).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true}, 4).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, false}, 4).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, false, true}, 4).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false, true}, 4).blockInProgress);

        // Partially known
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 1).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 1).blockInProgress);

        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 0).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 1).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 2).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 3).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 4).blockInProgress);
        assertEquals(false, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 5).blockInProgress);
        assertEquals(true, new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 6).blockInProgress);
    }

    @Test
    public void testOneSidedPartialBlocks() {
        System.out.println("oneSidedPartialBlocks");

        // Fully unknown
        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[0], 0).blocks);
        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[2], 0).blocks);
        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[5], 0).blocks);

        // Fully known
        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false}, 1).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true}, 1).blocks);
        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 2).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true}, 2).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 2).blocks);
        assertEquals(Arrays.asList(2), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true}, 2).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false}, 3).blocks);
        assertEquals(Arrays.asList(1, 1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true}, 3).blocks);
        assertEquals(Arrays.asList(2), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, true, false}, 3).blocks);
        assertEquals(Arrays.asList(2), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, true}, 3).blocks);
        assertEquals(Arrays.asList(1, 2), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true}, 4).blocks);
        assertEquals(Arrays.asList(1, 1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, false}, 4).blocks);
        assertEquals(Arrays.asList(1, 1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, false, true}, 4).blocks);
        assertEquals(Arrays.asList(1, 1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, true, false, true}, 4).blocks);

        // Partially known
        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[]{false, false}, 1).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false}, 1).blocks);

        assertEquals(Arrays.asList(), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 0).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 1).blocks);
        assertEquals(Arrays.asList(1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 2).blocks);
        assertEquals(Arrays.asList(1, 1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 3).blocks);
        assertEquals(Arrays.asList(1, 2), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 4).blocks);
        assertEquals(Arrays.asList(1, 2), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 5).blocks);
        assertEquals(Arrays.asList(1, 2, 1), new NonogramSolverHelper.OneSidedPartial(new boolean[]{true, false, true, true, false, true}, 6).blocks);
    }
    
    @Test
    public void testGetAllSolutions() {
        System.out.println("getAllSolutions");
        
        // Empty
        assertResultEquals(0, Arrays.asList(), getAllSolutions(Arrays.asList(), 0));
        assertResultEquals(1, Arrays.asList(new boolean[5]), getAllSolutions(Arrays.asList(), 5));
        
        // One segment
        assertResultEquals(2, Arrays.asList(), getAllSolutions(Arrays.asList(1), 0));
        assertResultEquals(3, Arrays.asList(), getAllSolutions(Arrays.asList(4), 3));
        assertResultEquals(4, Arrays.asList(new boolean[] {true}), getAllSolutions(Arrays.asList(1), 1));
        assertResultEquals(5, Arrays.asList(new boolean[] {true, false, false}, new boolean[] {false, true, false}, new boolean[] {false, false, true}), getAllSolutions(Arrays.asList(1), 3));
        assertResultEquals(6, Arrays.asList(new boolean[] {true, true, false}, new boolean[] {false, true, true}), getAllSolutions(Arrays.asList(2), 3));
        assertResultEquals(7, Arrays.asList(new boolean[] {true, true, true}), getAllSolutions(Arrays.asList(3), 3));
        
        // Multiple segments
        assertResultEquals(7, Arrays.asList(), getAllSolutions(Arrays.asList(1, 2), 3));
        assertResultEquals(8, Arrays.asList(new boolean[] {true, false, true, true}), getAllSolutions(Arrays.asList(1, 2), 4));
        assertResultEquals(9, Arrays.asList(new boolean[] {true, false, true, true, false}, new boolean[] {true, false, false, true, true}, new boolean[] {false, true, false, true, true}), getAllSolutions(Arrays.asList(1, 2), 5));
        assertResultEquals(10, Arrays.asList(), getAllSolutions(Arrays.asList(2, 1), 3));
        assertResultEquals(11, Arrays.asList(new boolean[] {true, true, false, true}), getAllSolutions(Arrays.asList(2, 1), 4));
        assertResultEquals(12, Arrays.asList(new boolean[] {true, true, false, true, false}, new boolean[] {true, true, false, false, true}, new boolean[] {false, true, true, false, true}), getAllSolutions(Arrays.asList(2, 1), 5));
    }

    private void assertResultEquals(int testCase, List<boolean[]> expected, List<boolean[]> actual) {
        if (!resultEquals(expected, actual)) {
            System.out.println("Expected: ");
            for (boolean[] b : expected) {
                System.out.println(Arrays.toString(b));
            }
            System.out.println("Actual: ");
            for (boolean[] b : actual) {
                System.out.println(Arrays.toString(b));
            }
            
            fail("Results not equal for case " + testCase);
        }
    }

    private boolean resultEquals(List<boolean[]> expected, List<boolean[]> actual) {
        if (expected.size() != actual.size()) {
            return false;
        }
        
        for (int i = 0; i < expected.size(); i++) {
            boolean[] e = expected.get(i);
            boolean[] a = actual.get(i);
            
            if (!Arrays.equals(e, a)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Test
    public void testIntersectAllMatchingSolutions() {
        System.out.println("intersectAllMatchingSolutions");
        
        // Empty
        assertArrayEquals("E0", toState(""), intersectAllMatchingSolutions(Arrays.asList(), toState("")));
        assertArrayEquals("E1", toState("---"), intersectAllMatchingSolutions(Arrays.asList(), toState("???")));
        assertArrayEquals("E2", toState("---"), intersectAllMatchingSolutions(Arrays.asList(), toState("--?")));
        
        // One segment
        assertArrayEquals("O1-0", toState("???"), intersectAllMatchingSolutions(Arrays.asList(1), toState("???")));
        assertArrayEquals("O1-1", toState("X--"), intersectAllMatchingSolutions(Arrays.asList(1), toState("X??")));
        assertArrayEquals("O1-2", toState("-X-"), intersectAllMatchingSolutions(Arrays.asList(1), toState("?X?")));
        assertArrayEquals("O1-3", toState("--X"), intersectAllMatchingSolutions(Arrays.asList(1), toState("??X")));
        assertArrayEquals("O1-4", toState("-??"), intersectAllMatchingSolutions(Arrays.asList(1), toState("-??")));
        assertArrayEquals("O1-5", toState("?-?"), intersectAllMatchingSolutions(Arrays.asList(1), toState("?-?")));
        assertArrayEquals("O1-6", toState("??-"), intersectAllMatchingSolutions(Arrays.asList(1), toState("??-")));
        assertArrayEquals("O1-7", toState("--X"), intersectAllMatchingSolutions(Arrays.asList(1), toState("--?")));
        assertArrayEquals("O1-8", toState("-X-"), intersectAllMatchingSolutions(Arrays.asList(1), toState("-?-")));
        assertArrayEquals("O1-9", toState("X--"), intersectAllMatchingSolutions(Arrays.asList(1), toState("?--")));
        
        assertArrayEquals("O2-0", toState("?X?"), intersectAllMatchingSolutions(Arrays.asList(2), toState("???")));
        assertArrayEquals("O2-1", toState("-XX"), intersectAllMatchingSolutions(Arrays.asList(2), toState("-??")));
        assertArrayEquals("O2-2", toState("?X?"), intersectAllMatchingSolutions(Arrays.asList(2), toState("?X?")));
        assertArrayEquals("O2-3", toState("XX-"), intersectAllMatchingSolutions(Arrays.asList(2), toState("X??")));
        assertArrayEquals("O2-4", toState("????"), intersectAllMatchingSolutions(Arrays.asList(2), toState("????")));
        
        assertArrayEquals("O3-0", toState("XXX"), intersectAllMatchingSolutions(Arrays.asList(3), toState("???")));
        assertArrayEquals("O3-1", toState("?XX?"), intersectAllMatchingSolutions(Arrays.asList(3), toState("????")));
        assertArrayEquals("O3-2", toState("??X??"), intersectAllMatchingSolutions(Arrays.asList(3), toState("?????")));
        
        // Multiple segments
        assertArrayEquals("M1-0", toState("X-X"), intersectAllMatchingSolutions(Arrays.asList(1, 1), toState("???")));
        assertArrayEquals("M1-1", toState("????"), intersectAllMatchingSolutions(Arrays.asList(1, 1), toState("????")));
        assertArrayEquals("M1-2", toState("XX-X"), intersectAllMatchingSolutions(Arrays.asList(2, 1), toState("????")));
        assertArrayEquals("M1-3", toState("X-XX"), intersectAllMatchingSolutions(Arrays.asList(1, 2), toState("????")));
        assertArrayEquals("M1-4", toState("?X???"), intersectAllMatchingSolutions(Arrays.asList(2, 1), toState("?????")));
        assertArrayEquals("M1-5", toState("?XX??XX?"), intersectAllMatchingSolutions(Arrays.asList(3, 3), toState("????????")));
    }
    
    private SolutionState[] toState(String state) {
        SolutionState[] result = new SolutionState[state.length()];
        
        for (int i = 0; i < state.length(); i++) {
            result[i] = charToSolutionState(state.charAt(i));
        }
        
        return result;
    }
    
    private SolutionState charToSolutionState(int c) {
        switch (c) {
            case '?': return SolutionState.UNKNOWN;
            case '-': return SolutionState.EMPTY;
            default: return SolutionState.FILLED;
        }
    }
}
