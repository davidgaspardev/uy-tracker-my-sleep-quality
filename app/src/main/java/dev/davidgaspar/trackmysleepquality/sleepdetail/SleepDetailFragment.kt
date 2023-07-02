package dev.davidgaspar.trackmysleepquality.sleepdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.davidgaspar.trackmysleepquality.R
import dev.davidgaspar.trackmysleepquality.database.SleepDatabase
import dev.davidgaspar.trackmysleepquality.databinding.FragmentSleepDetailBinding

class SleepDetailFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val binding: FragmentSleepDetailBinding = DataBindingUtil.inflate(
			inflater, R.layout.fragment_sleep_detail, container, false
		)
		val argument = SleepDetailFragmentArgs.fromBundle(arguments!!)
		val database = SleepDatabase.getInstance(context!!).sleepDatabaseDao
		val sleepDetailViewModelFactory = SleepDetailViewModelFactory(argument.sleepNightId, database)
		val sleepDetailViewModel = ViewModelProvider(this, sleepDetailViewModelFactory)[
				SleepDetailViewModel::class.java
		]

		binding.sleepDetailViewModel = sleepDetailViewModel
		binding.lifecycleOwner = this

		sleepDetailViewModel.navigateToSleepTracker.observe(viewLifecycleOwner) {
			it?.let {
				if (it) {
					findNavController().navigate(
						SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
					)
					sleepDetailViewModel.doneNavigating()
				}
			}
		}

		return binding.root
	}
}