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
package com.github.mangara.puzzles.io.logiquiz;

import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogiquizReader {

    public static boolean isLogiquiz(Path file) throws IOException {
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            int groupCount = 0;
            int firstGroupSize = -1;
            int currentGroupSize = 0;
            
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                if (line.isBlank()) {
                    if (currentGroupSize > 0) {
                        groupCount++;
                        
                        if (firstGroupSize < 0) {
                            firstGroupSize = currentGroupSize;
                        } else if (currentGroupSize != firstGroupSize) {
                            return false;
                        }

                        currentGroupSize = 0;
                    }
                } else {
                    currentGroupSize++;
                }
            }
            
            return groupCount > 0;
        }
    }

    public static Logiquiz readLogiquiz(Path file) throws IOException {
        List<List<String>> groups = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            readGroups(groups, in);
        }

        return new Logiquiz(groups, Collections.emptyList());
    }

    private static void readGroups(List<List<String>> groups, BufferedReader in) throws IOException {
        List<String> currentGroup = new ArrayList<>();

        for (String line = in.readLine(); line != null; line = in.readLine()) {
            if (line.isEmpty()) {
                if (!currentGroup.isEmpty()) {
                    groups.add(currentGroup);
                    currentGroup = new ArrayList<>();
                }
            } else {
                currentGroup.add(line);
            }
        }

        if (!currentGroup.isEmpty()) {
            groups.add(currentGroup);
        }
    }

}
