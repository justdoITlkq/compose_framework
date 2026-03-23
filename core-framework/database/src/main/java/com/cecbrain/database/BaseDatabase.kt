package com.cecbrain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cecbrain.database.converter.BigDecimalConverter
import com.cecbrain.database.entity.FrameworkInfoEntity


@Database(entities = [FrameworkInfoEntity::class], version = 1, exportSchema = false)
@TypeConverters(BigDecimalConverter::class)
abstract class BaseDatabase: RoomDatabase() {
}