package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geyugoapp.database.dtos.TaskDbDto
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TaskDbDto)

    @Query("SELECT * FROM tasks")
    fun observeAll(): Flow<List<TaskDbDto>>

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    suspend fun getTasksByUserId(userId: Long): List<TaskDbDto>

}