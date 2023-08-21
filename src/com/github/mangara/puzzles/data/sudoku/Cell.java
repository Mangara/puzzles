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

import java.util.Collection;
import java.util.Iterator;

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
    
    public static String toString(Collection<Cell> cells) {
        StringBuilder sb = new StringBuilder();
        
        if (cells.size() == 1) {
            sb.append(cells.iterator().next().toString());
        } else {
            sb.append("cells ");

            int i = 0;
            for (Cell cell : cells) {
                sb.append(cell.toString());
                
                if (i == cells.size() - 2) {
                    if (cells.size() > 2) {
                        sb.append(",");
                    }
                    sb.append(" and ");
                } else if (i < cells.size() - 2) {
                    sb.append(", ");
                }
                
                i++;
            }
        }
        
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.row;
        hash = 17 * hash + this.col;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        if (this.row != other.row) {
            return false;
        }
        return this.col == other.col;
    }
}
