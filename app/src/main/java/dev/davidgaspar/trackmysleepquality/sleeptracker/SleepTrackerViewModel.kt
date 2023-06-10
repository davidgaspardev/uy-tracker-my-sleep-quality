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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import dev.davidgaspar.trackmysleepquality.database.SleepDatabaseDao
import dev.davidgaspar.trackmysleepquality.database.SleepNight
import dev.davidgaspar.trackmysleepquality.formatNights
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
		val database: SleepDatabaseDao,
		application: Application) : AndroidViewModel(application) {

	private val tonight = MutableLiveData<SleepNight?>()
	val nights = database.getAll()

	val nightsString = Transformations.map(nights) { nights ->
		formatNights(nights, application.resources)
	}

	val startButtonVisible = Transformations.map(tonight) {
		it == null
	}

	val stopButtonVisible = Transformations.map(tonight) {
		it != null
	}

	val clearButtonVisible = Transformations.map(nights) {
		it.isNotEmpty()
	}

	private var _showSnackBarEvent = MutableLiveData<Boolean>()

	val showSnakBarEvent: LiveData<Boolean>
		get() = _showSnackBarEvent

	fun doneShowingSnackBar() {
		_showSnackBarEvent.value = false
	}

	private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
	val navigateToSleepQuality: LiveData<SleepNight?>
		get() = _navigateToSleepQuality

	fun doneNavigation() {
		_navigateToSleepQuality.value = null
	}

	init {
		initializeTonight()
	}

	private fun initializeTonight() {
		viewModelScope.launch {
			tonight.value = getTonightFromDatabase()
		}
	}

	private suspend fun getTonightFromDatabase(): SleepNight? = withContext(Dispatchers.IO) {
		var night = database.getTonight()
		if (night?.endTimeMilli != night?.startTimeMilli) {
			night = null
		}

		night
	}

	fun onStartTracking() {
		viewModelScope.launch {
			val newNight = SleepNight()
			recordNight(newNight)
			tonight.value = getTonightFromDatabase()
		}
	}

	private suspend fun recordNight(night: SleepNight) = withContext(Dispatchers.IO)  {
		database.add(night)
	}

	fun onStopTracking() {
		viewModelScope.launch {
			val oldNight  = tonight.value ?: return@launch
			oldNight.endTimeMilli = System.currentTimeMillis()
			updateNight(oldNight)
			_navigateToSleepQuality.value = oldNight
		}
	}

	private suspend fun updateNight(night: SleepNight) = withContext(Dispatchers.IO) {
		database.update(night)
	}

	fun onClear() {
		viewModelScope.launch {
			clear()
			tonight.value = null
			_showSnackBarEvent.value = true
		}
	}

	private suspend fun clear() = withContext(Dispatchers.IO) {
		database.clear()
	}
}

