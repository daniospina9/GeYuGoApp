package com.example.geyugoapp.ui.util

fun getSecondWord(text: String?): String? {
    if (text.isNullOrBlank()) {
        return null
    }
    val words = text.trim().split(' ')

    return words.getOrNull(1)
}