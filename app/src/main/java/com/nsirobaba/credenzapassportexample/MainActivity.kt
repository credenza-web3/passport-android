package com.nsirobaba.credenzapassportexample

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.credenza.credenzapassport.PassportListener
import com.credenza.credenzapassport.PassportUtility
import com.nsirobaba.credenzapassportexample.ui.theme.CredenzaPassportExampleTheme
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CredenzaPassportExampleTheme {
                // A surface container using the 'background' color from the theme
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

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val passportUtility = PassportUtility(context.applicationContext, object : PassportListener {

        override fun onLoginComplete(address: String) {
            showShortToast(context, "Login success for $address")
        }

        override fun onNFCScanComplete(address: String) {
            showShortToast(context, "NFC scan success for $address")
        }
    })

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        Button(onClick = {
            passportUtility.handleSignIn(context, "natalija.sirobaba@gmail.com")
        }) {
            Text(text = "Sign in")
        }

        Button(onClick = {
            coroutineScope.launch {
                val nftCheck = passportUtility.nftCheck(
                    context,
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
                val nftCheck = passportUtility.checkVersion(
                    context,
                    "0x4d20968f609bf10e06495529590623d5d858c5c7",
                    "OzzieContract"
                )
                showShortToast(context, "nftCheck: $nftCheck")
            }
        }) {
            Text(text = "checkVersion")
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