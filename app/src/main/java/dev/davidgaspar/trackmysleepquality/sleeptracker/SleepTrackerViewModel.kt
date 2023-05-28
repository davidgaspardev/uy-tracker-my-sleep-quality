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

package dev.davidgaspar.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.davidgaspar.trackmysleepquality.database.SleepDatabaseDao
import dev.davidgaspar.trackmysleepquality.database.SleepNight
import dev.davidgaspar.trackmysleepquality.formatNights
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
		val database: SleepDatabaseDao,
		application: Application) : AndroidViewModel(application) {

	// With this object (Job form kotlinx) I can cancel all coroutine in this scope.
	private var viewModelJob = Job()
	private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
	private val tonight = MutableLiveData<SleepNight?>()
	private val nights = database.getAll()

	val nightsString = Transformations.map(nights) { nights ->
		formatNights(nights, application.resources)
	}

	init {
		initializeTonight()
	}

	private fun initializeTonight() {
		uiScope.launch {
			tonight.value = getTonightFromDatabase()
		}
	}

	private suspend fun getTonightFromDatabase(): SleepNight? {
		return withContext(Dispatchers.IO) {
			var night = database.getTonight()
			if (night?.endTimeMilli != night?.startTimeMilli) {
				night = null
			}

			night
		}
	}

	fun onStartTracking() {
		uiScope.launch {
			val newNight = SleepNight()
			recordNight(newNight)
			tonight.value = getTonightFromDatabase()
		}
	}

	private suspend fun recordNight(night: SleepNight) {
		withContext(Dispatchers.IO) {
			database.add(night)
		}
	}

	fun onStopTracking() {
		uiScope.launch {
			val oldNight  = tonight.value ?: return@launch
			oldNight.endTimeMilli = System.currentTimeMillis()
			updateNight(oldNight)
		}
	}

	private suspend fun updateNight(night: SleepNight) {
		withContext(Dispatchers.IO) {
			database.update(night)
		}
	}

	override fun onCleared() {
		super.onCleared()
		this.viewModelJob.cancel()
	}

	fun onClear() {
		uiScope.launch {
			clear()
			tonight.value = null
		}
	}

	private suspend fun clear() {
		withContext(Dispatchers.IO) {
			database.clear()
		}
	}
}

