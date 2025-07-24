package com.example.geyugoapp.di

import com.example.geyugoapp.domain.users.usecases.GetUserById
import com.example.geyugoapp.domain.users.usecases.InsertUser
import com.example.geyugoapp.domain.users.usecases.ObserveAllUsers
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

}