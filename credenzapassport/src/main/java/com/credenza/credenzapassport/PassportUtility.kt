package com.credenza.credenzapassport

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import link.magic.android.EthNetwork
import link.magic.android.Magic
import link.magic.android.core.relayer.urlBuilder.CustomNodeConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithEmailOTPConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithMagicLinkConfiguration
import link.magic.android.modules.auth.response.DIDToken
import link.magic.android.modules.web3j.contract.MagicTxnManager
import okhttp3.Callback
import okhttp3.Request
import okio.ByteString.Companion.decodeHex
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jService
import org.web3j.protocol.core.methods.response.EthAccounts
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.tx.gas.ContractGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.math.BigInteger
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface PassportListener {
    fun onLoginComplete(address: String)
    fun onNFCScanComplete(address: String)
}


class PassportUtility(
    context: Context,
    val passportListener: PassportListener
) {

//    /// The authentication token to be used for API calls.
//    private var authenticationTokenC = ""
//
//    /// The address of the NFT smart contract.
//    private var nftContractAddressC = ""
//
//    /// The address of the stored value smart contract.
//    private var storedValueContractAddressC = ""
//
//    /// The address of the connected smart contract.
//    private var connectedContractAddressC = ""

    /// An instance of the Magic class.

    companion object {
        private const val TAG = "PassportUtility"

        private const val credenzaApiKey = "pk_live_A2A484A5087E5029"

        private val PREFERENCE_KEY_TOKEN = stringPreferencesKey("token")

        private val CONTRACT_CLASSES = mapOf("OzzieContract" to OzzieContract::class.java)
    }

    private val magic = Magic(
        context,
        credenzaApiKey,
        CustomNodeConfiguration("https://rpc-mumbai.maticvigil.com/", "80001")
    )
    private val web3j = Web3j.build(magic.rpcProvider)

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passport")

//    val accountToken =
//        "WyIweDU5MDY4N2NhMzk2NGJkM2E1YWViYzllODU2ZTkwZDFmNzE1MzIwZWE3MDFiOGExMzg5MTc4MDhjMDA2ZDhiZGUzNjE4ZTgyZTNmYWViYmNlOWJkY2RkYTk2YWVhNWRlMzVhNTY4OGMzYTljNDhiNmY3OGQ5MWZkM2Q0MzYyOTRjMWIiLCJ7XCJpYXRcIjoxNjg0OTM4MzIwLFwiZXh0XCI6MTY4NDkzOTIyMCxcImlzc1wiOlwiZGlkOmV0aHI6MHg2MTJCZjBiRDRjMzUxOTEyOUQ2N2U1M0Y0ZEJhODE3ZThDZTQ1N2FjXCIsXCJzdWJcIjpcIkFQMW1SRlNJQjVaaTBGeEFVWGE0Z2hEN3VSWGdIbXNPR2l1eVVrZ3Y1MkE9XCIsXCJhdWRcIjpcIjVtYzBJLVdNVmZyNHBvTE5vUzJvek1CTzkzUlVJSG42WlBJRV91TlhOQnc9XCIsXCJuYmZcIjoxNjg0OTM4MzIwLFwidGlkXCI6XCI2ZGFlNzNlNS1iZDBmLTQyZjQtYTM1Ni1hODg1ODBmZWRjZGZcIixcImFkZFwiOlwiMHg1ZmZjNjRlMzM4NjYxNGJkM2Q4NGM4ZDkyZWZlOWI0NTZhZmY1ODU3MWEzNTBhOGRhYWExNGRhMjFmNWI4ZjVlNjc2NGFlOTYxMmQ0NWMyOTRmNzc2YzRhYjg2OWE4YzVlNTc5NzIzYjM4NjAzZDZiODI0YTgwMmY2MWUwZjhjMzFjXCJ9Il0="


    /**
    Initializes the credentials needed for API calls and smart contract interaction.
    - Parameters:
    - authenticationToken: The authentication token to be used for API calls.
    - nftContractAddress: The address of the NFT smart contract.
    - storedValueContractAddress: The address of the stored value smart contract.
    - connectedContractAddress: The address of the connected smart contract.
     */
//    public fun initializeCredentials(
//        authenticationToken: String,
//        nftContractAddress: String,
//        storedValueContractAddress: String,
//        connectedContractAddress: String
//    ) {
//        authenticationTokenC =
//            "https://deep-index.moralis.io/api/v2/$authenticationToken/logs?chain=rinkeby" // moralisURL
//        nftContractAddressC = nftContractAddress
//        storedValueContractAddressC = storedValueContractAddress
//        connectedContractAddressC = connectedContractAddress
//    }


    /**
    Handles sign-in for a given email address using Magic.link SDK.
    - Parameters:
    - emailAddress: An email address for which to perform sign-in.
    - Note: Stores the login token in DataStore after successful authentication.
     */
    fun handleSignIn(context: Context, emailAddress: String) {
        Log.d("ABC1716", "handleSignIn: $emailAddress")

        val configuration = LoginWithMagicLinkConfiguration(emailAddress)
        magic.auth
            .loginWithMagicLink(context, configuration)
            .whenComplete { token: DIDToken?, error: Throwable? ->

                error?.let {
                    Log.e(TAG, "Failed to login", error)
                    return@whenComplete
                }

                token?.let {
                    if (token.hasError()) {
                        Log.e(TAG, "Failed to login ${token.error}")
                        return@whenComplete
                    }

                    runBlocking {
                        context.dataStore.edit {
                            it[PREFERENCE_KEY_TOKEN] = token.result
                        }
                    }

                    getAccount()
                }
            }
    }

    /**
    Gets the user's Ethereum public address using the Magic.link SDK and passes it to the `listener` instance.
    - Note: Calls `loginComplete(address:)` of the `listener` instance to pass the Ethereum public address to it.
     */
    private fun getAccount() {
        web3j.ethAccounts()
            .sendAsync()
            .whenComplete { accResponse: EthAccounts?, error: Throwable? ->
                error?.let {
                    Log.e(TAG, "Failed to get accounts", error)
                    return@whenComplete
                }

                accResponse?.let {
                    if (accResponse.hasError()) {
                        Log.e(TAG, "Failed to login ${accResponse.error}")
                        return@whenComplete
                    }

                    accResponse.result?.firstOrNull()?.let {

                        passportListener.onLoginComplete(it)

                    } ?: run {
                        Log.e(TAG, "No Account Found")
                    }
                }
            }
    }

    /**
    Checks the balance of NFT (Non-Fungible Token) of a user for a given contract address.
    - Parameters:
    - contractAddress: An Ethereum contract address for which to check NFT balance.
    - userAddress: An Ethereum public address of the user whose NFT balance to check.
    - Returns: The balance of NFT.
     */
    suspend fun nftCheck(
        context: Context,
        contractAddress: String,
        userAddress: String
    ): BigInteger? {
        TODO("Not yet implemented")
    }

    /**
    This asynchronous function checks the version of a contract for the given contract address and type.
    It gets the contract ABI (Application Binary Interface) using the 'getContractABI' function,
    constructs a contract instance, calls the 'getVersion' function to get the contract version,
    and then returns the version string or "NONE" if there was an error.
    - Parameters:
    - contractAddress: The contract address for which to check the version.
    - contractType: The type of the contract for which to check the version.
    - Returns: A string containing the contract version or "NONE" if there was an error.
     */
    suspend fun checkVersion(
        context: Context,
        contractAddress: String,
        contractType: String
    ): String {
        TODO("Not yet implemented")
    }

    private suspend fun getAccountToken(context: Context): String? =
        context.dataStore.data.firstOrNull()?.get(PREFERENCE_KEY_TOKEN)
}