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
package com.github.mangara.puzzles.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Nonogram implements Puzzle {

    private final List<List<Integer>> sideNumbers;
    private final List<List<Integer>> topNumbers;
    
    public Nonogram(List<List<Integer>> sideNumbers, List<List<Integer>> topNumbers) {
        if (sideNumbers == null || topNumbers == null) {
            throw new IllegalArgumentException("Numbers may not be null");
        }

        if (sideNumbers.isEmpty() || topNumbers.isEmpty()) {
            throw new IllegalArgumentException("Numbers may not be empty");
        }

        this.sideNumbers = deepImmutableCopy(sideNumbers);
        this.topNumbers = deepImmutableCopy(topNumbers);
    }
    
    @Override
    public PuzzleType getType() {
        return PuzzleType.NONOGRAM;
    }

    public List<List<Integer>> getSideNumbers() {
        return sideNumbers;
    }

    public List<List<Integer>> getTopNumbers() {
        return topNumbers;
    }

    public int getWidth() {
        return topNumbers.size();
    }

    public int getHeight() {
        return sideNumbers.size();
    }
    
    private List<List<Integer>> deepImmutableCopy(List<List<Integer>> original) {
        List<List<Integer>> result = new ArrayList<>(original.size());
        for (List<Integer> list : original) {
            List<Integer> dup = new ArrayList<>(list.size());
            Collections.copy(dup, list);
            result.add(Collections.unmodifiableList(dup));
        }
        return Collections.unmodifiableList(result);
    }
}
