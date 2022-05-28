/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.example.recyclersample.data

import android.content.res.Resources
import com.example.recyclersample.R

/* Returns initial list of Armies. */
fun ArmyList(resources: Resources): List<Army> {
    val armies: MutableList<Army> = mutableListOf()
    var id = 1L
    for (armyName in resources.getStringArray(R.array.armies)) {
        armies.add(
            Army(
                id,
                armyName,
                R.drawable.rose,
                resources.getString(R.string.flower1_description),
                false,
                false,
                false
            )
        )
        id += 1;
    }
    return armies;
}
