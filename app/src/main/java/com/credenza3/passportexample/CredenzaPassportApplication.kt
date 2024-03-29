package com.credenza3.passportexample

import android.app.Application
import link.magic.android.Magic
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration

private const val credenzaApiKey = "pk_live_A2A484A5087E5029"
private const val rpcUrl = "https://rpc-mumbai.maticvigil.com/"
const val chainId = "80001"

class CredenzaPassportApplication : Application() {

    lateinit var magic: Magic

    override fun onCreate() {
        super.onCreate()

        magic = Magic(
            this,
            credenzaApiKey,
            CustomNodeConfiguration(rpcUrl, chainId)
        )
    }
}