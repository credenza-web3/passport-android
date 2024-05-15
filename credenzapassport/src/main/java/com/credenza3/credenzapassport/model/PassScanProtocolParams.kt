package com.credenza3.credenzapassport.model

import com.google.gson.Gson

sealed class PassScanProtocolParams(
    open val scanType: String,
)

data class AirDropParams(
    override val scanType: String,
    val contractAddress: String,
    val tokenId: Int,
    val amount: Int,
    val chainId: String,
) : PassScanProtocolParams(scanType)

data class LoyaltyPointsRequestParams(
    override val scanType: String,
    val contractAddress: String,
    val eventId: String,
    val chainId: String,
) : PassScanProtocolParams(scanType)

fun String.toPassScanProtocolParams(): PassScanProtocolParams {
    val gson = Gson()
    val scanType = gson.fromJson(this, PassScanProtocolParams::class.java).scanType
    return when (ScanType.fromName(scanType)) {
        ScanType.AirDrop -> gson.fromJson(this, AirDropParams::class.java)
        ScanType.RequestLoyaltyPoints -> gson.fromJson(this, LoyaltyPointsRequestParams::class.java)
        else -> throw IllegalArgumentException("Scan type '$scanType' is unknown")
    }
}