package com.example.geyugoapp.database.dtos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDbDto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val notificationTime: Long,
    val notificationsActive: Boolean
)
