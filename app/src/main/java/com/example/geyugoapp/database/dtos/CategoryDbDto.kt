package com.example.geyugoapp.database.dtos

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = UserDbDto::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class CategoryDbDto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val color: Long,
    val userId: Long?
)
