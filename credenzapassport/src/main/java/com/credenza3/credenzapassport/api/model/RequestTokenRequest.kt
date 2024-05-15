package com.credenza3.credenzapassport.api.model

data class RequestTokenRequest(
    val targetAddress: String,
    val tokenId: Int,
    val amount: Int,
)
