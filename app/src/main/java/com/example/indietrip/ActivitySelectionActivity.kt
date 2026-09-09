package com.example.indietrip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class ActivitySelectionActivity : Activity() {
    private lateinit var activityAdapter: ActivityOptionAdapter
    private lateinit var selectionCount: TextView
    private var country = ""
    private var state = ""
    private var city = ""
    private var departureDate = ""
    private var returnDate = ""
    private var preferences = emptyList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        readTripData()
        setupHeader()
        setupTripSummary()
        setupActivityList()
        setupActions()
        logTripData()
    }

    private fun readTripData() {
        country = intent.getStringExtra(TripExtras.COUNTRY).orEmpty()
        state = intent.getStringExtra(TripExtras.STATE).orEmpty()
        city = intent.getStringExtra(TripExtras.CITY).orEmpty()
        departureDate = intent.getStringExtra(TripExtras.DEPARTURE_DATE).orEmpty()
        returnDate = intent.getStringExtra(TripExtras.RETURN_DATE).orEmpty()
        preferences = intent.getStringArrayListExtra(TripExtras.PREFERENCES)
            ?: emptyList()
    }

    private fun setupHeader() {
        findViewById<ImageButton>(R.id.button_back).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.text_explore_title).text =
            getString(R.string.explore_city, city)
    }

    private fun setupTripSummary() {
        findViewById<TextView>(R.id.text_trip_location).text = getString(
            R.string.trip_location,
            city,
            state,
            country
        )

        findViewById<TextView>(R.id.text_trip_dates).text = getString(
            R.string.trip_date_range,
            departureDate,
            returnDate
        )

        findViewById<TextView>(R.id.text_trip_preferences).text =
            if (preferences.isEmpty()) {
                getString(R.string.all_preferences)
            } else {
                preferences.joinToString(" • ")
            }
    }

    private fun setupActivityList() {
        val activities = ActivityDataSource(this).loadActivities(preferences)
        selectionCount = findViewById(R.id.text_selection_count)
        activityAdapter = ActivityOptionAdapter(this, activities) { count ->
            updateSelectionCount(count)
        }

        findViewById<ListView>(R.id.list_activities).adapter = activityAdapter
        updateSelectionCount(0)
    }

    private fun setupActions() {
        findViewById<Button>(R.id.button_clear_selection).setOnClickListener {
            activityAdapter.clearSelection()
        }

        findViewById<Button>(R.id.button_continue).setOnClickListener {
            confirmSelection()
        }

        findViewById<TextView>(R.id.button_skip).setOnClickListener {
            activityAdapter.clearSelection()
            Log.i(LOG_TAG, "Activities skipped for $city")
            Toast.makeText(this, R.string.activities_skipped, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun confirmSelection() {
        val selectedActivities = activityAdapter.selectedActivities()

        if (selectedActivities.isEmpty()) {
            Toast.makeText(
                this,
                R.string.error_activity_required,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        Log.i(
            LOG_TAG,
            "ActivitySelectionActivity -> ActivityDetailsActivity: " +
                "country=$country, state=$state, city=$city, " +
                "departure=$departureDate, return=$returnDate, " +
                "preferences=${preferences.joinToString()}, " +
                "activities=${selectedActivities.joinToString { it.name }}"
        )

        openActivityDetails(
            selectedActivities.first(),
            selectedActivities.map { it.name }
        )
    }

    private fun openActivityDetails(
        activity: ActivityOption,
        selectedActivityNames: List<String>
    ) {
        val intent = Intent(this, ActivityDetailsActivity::class.java).apply {
            putExtra(TripExtras.COUNTRY, country)
            putExtra(TripExtras.STATE, state)
            putExtra(TripExtras.CITY, city)
            putExtra(TripExtras.DEPARTURE_DATE, departureDate)
            putExtra(TripExtras.RETURN_DATE, returnDate)
            putStringArrayListExtra(TripExtras.PREFERENCES, ArrayList(preferences))
            putStringArrayListExtra(
                TripExtras.SELECTED_ACTIVITIES,
                ArrayList(selectedActivityNames)
            )
            putExtra(TripExtras.ACTIVITY_NAME, activity.name)
            putStringArrayListExtra(
                TripExtras.ACTIVITY_PREFERENCES,
                ArrayList(activity.preferences)
            )
            putExtra(TripExtras.ACTIVITY_DESCRIPTION, activity.description)
            putExtra(TripExtras.ACTIVITY_DURATION, activity.durationHours)
            putExtra(TripExtras.ACTIVITY_DIFFICULTY, activity.difficulty)
            putExtra(TripExtras.ACTIVITY_IMAGE, activity.imageResource)
        }

        startActivity(intent)
    }

    private fun updateSelectionCount(count: Int) {
        selectionCount.text = resources.getQuantityString(
            R.plurals.activities_selected,
            count,
            count
        )
    }

    private fun logTripData() {
        Log.i(
            LOG_TAG,
            "Received trip: country=$country, state=$state, city=$city, " +
                "departure=$departureDate, return=$returnDate, " +
                "preferences=${preferences.joinToString()}"
        )
    }

    companion object {
        private const val LOG_TAG = "IndieTrip"
    }
}
