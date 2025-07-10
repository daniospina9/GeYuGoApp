package com.example.geyugoapp.di

import android.content.Context
import androidx.room.Room
import com.example.geyugoapp.database.AppDatabase
import com.example.geyugoapp.database.UserDao
import com.example.geyugoapp.datasource.UserDataSource
import com.example.geyugoapp.datasource.UserDataSourceImpl
import com.example.geyugoapp.repository.UserRepository
import com.example.geyugoapp.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
       @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "user-db"
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(
        appDatabase: AppDatabase
    ): UserDao = appDatabase.userDao()

    @Singleton
    @Provides
    fun provideUserDataSource(
        userDao: UserDao
    ): UserDataSource = UserDataSourceImpl(
        userDao = userDao
    )

    @Singleton
    @Provides
    fun provideUserRepository(
        userDataSource: UserDataSource
    ): UserRepository = UserRepositoryImpl(
        userDataSource = userDataSource
    )
}