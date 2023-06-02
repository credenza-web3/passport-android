package com.credenza.credenzapassport

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.credenza.credenzapassport.contracts.ConnectedPackagingContract
import com.credenza.credenzapassport.contracts.ERC20TestContract
import com.credenza.credenzapassport.contracts.LoyaltyContract
import com.credenza.credenzapassport.contracts.MetadataMembershipContract
import com.credenza.credenzapassport.contracts.NFTOwnership
import com.credenza.credenzapassport.contracts.OzzieContract
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import link.magic.android.Magic
import link.magic.android.modules.auth.requestConfiguration.LoginWithMagicLinkConfiguration
import link.magic.android.modules.auth.response.DIDToken
import link.magic.android.modules.web3j.contract.MagicTxnManager
import okhttp3.OkHttpClient
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthAccounts
import org.web3j.tx.Contract
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passport")

interface PassportListener {
    fun onLoginComplete(address: String)
    fun onNFCScanComplete(address: String)
}

class PassportUtility(
    context: Context,
    val magic: Magic,
    private val passportListener: PassportListener
) {

    companion object {
        private const val TAG = "PassportUtility"

        private val PREFERENCE_KEY_TOKEN = stringPreferencesKey("token")
        private val PREFERENCE_KEY_ACCOUNT = stringPreferencesKey("account")

        private val CONTRACT_CLASSES = mapOf(
            "OzzieContract" to OzzieContract::class.java,
            "MetadataMembershipContract" to MetadataMembershipContract::class.java,
            "LoyaltyContract" to LoyaltyContract::class.java,
            "ERC20TestContract" to ERC20TestContract::class.java,
            "ConnectedPackagingContract" to ConnectedPackagingContract::class.java,
            "NFTOwnership" to NFTOwnership::class.java,
        )
    }

    // The authentication token to be used for API calls.
    private var authenticationTokenC = ""

    // The address of the NFT smart contract.
    private var nftContractAddressC = ""

    // The address of the stored value smart contract.
    private var storedValueContractAddressC = ""

    // The address of the connected smart contract.
    private var connectedContractAddressC = ""

    private val web3j = Web3j.build(magic.rpcProvider)
    private val okHttpClient = OkHttpClient()

    private val dataStore: DataStore<Preferences> = context.dataStore

    /**
     * Initializes the credentials needed for API calls and smart contract interaction.
     *
     * @param authenticationToken: The authentication token to be used for API calls.
     * @param nftContractAddress: The address of the NFT smart contract.
     * @param storedValueContractAddress: The address of the stored value smart contract.
     * @param connectedContractAddress: The address of the connected smart contract.
     */
    fun initializeCredentials(
        authenticationToken: String,
        nftContractAddress: String,
        storedValueContractAddress: String,
        connectedContractAddress: String
    ) {
        authenticationTokenC = authenticationToken
        nftContractAddressC = nftContractAddress
        storedValueContractAddressC = storedValueContractAddress
        connectedContractAddressC = connectedContractAddress
    }

    /**
     * Retrieves a version number from a specific smart contract.
     *
     * @return An asynchronous task that returns the version number as a String.
     */
    suspend fun getVersion() =
        checkVersion("0x61ff3d77ab2befece7b1c8e0764ac973ad85a9ef", "LoyaltyContract")

    /**
     * Handles sign-in for a given email address using Magic.link SDK.
     * Note: Stores the login token in DataStore after successful authentication.
     *
     * @param context Context
     * @param emailAddress: An email address for which to perform sign-in.
     */
    fun handleSignIn(context: Context, emailAddress: String) {

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

                    saveDataStoreString(PREFERENCE_KEY_TOKEN, token.result)
                    getAccount()
                }
            }
    }

    /**
     * Gets the user's Ethereum public address using the Magic.link SDK and passes it to the `listener` instance.
     * Note: Calls `loginComplete(address:)` of the `listener` instance to pass the Ethereum public address to it.
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

                    accResponse.result?.firstOrNull()?.let { account ->

                        saveDataStoreString(PREFERENCE_KEY_ACCOUNT, account)
                        passportListener.onLoginComplete(account)

                    } ?: run {
                        Log.e(TAG, "No Account Found")
                    }
                }
            }
    }

    /**
     * Checks the balance of NFT (Non-Fungible Token) of a user for a given contract address.
     *
     * @param contractAddress: An Ethereum contract address for which to check NFT balance.
     * @param userAddress: An Ethereum public address of the user whose NFT balance to check.
     * @return The balance of NFT.
     */
    suspend fun nftCheck(
        contractAddress: String,
        userAddress: String
    ): BigInteger? =
        suspendCoroutine { continuation ->

            val contract = getContract(OzzieContract::class.java, contractAddress)

            contract.balanceOfBatch(listOf(userAddress), listOf(BigInteger("2")))
                .sendAsync()
                .whenComplete { response, throwable ->
                    throwable?.let {
                        Log.e(
                            TAG,
                            "Failed to get balanceOfBatch for contract $contractAddress, user $userAddress",
                            throwable
                        )
                        continuation.resumeWithException(it)
                        return@whenComplete
                    }

                    continuation.resume(response?.firstOrNull() as? BigInteger?)
                }
        }

    /**
     * This function checks the ownership of an NFT (Non-Fungible Token) for the given Ethereum address.
     * It constructs a contract instance, calls the 'balanceOfBatch' function to get the NFT balance,
     * and then prints the response message or error message accordingly.
     *
     * @param address: The Ethereum address for which to check the NFT ownership.
     */
    suspend fun checkNFTOwnership(
        address: String
    ): BigInteger? =
        suspendCoroutine { continuation ->

            val contract = getContract(NFTOwnership::class.java, nftContractAddressC)

            contract.balanceOfBatch(listOf(address), listOf(BigInteger("2")))
                .sendAsync()
                .whenComplete { response, throwable ->
                    throwable?.let {
                        Log.e(
                            TAG,
                            "Failed to get balanceOfBatch for contract $nftContractAddressC, user $address",
                            throwable
                        )
                        continuation.resumeWithException(it)
                        return@whenComplete
                    }

                    continuation.resume(response?.firstOrNull() as? BigInteger?)
                }
        }

    /**
     * This asynchronous function checks the version of a contract for the given contract address and type.
     * It gets the contract ABI (Application Binary Interface) using the 'getContractABI' function,
     * constructs a contract instance, calls the 'getVersion' function to get the contract version,
     * and then returns the version string or "NONE" if there was an error.
     *
     * @param contractAddress: The contract address for which to check the version.
     * @param contractType: The type of the contract for which to check the version.
     * @return A string containing the contract version or "NONE" if there was an error.
     */
    suspend fun checkVersion(
        contractAddress: String,
        contractType: String
    ): String =
        suspendCoroutine { continuation ->

            val contract = getContract(contractType, contractAddress)

            val versionRequest = when (contract) {
                is OzzieContract -> contract.version
                is MetadataMembershipContract -> contract.version
                is LoyaltyContract -> contract.version
                is ERC20TestContract -> contract.version
                is ConnectedPackagingContract -> contract.version
                else -> throw IllegalArgumentException("Contract of type $contractType is not supported")
            }

            versionRequest.sendAsync()
                .whenComplete { response, throwable ->
                    throwable?.let {
                        Log.e(
                            TAG,
                            "Failed to get version for contract $contractAddress",
                            throwable
                        )
                        continuation.resumeWithException(it)
                        return@whenComplete
                    }

                    continuation.resume(response)
                }
        }

    /**
     * Returns a smart contract object.
     *
     * @param contractClassName: The name of the smart contract class.
     * @param contractAddress: The address of the contract.
     * @return The object of the smart contract class
     */
    private fun getContract(
        contractClassName: String,
        contractAddress: String
    ): Contract = runBlocking {
        val contractClass = CONTRACT_CLASSES[contractClassName]?.asSubclass(Contract::class.java)
            ?: throw IllegalArgumentException("Contract of type $contractClassName is not supported")

        return@runBlocking getContract(
            contractClass,
            contractAddress
        )
    }

    private fun <T : Contract> getContract(
        contractClass: Class<T>,
        contractAddress: String
    ): T = runBlocking {
        val gasProvider = DefaultGasProvider()
        val accountAddress = getDataStoreString(PREFERENCE_KEY_ACCOUNT)
            ?: throw IllegalStateException("Current user is not signed in")

        return@runBlocking when (contractClass) {
            OzzieContract::class.java -> OzzieContract.load(
                contractAddress,
                web3j,
                MagicTxnManager(web3j, accountAddress),
                gasProvider
            ) as T

            MetadataMembershipContract::class.java -> MetadataMembershipContract.load(
                contractAddress,
                web3j,
                MagicTxnManager(web3j, accountAddress),
                gasProvider
            ) as T

            LoyaltyContract::class.java -> LoyaltyContract.load(
                contractAddress,
                web3j,
                MagicTxnManager(web3j, accountAddress),
                gasProvider
            ) as T

            ERC20TestContract::class.java -> ERC20TestContract.load(
                contractAddress,
                web3j,
                MagicTxnManager(web3j, accountAddress),
                gasProvider
            ) as T

            ConnectedPackagingContract::class.java -> ConnectedPackagingContract.load(
                contractAddress,
                web3j,
                MagicTxnManager(web3j, accountAddress),
                gasProvider
            ) as T

            NFTOwnership::class.java -> NFTOwnership.load(
                contractAddress,
                web3j,
                MagicTxnManager(web3j, accountAddress),
                gasProvider
            ) as T

            else -> throw IllegalArgumentException("Contract of type ${contractClass.name} is not supported")
        }
    }

    private fun saveDataStoreString(
        key: Preferences.Key<String>,
        value: String
    ) = runBlocking {
        dataStore.edit {
            it[key] = value
        }
    }

    private fun getDataStoreString(
        key: Preferences.Key<String>
    ): String? = runBlocking {
        dataStore.data.firstOrNull()?.get(key)
    }
}