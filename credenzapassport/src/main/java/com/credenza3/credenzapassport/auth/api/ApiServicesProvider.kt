package com.credenza3.credenzapassport.auth.api

import com.credenza3.credenzapassport.auth.AuthClient
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_ACCOUNTS_URL = "https://accounts.credenza3.com/"
const val BASE_EVM_URL = "https://evm.credenza3.com/"

private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_PREFIX_BEARER = "Bearer "

private const val HTTP_STATUS_CODE_UNAUTHORIZED = 401

private val okHttpClient: OkHttpClient by lazy { OkHttpClient.Builder().build() }
private val gsonConverterFactory: GsonConverterFactory by lazy {
    GsonConverterFactory.create(
        GsonBuilder().create()
    )
}

internal object AuthApiProvider {

    val authAPIService: AuthAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_ACCOUNTS_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(AuthAPIService::class.java)
    }
}

internal class EVMApiProvider(
    private val authClient: AuthClient,
) {

    val evmAPIService: EVMAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_EVM_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(getAuthInterceptor())
                    .build()
            )
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(EVMAPIService::class.java)
    }


    private fun getAuthInterceptor(): Interceptor =
        Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()

            authClient.getAccessToken()?.let { accessToken ->
                requestBuilder.addHeader(
                    HEADER_AUTHORIZATION,
                    HEADER_PREFIX_BEARER + accessToken
                )
            }

            val response = chain.proceed(requestBuilder.build())

            if (response.code == HTTP_STATUS_CODE_UNAUTHORIZED
                && authClient.isAuthorized()
            ) {
                val isRefreshSuccess = runBlocking {
                    authClient.refreshToken()
                }
                if (isRefreshSuccess) {
                    response.close()
                    return@Interceptor chain.proceed(requestBuilder.build())
                }
            }
            return@Interceptor response
        }
}