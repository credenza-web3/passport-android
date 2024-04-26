package com.credenza3.credenzapassport.auth.api

import com.credenza3.credenzapassport.auth.api.model.AccountAddressResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface EVMAPIService {

    @GET("accounts/address")
    suspend fun getAccountAddress(): Response<AccountAddressResponse>
}