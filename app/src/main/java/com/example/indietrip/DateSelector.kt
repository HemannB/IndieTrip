package com.example.indietrip

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.DateFormat
import java.util.Calendar

class DateSelector(
    private val context: Context,
    private val inputDepartureDate: EditText,
    private val inputReturnDate: EditText
) {
    private var departureDate: Calendar? = null
    private var returnDate: Calendar? = null

    fun setup() {
        inputDepartureDate.setOnClickListener {
            showDepartureDatePicker()
        }

        inputReturnDate.setOnClickListener {
            showReturnDatePicker()
        }
    }

    fun validate(): Boolean {
        val departureIsValid = departureDate != null
        val returnIsValid = returnDate != null

        inputDepartureDate.error = if (departureIsValid) {
            null
        } else {
            context.getString(R.string.error_departure_date_required)
        }

        inputReturnDate.error = if (returnIsValid) {
            null
        } else {
            context.getString(R.string.error_return_date_required)
        }

        return departureIsValid && returnIsValid
    }

    private fun showDepartureDatePicker() {
        val initialDate = departureDate ?: today()

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedDate = createDate(year, month, day)
                departureDate = selectedDate
                inputDepartureDate.setText(formatDate(selectedDate))
                inputDepartureDate.error = null

                if (returnDate?.before(departureDate) == true) {
                    returnDate = null
                    inputReturnDate.setText("")
                }
            },
            initialDate.get(Calendar.YEAR),
            initialDate.get(Calendar.MONTH),
            initialDate.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = today().timeInMillis
        }.show()
    }

    private fun showReturnDatePicker() {
        val minimumDate = departureDate ?: today()
        val initialDate = returnDate ?: minimumDate

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedDate = createDate(year, month, day)
                returnDate = selectedDate
                inputReturnDate.setText(formatDate(selectedDate))
                inputReturnDate.error = null
            },
            initialDate.get(Calendar.YEAR),
            initialDate.get(Calendar.MONTH),
            initialDate.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minimumDate.timeInMillis
        }.show()
    }

    private fun createDate(year: Int, month: Int, day: Int): Calendar {
        return Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    private fun today(): Calendar {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    private fun formatDate(date: Calendar): String {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(date.time)
    }
}
