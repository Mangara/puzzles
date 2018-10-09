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
package org.bitbucket.mangara.puzzles.generators;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bitbucket.mangara.puzzles.data.Nonogram;
import org.bitbucket.mangara.puzzles.io.NonogramPrinter;

public class NonogramGenerator {

    private static final String inputFile = "in.txt";
    private static final String outputFile = "out.png";

    private static final Set<Character> filledChars;

    static {
        filledChars = new HashSet<>();
        filledChars.add('X');
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        boolean[][] drawing = readFile(inputFile);
        Nonogram puzzle = generateNonogram(drawing);
        NonogramPrinter.printNonogram(puzzle, Paths.get(outputFile));
    }

    private static boolean[][] readFile(String inputFile) throws IOException {
        List<boolean[]> result = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8)) {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                boolean[] drawingLine = new boolean[line.length()];

                for (int i = 0; i < line.length(); i++) {
                    drawingLine[i] = filledChars.contains(line.charAt(i));
                }

                result.add(drawingLine);
            }
        }

        return result.toArray(new boolean[result.size()][result.get(0).length]);
    }

    public static Nonogram generateNonogram(boolean[][] drawing) {
        List<List<Integer>> side = computeSideNumbers(drawing);
        List<List<Integer>> top = computeTopNumbers(drawing);
        return new Nonogram(side, top);
    }

    public static List<List<Integer>> computeSideNumbers(boolean[][] drawing) {
        List<List<Integer>> result = new ArrayList<>();

        for (boolean[] row : drawing) {
            List<Integer> resultRow = new ArrayList<>();
            int filledCount = 0;

            for (int i = 0; i < row.length; i++) {
                if (row[i]) {
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

    public static List<List<Integer>> computeTopNumbers(boolean[][] drawing) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < drawing[0].length; i++) {
            List<Integer> resultColumn = new ArrayList<>();
            int filledCount = 0;

            for (int j = 0; j < drawing.length; j++) {
                if (drawing[j][i]) {
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
