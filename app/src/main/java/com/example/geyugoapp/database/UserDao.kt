package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.geyugoapp.database.dtos.UserDbDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun observeAll(): Flow<List<UserDbDto>>

    @Insert
    suspend fun insert(user: UserDbDto): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): UserDbDto?
}