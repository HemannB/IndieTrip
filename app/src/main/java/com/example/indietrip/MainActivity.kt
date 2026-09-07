package com.example.indietrip

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView

class MainActivity : Activity() {
    private lateinit var locationSelector: LocationSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupLocationSelector()
    }

    private fun setupLocationSelector() {
        val inputCountry =
            findViewById<AutoCompleteTextView>(R.id.input_country)

        val inputState =
            findViewById<AutoCompleteTextView>(R.id.input_state)

        val inputCity =
            findViewById<AutoCompleteTextView>(R.id.input_city)

        locationSelector = LocationSelector(
            this,
            inputCountry,
            inputState,
            inputCity
        )

        locationSelector.setup()
    }
}