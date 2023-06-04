/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you ay not use this file except in compliance with the License.
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

package dev.davidgaspar.trackmysleepquality.sleepquality

import dev.davidgaspar.trackmysleepquality.database.SleepDatabaseDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepQualityViewModel(
		private val sleepNightKey: Long = 0L,
		val database: SleepDatabaseDao
) : ViewModel() {

	private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
	val navigateToSleepTracker: LiveData<Boolean?>
		get() = _navigateToSleepTracker

	fun doneNavigating() {
		_navigateToSleepTracker.value = null
	}

	fun onSetSleepQuality(quality: Int) {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				val tonight = database.getById(sleepNightKey) ?: return@withContext
				tonight.sleepQuality = quality
				database.update(tonight)
			}
			_navigateToSleepTracker.value = true
		}
	}
}
