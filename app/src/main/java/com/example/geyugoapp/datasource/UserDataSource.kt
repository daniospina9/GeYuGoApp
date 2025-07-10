package com.example.geyugoapp.datasource

import com.example.geyugoapp.domain.users.models.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun observeAll(): Flow<List<User>>

    suspend fun insert(user: User)
}