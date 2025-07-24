package com.example.geyugoapp.datasource

import com.example.geyugoapp.database.UserDao
import com.example.geyugoapp.datasource.converters.toDbDto
import com.example.geyugoapp.datasource.converters.toUser
import com.example.geyugoapp.domain.users.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataSourceImpl(
    private val userDao: UserDao
): UserDataSource {

    override fun observeAll(): Flow<List<User>> = userDao.observeAll().map { flowList ->
        flowList.map { it.toUser() }
    }

    override suspend fun insert(user: User): Long {
        return userDao.insert(user.toDbDto())
    }

    override suspend fun getUserById(userId: Long): User {
        return userDao.getUserById(userId).toUser()
    }
}