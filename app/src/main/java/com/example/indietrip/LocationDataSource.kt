package com.example.indietrip

import android.content.Context
import org.json.JSONArray

class LocationDataSource(
    private val context: Context
) {

    fun loadCountries(): List<Country> {
        val json = context.assets
            .open("countries.json")
            .bufferedReader()
            .use { it.readText() }

        val jsonArray = JSONArray(json)
        val countries = mutableListOf<Country>()

        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getJSONObject(i)

            countries.add(
                Country(
                    id = item.getInt("id"),
                    name = item.getString("name")
                )
            )
        }

        return countries
    }
}