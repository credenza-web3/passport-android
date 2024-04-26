package com.credenza3.credenzapassport.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.credenza3.credenzapassport.KEY_OAUTH_REDIRECT_URL
import com.credenza3.credenzapassport.PassportUtility
import com.credenza3.credenzapassport.getMetaData
import kotlinx.coroutines.launch

private const val PARAM_NAME_CODE = "code"
private const val PARAM_NAME_STATE = "state"

internal class AuthActivity : ComponentActivity() {

    private var waitingAuthResult: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val authClient = PassportUtility.authClient

        if (!isRedirectURLIntent(intent)) {
            authClient.launchSignInPage(this)
        } else {
            handleRedirectURLIntent(intent)
        }

        println(authClient)
    }

    private fun handleRedirectURLIntent(intent: Intent?) {
        lifecycleScope.launch {
            waitingAuthResult = false

            val code = intent?.data?.getQueryParameter(PARAM_NAME_CODE) ?: return@launch
            val state = intent.data?.getQueryParameter(PARAM_NAME_STATE) ?: return@launch

            PassportUtility.authClient.onSignInCallback(code, state)

            finish()
        }
    }


    private fun isRedirectURLIntent(intent: Intent?): Boolean =
        getMetaData(KEY_OAUTH_REDIRECT_URL)?.let { redirectURLStr ->
            val redirectURL = Uri.parse(redirectURLStr)
            return@let intent?.data?.scheme == redirectURL.scheme
                    && intent?.data?.host == redirectURL.host
                    && intent?.data?.path == redirectURL.path
        } ?: false
}