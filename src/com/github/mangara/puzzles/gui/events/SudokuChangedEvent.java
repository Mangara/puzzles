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
package com.github.mangara.puzzles.gui.events;

public class SudokuChangedEvent {

    public static final SudokuChangedEvent GLOBAL = new SudokuChangedEvent(-1, -1, -1, -1);

    private final int row;
    private final int col;
    private final int oldDigit;
    private final int newDigit;

    public SudokuChangedEvent(int row, int col, int oldDigit, int newDigit) {
        this.row = row;
        this.col = col;
        this.oldDigit = oldDigit;
        this.newDigit = newDigit;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getOldDigit() {
        return oldDigit;
    }

    public int getNewDigit() {
        return newDigit;
    }
}
