package com.credenza3.credenzapassport.api.model

data class ValidateRulesetRequest(
    val passportId: String,
    val ruleSetId: String,
)
