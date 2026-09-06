package com.example.indietrip

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter

class MainActivity : Activity() {
    private lateinit var inputCountry : AutoCompleteTextView
    private lateinit var inputState : AutoCompleteTextView
    private lateinit var inputCity : AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        getReferencesFromInput()
        setupCountryAutocomplete()
        setupStateAutocomplete()
        setupCityAutocomplete()
    }

    private fun getReferencesFromInput() {
        inputCountry = findViewById(R.id.input_country)
        inputState = findViewById(R.id.input_state)
        inputCity = findViewById(R.id.input_city)
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

        val countryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            countries
        )

        inputCountry.setAdapter(countryAdapter)

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

        val stateAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            states
        )

        inputState.setAdapter(stateAdapter)

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

        val cityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            cities
        )

        inputCity.setAdapter(cityAdapter)
    }
}