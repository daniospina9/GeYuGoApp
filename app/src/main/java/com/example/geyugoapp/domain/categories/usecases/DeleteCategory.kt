package com.example.geyugoapp.domain.categories.usecases

import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.repository.CategoryRepository

class DeleteCategory(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        repository.delete(category)
    }
}