package com.example.indietrip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText

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
                openActivitySelection(preferenceInputs)
            }
        }
    }

    private fun openActivitySelection(preferenceInputs: List<CheckBox>) {
        val country = locationSelector.selectedCountryName()
        val state = locationSelector.selectedStateName()
        val city = locationSelector.selectedCityName()
        val departureDate = dateSelector.departureDateText()
        val returnDate = dateSelector.returnDateText()
        val departureDateMillis = dateSelector.departureDateMillis()
        val returnDateMillis = dateSelector.returnDateMillis()
        val preferences = ArrayList(
            preferenceInputs
                .filter { it.isChecked }
                .map { it.text.toString() }
        )

        Log.i(
            LOG_TAG,
            "MainActivity -> ActivitySelectionActivity: country=$country, " +
                "state=$state, city=$city, departure=$departureDate, " +
                "return=$returnDate, preferences=${preferences.joinToString()}"
        )

        val intent = Intent(this, ActivitySelectionActivity::class.java).apply {
            putExtra(TripExtras.COUNTRY, country)
            putExtra(TripExtras.STATE, state)
            putExtra(TripExtras.CITY, city)
            putExtra(TripExtras.DEPARTURE_DATE, departureDate)
            putExtra(TripExtras.RETURN_DATE, returnDate)
            putExtra(TripExtras.DEPARTURE_DATE_MILLIS, departureDateMillis)
            putExtra(TripExtras.RETURN_DATE_MILLIS, returnDateMillis)
            putStringArrayListExtra(TripExtras.PREFERENCES, preferences)
        }

        startActivity(intent)
    }

    companion object {
        private const val LOG_TAG = "IndieTrip"
    }
}
