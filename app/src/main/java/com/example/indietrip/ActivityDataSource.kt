package com.example.indietrip

import android.content.Context

class ActivityDataSource(
    private val context: Context
) {
    fun loadActivities(selectedPreferences: List<String>): List<ActivityOption> {
        val activities = listOf(
            ActivityOption(
                id = 1,
                name = context.getString(R.string.activity_scenic_trail),
                preferences = listOf(
                    context.getString(R.string.preference_adventure),
                    context.getString(R.string.preference_nature),
                    context.getString(R.string.preference_mountain),
                    context.getString(R.string.preference_photography)
                ),
                duration = context.getString(R.string.duration_three_hours),
                difficulty = context.getString(R.string.difficulty_moderate),
                imageResource = R.drawable.activity_trail
            ),
            ActivityOption(
                id = 2,
                name = context.getString(R.string.activity_local_museum),
                preferences = listOf(
                    context.getString(R.string.preference_culture)
                ),
                duration = context.getString(R.string.duration_one_hour),
                difficulty = context.getString(R.string.difficulty_easy),
                imageResource = R.drawable.activity_museum
            ),
            ActivityOption(
                id = 3,
                name = context.getString(R.string.activity_riverside_picnic),
                preferences = listOf(
                    context.getString(R.string.preference_nature),
                    context.getString(R.string.preference_relaxation),
                    context.getString(R.string.preference_food)
                ),
                duration = context.getString(R.string.duration_two_hours),
                difficulty = context.getString(R.string.difficulty_easy),
                imageResource = R.drawable.activity_picnic
            ),
            ActivityOption(
                id = 4,
                name = context.getString(R.string.activity_camping_experience),
                preferences = listOf(
                    context.getString(R.string.preference_camping),
                    context.getString(R.string.preference_adventure),
                    context.getString(R.string.preference_nature)
                ),
                duration = context.getString(R.string.duration_full_day),
                difficulty = context.getString(R.string.difficulty_moderate),
                imageResource = R.drawable.activity_camping
            ),
            ActivityOption(
                id = 5,
                name = context.getString(R.string.activity_coastal_drive),
                preferences = listOf(
                    context.getString(R.string.preference_beach),
                    context.getString(R.string.preference_road_trip),
                    context.getString(R.string.preference_photography)
                ),
                duration = context.getString(R.string.duration_half_day),
                difficulty = context.getString(R.string.difficulty_easy),
                imageResource = R.drawable.activity_beach
            )
        )

        if (selectedPreferences.isEmpty()) {
            return activities
        }

        return activities.filter { activity ->
            activity.preferences.any { it in selectedPreferences }
        }
    }
}
