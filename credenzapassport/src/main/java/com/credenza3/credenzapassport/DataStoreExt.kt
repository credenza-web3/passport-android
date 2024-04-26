package com.credenza3.credenzapassport

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

fun DataStore<Preferences>.saveDataStoreString(
    key: Preferences.Key<String>,
    value: String
) = runBlocking {
    edit {
        it[key] = value
    }
}

fun DataStore<Preferences>.getDataStoreString(
    key: Preferences.Key<String>
): String? = runBlocking {
    data.firstOrNull()?.get(key)
}

fun DataStore<Preferences>.clearDataStoreValue(
    key: Preferences.Key<String>,
) = runBlocking {
    edit {
        it.remove(key)
    }
}