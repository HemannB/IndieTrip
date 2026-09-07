package com.example.indietrip

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var locationSelector: LocationSelector
    private lateinit var dateSelector: DateSelector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupLocationSelector()
        setupDateSelector()
        setupNextButton()
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

    private fun setupNextButton() {
        val preferenceInputs = listOf(
            findViewById<CheckBox>(R.id.check_beach),
            findViewById<CheckBox>(R.id.check_adventure),
            findViewById<CheckBox>(R.id.check_camping),
            findViewById<CheckBox>(R.id.check_food),
            findViewById<CheckBox>(R.id.check_nature),
            findViewById<CheckBox>(R.id.check_road_trip),
            findViewById<CheckBox>(R.id.check_culture),
            findViewById<CheckBox>(R.id.check_relaxation),
            findViewById<CheckBox>(R.id.check_mountain),
            findViewById<CheckBox>(R.id.check_photography)
        )

        findViewById<Button>(R.id.btn_next).setOnClickListener {
            val locationIsValid = locationSelector.validate()
            val datesAreValid = dateSelector.validate()

            if (locationIsValid && datesAreValid) {
                val selectedPreferences = preferenceInputs.count { it.isChecked }
                val message = resources.getQuantityString(
                    R.plurals.trip_form_ready,
                    selectedPreferences,
                    selectedPreferences
                )

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
