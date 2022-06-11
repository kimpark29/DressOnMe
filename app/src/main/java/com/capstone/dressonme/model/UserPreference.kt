package com.capstone.dressonme.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class UserPreference @Inject constructor(@ApplicationContext val context: Context) {

  private val dataStore = context.dataStore

  fun getUser(): Flow<User> {
    return dataStore.data.map {
      User(
        it[USER_ID] ?: "",
        it[TOKEN_KEY] ?: "",
        it[EMAIL] ?: "",
        it[NAME] ?: ""
      )
    }
  }

  suspend fun saveUser(user: User) {
    dataStore.edit {
      it[USER_ID] = user.userId
      it[TOKEN_KEY] = user.token
      it[EMAIL] = user.email
      it[NAME] = user.name
    }
  }

  suspend fun logout() {
    dataStore.edit {
      it[USER_ID] = ""
      it[TOKEN_KEY] = ""
      it[EMAIL] = ""
      it[NAME] = ""
    }
  }

  companion object {
    private val USER_ID = stringPreferencesKey("userid")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val EMAIL = stringPreferencesKey("email")
    private val NAME = stringPreferencesKey("name")
  }
}