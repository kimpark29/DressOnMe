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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("processPreference")

class ProcessPreference @Inject constructor(@ApplicationContext val context: Context) {
    private val dataStore = context.dataStore

    fun getProcess() : Flow<Process> {
        return dataStore.data.map {
            Process(
                it[ID] ?: "",
                it[USER_ID] ?: "",
                it[LINK_MODEL] ?: "",
                it[LINK_FILTERING] ?: "",
                it[LINK_RESULT] ?: "",
            )
        }
    }

    suspend fun saveProcess(process: Process) {
        dataStore.edit {
            it[ID] = process._id
            it[USER_ID] = process.userId
            it[LINK_MODEL] = process.linkModel
            it[LINK_FILTERING] = process.linkFiltering
            it[LINK_RESULT] = process.linkResult
        }
    }

    companion object {
        private val ID = stringPreferencesKey("_id")
        private val USER_ID = stringPreferencesKey("userId")
        private val LINK_MODEL = stringPreferencesKey("linkModel")
        private val LINK_FILTERING = stringPreferencesKey("linkFiltering")
        private val LINK_RESULT = stringPreferencesKey("linkResult")
    }
}