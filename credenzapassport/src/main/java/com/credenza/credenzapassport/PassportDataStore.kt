package com.credenza.credenzapassport

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey


private val PREFERENCE_KEY_TOKEN = stringPreferencesKey("token")
private val PREFERENCE_KEY_ACCOUNT = stringPreferencesKey("account")

class PassportDataStore(
    private val dataStore: DataStore<Preferences>
) {
    fun saveAccount(account: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_ACCOUNT, account)

    fun getAccount(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_ACCOUNT)

    fun saveToken(token: String) = dataStore.saveDataStoreString(PREFERENCE_KEY_TOKEN, token)

    fun getToken(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_TOKEN)
}