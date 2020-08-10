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
package com.github.mangara.puzzles.data;

public class CreateLogiquizSettings implements CreatePuzzleSettings {
    
    private final int groupCount;
    private final int groupSize;

    public CreateLogiquizSettings(int groupCount, int groupSize) {
        this.groupCount = groupCount;
        this.groupSize = groupSize;
    }
    
    @Override
    public PuzzleType getType() {
        return PuzzleType.LOGIQUIZ;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public int getGroupSize() {
        return groupSize;
    }
}
