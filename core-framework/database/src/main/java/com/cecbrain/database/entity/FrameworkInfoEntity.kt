package com.cecbrain.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "framework_info")
data class FrameworkInfoEntity(
    @PrimaryKey val id: Int = 1,
    val dbVersion: String = "1.0.0"
)