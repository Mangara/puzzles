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
package com.github.mangara.puzzles.data.nonogram;

import com.github.mangara.puzzles.data.CreatePuzzleSettings;
import com.github.mangara.puzzles.data.PuzzleType;

public class CreateNonogramSettings implements CreatePuzzleSettings {

    private final int width;
    private final int height;

    public CreateNonogramSettings(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public PuzzleType getType() {
        return PuzzleType.NONOGRAM;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
