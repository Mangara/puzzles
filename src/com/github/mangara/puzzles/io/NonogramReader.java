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
package com.github.mangara.puzzles.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.github.mangara.puzzles.data.SolvedNonogram;
import com.github.mangara.puzzles.generators.NonogramGenerator;
import java.util.HashSet;
import java.util.Set;

public class NonogramReader {

    /**
     * Checks whether the given file is likely to be a Nonogram.
     * 
     * It uses two key features: nonogram files have no blank lines, and they
     * only contain two non-whitespace characters (empty and filled squares).
     * 
     * @param file
     * @return
     * @throws IOException 
     */
    public static boolean isNonogram(Path file) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            Set<Character> characters = new HashSet<>();
            int blankLines = 0;
            
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (line.isBlank()) {
                    blankLines++;
                }
                
                for (char c : line.toCharArray()) {
                    if (!Character.isWhitespace(c)) {
                        characters.add(c);
                    }
                }
            }
            
            characters.remove(NonogramWriter.FILLED);
            characters.remove(NonogramWriter.EMPTY);
            
            return blankLines == 0 && characters.isEmpty();
        }
    }
    
    public static SolvedNonogram readNonogram(Path inputFile) throws IOException {
        boolean[][] drawing = readDrawing(inputFile);
        return NonogramGenerator.generateNonogram(drawing);
    }

    private static boolean[][] readDrawing(Path inputFile) throws IOException {
        List<boolean[]> result = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(inputFile, StandardCharsets.UTF_8)) {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                result.add(readLine(line));
            }
        }

        return toDrawing(result);
    }
    
    private static boolean[] readLine(String line) {
        boolean[] drawingLine = new boolean[line.length()];

        for (int i = 0; i < line.length(); i++) {
            drawingLine[i] = (line.charAt(i) == NonogramWriter.FILLED);
        }

        return drawingLine;
    }
    
    private static boolean[][] toDrawing(List<boolean[]> lines) {
        boolean[][] result = new boolean[lines.get(0).length][lines.size()];
        
        for (int i = 0; i < lines.get(0).length; i++) {
            for (int j = 0; j < lines.size(); j++) {
                result[i][j] = lines.get(j)[i];
            }
        }
        
        return result;
    }
}
