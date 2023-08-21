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
package com.github.mangara.puzzles.solvers.sudoku;

import com.github.mangara.puzzles.data.sudoku.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Region {

    public enum Type {
        ROW, COLUMN, BOX;
    }

    public final Type type;
    public final int index;
    public final List<Cell> cells;

    public Region(Type type, int index, List<Cell> cells) {
        this.type = type;
        this.index = index;
        this.cells = Collections.unmodifiableList(cells);
    }

    public static Region row(int row) {
        List<Cell> cells = new ArrayList<>(9);

        for (int col = 0; col < 9; col++) {
            cells.add(new Cell(row, col));
        }

        return new Region(Type.ROW, row, cells);
    }

    public static Region col(int col) {
        List<Cell> cells = new ArrayList<>(9);

        for (int row = 0; row < 9; row++) {
            cells.add(new Cell(row, col));
        }

        return new Region(Type.COLUMN, col, cells);
    }

    public static Region box(int box) {
        List<Cell> cells = new ArrayList<>(9);

        int boxRow = 3 * (box / 3);
        int boxCol = 3 * (box % 3);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                cells.add(new Cell(boxRow + r, boxCol + c));
            }
        }

        return new Region(Type.BOX, box, cells);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        switch (type) {
            case ROW:
                sb.append("row ");
                break;
            case COLUMN:
                sb.append("column ");
                break;
            case BOX:
                sb.append("box ");
                break;
            default:
                throw new InternalError("Unknown region type: " + type);
        }
        
        sb.append(Integer.toString(index + 1));
        
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.type);
        hash = 67 * hash + this.index;
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
        final Region other = (Region) obj;
        if (this.index != other.index) {
            return false;
        }
        return this.type == other.type;
    }
}
