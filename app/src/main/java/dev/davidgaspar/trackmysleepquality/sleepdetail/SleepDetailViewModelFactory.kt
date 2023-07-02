package dev.davidgaspar.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import dev.davidgaspar.trackmysleepquality.database.SleepDatabaseDao

class SleepDetailViewModelFactory(
	private val sleepNightId: Long,
	private val database: SleepDatabaseDao,
): ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if(modelClass.isAssignableFrom(SleepDetailViewModel::class.java)) {
			return SleepDetailViewModel(sleepNightId, database) as T
		}

		throw IllegalArgumentException("Unknown ViewModel class")
	}
}