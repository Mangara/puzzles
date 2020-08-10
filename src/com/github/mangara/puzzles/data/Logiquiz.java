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
package com.github.mangara.puzzles.data;

import java.util.List;

public class Logiquiz implements Puzzle {

    private final List<List<String>> groups;
    private final List<String> clues;
    private final int groupCount;
    private final int groupSize;

    public Logiquiz(List<List<String>> groups, List<String> clues) {
        validate(groups);
        
        this.groups = ListUtils.deepImmutableCopy(groups);
        this.groupCount = groups.size();
        this.groupSize = groups.get(0).size();
        this.clues = ListUtils.immutableCopy(clues);
    }
    
    @Override
    public PuzzleType getType() {
        return PuzzleType.LOGIQUIZ;
    }

    public List<List<String>> getGroups() {
        return groups;
    }

    public List<String> getClues() {
        return clues;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public int getGroupSize() {
        return groupSize;
    }
    
    private void validate(List<List<String>> groups) {
        validateGroupCount(groups);
        validateGroupSizes(groups);
    }

    private void validateGroupCount(List<List<String>> groups) {
        if (groups.size() < 2) {
            throw new IllegalArgumentException("A logiquiz requires at least 2 groups");
        }
    }

    private void validateGroupSizes(List<List<String>> groups) {
        int firstSize = groups.get(0).size();
        for (List<String> group : groups) {
            if (group.size() < 2) {
                throw new IllegalArgumentException("All groups in a logiquiz require at least 2 entries");
            }
            if (group.size() != firstSize) {
                throw new IllegalArgumentException("All groups in a logiquiz must have the same number of entries");
            }
        }
    }
}
