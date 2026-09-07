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
    private val cities = dataSource.loadCities()
    private var filteredStates = emptyList<State>()
    private var filteredCities = emptyList<City>()

    fun setup() {
        setupCountryAutocomplete()
        setupStateAutocomplete()
        setupCityAutocomplete()
    }

    private fun setupCountryAutocomplete() {
        val countryNames = countries.map { it.name }

        inputCountry.setAdapter(createAdapter(countryNames))

        inputCountry.setOnClickListener {
            inputCountry.showDropDown()
        }

        inputCountry.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countries[position]

            filteredStates = states.filter {
                it.countryId == selectedCountry.id
            }
            filteredCities = emptyList()

            inputState.setText("", false)
            inputCity.setText("", false)
            inputState.setAdapter(createAdapter(filteredStates.map { it.name }))
            inputCity.setAdapter(createAdapter(emptyList()))
            inputState.isEnabled = filteredStates.isNotEmpty()
            inputCity.isEnabled = false
        }
    }

    private fun setupStateAutocomplete() {
        inputState.setAdapter(createAdapter(emptyList()))

        inputState.setOnClickListener {
            inputState.showDropDown()
        }

        inputState.setOnItemClickListener { _, _, position, _ ->
            val selectedState = filteredStates[position]

            filteredCities = cities.filter {
                it.stateId == selectedState.id
            }

            inputCity.setText("", false)
            inputCity.setAdapter(createAdapter(filteredCities.map { it.name }))
            inputCity.isEnabled = filteredCities.isNotEmpty()
        }
    }

    private fun setupCityAutocomplete() {
        inputCity.setAdapter(createAdapter(emptyList()))

        inputCity.setOnClickListener {
            inputCity.showDropDown()
        }
    }

    private fun createAdapter(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            items
        )
    }
}
