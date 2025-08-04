package com.example.geyugoapp.domain.categories.usecases

import com.example.geyugoapp.repository.CategoryRepository

class GetCountCategoriesByName(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(name: String, userId: Long): Int {
        return repository.getCountCategoriesByName(name, userId)
    }
}