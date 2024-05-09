package com.credenza3.credenzapassport.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Range
import androidx.browser.customtabs.CustomTabsIntent
import com.credenza3.credenzapassport.PassportDataStore
import com.credenza3.credenzapassport.api.AuthApiProvider
import com.credenza3.credenzapassport.api.BASE_ACCOUNTS_URL
import com.credenza3.credenzapassport.base64URLEncode
import com.credenza3.credenzapassport.hashSha256
import java.security.SecureRandom
import java.util.Random
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val PARAM_NAME_CLIENT_ID = "client_id"
private const val PARAM_NAME_STATE = "state"
private const val PARAM_NAME_REDIRECT_URL = "redirect_uri"

private const val PARAM_NAME_SCOPE = "scope"
private const val PARAM_VALUE_SCOPE =
    "openid profile email phone blockchain.evm blockchain.evm.write offline.access"

private const val PARAM_NAME_RESPONSE_TYPE = "response_type"
private const val PARAM_VALUE_RESPONSE_TYPE = "code"

private const val PARAM_NAME_NONCE = "nonce"
private const val PARAM_VALUE_NONCE = "nonce123"

private const val PARAM_NAME_CODE_CHALLENGE = "code_challenge"
private const val PARAM_NAME_CODE_CHALLENGE_METHOD = "code_challenge_method"
private const val PARAM_VALUE_CODE_CHALLENGE_METHOD = "S256"

private val STATE_LENGTH_RANGE = Range(10, 512)
private const val STATE_ALLOWED_SYMBOLS =
    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_-="

internal class AuthClient(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectURL: String,
    private val passportDataStore: PassportDataStore,
) {

    private var stateValue: String? = null
    private val codeVerifier = generateCodeVerifier()

    private var signInContinuation: Continuation<Boolean>? = null

    fun isAuthorized(): Boolean = passportDataStore.getAccessToken() != null

    fun getAccessToken(): String? = passportDataStore.getAccessToken()

    suspend fun signIn(activity: Activity): Boolean {
        return suspendCoroutine { continuation ->
            activity.startActivity(Intent(activity, AuthActivity::class.java))
            signInContinuation = continuation
        }
    }

    suspend fun signInSilently(activity: Activity): Boolean {
        return suspendCoroutine { continuation ->
            activity.startActivity(Intent(activity, AuthActivity::class.java))
            signInContinuation = continuation
        }
    }

    fun launchSignInPage(activity: Activity) {
        val customTabIntent = CustomTabsIntent.Builder().build()
        customTabIntent.launchUrl(
            activity,
            generateSignUri()
        )
    }

    private fun generateSignUri(): Uri {
        stateValue = generateState()
        return Uri.parse(BASE_ACCOUNTS_URL + "oauth2/authorize")
            .buildUpon()
            .appendQueryParameter(PARAM_NAME_CLIENT_ID, clientId)
            .appendQueryParameter(PARAM_NAME_STATE, stateValue)
            .appendQueryParameter(PARAM_NAME_REDIRECT_URL, redirectURL)
            .appendQueryParameter(PARAM_NAME_SCOPE, PARAM_VALUE_SCOPE)
            .appendQueryParameter(PARAM_NAME_RESPONSE_TYPE, PARAM_VALUE_RESPONSE_TYPE)
            .appendQueryParameter(PARAM_NAME_NONCE, PARAM_VALUE_NONCE)
            .appendQueryParameter(PARAM_NAME_CODE_CHALLENGE, generateCodeChallenge())
            .appendQueryParameter(
                PARAM_NAME_CODE_CHALLENGE_METHOD,
                PARAM_VALUE_CODE_CHALLENGE_METHOD
            )
            .build()
    }

    private fun generateState(): String {
        val state = StringBuilder()
        val stateLength =
            Random().nextInt(STATE_LENGTH_RANGE.upper - STATE_LENGTH_RANGE.lower) + STATE_LENGTH_RANGE.lower
        repeat(stateLength) {
            val nextCharIndex = Random().nextInt(STATE_ALLOWED_SYMBOLS.length - 1)
            val nextChar = STATE_ALLOWED_SYMBOLS[nextCharIndex]
            state.append(nextChar)
        }
        return state.toString()
    }

    private fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val code = ByteArray(32)
        secureRandom.nextBytes(code)
        return Base64.encodeToString(code, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }

    private fun generateCodeChallenge(): String = codeVerifier.hashSha256().base64URLEncode()

    suspend fun onSignInCallback(code: String, state: String): Boolean {
        return signInContinuation?.let { continuation ->
            if (state != stateValue) {
                continuation.resumeWithException(AuthException("Invalid state value in redirect url"))
                return false
            }

            try {
                val response = AuthApiProvider.authAPIService.getTokenByCode(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    code = code,
                    codeVerifier = codeVerifier,
                    redirectURI = redirectURL,
                )
                return response.body()?.let { tokenResponse ->

                    passportDataStore.saveAccessToken(tokenResponse.accessToken)
                    passportDataStore.saveRefreshToken(tokenResponse.refreshToken)

                    continuation.resume(true)

                    return true

                } ?: run {
                    continuation.resumeWithException(AuthException("Failed to get access token"))
                    return@run false
                }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
                return false
            }
        } ?: false
    }

    suspend fun refreshToken(): Boolean {
        val response = AuthApiProvider.authAPIService.refreshToken(
            clientId = clientId,
            clientSecret = clientSecret,
            refreshToken = passportDataStore.getRefreshToken(),
        )
        return response.body()?.let { tokenResponse ->

            passportDataStore.saveAccessToken(tokenResponse.accessToken)
            passportDataStore.saveRefreshToken(tokenResponse.refreshToken)

            return true

        } ?: false
    }

    fun logout() {
        passportDataStore.removeAccessToken()
        passportDataStore.removeRefreshToken()
        passportDataStore.removeUserAccount()
    }
}

class AuthException : Exception {

    private var errorMessageResId: Int? = null

    constructor(cause: Throwable?, errorMessageResId: Int? = null) : super(cause) {
        this.errorMessageResId = errorMessageResId
    }

    constructor(errorMessage: String) : super(errorMessage)
}