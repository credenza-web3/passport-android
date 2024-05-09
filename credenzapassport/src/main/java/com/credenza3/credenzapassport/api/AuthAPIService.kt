package com.credenza3.credenzapassport.api

import com.credenza3.credenzapassport.api.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private const val PARAM_VALUE_GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"
private const val PARAM_VALUE_GRANT_TYPE_REFRESH_TOKEN = "refresh_token"

internal interface AuthAPIService {

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getTokenByCode(
        @Field(value = "client_id") clientId: String,
        @Field(value = "client_secret") clientSecret: String,
        @Field(value = "grant_type") grantType: String = PARAM_VALUE_GRANT_TYPE_AUTHORIZATION_CODE,
        @Field(value = "code") code: String,
        @Field(value = "code_verifier") codeVerifier: String,
        @Field(value = "redirect_uri") redirectURI: String,
    ): Response<TokenResponse>

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun refreshToken(
        @Field(value = "client_id") clientId: String,
        @Field(value = "client_secret") clientSecret: String,
        @Field(value = "grant_type") grantType: String = PARAM_VALUE_GRANT_TYPE_REFRESH_TOKEN,
        @Field(value = "refresh_token") refreshToken: String?,
    ): Response<TokenResponse>
}