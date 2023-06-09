/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.davidgaspar.trackmysleepquality.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

// TODO (01) Create the SleepNight class.
@Entity(tableName = "sleep_night")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time_milli")
	var endTimeMilli: Long = startTimeMilli,
    @ColumnInfo(name = "sleep_rating")
    var sleepQuality: Int = -1
)