package com.example.indietrip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class TripSummaryAdapter(
    context: Context,
    private val activities: List<PlannedActivity>
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = activities.size

    override fun getItem(position: Int): PlannedActivity = activities[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_trip_summary, parent, false)
            holder = ViewHolder(
                image = view.findViewById(R.id.image_summary_activity),
                name = view.findViewById(R.id.text_summary_activity_name),
                preferences = view.findViewById(
                    R.id.text_summary_activity_preferences
                ),
                details = view.findViewById(R.id.text_summary_activity_details),
                equipment = view.findViewById(
                    R.id.text_summary_activity_equipment
                )
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val activity = getItem(position)
        val duration = view.resources.getQuantityString(
            R.plurals.hours,
            activity.durationHours,
            activity.durationHours
        )
        val people = view.resources.getQuantityString(
            R.plurals.people_count,
            activity.people,
            activity.people
        )

        holder.image.setImageResource(activity.imageResource)
        holder.image.contentDescription = activity.name
        holder.name.text = activity.name
        holder.preferences.text = activity.preferences.joinToString(" • ")
        holder.details.text = view.context.getString(
            R.string.planned_activity_details,
            duration,
            activity.difficulty,
            activity.startTime,
            people
        )
        holder.equipment.visibility = if (activity.equipmentReminder) {
            View.VISIBLE
        } else {
            View.GONE
        }

        return view
    }

    private data class ViewHolder(
        val image: ImageView,
        val name: TextView,
        val preferences: TextView,
        val details: TextView,
        val equipment: TextView
    )
}
