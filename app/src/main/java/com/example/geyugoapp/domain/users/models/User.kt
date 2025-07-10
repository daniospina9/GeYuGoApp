package com.example.geyugoapp.domain.users.models

data class User(
    val id: Int = 0,
    val name: String,
    val notificationTime: Long,
    val notificationsActive: Boolean
)
