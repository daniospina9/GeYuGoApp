package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.geyugoapp.database.dtos.CategoryDbDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun observeAll(): Flow<List<CategoryDbDto>>

    @Insert
    suspend fun insert(category: CategoryDbDto)

    @Delete
    suspend fun delete(category: CategoryDbDto)
}