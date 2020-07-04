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
package com.github.mangara.puzzles;

import java.io.IOException;
import java.nio.file.Paths;
import com.github.mangara.puzzles.data.Nonogram;
import com.github.mangara.puzzles.io.NonogramPrinter;
import com.github.mangara.puzzles.io.NonogramReader;

public class NonogramMain {

    private static final String INPUT_FILE = "in.txt";
    private static final String OUTPUT_FILE = "out.png";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Nonogram puzzle = NonogramReader.readNonogram(Paths.get(INPUT_FILE));
        NonogramPrinter.printNonogram(puzzle, Paths.get(OUTPUT_FILE));
    }
}
