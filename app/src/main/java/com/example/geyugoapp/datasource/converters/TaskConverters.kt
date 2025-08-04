package com.example.geyugoapp.datasource.converters

import com.example.geyugoapp.database.dtos.TaskDbDto
import com.example.geyugoapp.domain.task.models.Task

fun Task.toDbDto(): TaskDbDto {
    return TaskDbDto (
        id = id,
        name = name,
        dateTime = dateTime,
        isTimeSet = isTimeSet,
        userId = userId,
        categoryId = categoryId,
        isClicked = isClicked
    )
}

fun TaskDbDto.toTask(): Task {
    return Task(
        id = id,
        name = name,
        dateTime = dateTime,
        isTimeSet = isTimeSet,
        userId = userId,
        categoryId = categoryId,
        isClicked = isClicked
    )
}