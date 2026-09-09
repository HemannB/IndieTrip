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
                description = context.getString(R.string.description_scenic_trail),
                durationHours = 3,
                difficulty = context.getString(R.string.difficulty_moderate),
                imageResource = R.drawable.activity_trail
            ),
            ActivityOption(
                id = 2,
                name = context.getString(R.string.activity_local_museum),
                preferences = listOf(
                    context.getString(R.string.preference_culture)
                ),
                description = context.getString(R.string.description_local_museum),
                durationHours = 1,
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
                description = context.getString(R.string.description_riverside_picnic),
                durationHours = 2,
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
                description = context.getString(R.string.description_camping_experience),
                durationHours = 6,
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
                description = context.getString(R.string.description_coastal_drive),
                durationHours = 4,
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
