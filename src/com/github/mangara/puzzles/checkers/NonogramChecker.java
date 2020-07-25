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
package com.github.mangara.puzzles.checkers;

import com.github.mangara.puzzles.data.Nonogram;
import java.util.ArrayList;
import java.util.List;

public class NonogramChecker {

    public static boolean isValidSolution(Nonogram nonogram, boolean[][] drawing) {
        return validColumns(nonogram, drawing) && validRows(nonogram, drawing);
    }

    private static boolean validColumns(Nonogram nonogram, boolean[][] drawing) {
        for (int col = 0; col < nonogram.getWidth(); col++) {
            List<Integer> numbers = nonogram.getTopNumbers().get(col);
            boolean[] solution = drawing[col];
            
            if (!validBasicSolution(numbers, solution)) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean validRows(Nonogram nonogram, boolean[][] drawing) {
        for (int row = 0; row < nonogram.getHeight(); row++) {
            List<Integer> numbers = nonogram.getSideNumbers().get(row);
            
            boolean[] solution = new boolean[nonogram.getWidth()];
            for (int i = 0; i < nonogram.getWidth(); i++) {
                solution[i] = drawing[i][row];
            }
            
            if (!validBasicSolution(numbers, solution)) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean validBasicSolution(List<Integer> numbers, boolean[] solution) {
        return numbers.equals(extractBlocks(solution));
    }

    private static List<Integer> extractBlocks(boolean[] solution) {
        List<Integer> result = new ArrayList<>();

        int currentBlockLength = 0;

        for (int i = 0; i < solution.length; i++) {
            if (solution[i]) {
                currentBlockLength++;
            } else {
                if (currentBlockLength > 0) {
                    result.add(currentBlockLength);
                }
                currentBlockLength = 0;
            }
        }

        if (currentBlockLength > 0 || result.isEmpty()) {
            result.add(currentBlockLength);
        }

        return result;
    }
}
