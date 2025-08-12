package com.example.geyugoapp.datasource

import com.example.geyugoapp.domain.users.models.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun observeAll(): Flow<List<User>>

    suspend fun insert(user: User): Long

    suspend fun getUserById(userId: Long): User

    suspend fun getAllUsers(): List<User>

    suspend fun deleteUser(user: User)

    suspend fun getUsersCount(): Int

    suspend fun updateAllUsersOnlineStatus(online: Boolean)

    suspend fun updateUserOnlineStatus(userId: Long, online: Boolean)

    suspend fun getUserIdByOnlineStatus(isOnline: Boolean): Long

}