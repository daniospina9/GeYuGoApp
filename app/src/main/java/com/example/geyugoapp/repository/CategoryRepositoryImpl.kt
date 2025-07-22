package com.example.geyugoapp.repository

import com.example.geyugoapp.datasource.CategoryDataSource
import com.example.geyugoapp.domain.categories.models.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(
    private val categoryDataSource: CategoryDataSource
): CategoryRepository {

    override fun observeAll(): Flow<List<Category>> = categoryDataSource.observeAll()

    override suspend fun insert(category: Category) {
        categoryDataSource.insert(category)
    }

    override suspend fun delete(category: Category) {
        categoryDataSource.delete(category)
    }
}