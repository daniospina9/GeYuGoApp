package com.example.geyugoapp.database

import androidx.room.Dao
import androidx.room.Delete
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
    suspend fun getUserById(userId: Long): UserDbDto

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserDbDto>

    @Delete
    suspend fun deleteUser(user: UserDbDto)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUsersCount(): Int

    @Query("UPDATE users SET online = :online")
    suspend fun updateAllUsersOnlineStatus(online: Boolean)

    @Query("UPDATE users SET online = :online WHERE id = :userId")
    suspend fun updateUserOnlineStatus(userId: Long, online: Boolean)

    @Query("SELECT id FROM users WHERE online = :isOnline LIMIT 1")
    suspend fun getUserIdByOnlineStatus(isOnline: Boolean): Long

}