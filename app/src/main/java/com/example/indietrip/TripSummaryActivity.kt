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
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class TripSummaryActivity : Activity() {
    private var country = ""
    private var state = ""
    private var city = ""
    private var departureDate = ""
    private var returnDate = ""
    private var departureDateMillis = 0L
    private var returnDateMillis = 0L
    private var preferences = emptyList<String>()
    private var selectedActivityNames = emptyList<String>()
    private var configuredActivityName = ""
    private var configuredDuration = 1
    private var configuredDifficulty = ""
    private var configuredStartTime = ""
    private var configuredPeople = 1
    private var configuredEquipment = false
    private var plannedActivities = emptyList<PlannedActivity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_summary)

        readData()
        plannedActivities = createPlannedActivities()
        setupHeader()
        setupTripSummary()
        setupActivityList()
        setupActions()
        logReceivedData()
    }

    private fun readData() {
        country = intent.getStringExtra(TripExtras.COUNTRY).orEmpty()
        state = intent.getStringExtra(TripExtras.STATE).orEmpty()
        city = intent.getStringExtra(TripExtras.CITY).orEmpty()
        departureDate = intent.getStringExtra(TripExtras.DEPARTURE_DATE).orEmpty()
        returnDate = intent.getStringExtra(TripExtras.RETURN_DATE).orEmpty()
        departureDateMillis = intent.getLongExtra(
            TripExtras.DEPARTURE_DATE_MILLIS,
            0L
        )
        returnDateMillis = intent.getLongExtra(
            TripExtras.RETURN_DATE_MILLIS,
            0L
        )
        preferences = intent.getStringArrayListExtra(TripExtras.PREFERENCES)
            ?: emptyList()
        selectedActivityNames = intent.getStringArrayListExtra(
            TripExtras.SELECTED_ACTIVITIES
        ) ?: emptyList()
        configuredActivityName = intent.getStringExtra(
            TripExtras.ACTIVITY_NAME
        ).orEmpty()
        configuredDuration = intent.getIntExtra(TripExtras.ACTIVITY_DURATION, 1)
        configuredDifficulty = intent.getStringExtra(
            TripExtras.ACTIVITY_DIFFICULTY
        ).orEmpty()
        configuredStartTime = intent.getStringExtra(
            TripExtras.ACTIVITY_START_TIME
        ).orEmpty()
        configuredPeople = intent.getIntExtra(TripExtras.ACTIVITY_PEOPLE, 1)
        configuredEquipment = intent.getBooleanExtra(
            TripExtras.ACTIVITY_EQUIPMENT,
            false
        )
    }

    private fun createPlannedActivities(): List<PlannedActivity> {
        val optionsByName = ActivityDataSource(this)
            .loadActivities(emptyList())
            .associateBy { it.name }

        return selectedActivityNames.mapNotNull { activityName ->
            val option = optionsByName[activityName] ?: return@mapNotNull null
            val isConfigured = option.name == configuredActivityName

            PlannedActivity(
                name = option.name,
                preferences = option.preferences,
                durationHours = if (isConfigured) {
                    configuredDuration
                } else {
                    option.durationHours
                },
                difficulty = if (isConfigured) {
                    configuredDifficulty
                } else {
                    option.difficulty
                },
                startTime = if (isConfigured) {
                    configuredStartTime
                } else {
                    getString(R.string.default_start_time)
                },
                people = if (isConfigured) configuredPeople else DEFAULT_PEOPLE,
                equipmentReminder = isConfigured && configuredEquipment,
                imageResource = option.imageResource
            )
        }
    }

    private fun setupHeader() {
        findViewById<ImageButton>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun setupTripSummary() {
        findViewById<TextView>(R.id.text_summary_trip_location).text = getString(
            R.string.trip_location,
            city,
            state,
            country
        )
        findViewById<TextView>(R.id.text_summary_trip_dates).text = getString(
            R.string.trip_date_range,
            departureDate,
            returnDate
        )

        val days = calculateTripDays()
        findViewById<TextView>(R.id.text_summary_trip_duration).text =
            resources.getQuantityString(R.plurals.days, days, days)

        findViewById<TextView>(R.id.text_summary_trip_preferences).text =
            if (preferences.isEmpty()) {
                getString(R.string.all_preferences)
            } else {
                preferences.joinToString(" • ")
            }
    }

    private fun setupActivityList() {
        findViewById<ListView>(R.id.list_trip_activities).adapter =
            TripSummaryAdapter(this, plannedActivities)

        val activityCount = plannedActivities.size
        findViewById<TextView>(R.id.text_total_activities).text =
            resources.getQuantityString(
                R.plurals.activities_selected,
                activityCount,
                activityCount
            )

        val totalHours = plannedActivities.sumOf { it.durationHours }
        val formattedHours = resources.getQuantityString(
            R.plurals.hours,
            totalHours,
            totalHours
        )
        findViewById<TextView>(R.id.text_total_time).text = getString(
            R.string.total_planned_time,
            formattedHours
        )
    }

    private fun setupActions() {
        findViewById<Button>(R.id.button_finish_planning).setOnClickListener {
            finishPlanning()
        }

        findViewById<TextView>(R.id.button_edit_activities).setOnClickListener {
            Log.i(LOG_TAG, "TripSummaryActivity -> ActivitySelectionActivity")
            val intent = Intent(this, ActivitySelectionActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }
    }

    private fun calculateTripDays(): Int {
        if (departureDateMillis == 0L || returnDateMillis == 0L) {
            return 1
        }

        val zoneId = ZoneId.systemDefault()
        val departure = Instant.ofEpochMilli(departureDateMillis)
            .atZone(zoneId)
            .toLocalDate()
        val returnDay = Instant.ofEpochMilli(returnDateMillis)
            .atZone(zoneId)
            .toLocalDate()

        return ChronoUnit.DAYS.between(departure, returnDay)
            .toInt()
            .coerceAtLeast(1)
    }

    private fun finishPlanning() {
        val activityDetails = plannedActivities.joinToString { activity ->
            "${activity.name}: duration=${activity.durationHours}h, " +
                "difficulty=${activity.difficulty}, " +
                "startTime=${activity.startTime}, people=${activity.people}, " +
                "equipment=${activity.equipmentReminder}"
        }

        Log.i(
            LOG_TAG,
            "Trip finished: country=$country, state=$state, city=$city, " +
                "departure=$departureDate, return=$returnDate, " +
                "preferences=${preferences.joinToString()}, " +
                "activities=[$activityDetails]"
        )
        Toast.makeText(this, R.string.trip_planning_finished, Toast.LENGTH_SHORT)
            .show()
    }

    private fun logReceivedData() {
        Log.i(
            LOG_TAG,
            "Received trip summary: country=$country, state=$state, city=$city, " +
                "departure=$departureDate, return=$returnDate, " +
                "preferences=${preferences.joinToString()}, " +
                "activities=${selectedActivityNames.joinToString()}"
        )
    }

    companion object {
        private const val LOG_TAG = "IndieTrip"
        private const val DEFAULT_PEOPLE = 2
    }
}
