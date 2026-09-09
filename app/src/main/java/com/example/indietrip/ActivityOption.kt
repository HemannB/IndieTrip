package com.example.indietrip

data class ActivityOption(
    val id: Int,
    val name: String,
    val preferences: List<String>,
    val duration: String,
    val difficulty: String,
    val imageResource: Int
)
