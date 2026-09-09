package com.example.indietrip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class ActivityOptionAdapter(
    context: Context,
    private val activities: List<ActivityOption>,
    private val onSelectionChanged: (Int) -> Unit
) : BaseAdapter() {
    private val inflater = LayoutInflater.from(context)
    private val selectedActivityIds = mutableSetOf<Int>()

    override fun getCount(): Int = activities.size

    override fun getItem(position: Int): ActivityOption = activities[position]

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_activity_option, parent, false)
            holder = ViewHolder(
                image = view.findViewById(R.id.image_activity),
                name = view.findViewById(R.id.text_activity_name),
                preferences = view.findViewById(R.id.text_activity_preferences),
                details = view.findViewById(R.id.text_activity_details),
                selected = view.findViewById(R.id.check_activity)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val activity = getItem(position)
        holder.image.setImageResource(activity.imageResource)
        holder.image.contentDescription = activity.name
        holder.name.text = activity.name
        holder.preferences.text = activity.preferences.joinToString(" • ")
        holder.details.text = view.context.getString(
            R.string.activity_details,
            activity.duration,
            activity.difficulty
        )

        holder.selected.setOnCheckedChangeListener(null)
        holder.selected.isChecked = activity.id in selectedActivityIds
        holder.selected.setOnCheckedChangeListener { _, isChecked ->
            setSelected(activity.id, isChecked)
        }

        view.setOnClickListener {
            setSelected(activity.id, activity.id !in selectedActivityIds)
        }

        return view
    }

    fun selectedActivities(): List<ActivityOption> {
        return activities.filter { it.id in selectedActivityIds }
    }

    fun clearSelection() {
        selectedActivityIds.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    private fun setSelected(activityId: Int, isSelected: Boolean) {
        if (isSelected) {
            selectedActivityIds.add(activityId)
        } else {
            selectedActivityIds.remove(activityId)
        }

        notifyDataSetChanged()
        onSelectionChanged(selectedActivityIds.size)
    }

    private data class ViewHolder(
        val image: ImageView,
        val name: TextView,
        val preferences: TextView,
        val details: TextView,
        val selected: CheckBox
    )
}
