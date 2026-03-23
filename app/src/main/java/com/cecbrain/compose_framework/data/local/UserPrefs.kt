package com.cecbrain.compose_framework.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cecbrain.datastore.BaseDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(
    dataStore: DataStore<Preferences>
) : BaseDataStore(dataStore) {

    private val TOKEN = stringPreferencesKey("token")

    val token = getValue(TOKEN, "")

    suspend fun saveToken(value: String) = setValue(TOKEN, value)
}