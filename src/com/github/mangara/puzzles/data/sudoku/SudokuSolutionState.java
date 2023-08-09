/*
 * Copyright 2023 Sander Verdonschot.
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

package com.github.mangara.puzzles.data.sudoku;

import java.util.Arrays;

public class SudokuSolutionState {
    public static int BLANK = 0;
    
    public boolean given;
    public int digit;
    public boolean[] possible; // possible[3] = true => this square can contain a 4
    public boolean[] pencilmark;

    public SudokuSolutionState() {
        this(BLANK);
    }
    
    public SudokuSolutionState(int number) {
        this(number, false);
    }
    
    public SudokuSolutionState(int number, boolean given) {
        this.digit = number;
        this.given = given;
       
        possible = new boolean[9];
        if (number == BLANK) {
            Arrays.fill(possible, true);
        } else {
            Arrays.fill(possible, false);
            possible[number - 1] = true;
        }
        
        pencilmark = new boolean[9];
        Arrays.fill(pencilmark, false);
    }
    
    public SudokuSolutionState(SudokuSolutionState state) {
        this.digit = state.digit;
        this.given = state.given;
       
        possible = new boolean[9];
        System.arraycopy(state.possible, 0, possible, 0, 9);
        
        pencilmark = new boolean[9];
        System.arraycopy(state.pencilmark, 0, pencilmark, 0, 9);
    }

    public boolean isPossible(int digit) {
        return possible[digit - 1];
    }
    
    public void setPossible(int digit, boolean possible) {
        this.possible[digit - 1] = possible;
    }
    
    public void removePossible(int digit) {
        if (this.digit != digit) {
            setPossible(digit, false);
        }
    }
    
    public void setDigit(int digit) {
        this.digit = digit;
        
        Arrays.fill(possible, false);
        setPossible(digit, true);
    }
}
