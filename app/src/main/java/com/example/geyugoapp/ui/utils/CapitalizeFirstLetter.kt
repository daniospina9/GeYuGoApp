package com.example.geyugoapp.ui.utils

fun capitalizeFirstLetter(text: String?): String {
    if (text.isNullOrEmpty()) {
        return ""
    }
    return text.first().uppercaseChar() + text.substring(1)
}