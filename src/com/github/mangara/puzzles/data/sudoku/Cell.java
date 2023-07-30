/*
 * Copyright 2023 Sander Verdonschot <sander.verdonschot at gmail.com>.
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

public class Cell {
    public final int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int box() {
        return 3 * (row / 3) + col / 3;
    }
    
    public int displayRow() {
        return row + 1;
    }
    
    public int displayCol() {
        return col + 1;
    }
    
    public int displayBox() {
        return box() + 1;
    }
    
    @Override
    public String toString() {
        return "r" + displayRow() + "c" + displayCol();
    }
}
