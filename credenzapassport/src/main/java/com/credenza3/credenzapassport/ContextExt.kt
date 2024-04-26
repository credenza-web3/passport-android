package com.credenza3.credenzapassport

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.lang.IllegalArgumentException

fun Context.getMetaData(name: String): String? {
    val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        )
    }
    return applicationInfo.metaData.getString(name)
}

fun Context.getMetaDataOrThrowException(name: String): String =
    getMetaData(name) ?: throw IllegalArgumentException("Missing meta-data '$name'")