package com.example.indietrip

data class Country(
    val id: Int,
    val name: String
)

data class State(
    val id: Int,
    val name: String,
    val countryId: Int
)

data class City(
    val id: Int,
    val name: String,
    val stateId: Int
)
