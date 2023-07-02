package dev.davidgaspar.trackmysleepquality.sleepdetail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.davidgaspar.trackmysleepquality.database.SleepDatabaseDao
import dev.davidgaspar.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepDetailViewModel(
	private val sleepNightId: Long,
	private val database: SleepDatabaseDao,
): ViewModel() {

	init {
		viewModelScope.launch {
			withContext(Dispatchers.IO) {
				val tonight = database.getById(sleepNightId) ?: return@withContext
				night.postValue(tonight)
			}
		}
	}

	private val night = MutableLiveData<SleepNight>()
	fun getNight() = night

	private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
	val navigateToSleepTracker: LiveData<Boolean?>
		get() = _navigateToSleepTracker

	fun doneNavigating() {
		_navigateToSleepTracker.value = null
	}

	fun onClose() {
		_navigateToSleepTracker.value = true
	}
}