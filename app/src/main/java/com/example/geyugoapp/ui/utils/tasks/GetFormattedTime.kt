package com.example.geyugoapp.ui.utils.tasks

import androidx.compose.runtime.Composable

data class TimeFormats(
    val amPm: String,
    val formattedTime: String
)

fun getFormattedTime(
    hour24: Int,
    minute: Int
): TimeFormats {
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }
    val amPm = if (hour24 < 12) "AM" else "PM"
    val formattedTime = String.format("%02d:%02d", hour12, minute)

    return TimeFormats(
        amPm = amPm,
        formattedTime = formattedTime
    )
}