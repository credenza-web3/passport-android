package com.credenza3.credenzapassport

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey


private val PREFERENCE_KEY_ACCESS_TOKEN = stringPreferencesKey("accessToken")
private val PREFERENCE_KEY_REFRESH_TOKEN = stringPreferencesKey("refreshToken")
private val PREFERENCE_KEY_USER_ACCOUNT = stringPreferencesKey("userAccount")
private val PREFERENCE_KEY_ADMIN_ACCOUNT = stringPreferencesKey("adminAccount")

class PassportDataStore(
    private val dataStore: DataStore<Preferences>,
) {
    fun saveAdminAccount(adminAccount: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_ADMIN_ACCOUNT, adminAccount)

    fun getAdminAccount(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_ADMIN_ACCOUNT)

    fun saveUserAccount(userAccount: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_USER_ACCOUNT, userAccount)

    fun getUserAccount(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_USER_ACCOUNT)

    fun removeUserAccount() = dataStore.clearDataStoreValue(PREFERENCE_KEY_USER_ACCOUNT)

    fun saveAccessToken(accessToken: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_ACCESS_TOKEN, accessToken)

    fun getAccessToken(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_ACCESS_TOKEN)

    fun removeAccessToken() = dataStore.clearDataStoreValue(PREFERENCE_KEY_ACCESS_TOKEN)

    fun saveRefreshToken(refreshToken: String) =
        dataStore.saveDataStoreString(PREFERENCE_KEY_REFRESH_TOKEN, refreshToken)

    fun getRefreshToken(): String? = dataStore.getDataStoreString(PREFERENCE_KEY_REFRESH_TOKEN)

    fun removeRefreshToken() = dataStore.clearDataStoreValue(PREFERENCE_KEY_REFRESH_TOKEN)
}