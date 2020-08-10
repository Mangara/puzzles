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
package com.github.mangara.puzzles.generators;

import com.github.mangara.puzzles.data.CreateLogiquizSettings;
import com.github.mangara.puzzles.data.Logiquiz;
import java.util.Collections;
import java.util.List;

public class LogiquizGenerator {

    public static final String PLACEHOLDER = "";
    
    public static Logiquiz create(CreateLogiquizSettings settings) {
        List<List<String>> groups = generateGroups(settings);
        List<String> clues = Collections.emptyList();
        return new Logiquiz(groups, clues);
    }

    private static List<List<String>> generateGroups(CreateLogiquizSettings settings) {
        List<String> emptyGroup = Collections.nCopies(settings.getGroupSize(), PLACEHOLDER);
        return Collections.nCopies(settings.getGroupCount(), emptyGroup);
    }
    
}
