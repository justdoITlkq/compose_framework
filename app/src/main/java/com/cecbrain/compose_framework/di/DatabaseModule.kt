package com.cecbrain.compose_framework.di

import android.content.Context
import androidx.room.Room
import com.cecbrain.compose_framework.data.AppDatabase
import com.cecbrain.compose_framework.data.dao.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao {
        return db.todoDao()
    }
}