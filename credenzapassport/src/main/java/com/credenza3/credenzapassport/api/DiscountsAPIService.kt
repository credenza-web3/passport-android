package com.credenza3.credenzapassport.api

import com.credenza3.credenzapassport.api.model.RequestTokenRequest
import com.credenza3.credenzapassport.api.model.RequestTokenResponse
import com.credenza3.credenzapassport.api.model.ValidateRulesetRequest
import com.credenza3.credenzapassport.api.model.ValidateRulesetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface DiscountsAPIService {

    @POST("discounts/rulesets/validate")
    suspend fun validateRuleset(@Body validateRulesetRequest: ValidateRulesetRequest): Response<ValidateRulesetResponse>

    @POST("chains/{chainId}/{contractAddress}/tokens/airDrop")
    suspend fun requestToken(
        @Path("chainId") chainId: String,
        @Path("contractAddress") contractAddress: String,
        @Body requestTokenRequest: RequestTokenRequest,
    ): Response<RequestTokenResponse>

    @POST("chains/{chainId}/requestLoyaltyPoints")
    suspend fun requestLoyaltyPoints(
        @Path("chainId") chainId: String,
        @Query("eventId") eventId: String,
        @Query("contractAddress") contractAddress: String,
    ): Response<Unit>
}