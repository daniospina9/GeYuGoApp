package com.example.geyugoapp.domain.categories.usecases

import com.example.geyugoapp.domain.categories.models.Category
import com.example.geyugoapp.repository.CategoryRepository

class GetCategoryIdByName(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(name: String, userId: Long): Category  {
        return repository.getCategoryIdByName(name, userId)
    }
}