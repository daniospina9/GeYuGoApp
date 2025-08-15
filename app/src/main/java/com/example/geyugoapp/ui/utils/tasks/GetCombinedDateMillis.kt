package com.example.geyugoapp.ui.utils.tasks

import android.icu.util.Calendar
import android.util.Log

fun getCombinedDateTimeMillis(
    selectedDateMillis: Long?,
    hour: Int,
    minute: Int,
): Long? {
    val TAG = "GetCombinedDateTimeMillis"
    
    Log.d(TAG, "=== DATE TIME CALCULATION DEBUG ===")
    Log.d(TAG, "selectedDateMillis: $selectedDateMillis")
    if (selectedDateMillis != null) {
        Log.d(TAG, "Selected date: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(selectedDateMillis))}")
    }
    Log.d(TAG, "Hour: $hour, Minute: $minute")
    
    selectedDateMillis ?: return null

    val combinedCalendar = Calendar.getInstance()
    combinedCalendar.timeInMillis = selectedDateMillis

    combinedCalendar.set(Calendar.HOUR_OF_DAY, hour)
    combinedCalendar.set(Calendar.MINUTE, minute)
    combinedCalendar.set(Calendar.SECOND, 0)
    combinedCalendar.set(Calendar.MILLISECOND, 0)

    val result = combinedCalendar.timeInMillis
    Log.d(TAG, "Final result: $result")
    Log.d(TAG, "Final date: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(result))}")
    Log.d(TAG, "Current time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}")
    Log.d(TAG, "=====================================")

    return result
}