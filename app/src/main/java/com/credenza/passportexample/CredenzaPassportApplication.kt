package com.credenza.passportexample

import android.app.Application
import link.magic.android.Magic
import link.magic.android.core.relayer.urlBuilder.CustomNodeConfiguration

private const val credenzaApiKey = "pk_live_A2A484A5087E5029"

class CredenzaPassportApplication : Application() {

    lateinit var magic: Magic

    override fun onCreate() {
        super.onCreate()

        magic = Magic(
            this,
            credenzaApiKey,
            CustomNodeConfiguration("https://rpc-mumbai.maticvigil.com/", "80001")
        )
    }
}