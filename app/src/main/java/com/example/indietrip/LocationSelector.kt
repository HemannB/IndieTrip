package com.example.indietrip

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
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
    private var selectedCountry: Country? = null
    private var selectedState: State? = null
    private var selectedCity: City? = null

    fun setup() {
        setupCountryAutocomplete()
        setupStateAutocomplete()
        setupCityAutocomplete()
        monitorSelections()
    }

    fun validate(): Boolean {
        val countryIsValid =
            inputCountry.text.toString() == selectedCountry?.name
        val stateIsValid =
            inputState.text.toString() == selectedState?.name
        val cityIsValid =
            inputCity.text.toString() == selectedCity?.name

        inputCountry.error = if (countryIsValid) {
            null
        } else {
            context.getString(R.string.error_country_required)
        }

        inputState.error = if (stateIsValid) {
            null
        } else {
            context.getString(R.string.error_state_required)
        }

        inputCity.error = if (cityIsValid) {
            null
        } else {
            context.getString(R.string.error_city_required)
        }

        when {
            !countryIsValid -> inputCountry.requestFocus()
            !stateIsValid -> inputState.requestFocus()
            !cityIsValid -> inputCity.requestFocus()
        }

        return countryIsValid && stateIsValid && cityIsValid
    }

    fun selectedCountryName(): String = selectedCountry?.name.orEmpty()

    fun selectedStateName(): String = selectedState?.name.orEmpty()

    fun selectedCityName(): String = selectedCity?.name.orEmpty()

    private fun setupCountryAutocomplete() {
        val countryNames = countries.map { it.name }

        inputCountry.setAdapter(createAdapter(countryNames))

        inputCountry.setOnClickListener {
            inputCountry.showDropDown()
        }

        inputCountry.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position).toString()
            selectedCountry = countries.first { it.name == selectedName }
            inputCountry.error = null

            filteredStates = states.filter {
                it.countryId == selectedCountry?.id
            }
            filteredCities = emptyList()
            selectedState = null
            selectedCity = null

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

        inputState.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position).toString()
            selectedState = filteredStates.first { it.name == selectedName }
            inputState.error = null

            filteredCities = cities.filter {
                it.stateId == selectedState?.id
            }
            selectedCity = null

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

        inputCity.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position).toString()
            selectedCity = filteredCities.first { it.name == selectedName }
            inputCity.error = null
        }
    }

    private fun monitorSelections() {
        monitorInput(inputCountry, { selectedCountry?.name }) {
            clearCountrySelection()
        }

        monitorInput(inputState, { selectedState?.name }) {
            clearStateSelection()
        }

        monitorInput(inputCity, { selectedCity?.name }) {
            selectedCity = null
        }
    }

    private fun monitorInput(
        input: AutoCompleteTextView,
        selectedName: () -> String?,
        clearSelection: () -> Unit
    ) {
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit

            override fun afterTextChanged(text: Editable?) {
                if (text.toString() != selectedName()) {
                    input.error = null
                    clearSelection()
                }
            }
        })
    }

    private fun clearCountrySelection() {
        selectedCountry = null
        selectedState = null
        filteredStates = emptyList()
        inputState.setText("", false)
        inputState.setAdapter(createAdapter(emptyList()))
        inputState.isEnabled = false
        clearStateSelection()
    }

    private fun clearStateSelection() {
        selectedState = null
        selectedCity = null
        filteredCities = emptyList()
        inputCity.setText("", false)
        inputCity.setAdapter(createAdapter(emptyList()))
        inputCity.isEnabled = false
    }

    private fun createAdapter(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            items
        )
    }
}
