package com.example.geyugoapp.domain.users.models

data class User(
    val id: Long = 0L,
    val name: String,
    val notificationTime: Long = 300000L,
    val notificationsActive: Boolean = true,
    val online: Boolean = false
)
