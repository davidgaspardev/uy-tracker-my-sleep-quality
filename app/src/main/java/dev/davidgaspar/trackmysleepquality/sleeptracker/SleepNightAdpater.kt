package dev.davidgaspar.trackmysleepquality.sleeptracker

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.davidgaspar.trackmysleepquality.database.SleepNight

class SleepNightAdpater: RecyclerView.Adapter<TextItemViewHolder>() {
	var data = listOf<SleepNight>()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
		return TextItemViewHolder(TextView(parent.context))
	}

	override fun getItemCount() = data.size

	override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
		val item = data[position]
		holder.textView.text = item.sleepQuality.toString()
	}
}