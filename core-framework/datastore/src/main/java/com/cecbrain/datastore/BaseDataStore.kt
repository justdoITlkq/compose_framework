package com.cecbrain.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


abstract class BaseDataStore(private val dataStore: DataStore<Preferences>) {


    // 统一的读取逻辑，处理 IO 异常
    protected fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences()) else throw exception
            }
            .map { preferences -> preferences[key] ?: defaultValue }
    }

    // 统一的写入逻辑
    protected suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // 清空数据
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}