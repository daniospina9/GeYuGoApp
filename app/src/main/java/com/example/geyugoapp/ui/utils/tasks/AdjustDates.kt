package com.example.geyugoapp.ui.util.tasks

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun adjustDates(
    dateString: String
): String {

    val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    dateFormat.isLenient = false

    try {
        val date = dateFormat.parse(dateString)

        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        } else {
            return dateString
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1)

        return dateFormat.format(calendar.time)

    } catch (e: Exception) {
        println("Error processing date '$dateString': ${e.message}")
        return dateString
    }
}