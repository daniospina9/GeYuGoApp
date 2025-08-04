package com.example.geyugoapp.domain.task.models

data class Task(
    val id: Long = 0L,
    val name: String,
    val dateTime: Long,
    val isTimeSet: Boolean = true,
    val userId: Long,
    val categoryId: Long,
    val isClicked: Boolean = false
)