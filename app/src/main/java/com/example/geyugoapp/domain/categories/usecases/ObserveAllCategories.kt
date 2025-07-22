package com.example.geyugoapp.domain.categories.usecases

import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class ObserveAllCategories(
    private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.observeAll()
}