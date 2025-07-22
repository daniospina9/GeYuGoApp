package com.example.geyugoapp.datasource.converters

import com.example.geyugoapp.database.dtos.CategoryDbDto
import com.example.geyugoapp.domain.categories.models.Category
import kotlin.Long

fun Category.toDbDto(): CategoryDbDto {
    return CategoryDbDto(
        id = id,
        name = name,
        color = color,
        userId = userId
    )
}

fun CategoryDbDto.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        color = color,
        userId = userId
    )
}