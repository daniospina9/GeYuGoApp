package com.example.geyugoapp.datasource

import com.example.geyugoapp.database.CategoryDao
import com.example.geyugoapp.datasource.converters.toCategory
import com.example.geyugoapp.datasource.converters.toDbDto
import com.example.geyugoapp.domain.categories.models.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryDataSourceImpl(
    private val categoryDao: CategoryDao
): CategoryDataSource {

    override fun observeAll(): Flow<List<Category>> = categoryDao.observeAll().map { listFlow ->
        listFlow.map { it.toCategory() }
    }

    override suspend fun getCategoriesByUserId(userId: Long): List<Category> {
        return categoryDao.getCategoriesByUserId(userId).map { it.toCategory() }
    }

    override suspend fun insert(category: Category) {
        categoryDao.insert(category.toDbDto())
    }

    override suspend fun delete(category: Category) {
        categoryDao.delete(category.toDbDto())
    }

    override suspend fun update(category: Category) {
        categoryDao.update(category.toDbDto())
    }
}