package com.example.geyugoapp.domain.task

data class Task(
    val id: Long = 0L,
    val name: String,
    val dateTime: Long,
    val isTimeSet: Boolean,
    val userId: Long,
    val categoryId: Long
)