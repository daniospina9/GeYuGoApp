package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Insert
import com.example.geyugoapp.database.dtos.TaskDbDto

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TaskDbDto)
}