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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static <E> List<E> immutableCopy(List<E> original) {
        return Collections.unmodifiableList(new ArrayList<>(original));
    }
    
    public static <E> List<List<E>> deepImmutableCopy(List<List<E>> original) {
        List<List<E>> result = new ArrayList<>(original.size());
        for (List<E> list : original) {
            result.add(immutableCopy(list));
        }
        return Collections.unmodifiableList(result);
    }
    
}
