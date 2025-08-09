package com.example.geyugoapp.ui.util

import androidx.compose.runtime.Composable

@Composable
fun getFirstWord(text: String?): String? {
    if (text.isNullOrBlank()) {
        return null
    }
    return text.trim().split(delimiters = charArrayOf(' '), limit = 2).firstOrNull()
}