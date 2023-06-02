package com.nsirobaba.credenzapassportexample

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.credenza.credenzapassport.PassportListener
import com.credenza.credenzapassport.PassportUtility
import com.nsirobaba.credenzapassportexample.ui.theme.CredenzaPassportExampleTheme
import kotlinx.coroutines.launch

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

    val passportUtility = PassportUtility(
        context.applicationContext,
        (context.applicationContext as CredenzaPassportApplication).magic,
        object : PassportListener {

            override fun onLoginComplete(address: String) {
                showShortToast(context, "Login success for $address")
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

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        var email by remember { mutableStateOf("") }
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = {
                Text("Enter your email")
            }
        )

        Button(onClick = {
            passportUtility.handleSignIn(context, email)
        }) {
            Text(text = "Sign in")
        }

        Button(onClick = {
            coroutineScope.launch {
                val result = passportUtility.getVersion()
                showShortToast(context, "nftCheck: $result")
            }
        }) {
            Text(text = "getVersion")
        }

        Button(onClick = {
            coroutineScope.launch {
                val nftCheck = passportUtility.nftCheck(
                    "0x4d20968f609bf10e06495529590623d5d858c5c7",
                    "0x2d3e53bea19d756624dbfa3a9cd9b616878cf698"
                )
                showShortToast(context, "nftCheck: $nftCheck")
            }
        }) {
            Text(text = "nftCheck")
        }

        Button(onClick = {
            coroutineScope.launch {
                val checkVersion = passportUtility.checkVersion(
                    "0x4d20968f609bf10e06495529590623d5d858c5c7",
                    "OzzieContract"
                )
                showShortToast(context, "checkVersion: $checkVersion")
            }
        }) {
            Text(text = "checkVersion OzzieContract")
        }

        Button(onClick = {
            coroutineScope.launch {
                val result = passportUtility.checkVersion(
                    "0x61ff3d77ab2befece7b1c8e0764ac973ad85a9ef",
                    "LoyaltyContract"
                )
                showShortToast(context, result)
            }
        }) {
            Text(text = "checkVersion LoyaltyContract")
        }

        Button(onClick = {
            coroutineScope.launch {
                val result = passportUtility.checkNFTOwnership(
                    "0xfb28530d9d065ec81e826fa61baa51748c1ee775"
                )
                showShortToast(context, "$result")
            }
        }) {
            Text(text = "checkNFTOwnership")
        }
    }
}

private fun showShortToast(context: Context, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CredenzaPassportExampleTheme {
        MainContent()
    }
}