/*
 * Copyright 2016 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
package com.github.mangara.puzzles.generators;

import com.github.mangara.puzzles.data.CreateNonogramSettings;
import java.util.ArrayList;
import java.util.List;
import com.github.mangara.puzzles.data.SolvedNonogram;

public class NonogramGenerator {

    public static SolvedNonogram create(CreateNonogramSettings settings) {
        boolean[][] drawing = new boolean[settings.getWidth()][settings.getHeight()];
        return generateNonogram(drawing);
    }
    
    public static SolvedNonogram generateNonogram(boolean[][] drawing) {
        List<List<Integer>> side = computeSideNumbers(drawing);
        List<List<Integer>> top = computeTopNumbers(drawing);
        return new SolvedNonogram(side, top, drawing);
    }

    private static List<List<Integer>> computeSideNumbers(boolean[][] drawing) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < drawing[0].length; i++) {
            List<Integer> resultRow = new ArrayList<>();
            int filledCount = 0;

            for (int j = 0; j < drawing.length; j++) {
                if (drawing[j][i]) {
                    filledCount++;
                } else if (filledCount > 0) {
                    resultRow.add(filledCount);
                    filledCount = 0;
                }
            }

            if (filledCount > 0) {
                resultRow.add(filledCount);
            } else if (resultRow.isEmpty()) {
                resultRow.add(0);
            }

            result.add(resultRow);
        }

        return result;
    }

    private static List<List<Integer>> computeTopNumbers(boolean[][] drawing) {
        List<List<Integer>> result = new ArrayList<>();

        for (boolean[] column : drawing) {
            List<Integer> resultColumn = new ArrayList<>();
            int filledCount = 0;

            for (int i = 0; i < column.length; i++) {
                if (column[i]) {
                    filledCount++;
                } else if (filledCount > 0) {
                    resultColumn.add(filledCount);
                    filledCount = 0;
                }
            }

            if (filledCount > 0) {
                resultColumn.add(filledCount);
            } else if (resultColumn.isEmpty()) {
                resultColumn.add(0);
            }

            result.add(resultColumn);
        }

        return result;
    }
}
