package com.cecbrain.database.di

import android.content.Context
import androidx.room.Room
import com.cecbrain.database.BaseDatabase
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
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): BaseDatabase {
        return Room.databaseBuilder(
            context,
            BaseDatabase::class.java,
            "cecbrain_db"
        )
            .fallbackToDestructiveMigration(false) // 开发阶段：数据库结构改变直接清空重新创建
            .build()
    }
}