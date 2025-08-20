package com.example.geyugoapp.database.dtos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = CategoryDbDto::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class TaskDbDto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val dateTime: Long,
    val isTimeSet: Boolean,
    val userId: Long,
    val categoryId: Long,
    val isClicked: Boolean,
    val notificationId: String? = null
)