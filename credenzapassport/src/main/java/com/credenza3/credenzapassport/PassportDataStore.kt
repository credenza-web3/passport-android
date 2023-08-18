package com.credenza3.credenzapassport

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey


private val PREFERENCE_KEY_TOKEN = stringPreferencesKey("token")
private val PREFERENCE_KEY_USER_ACCOUNT = stringPreferencesKey("userAccount")
private val PREFERENCE_KEY_ADMIN_ACCOUNT = stringPreferencesKey("adminAccount")

class PassportDataStore(
    private val dataStore: DataStore<Preferences>
) {
    fun saveAdminAccount(adminAccount: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_ADMIN_ACCOUNT, adminAccount)

    fun getAdminAccount(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_ADMIN_ACCOUNT)

    fun saveUserAccount(userAccount: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_USER_ACCOUNT, userAccount)

    fun getUserAccount(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_USER_ACCOUNT)

    fun saveToken(token: String) = dataStore.saveDataStoreString(PREFERENCE_KEY_TOKEN, token)

    fun getToken(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_TOKEN)
}