package com.example.indietrip

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.EditText

class MainActivity : Activity() {
    private lateinit var locationSelector: LocationSelector
    private lateinit var dateSelector: DateSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupLocationSelector()
        setupDateSelector()
    }

    private fun setupLocationSelector() {
        val inputCountry =
            findViewById<AutoCompleteTextView>(R.id.input_country)

        val inputState =
            findViewById<AutoCompleteTextView>(R.id.input_state)

        val inputCity =
            findViewById<AutoCompleteTextView>(R.id.input_city)

        val dataSource = LocationDataSource(this)

        locationSelector = LocationSelector(
            this,
            inputCountry,
            inputState,
            inputCity,
            dataSource
        )

        locationSelector.setup()
    }

    private fun setupDateSelector() {
        val inputDepartureDate =
            findViewById<EditText>(R.id.input_departure_date)

        val inputReturnDate =
            findViewById<EditText>(R.id.input_return_date)

        dateSelector = DateSelector(
            this,
            inputDepartureDate,
            inputReturnDate
        )

        dateSelector.setup()
    }
}
