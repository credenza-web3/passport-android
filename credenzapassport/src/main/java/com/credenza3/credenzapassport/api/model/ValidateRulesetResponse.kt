package com.credenza3.credenzapassport.api.model

data class ValidateRulesetResponse(
    val userAddress: String,
    val discount: Discount,
)

data class Discount(
    val rate: String,
    val code: String,
)