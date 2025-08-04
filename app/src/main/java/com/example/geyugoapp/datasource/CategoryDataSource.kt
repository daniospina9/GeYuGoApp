package com.example.geyugoapp.datasource

import com.example.geyugoapp.domain.categories.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {

    fun observeAll(): Flow<List<Category>>

    suspend fun getCategoriesByUserId(userId: Long): List<Category>

    suspend fun getCategoryIdByName(name: String, userId: Long): Category

    suspend fun getCountCategoriesByName(name: String, userId: Long): Int

    suspend fun insert(category: Category)

    suspend fun delete(category: Category)

    suspend fun update(category: Category)
}