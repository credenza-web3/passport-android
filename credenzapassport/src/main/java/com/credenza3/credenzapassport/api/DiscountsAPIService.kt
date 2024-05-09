package com.credenza3.credenzapassport.api

import com.credenza3.credenzapassport.api.model.ValidateRulesetRequest
import com.credenza3.credenzapassport.api.model.ValidateRulesetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface DiscountsAPIService {

    @POST("discounts/rulesets/validate")
    suspend fun validateRuleset(@Body validateRulesetRequest: ValidateRulesetRequest): Response<ValidateRulesetResponse>
}