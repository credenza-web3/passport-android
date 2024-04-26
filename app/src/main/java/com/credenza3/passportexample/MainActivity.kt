package com.credenza3.passportexample

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.credenza3.credenzapassport.PassportListener
import com.credenza3.credenzapassport.PassportUtility
import com.credenza3.passportexample.ui.theme.CredenzaPassportExampleTheme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CredenzaPassportExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(this)
                }
            }
        }
    }
}

@Composable
fun MainContent(
    activity: Activity,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var userAddress: String? by remember { mutableStateOf(null) }

    PassportUtility.init(
        context = context.applicationContext,
        chainId = chainId.toLong(),
        passportListener = object : PassportListener {

            override fun onLoginComplete(address: String) {
                showShortToast(context, "Login success for $address")
                userAddress = address
            }

            override fun onNFCScanComplete(address: String) {
                showShortToast(context, "NFC scan success for $address")
            }
        })

    PassportUtility.initializeCredentials(
        authenticationToken = "4ouieBFuLE1J5icxWwsYiOY1VzPNxwQiQX4FfjWm9sTtjlFFm9sFbefxB83iNf2C",
        nftContractAddress = "0x4d20968f609bf10e06495529590623d5d858c5c7", // OzzieContract
        storedValueContractAddress = "",
        connectedContractAddress = ""
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        if (!PassportUtility.isUserLoggedIn()) {
            Button(onClick = {
                coroutineScope.launch {
                    PassportUtility.handleSignIn(activity)
                }
            }) {
                Text(text = "Sign in")
            }
        } else {
            userAddress = PassportUtility.getCurrentAccount()
            Button(onClick = {
                coroutineScope.launch {
                    PassportUtility.logout()
                    userAddress = null
                }
            }) {
                Text(text = "Sign out")
            }

            SectionDivider()
        }

        userAddress?.let { userAddress ->

            AsyncButton(
                title = "getVersion",
                doAction = { PassportUtility.getVersion() }
            )


//            AsyncButton(
//                title = "nftCheck",
//                doAction = {
//                    PassportUtility.nftCheck(
//                        "0x4d20968f609bf10e06495529590623d5d858c5c7",
//                        userAddress
//                    )?.toString() ?: ""
//                }
//            )

//            AsyncButton(
//                title = "checkVersion OzzieContract",
//                doAction = {
//                    PassportUtility.checkVersion(
//                        "0x4d20968f609bf10e06495529590623d5d858c5c7",
//                        "OzzieContract"
//                    )
//                }
//            )

            AsyncButton(
                title = "checkVersion LedgerContract",
                doAction = {
                    PassportUtility.checkVersion(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        "LedgerContract"
                    )
                }
            )

            AsyncButton(
                title = "checkNFTOwnership",
                doAction = {
                    PassportUtility.checkNFTOwnership(
                        "0xfb28530d9d065ec81e826fa61baa51748c1ee775"
                    )?.toString() ?: ""
                }
            )

            SectionDivider()

            AsyncButton(
                title = "addMembership",
                doAction = {
                    PassportUtility.addMembership(
                        "0x3d549e5078aa0b168e274493a640a718f5e16647",
                        userAddress,
                        "{\"tir\": \"gold\"}",
                    )
                    "Membership added"
                }
            )

            AsyncButton(
                title = "removeMembership",
                doAction = {
                    PassportUtility.removeMembership(
                        "0x3d549e5078aa0b168e274493a640a718f5e16647",
                        userAddress
                    )
                    "Membership removed"
                }
            )

            AsyncButton(
                title = "confirmMembership",
                doAction = {
                    val hasMembership = PassportUtility.confirmMembership(
                        "0x3d549e5078aa0b168e274493a640a718f5e16647",
                        userAddress
                    )
                    if (hasMembership) "Has membership" else "No membership"
                }
            )

            AsyncButton(
                title = "getMembershipMetadata",
                doAction = {
                    PassportUtility.getMembershipMetadata(
                        "0x3d549e5078aa0b168e274493a640a718f5e16647",
                        userAddress
                    )
                }
            )

            SectionDivider()

            AsyncButton(
                title = "loyaltyCheck",
                doAction = {
                    PassportUtility.loyaltyCheck(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        userAddress
                    ).toString()
                }
            )

            AsyncButton(
                title = "loyaltyAdd",
                doAction = {
                    PassportUtility.loyaltyAdd(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        userAddress,
                        123.toBigInteger(),
                        456.toBigInteger()
                    )
                    "Loyalty added"
                }
            )

            AsyncButton(
                title = "convertPointsToCoins",
                doAction = {
                    PassportUtility.convertPointsToCoins(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        userAddress,
                        123.toBigInteger()
                    )
                    "Converted"
                }
            )

            AsyncButton(
                title = "loyaltyForfeit",
                doAction = {
                    PassportUtility.loyaltyForfeit(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        userAddress,
                        123.toBigInteger()
                    )
                    "Points forfeited"
                }
            )

            AsyncButton(
                title = "loyaltyRedeem",
                doAction = {
                    PassportUtility.loyaltyRedeem(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        userAddress,
                        123.toBigInteger(),
                        456.toBigInteger()
                    )
                    "Points reduced"
                }
            )

            AsyncButton(
                title = "loyaltyLifetimeCheck",
                doAction = {
                    PassportUtility.loyaltyLifetimeCheck(
                        "0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F",
                        userAddress
                    ).toString()
                }
            )

            SectionDivider()

            AsyncButton(
                title = "svCheck",
                doAction = {
                    PassportUtility.svCheck(
                        "0x2c88F13a02e2798D214A88A8Be3c7fb82aCeA3a7",
                        userAddress
                    ).toString()
                }
            )

//            AsyncButton(
//                title = "getContractABI OzzieContract",
//                doAction = {
//                    PassportUtility.getContractABI(
//                        "OzzieContract"
//                    )
//                }
//            )

            AsyncButton(
                title = "authN",
                doAction = {
                    PassportUtility.authN()
                    ""
                }
            )
        }

        SectionDivider()

        Button(onClick = {
            PassportUtility.readNFC(context as Activity)
        }) {
            Text(text = "readNFC")
        }

        AsyncButton(
            title = "scanQRCode",
            doAction = {
                PassportUtility.scanQRCode(context) ?: ""
            }
        )
    }
}

@Composable
fun AsyncButton(
    title: String,
    doAction: suspend () -> String,
    modifier: Modifier = Modifier,
) {
    var isInProgress by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        Log.e(TAG, "Error: ${e.message}", e)
        isInProgress = false
        showShortToast(context, "Error: ${e.message ?: "Unknown"}")
    }


    Row(
        verticalAlignment = CenterVertically,
        modifier = modifier
    ) {
        Button(
            onClick = {
                isInProgress = true
                coroutineScope.launch(exceptionHandler + SupervisorJob()) {
                    val result = doAction()
                    isInProgress = false
                    showShortToast(context, "$title: $result")
                }
            }
        ) {
            Text(text = title)
        }

        if (isInProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(7.dp)
                    .size(20.dp)
            )
        }
    }
}

private fun showShortToast(context: Context, message: String) {
    (context as MainActivity).lifecycleScope.launch(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SectionDivider(modifier: Modifier = Modifier) {
    Divider(modifier = modifier.padding(vertical = 12.dp))
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CredenzaPassportExampleTheme {
//        MainContent()
    }
}

@Preview
@Composable
fun AsyncButtonPreview() {
    CredenzaPassportExampleTheme {
        AsyncButton(title = "Sign in", doAction = { "" })
    }
}