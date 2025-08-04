package com.example.geyugoapp.di

import com.example.geyugoapp.database.AppDatabase
import com.example.geyugoapp.database.CategoryDao
import com.example.geyugoapp.datasource.CategoryDataSource
import com.example.geyugoapp.datasource.CategoryDataSourceImpl
import com.example.geyugoapp.domain.categories.usecases.DeleteCategory
import com.example.geyugoapp.domain.categories.usecases.GetCategoriesByUserId
import com.example.geyugoapp.domain.categories.usecases.GetCategoryIdByName
import com.example.geyugoapp.domain.categories.usecases.GetCountCategoriesByName
import com.example.geyugoapp.domain.categories.usecases.InsertCategory
import com.example.geyugoapp.domain.categories.usecases.ObserveAllCategories
import com.example.geyugoapp.domain.categories.usecases.UpdateCategory
import com.example.geyugoapp.repository.CategoryRepository
import com.example.geyugoapp.repository.CategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryUseCaseModule {

    @Singleton
    @Provides
    fun provideCategoryDao(
        appDatabase: AppDatabase
    ): CategoryDao = appDatabase.categoryDao()

    @Singleton
    @Provides
    fun provideCategoryDataSource(
        categoryDao: CategoryDao
    ): CategoryDataSource = CategoryDataSourceImpl(
        categoryDao = categoryDao
    )

    @Singleton
    @Provides
    fun provideCategoryRepository(
        categoryDataSource: CategoryDataSource
    ): CategoryRepository = CategoryRepositoryImpl(
        categoryDataSource = categoryDataSource
    )

    @Singleton
    @Provides
    fun provideObserveAllCategories(
        repository: CategoryRepository
    ): ObserveAllCategories = ObserveAllCategories(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideInsertCategory(
        repository: CategoryRepository
    ): InsertCategory = InsertCategory(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideDeleteCategory(
        repository: CategoryRepository
    ): DeleteCategory = DeleteCategory(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetCategoriesByUserId(
        repository: CategoryRepository
    ): GetCategoriesByUserId = GetCategoriesByUserId(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideUpdateCategory(
        repository: CategoryRepository
    ): UpdateCategory = UpdateCategory(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetCategoryIdByName(
        repository: CategoryRepository
    ): GetCategoryIdByName = GetCategoryIdByName(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetCountCategoryByName(
        repository: CategoryRepository
    ): GetCountCategoriesByName = GetCountCategoriesByName(
        repository = repository
    )
}