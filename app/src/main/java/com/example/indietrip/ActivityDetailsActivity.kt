package com.example.indietrip

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import java.util.Locale

class ActivityDetailsActivity : Activity() {
    private lateinit var durationInput: SeekBar
    private lateinit var durationText: TextView
    private lateinit var difficultyInput: RadioGroup
    private lateinit var startTimeInput: EditText
    private lateinit var peopleText: TextView
    private lateinit var equipmentInput: CheckBox
    private var country = ""
    private var state = ""
    private var city = ""
    private var departureDate = ""
    private var returnDate = ""
    private var departureDateMillis = 0L
    private var returnDateMillis = 0L
    private var tripPreferences = emptyList<String>()
    private var selectedActivities = emptyList<String>()
    private var activityName = ""
    private var activityPreferences = emptyList<String>()
    private var activityDescription = ""
    private var defaultDuration = 1
    private var defaultDifficulty = ""
    private var activityImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        readData()
        setupHeader()
        setupActivityDetails()
        setupDuration()
        setupDifficulty()
        setupStartTime()
        setupPeopleCounter()
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
        tripPreferences = intent.getStringArrayListExtra(TripExtras.PREFERENCES)
            ?: emptyList()
        selectedActivities = intent.getStringArrayListExtra(
            TripExtras.SELECTED_ACTIVITIES
        ) ?: emptyList()
        activityName = intent.getStringExtra(TripExtras.ACTIVITY_NAME).orEmpty()
        activityPreferences = intent.getStringArrayListExtra(
            TripExtras.ACTIVITY_PREFERENCES
        ) ?: emptyList()
        activityDescription = intent.getStringExtra(
            TripExtras.ACTIVITY_DESCRIPTION
        ).orEmpty()
        defaultDuration = intent.getIntExtra(TripExtras.ACTIVITY_DURATION, 1)
        defaultDifficulty = intent.getStringExtra(
            TripExtras.ACTIVITY_DIFFICULTY
        ).orEmpty()
        activityImage = intent.getIntExtra(TripExtras.ACTIVITY_IMAGE, 0)
    }

    private fun setupHeader() {
        findViewById<ImageButton>(R.id.button_back).setOnClickListener {
            finish()
        }
    }

    private fun setupActivityDetails() {
        findViewById<ImageView>(R.id.image_activity_detail).apply {
            if (activityImage != 0) {
                setImageResource(activityImage)
            }
            contentDescription = activityName
        }

        findViewById<TextView>(R.id.text_activity_detail_name).text = activityName
        findViewById<TextView>(R.id.text_activity_detail_preferences).text =
            activityPreferences.joinToString(" • ")
        findViewById<TextView>(R.id.text_activity_description).text =
            activityDescription
    }

    private fun setupDuration() {
        durationInput = findViewById(R.id.input_duration)
        durationText = findViewById(R.id.text_duration_value)
        durationInput.progress = (defaultDuration - 1).coerceIn(0, MAX_DURATION - 1)
        updateDurationText(durationInput.progress + 1)

        durationInput.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    updateDurationText(progress + 1)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            }
        )
    }

    private fun setupDifficulty() {
        difficultyInput = findViewById(R.id.input_difficulty)

        val selectedId = when (defaultDifficulty) {
            getString(R.string.difficulty_easy) -> R.id.difficulty_easy
            getString(R.string.difficulty_hard) -> R.id.difficulty_hard
            else -> R.id.difficulty_moderate
        }

        difficultyInput.check(selectedId)
    }

    private fun setupStartTime() {
        startTimeInput = findViewById(R.id.input_start_time)
        startTimeInput.setOnClickListener {
            val time = startTimeInput.text.toString().split(":")
            val hour = time.getOrNull(0)?.toIntOrNull() ?: DEFAULT_HOUR
            val minute = time.getOrNull(1)?.toIntOrNull() ?: 0

            TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    startTimeInput.setText(
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            selectedHour,
                            selectedMinute
                        )
                    )
                },
                hour,
                minute,
                true
            ).show()
        }
    }

    private fun setupPeopleCounter() {
        peopleText = findViewById(R.id.text_people_count)
        updatePeopleText(DEFAULT_PEOPLE)

        findViewById<Button>(R.id.button_remove_person).setOnClickListener {
            changePeople(-1)
        }

        findViewById<Button>(R.id.button_add_person).setOnClickListener {
            changePeople(1)
        }

        equipmentInput = findViewById(R.id.input_equipment_reminder)
    }

    private fun setupActions() {
        findViewById<Button>(R.id.button_add_to_trip).setOnClickListener {
            addActivityToTrip()
        }

        findViewById<TextView>(R.id.button_cancel).setOnClickListener {
            finish()
        }
    }

    private fun changePeople(change: Int) {
        val currentPeople = peopleText.text.toString().toIntOrNull()
            ?: DEFAULT_PEOPLE
        val newPeople = (currentPeople + change).coerceIn(1, MAX_PEOPLE)
        updatePeopleText(newPeople)
    }

    private fun updatePeopleText(people: Int) {
        peopleText.text = String.format(Locale.getDefault(), "%d", people)
    }

    private fun updateDurationText(duration: Int) {
        durationText.text = resources.getQuantityString(
            R.plurals.hours,
            duration,
            duration
        )
    }

    private fun addActivityToTrip() {
        val duration = durationInput.progress + 1
        val difficulty = findViewById<RadioButton>(
            difficultyInput.checkedRadioButtonId
        ).text.toString()
        val startTime = startTimeInput.text.toString()
        val people = peopleText.text.toString().toInt()

        Log.i(
            LOG_TAG,
            "ActivityDetailsActivity -> TripSummaryActivity: " +
                "country=$country, state=$state, city=$city, " +
                "departure=$departureDate, return=$returnDate, " +
                "preferences=${tripPreferences.joinToString()}, " +
                "activities=${selectedActivities.joinToString()}, " +
                "configuredActivity=$activityName, duration=${duration}h, " +
                "difficulty=$difficulty, startTime=$startTime, " +
                "people=$people, equipment=${equipmentInput.isChecked}"
        )

        val intent = Intent(this, TripSummaryActivity::class.java).apply {
            putExtra(TripExtras.COUNTRY, country)
            putExtra(TripExtras.STATE, state)
            putExtra(TripExtras.CITY, city)
            putExtra(TripExtras.DEPARTURE_DATE, departureDate)
            putExtra(TripExtras.RETURN_DATE, returnDate)
            putExtra(TripExtras.DEPARTURE_DATE_MILLIS, departureDateMillis)
            putExtra(TripExtras.RETURN_DATE_MILLIS, returnDateMillis)
            putStringArrayListExtra(
                TripExtras.PREFERENCES,
                ArrayList(tripPreferences)
            )
            putStringArrayListExtra(
                TripExtras.SELECTED_ACTIVITIES,
                ArrayList(selectedActivities)
            )
            putExtra(TripExtras.ACTIVITY_NAME, activityName)
            putExtra(TripExtras.ACTIVITY_DURATION, duration)
            putExtra(TripExtras.ACTIVITY_DIFFICULTY, difficulty)
            putExtra(TripExtras.ACTIVITY_START_TIME, startTime)
            putExtra(TripExtras.ACTIVITY_PEOPLE, people)
            putExtra(TripExtras.ACTIVITY_EQUIPMENT, equipmentInput.isChecked)
        }

        startActivity(intent)
    }

    private fun logReceivedData() {
        Log.i(
            LOG_TAG,
            "Received activity: name=$activityName, " +
                "preferences=${activityPreferences.joinToString()}, " +
                "duration=${defaultDuration}h, difficulty=$defaultDifficulty"
        )
    }

    companion object {
        private const val LOG_TAG = "IndieTrip"
        private const val DEFAULT_HOUR = 9
        private const val DEFAULT_PEOPLE = 2
        private const val MAX_PEOPLE = 20
        private const val MAX_DURATION = 6
    }
}
