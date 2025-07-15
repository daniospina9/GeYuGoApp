package com.example.geyugoapp.repository

import com.example.geyugoapp.domain.users.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observeAll(): Flow<List<User>>

    suspend fun insert(user: User): Long

    suspend fun getUserById(userId: Long): User?
}