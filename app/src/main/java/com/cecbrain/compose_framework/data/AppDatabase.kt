package com.cecbrain.compose_framework.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cecbrain.compose_framework.data.dao.TodoDao
import com.cecbrain.compose_framework.data.entity.TodoEntity


@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}