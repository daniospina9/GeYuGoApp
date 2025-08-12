package com.example.geyugoapp.di

import com.example.geyugoapp.domain.users.usecases.DeleteUser
import com.example.geyugoapp.domain.users.usecases.GetAllUsers
import com.example.geyugoapp.domain.users.usecases.GetUserById
import com.example.geyugoapp.domain.users.usecases.GetUserIdByOnlineStatus
import com.example.geyugoapp.domain.users.usecases.GetUsersCount
import com.example.geyugoapp.domain.users.usecases.InsertUser
import com.example.geyugoapp.domain.users.usecases.ObserveAllUsers
import com.example.geyugoapp.domain.users.usecases.UpdateAllUsersOnlineStatus
import com.example.geyugoapp.domain.users.usecases.UpdateUserOnlineStatus
import com.example.geyugoapp.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Singleton
    @Provides
    fun provideInsertUser(
        repository: UserRepository
    ): InsertUser = InsertUser(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideObserveAllUsers(
        repository: UserRepository
    ): ObserveAllUsers = ObserveAllUsers(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetUserById(
        repository: UserRepository
    ): GetUserById = GetUserById(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetAllUsers(
        repository: UserRepository
    ): GetAllUsers = GetAllUsers(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideDeleteUser(
        repository: UserRepository
    ): DeleteUser = DeleteUser (
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetUsersCount(
        repository: UserRepository
    ): GetUsersCount = GetUsersCount(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideUpdateAllUsersOnlineStatus(
        repository: UserRepository
    ): UpdateAllUsersOnlineStatus = UpdateAllUsersOnlineStatus(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideUpdateUserOnlineStatus(
        repository: UserRepository
    ): UpdateUserOnlineStatus = UpdateUserOnlineStatus(
        repository = repository
    )

    @Singleton
    @Provides
    fun provideGetUserIdByOnlineStatus(
        repository: UserRepository
    ): GetUserIdByOnlineStatus = GetUserIdByOnlineStatus(
        repository = repository
    )

}