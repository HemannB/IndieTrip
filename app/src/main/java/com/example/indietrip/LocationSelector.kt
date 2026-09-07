package com.example.indietrip

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class LocationSelector(
    private val context: Context,
    private val inputCountry: AutoCompleteTextView,
    private val inputState: AutoCompleteTextView,
    private val inputCity: AutoCompleteTextView,
    private val dataSource: LocationDataSource
) {
    private val countries = dataSource.loadCountries()
    private val states = dataSource.loadStates()
    private var filteredStates = emptyList<State>()

    fun setup() {
        setupCountryAutocomplete()
        setupStateAutocomplete()
        setupCityAutocomplete()
    }

    private fun setupCountryAutocomplete() {
        val countryNames = countries.map { it.name }

        inputCountry.setAdapter(createAdapter(countryNames))

        inputCountry.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countries[position]

            filteredStates = states.filter {
                it.countryId == selectedCountry.id
            }

            inputState.setText("", false)
            inputCity.setText("", false)
            inputState.setAdapter(createAdapter(filteredStates.map { it.name }))
            inputState.isEnabled = filteredStates.isNotEmpty()
            inputCity.isEnabled = false
        }
    }

    private fun setupStateAutocomplete() {
        inputState.setAdapter(createAdapter(emptyList()))

        inputState.setOnItemClickListener { _, _, _, _ ->
            inputCity.isEnabled = true
        }
    }

    private fun setupCityAutocomplete() {
        val cities = listOf(
            "Santiago",
            "Santa Maria",
            "Porto Alegre",
            "Caxias do Sul"
        )

        inputCity.setAdapter(createAdapter(cities))
    }

    private fun createAdapter(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            items
        )
    }
}
