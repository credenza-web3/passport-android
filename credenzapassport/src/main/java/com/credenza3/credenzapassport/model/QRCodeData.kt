package com.credenza3.credenzapassport.model

data class QRCodeData(
    val scanType: String,
    val date: String,
    val chainId: String,
    val sig: String,
)