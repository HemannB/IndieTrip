package com.example.indietrip

data class PlannedActivity(
    val name: String,
    val preferences: List<String>,
    val durationHours: Int,
    val difficulty: String,
    val startTime: String,
    val people: Int,
    val equipmentReminder: Boolean,
    val imageResource: Int
)
