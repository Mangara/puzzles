/*
 * Copyright 2020 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
package com.github.mangara.puzzles.data.nonogram;

import com.github.mangara.puzzles.checkers.nonogram.NonogramChecker;
import java.util.List;

public class SolvedNonogram extends Nonogram {

    private final boolean[][] drawing;
    
    public SolvedNonogram(List<List<Integer>> sideNumbers, List<List<Integer>> topNumbers, boolean[][] drawing) {
        super(sideNumbers, topNumbers);

        // Check solution
        if (!NonogramChecker.isValidSolution(this, drawing)) {
            throw new IllegalArgumentException("Invalid solution");
        }
        
        this.drawing = deepCopy(drawing);
    }
    
    public SolvedNonogram(Nonogram nonogram, boolean[][] drawing) {
        this(nonogram.getSideNumbers(), nonogram.getTopNumbers(), drawing);
    }

    public boolean[][] getDrawing() {
        return deepCopy(drawing);
    }

    private boolean[][] deepCopy(boolean[][] original) {
        int n = original.length, m = original[0].length;
        boolean[][] result = new boolean[n][m];
        
        for (int i = 0; i < n; i++) {
            result[i] = new boolean[m];
            System.arraycopy(original[i], 0, result[i], 0, m);
        }
        
        return result;
    }
}
