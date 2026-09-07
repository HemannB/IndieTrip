package com.example.indietrip

import android.content.Context
import org.json.JSONArray

class LocationDataSource(
    private val context: Context
) {

    fun loadCountries(): List<Country> {
        val items = loadJsonArray("countries.json")

        return List(items.length()) { index ->
            val item = items.getJSONObject(index)

            Country(
                id = item.getInt("id"),
                name = item.getString("name")
            )
        }
    }

    fun loadStates(): List<State> {
        val items = loadJsonArray("states.json")

        return List(items.length()) { index ->
            val item = items.getJSONObject(index)

            State(
                id = item.getInt("id"),
                name = item.getString("name"),
                countryId = item.getInt("country_id")
            )
        }
    }

    private fun loadJsonArray(fileName: String): JSONArray {
        val json = context.assets
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }

        return JSONArray(json)
    }
}
