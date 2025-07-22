package com.example.geyugoapp.domain.categories.usecases

import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.repository.CategoryRepository

class GetCategoriesByUserId(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(userId: Long): List<Category> {
        return repository.getCategoriesByUserId(userId)
    }
}