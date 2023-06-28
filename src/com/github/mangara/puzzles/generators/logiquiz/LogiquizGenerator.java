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
package com.github.mangara.puzzles.generators.logiquiz;

import com.github.mangara.puzzles.data.logiquiz.CreateLogiquizSettings;
import com.github.mangara.puzzles.data.logiquiz.Logiquiz;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogiquizGenerator {
    
    public static Logiquiz create(CreateLogiquizSettings settings) {
        List<List<String>> groups = generateGroups(settings);
        List<String> clues = Collections.emptyList();
        return new Logiquiz(groups, clues);
    }

    private static List<List<String>> generateGroups(CreateLogiquizSettings settings) {
        List<List<String>> groups = new ArrayList<>(settings.getGroupCount());
        
        for (int groupNumber = 0; groupNumber < settings.getGroupCount(); groupNumber++) {
            groups.add(generateGroup(settings, groupNumber));
        }
        
        return groups;
    }

    private static List<String> generateGroup(CreateLogiquizSettings settings, int groupNumber) {
        List<String> group = new ArrayList<>(settings.getGroupSize());
        
        for (int entryNumber = 0; entryNumber < settings.getGroupSize(); entryNumber++) {
            String entry = Character.toString('A' + groupNumber) + Integer.toString(entryNumber);
            group.add(entry);
        }
        
        return group;
    }
}
