package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.geyugoapp.database.dtos.TaskDbDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TaskDbDto)

    @Delete
    suspend fun deleteTask(task: TaskDbDto)

    @Query("SELECT * FROM tasks")
    fun observeAll(): Flow<List<TaskDbDto>>

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    suspend fun getTasksByUserId(userId: Long): List<TaskDbDto>

    @Update
    suspend fun updateTask(task: TaskDbDto)

    @Query("SELECT COUNT(*) FROM tasks WHERE categoryId = :categoryId")
    suspend fun getCountTasksByCategory(categoryId: Long): Int
}