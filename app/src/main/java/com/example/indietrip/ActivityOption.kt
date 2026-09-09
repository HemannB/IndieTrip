package com.example.indietrip

data class ActivityOption(
    val id: Int,
    val name: String,
    val preferences: List<String>,
    val description: String,
    val durationHours: Int,
    val difficulty: String,
    val imageResource: Int
)
