package com.example.geyugoapp.repository

import com.example.geyugoapp.domain.categories.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun observeAll(): Flow<List<Category>>

    suspend fun getCategoriesByUserId(userId: Long): List<Category>
    suspend fun insert(category: Category)

    suspend fun delete(category: Category)
}