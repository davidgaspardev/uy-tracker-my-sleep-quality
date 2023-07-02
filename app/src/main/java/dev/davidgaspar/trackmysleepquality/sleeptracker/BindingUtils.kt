package dev.davidgaspar.trackmysleepquality.sleeptracker

import android.widget.TextView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import dev.davidgaspar.trackmysleepquality.R
import dev.davidgaspar.trackmysleepquality.convertDurationToFormatted
import dev.davidgaspar.trackmysleepquality.convertNumericQualityToString
import dev.davidgaspar.trackmysleepquality.database.SleepNight

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) {
	item?.let {
		text = convertDurationToFormatted(it.startTimeMilli, it.endTimeMilli, resources)
	}
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?) {
	item?.let {
		text = convertNumericQualityToString(it.sleepQuality, resources)
	}
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?) {
	item?.let {
		setImageResource(
			when (it.sleepQuality) {
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
}
