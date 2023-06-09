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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.Delete

@Dao
interface SleepDatabaseDao {
    @Insert
    fun add(night: SleepNight)

    @Query("SELECT * FROM sleep_night WHERE id = :id")
    fun getById(id: Long): SleepNight?

    @Query("SELECT * FROM sleep_night ORDER BY id DESC")
    fun getAll(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM sleep_night ORDER BY id DESC LIMIT 1")
    fun getTonight(): SleepNight?

    @Update
    fun update(night: SleepNight)

    @Delete
    fun del(nights: List<SleepNight>): Int

    @Query("DELETE FROM sleep_night")
    fun clear()

    @Query("SELECT COUNT(*) FROM sleep_night")
    fun getCount(): Long
}
