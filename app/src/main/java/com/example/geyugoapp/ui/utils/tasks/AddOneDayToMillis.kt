package com.example.geyugoapp.ui.utils.tasks

import android.icu.util.Calendar

fun addOneDayToMillis(originalMillis: Long?): Long? {
    if (originalMillis == null) {
        return null
    }

    val calendar = Calendar.getInstance()

    calendar.timeInMillis = originalMillis

    calendar.add(Calendar.DAY_OF_YEAR, 1)

    return calendar.timeInMillis
}