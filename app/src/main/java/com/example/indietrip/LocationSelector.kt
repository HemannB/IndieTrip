package com.example.indietrip

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class LocationSelector(
    private val context: Context,
    private val inputCountry: AutoCompleteTextView,
    private val inputState: AutoCompleteTextView,
    private val inputCity: AutoCompleteTextView
) {

    fun setup() {
        setupCountryAutocomplete()
        setupStateAutocomplete()
        setupCityAutocomplete()
    }

    private fun setupCountryAutocomplete() {
        val countries = listOf(
            "Brazil",
            "Canada",
            "China",
            "Japan",
            "Jamaica",
            "Mexico",
            "Portugal"
        )

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            countries
        )

        inputCountry.setAdapter(adapter)

        inputCountry.setOnItemClickListener { _, _, _, _ ->
            inputState.isEnabled = true
        }
    }

    private fun setupStateAutocomplete() {
        val states = listOf(
            "Rio Grande do Sul",
            "Santa Catarina",
            "Paraná",
            "São Paulo"
        )

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            states
        )

        inputState.setAdapter(adapter)

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

        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            cities
        )

        inputCity.setAdapter(adapter)
    }
}