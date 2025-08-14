package com.example.geyugoapp.ui.utils

fun getFirstWord(text: String?): String? {
    if (text.isNullOrBlank()) {
        return null
    }
    return text.trim().split(delimiters = charArrayOf(' '), limit = 2).firstOrNull()
}