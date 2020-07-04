/*
 * Copyright 2020 Sander Verdonschot.
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Saves a nonogram as a plain text file.
 */
public class NonogramWriter {
    
    static final char FILLED = 'X';
    static final char EMPTY = '-';
    
    public void save(boolean[][] drawing, Path file) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file)) {
            for (int row = 0; row < drawing[0].length; row++) {
                writeRow(drawing, row, out);
            }
        }
    }

    private void writeRow(boolean[][] drawing, int row, BufferedWriter out) throws IOException {
        for (int i = 0; i < drawing.length; i++) {
            out.write(squareToChar(drawing[i][row]));
        }
        out.newLine();
    }

    private char squareToChar(boolean value) {
        if (value) {
            return FILLED;
        } else {
            return EMPTY;
        }
    }
}
