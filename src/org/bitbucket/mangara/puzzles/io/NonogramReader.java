/*
 * Copyright 2018 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
package org.bitbucket.mangara.puzzles.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bitbucket.mangara.puzzles.data.Nonogram;
import org.bitbucket.mangara.puzzles.generators.NonogramGenerator;

public class NonogramReader {

    private static final Set<Character> FILLED_CHARS;

    static {
        FILLED_CHARS = new HashSet<>();
        FILLED_CHARS.add('X');
    }

    public static Nonogram readNonogram(Path inputFile) throws IOException {
        boolean[][] drawing = readDrawing(inputFile);
        return NonogramGenerator.generateNonogram(drawing);
    }

    private static boolean[][] readDrawing(Path inputFile) throws IOException {
        List<boolean[]> result = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                boolean[] drawingLine = new boolean[line.length()];

                for (int i = 0; i < line.length(); i++) {
                    drawingLine[i] = FILLED_CHARS.contains(line.charAt(i));
                }

                result.add(drawingLine);
            }
        }

        return result.toArray(new boolean[result.size()][result.get(0).length]);
    }
}
