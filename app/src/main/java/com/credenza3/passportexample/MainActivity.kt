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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
                    MainContent()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var userAddress: String? by remember { mutableStateOf(null) }

    val passportUtility = PassportUtility(
        context = context.applicationContext,
        magic = (context.applicationContext as CredenzaPassportApplication).magic,
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

    passportUtility.initializeCredentials(
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

        var email by remember { mutableStateOf("") }
        TextField(
            value = email,
            onValueChange = { email = it.trim() },
            placeholder = {
                Text("Enter your email")
            },
            keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Email)
        )

        Button(onClick = {
            passportUtility.handleSignIn(context, email)
        }) {
            Text(text = "Sign in")
        }

        userAddress?.let { userAddress ->
            AsyncButton(
                title = "getVersion",
                doAction = { passportUtility.getVersion() }
            )

            AsyncButton(
                title = "nftCheck",
                doAction = {
                    passportUtility.nftCheck(
                        "0x4d20968f609bf10e06495529590623d5d858c5c7",
                        userAddress
                    )?.toString() ?: ""
                }
            )

            AsyncButton(
                title = "checkVersion OzzieContract",
                doAction = {
                    passportUtility.checkVersion(
                        "0x4d20968f609bf10e06495529590623d5d858c5c7",
                        "OzzieContract"
                    )
                }
            )

            AsyncButton(
                title = "checkVersion LedgerContract",
                doAction = {
                    passportUtility.checkVersion(
                        "0x61ff3d77ab2befece7b1c8e0764ac973ad85a9ef",
                        "LedgerContract"
                    )
                }
            )

            AsyncButton(
                title = "checkNFTOwnership",
                doAction = {
                    passportUtility.checkNFTOwnership(
                        "0xfb28530d9d065ec81e826fa61baa51748c1ee775"
                    )?.toString() ?: ""
                }
            )

            AsyncButton(
                title = "addMembership",
                doAction = {
                    passportUtility.addMembership(
                        "0xDf3c92e0FD7eCc8199a453C7D72C685E5578b1fb",
                        userAddress
                    )
                    "Membership added"
                }
            )

            AsyncButton(
                title = "removeMembership",
                doAction = {
                    passportUtility.removeMembership(
                        "0xDf3c92e0FD7eCc8199a453C7D72C685E5578b1fb",
                        userAddress
                    )
                    "Membership removed"
                }
            )

            AsyncButton(
                title = "confirmMembership",
                doAction = {
                    val hasMembership = passportUtility.confirmMembership(
                        "0xDf3c92e0FD7eCc8199a453C7D72C685E5578b1fb",
                        "0xa8c9d33bf5990ae10a685a5f4869ba5dcb176ae8",
                        userAddress
                    )
                    if (hasMembership) "Has membership" else "No membership"
                }
            )

            AsyncButton(
                title = "getMembershipMetadata",
                doAction = {
                    passportUtility.getMembershipMetadata(
                        "0x3366F71c99A4684282BfE8af800194abeEF5F4C3",
                        "0xa8c9d33bf5990ae10a685a5f4869ba5dcb176ae8",
                        userAddress
                    )
                }
            )

            AsyncButton(
                title = "loyaltyCheck",
                doAction = {
                    passportUtility.loyaltyCheck(
                        "0xd7bf8920414268d891eb0451f4da79f98bebc9a2",
                        userAddress
                    ).toString()
                }
            )

            AsyncButton(
                title = "loyaltyAdd",
                doAction = {
                    passportUtility.loyaltyAdd(
                        "0xd7bf8920414268d891eb0451f4da79f98bebc9a2",
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
                    passportUtility.convertPointsToCoins(
                        "0xd7bf8920414268d891eb0451f4da79f98bebc9a2",
                        userAddress,
                        123.toBigInteger()
                    )
                    "Converted"
                }
            )

            AsyncButton(
                title = "loyaltyForfeit",
                doAction = {
                    passportUtility.loyaltyForfeit(
                        "0xd7bf8920414268d891eb0451f4da79f98bebc9a2",
                        userAddress,
                        123.toBigInteger()
                    )
                    "Points forfeited"
                }
            )

            AsyncButton(
                title = "loyaltyRedeem",
                doAction = {
                    passportUtility.loyaltyRedeem(
                        "0xd7bf8920414268d891eb0451f4da79f98bebc9a2",
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
                    passportUtility.loyaltyLifetimeCheck(
                        "0xd7bf8920414268d891eb0451f4da79f98bebc9a2",
                        userAddress
                    ).toString()
                }
            )

            AsyncButton(
                title = "svCheck",
                doAction = {
                    passportUtility.svCheck(
                        "0x893fBedDaDfdfb836CC069902F7270eA56fD6ebF",
                        userAddress
                    ).toString()
                }
            )

            AsyncButton(
                title = "getContractABI OzzieContract",
                doAction = {
                    passportUtility.getContractABI(
                        "OzzieContract"
                    )
                }
            )

            AsyncButton(
                title = "authN",
                doAction = {
                    passportUtility.authN()
                    ""
                }
            )
        }

        Button(onClick = {
            passportUtility.readNFC(context as Activity)
        }) {
            Text(text = "readNFC")
        }

        AsyncButton(
            title = "scanQRCode",
            doAction = {
                passportUtility.scanQRCode(context) ?: ""
            }
        )
    }
}

@Composable
fun AsyncButton(
    title: String,
    doAction: suspend () -> String,
    modifier: Modifier = Modifier
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CredenzaPassportExampleTheme {
        MainContent()
    }
}

@Preview
@Composable
fun AsyncButtonPreview() {
    CredenzaPassportExampleTheme {
        AsyncButton(title = "Sign in", doAction = { "" })
    }
}