package com.example.indietrip

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView

class MainActivity : Activity() {
    private lateinit var inputCountry : AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getReferencesFromInput()
    }

    private fun getReferencesFromInput(){
        inputCountry = findViewById(R.id.input_country)
    }

}