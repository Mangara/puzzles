/*
 * Copyright 2016 Sander Verdonschot <sander.verdonschot at gmail.com>.
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
package org.bitbucket.mangara.puzzles.io;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class FontDimensions {

    public static Rectangle2D computeMaxTextDimension(Iterable<String> words, FontRenderContext frc, Font font) {
        double maxTextHeight = 0;
        double maxTextWidth = 0;

        for (String word : words) {
            TextLayout text = new TextLayout(word, font, frc);
            maxTextHeight = Math.max(text.getBounds().getHeight(), maxTextHeight);
            maxTextWidth = Math.max(text.getBounds().getWidth(), maxTextWidth);
        }

        return new Rectangle2D.Double(0, 0, maxTextWidth, maxTextHeight);
    }

    private FontDimensions() {
    }
}
