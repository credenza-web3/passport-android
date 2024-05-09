package com.credenza3.credenzapassport.api.model

import com.google.gson.annotations.SerializedName

internal data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("id_token")
    val idToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,
)
