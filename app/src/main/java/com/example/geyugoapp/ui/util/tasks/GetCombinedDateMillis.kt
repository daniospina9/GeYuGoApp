package com.example.geyugoapp.ui.util.tasks

import android.icu.util.Calendar

fun getCombinedDateTimeMillis(
    selectedDateMillis: Long?,
    hour: Int,
    minute: Int,
): Long? {
    selectedDateMillis ?: return null

    val combinedCalendar = Calendar.getInstance()
    combinedCalendar.timeInMillis = selectedDateMillis

    combinedCalendar.set(Calendar.HOUR_OF_DAY, hour)
    combinedCalendar.set(Calendar.MINUTE, minute)
    combinedCalendar.set(Calendar.SECOND, 0)
    combinedCalendar.set(Calendar.MILLISECOND, 0)

    return combinedCalendar.timeInMillis

}