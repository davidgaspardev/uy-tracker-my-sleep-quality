package dev.davidgaspar.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.davidgaspar.trackmysleepquality.R
import dev.davidgaspar.trackmysleepquality.convertDurationToFormatted
import dev.davidgaspar.trackmysleepquality.convertNumericQualityToString
import dev.davidgaspar.trackmysleepquality.database.SleepNight
import dev.davidgaspar.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter: ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder.from(parent)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = getItem(position)
		holder.bind(item)
	}

	class ViewHolder private constructor (private val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(
			item: SleepNight,
		) {
			val res = itemView.context.resources
			binding.sleepLength.text =
				convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
			binding.qualityString.text =
				convertNumericQualityToString(item.sleepQuality, res)
			binding.qualityImage.setImageResource(
				when (item.sleepQuality) {
					0 -> R.drawable.ic_sleep_0
					1 -> R.drawable.ic_sleep_1
					2 -> R.drawable.ic_sleep_2
					3 -> R.drawable.ic_sleep_3
					4 -> R.drawable.ic_sleep_4
					5 -> R.drawable.ic_sleep_5
					else -> R.drawable.ic_sleep_active
				}
			)
		}

		companion object {
			fun from(parent: ViewGroup): ViewHolder {
				val layoutInflater = LayoutInflater.from(parent.context)
				val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
				return ViewHolder(binding)
			}
		}
	}
}

class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>() {
	override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
		return oldItem.id == newItem.id
	}

	override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
		return oldItem == newItem
	}
}