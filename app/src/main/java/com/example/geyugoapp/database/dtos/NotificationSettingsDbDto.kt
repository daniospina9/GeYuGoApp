package com.example.geyugoapp.database.dtos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notification_settings",
    foreignKeys = [
        ForeignKey(
            entity = UserDbDto::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NotificationSettingsDbDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Long,
    val areNotificationsEnabled: Boolean = false
)