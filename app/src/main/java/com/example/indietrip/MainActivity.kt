package com.example.indietrip

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter

class MainActivity : Activity() {
    private lateinit var inputCountry : AutoCompleteTextView
    private lateinit var inputState : AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getReferencesFromInput()
        setupCountryAutocomplete()
    }

    private fun getReferencesFromInput(){
        inputCountry = findViewById(R.id.input_country)
        inputState = findViewById(R.id.input_state)
    }

    private fun setupCountryAutocomplete(){
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
            this,
            android.R.layout.simple_dropdown_item_1line,
            countries
        )

        inputCountry.setAdapter(adapter)
        inputCountry.setOnClickListener {
            inputState.isEnabled = true
        }

        inputState.setAdapter(adapter)



    }
}