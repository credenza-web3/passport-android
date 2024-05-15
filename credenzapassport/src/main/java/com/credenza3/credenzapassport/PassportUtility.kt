package com.credenza3.credenzapassport

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.FLAG_READER_NFC_A
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NfcV
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.credenza3.credenzapassport.api.DiscountApiProvider
import com.credenza3.credenzapassport.api.DiscountsAPIService
import com.credenza3.credenzapassport.api.EVMAPIService
import com.credenza3.credenzapassport.api.EVMApiProvider
import com.credenza3.credenzapassport.api.model.RequestTokenRequest
import com.credenza3.credenzapassport.api.model.ValidateRulesetRequest
import com.credenza3.credenzapassport.api.model.ValidateRulesetResponse
import com.credenza3.credenzapassport.auth.AuthClient
import com.credenza3.credenzapassport.contracts.ConnectedPackagingContract
import com.credenza3.credenzapassport.contracts.CredenzaToken
import com.credenza3.credenzapassport.contracts.LedgerContract
import com.credenza3.credenzapassport.contracts.NFTOwnership
import com.credenza3.credenzapassport.contracts.OzzieContract
import com.credenza3.credenzapassport.contracts.SellableMetadataMembershipContract
import com.credenza3.credenzapassport.model.AirDropParams
import com.credenza3.credenzapassport.model.DeviceScanType
import com.credenza3.credenzapassport.model.LoyaltyPointsRequestParams
import com.credenza3.credenzapassport.model.PassScanProtocolParams
import com.credenza3.credenzapassport.model.QRCodeData
import com.credenza3.credenzapassport.model.toPassScanProtocolParams
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.gson.Gson
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.web3j.crypto.Credentials
import org.web3j.crypto.Sign
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.io.IOException
import java.math.BigInteger
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "passport")
private const val KEY_KRYPTKEY = "com.credenza3.credenzapassport.KRYPTKEY"
private const val KEY_OAUTH_CLIENT_ID = "com.credenza3.credenzapassport.OAUTH_CLIENT_ID"
private const val KEY_OAUTH_CLIENT_SECRET = "com.credenza3.credenzapassport.OAUTH_CLIENT_SECRET"
const val KEY_OAUTH_REDIRECT_URL = "com.credenza3.credenzapassport.OAUTH_REDIRECT_URL"
const val KEY_PASS_CODE_BASE_URL = "com.credenza3.credenzapassport.PASS_CODE_BASE_URL"

private const val SCAN_TYPE_PASSPORT_ID = "PASSPORT_ID"
private const val DATE_FORMATE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

private const val TAG = "PassportUtility"

interface PassportListener {
    fun onLoginComplete(address: String)
    fun onLoginFailed(error: String)
    fun onNFCScanComplete(address: String)
    fun onQRScannerSuccess(address: String)
    fun onQRScannerFailed(e: Throwable)
    fun onQRScannerCancel()
    fun onPassScanComplete(response: String)
}

object PassportUtility {

    // The authentication token to be used for API calls.
    private var authenticationTokenC = ""

    // The address of the NFT smart contract.
    private var nftContractAddressC = ""

    // The address of the stored value smart contract.
    private var storedValueContractAddressC = ""

    // The address of the connected smart contract.
    private var connectedContractAddressC = ""


    private var chainId: Long = 0
    internal lateinit var authClient: AuthClient
    private lateinit var web3j: Web3j
    private val okHttpClient = OkHttpClient()
    private lateinit var evmAPIService: EVMAPIService
    private lateinit var discountsAPIService: DiscountsAPIService
    private lateinit var passportDataStore: PassportDataStore
    private lateinit var contractUtils: ContractUtils

    private var nfcAdapter: NfcAdapter? = null

    lateinit var passportListener: PassportListener

    private var deviceScanType: DeviceScanType = DeviceScanType.NFC
    private var shouldMakeStringCheck: Boolean = false

    fun init(
        context: Context,
        chainId: Long,
        passportListener: PassportListener,
    ) {
        this.chainId = chainId

        this.passportListener = passportListener
        passportDataStore = PassportDataStore(context.dataStore)

        web3j =
            Web3j.build(HttpService("https://rpc-amoy.polygon.technology")) //TODO remove hardcode
        contractUtils = ContractUtils(okHttpClient, web3j, chainId, passportDataStore)

        authClient = AuthClient(
            clientId = context.getMetaDataOrThrowException(KEY_OAUTH_CLIENT_ID),
            clientSecret = context.getMetaDataOrThrowException(KEY_OAUTH_CLIENT_ID),
            redirectURL = context.getMetaDataOrThrowException(KEY_OAUTH_REDIRECT_URL),
            passportDataStore = passportDataStore,
        )

        getAdminAccount(context)?.let { adminAccount ->
            passportDataStore.saveAdminAccount(adminAccount)
        }

        evmAPIService = EVMApiProvider(authClient).evmAPIService

        discountsAPIService = DiscountApiProvider(
            authClient = authClient,
            baseURL = context.getMetaDataOrThrowException(KEY_PASS_CODE_BASE_URL)
        ).discountAPIService
    }

    private fun getAdminAccount(context: Context): String? = context.getMetaData(KEY_KRYPTKEY)

    fun activatePassScan(context: Context) {
        shouldMakeStringCheck = true
        startQRScan(context)
    }

    fun scanQR(context: Context) {
        startQRScan(context)
    }

    private fun startQRScan(context: Context) {
        val moduleInstall = ModuleInstall.getClient(context.applicationContext)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(context.applicationContext))
            .build()
        moduleInstall
            .installModules(moduleInstallRequest)
            .addOnSuccessListener {
                if (it.areModulesAlreadyInstalled()) {

                    GmsBarcodeScanning.getClient(context)
                        .startScan()
                        .addOnSuccessListener { barcode ->

                            deviceScanType = DeviceScanType.QR

                            barcode.url?.url?.let { scanResult ->
                                if (shouldMakeStringCheck) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        passScanProtocolRouter(scanResult.toPassScanProtocolParams())
                                    }
                                } else {
                                    passportListener.onQRScannerSuccess(scanResult)
                                }
                            }
                        }
                        .addOnCanceledListener {
                            passportListener.onQRScannerCancel()
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Failed to scan QR code", e)
                            passportListener.onQRScannerFailed(e)
                        }
                } else {
                    passportListener.onQRScannerFailed(IllegalStateException("GmsBarcodeScanning model is not installed"))
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to scan QR code", e)
                passportListener.onQRScannerFailed(e)
            }
    }

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
        connectedContractAddress: String,
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
        checkVersion("0x0E53B4BFf707d9BE5582D9D2a4e6c00dc5Fca16F", "LedgerContract")

    /**
     * Performs a GET request using the provided authentication token.
     */
    fun authN() {
        runBlocking(Dispatchers.IO) {
            val request = Request.Builder()
                .url("https://deep-index.moralis.io/api/v2/0x56bafed9ba9f918594505d93f283b26700ae1d9f/logs?chain=mumbai") // TODO use some enum for prod/test chain?
                .header("x-api-key", authenticationTokenC)
                .build()
            okHttpClient.newCall(request).execute().use { response ->
                val responseStr = response.body?.string()
            }
        }
    }

    /**
     * Handles sign-in for a given email address.
     * Note: Stores the login token in DataStore after successful authentication.
     *
     * @param context Context
     */
    suspend fun handleSignIn(activity: Activity) {

        val isAuthorized = authClient.signIn(activity)

        if (isAuthorized) {
            getAccount()
        } else {
            Log.e(TAG, "Failed to login")
        }
    }

    /**
     * Gets the user's Ethereum public address and passes it to the `listener` instance.
     * Note: Calls `loginComplete(address:)` of the `listener` instance to pass the Ethereum public address to it.
     */
    suspend fun getAccount() {
        evmAPIService.getAccountAddress().body()?.address?.let { account ->

            passportDataStore.saveUserAccount(account)
            passportListener.onLoginComplete(account)

        } ?: run {
            Log.e(TAG, "No Account Found")
            passportListener.onLoginFailed("No Account Found")
        }
    }

    fun getCurrentAccount(): String? = passportDataStore.getUserAccount()

    fun isUserLoggedIn(): Boolean = authClient.isAuthorized()

    fun logout() = authClient.logout()


    /**
     * Checks the balance of NFT (Non-Fungible Token) of a user for a given contract address.
     *
     * @param contractAddress: An Ethereum contract address for which to check NFT balance.
     * @param userAddress: An Ethereum public address of the user whose NFT balance to check.
     * @return The balance of NFT.
     */
    suspend fun nftCheck(
        contractAddress: String,
        userAddress: String,
    ): BigInteger? =
        suspendCoroutine { continuation ->

            val contract =
                contractUtils.getContractAsUser(OzzieContract::class.java, contractAddress)

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
        address: String,
    ): BigInteger? =
        suspendCoroutine { continuation ->

            val contract =
                contractUtils.getContractAsUser(NFTOwnership::class.java, nftContractAddressC)

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
     * It gets the contract ABI (Application Binary Interface) using the 'contractUtils.getContractABI' function,
     * constructs a contract instance, calls the 'getVersion' function to get the contract version,
     * and then returns the version string or "NONE" if there was an error.
     *
     * @param contractAddress: The contract address for which to check the version.
     * @param contractType: The type of the contract for which to check the version.
     *
     * @throws IllegalArgumentException if contract type is not supported
     * @throws Throwable if it failed to get version for contract
     *
     * @return A string containing the contract version
     */
    suspend fun checkVersion(
        contractAddress: String,
        contractType: String,
    ): String =
        suspendCoroutine { continuation ->

            val contract = contractUtils.getContractAsUser(contractType, contractAddress)

            val versionRequest = when (contract) {
                is OzzieContract -> contract.version
                is LedgerContract -> contract.version
                is CredenzaToken -> contract.version
                is ConnectedPackagingContract -> contract.version
                is SellableMetadataMembershipContract -> contract.version
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
     * This asynchronous function adds a membership to the given contract address for the given user Ethereum address and metadata.
     * It gets the contract ABI (Application Binary Interface) using the 'contractUtils.getContractABI' function,
     * constructs a contract instance, and then creates a transaction to call the 'addMembership' function
     * with the given user address and metadata. It signs the transaction with the private key and then sends
     * the transaction to the network to be mined.
     *
     * @param contractAddress: The contract address to which to add the membership.
     * @param userAddress: The Ethereum address of the user to add as a member.
     * @param metadata: The metadata to associate with the membership.
     */
    suspend fun addMembership(
        contractAddress: String,
        userAddress: String,
        metadata: String,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(
                SellableMetadataMembershipContract::class.java,
                contractAddress
            )

        contract.addMembership(userAddress, metadata)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to addMembership for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * Removes membership of a user from a contract on the Ethereum blockchain.
     *
     * @param contractAddress: The Ethereum address of the contract.
     * @param userAddress: The Ethereum address of the user whose membership needs to be removed.
     */
    suspend fun removeMembership(
        contractAddress: String,
        userAddress: String,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(
                SellableMetadataMembershipContract::class.java,
                contractAddress
            )

        contract.removeMembership(userAddress)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to removeMembership for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * Checks if a user is a confirmed member of a contract on the Ethereum blockchain.
     *
     * @param contractAddress: The Ethereum address of the contract.
     * @param ownerAddress: The Ethereum address of the owner of the contract.
     * @param userAddress: The Ethereum address of the user to be checked.
     * @return A boolean value indicating whether the user is a confirmed member of the contract.
     */
    suspend fun confirmMembership(
        contractAddress: String,
        userAddress: String,
    ): Boolean = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsUser(
                SellableMetadataMembershipContract::class.java,
                contractAddress
            )

        contract.confirmMembership(userAddress)
            .sendAsync()
            .whenComplete { result, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to checkMembership for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(result)
            }
    }

    /**
     * For members, this method will retrieve the metadata that was stored by customerAddress
     * associated with a member with the public key customerAddress. Like confirmMembership,
     * this is accessible to all authorized callers as defined by the contract owner.
     *
     * @param contractAddress: The Ethereum address of the contract.
     * @param ownerAddress: The Ethereum address of the owner of the contract.
     * @param userAddress: The Ethereum address of the user to be checked.
     * @return String with membership metadata.
     */
    suspend fun getMembershipMetadata(
        contractAddress: String,
        userAddress: String,
    ): String = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsUser(
                SellableMetadataMembershipContract::class.java,
                contractAddress
            )

        contract.getMembershipMetadata(userAddress)
            .sendAsync()
            .whenComplete { result, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to getMembershipMetadata for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(result)
            }
    }


    /**
     * Calculates the loyalty points of a given user for the specified loyalty contract.
     *
     * @param contractAddress: The Ethereum address of the loyalty contract.
     * @param userAddress: The Ethereum address of the user to check loyalty points for.
     * @return A `BigInteger` representing the user's loyalty points.
     */
    suspend fun loyaltyCheck(
        contractAddress: String,
        userAddress: String,
    ): BigInteger = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsUser(LedgerContract::class.java, contractAddress)

        contract.checkPoints(userAddress)
            .sendAsync()
            .whenComplete { points, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to loyaltyCheck for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(points)
            }
    }

    /**
     * Adds loyalty points to the user's account.
     *
     * @param contractAddress: The address of the loyalty contract.
     * @param userAddress: The address of the user's account to add points to.
     * @param points: The number of points to add to the user's account.
     * @param eventId: The id of event.
     */
    suspend fun loyaltyAdd(
        contractAddress: String,
        userAddress: String,
        points: BigInteger,
        eventId: BigInteger,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(LedgerContract::class.java, contractAddress)

        contract.addPoints(userAddress, points, eventId)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to loyaltyAdd for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * Many loyalty programs want to reward users by converting points to stored value.
     * This transaction redeems points and increases stored value balances for recipient.
     *
     * @param contractAddress: The address of the loyalty contract.
     * @param userAddress: The address of the user's account to convert points.
     * @param points: The number of points to convert.
     */
    suspend fun convertPointsToCoins(
        contractAddress: String,
        userAddress: String,
        points: BigInteger,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(LedgerContract::class.java, contractAddress)

        contract.convertPointsToCoins(userAddress, points)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to convertPointsToCoins for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * Separated from redemption, this is called if points expire or other activities cause
     * a balance to be reduced by amount without any benefit going to the member recipient.
     *
     * @param contractAddress: The address of the loyalty contract.
     * @param userAddress: The address of the user's account to remove points from.
     * @param points: The number of points to remove from the user's account.
     */
    suspend fun loyaltyForfeit(
        contractAddress: String,
        userAddress: String,
        points: BigInteger,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(LedgerContract::class.java, contractAddress)

        contract.forfeitPoints(userAddress, points)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to loyaltyForfeit for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * If points are to be converted into stored value or rewards, this can be called to reduce
     * the current points balance for the recipient by pointsAmt, associated with a redemption
     * event eventId.
     *
     * @param contractAddress: The address of the loyalty contract.
     * @param userAddress: The address of the user's account to remove points from.
     * @param points: The number of points to remove from the user's account.
     * @param eventId: The id of event.
     */
    suspend fun loyaltyRedeem(
        contractAddress: String,
        userAddress: String,
        points: BigInteger,
        eventId: BigInteger,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(LedgerContract::class.java, contractAddress)

        contract.redeemPoints(userAddress, points, eventId)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to loyaltyRedeem for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }


    /**
     * Returns the balance of current points owned by recipient, which does NOT take all redemptions
     * and forfeitures into account. This amount can only grow.
     *
     * @param contractAddress: The Ethereum address of the loyalty contract.
     * @param userAddress: The Ethereum address of the user to check loyalty points for.
     * @return A `BigInteger` representing the user's loyalty points.
     */
    suspend fun loyaltyLifetimeCheck(
        contractAddress: String,
        userAddress: String,
    ): BigInteger = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsUser(LedgerContract::class.java, contractAddress)

        contract.checkLifetimePoints(userAddress)
            .sendAsync()
            .whenComplete { points, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to loyaltyLifetimeCheck for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(points)
            }
    }

    /**
     * Checks the balance of a user's account for a given ERC20 token contract.
     *
     * @param contractAddress: The address of the ERC20 token contract.
     * @param userAddress: The address of the user's account to check the balance of.
     * @return The balance of the user's account.
     */
    suspend fun svCheck(
        contractAddress: String,
        userAddress: String,
    ): BigInteger = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsUser(CredenzaToken::class.java, contractAddress)

        contract.balanceOf(userAddress)
            .sendAsync()
            .whenComplete { value, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to svCheck for contract $contractAddress",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(value)
            }
    }

    /**
     * Retrieves the connection information for a given serial number from the Connected Packaging smart contract.
     *
     * @param serialNumber: The serial number of the connected packaging.
     * @return The Ethereum address of the connected packaging.
     */
    suspend fun connectedPackageQueryPass(
        serialNumber: String,
    ): String = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsUser(
                ConnectedPackagingContract::class.java,
                connectedContractAddressC
            )

        contract.retrieveConnection(serialNumber)
            .sendAsync()
            .whenComplete { value, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to connectedPackageQuery for contract $connectedContractAddressC",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(value)
            }
    }

    /**
     * Claims a connection for a given user address and serial number in the Connected Packaging smart contract.
     *
     * @param userAddress: The Ethereum address of the user.
     * @param serialNumber: The serial number of the connected packaging.
     */
    suspend fun connectedPackagePublish(
        userAddress: String,
        serialNumber: String,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(
                ConnectedPackagingContract::class.java,
                connectedContractAddressC
            )

        contract.claimConnection(serialNumber, userAddress)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to connectedPackagePublish for contract $connectedContractAddressC",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * Revokes a connection for a given serial number in the Connected Packaging smart contract.
     *
     * @param serialNumber: The serial number of the connected packaging.
     */
    suspend fun connectedPackagePurge(
        serialNumber: String,
    ) = suspendCoroutine { continuation ->

        val contract =
            contractUtils.getContractAsAdmin(
                ConnectedPackagingContract::class.java,
                connectedContractAddressC
            )

        contract.revokeConnection(serialNumber)
            .sendAsync()
            .whenComplete { _, throwable ->
                throwable?.let {
                    Log.e(
                        TAG,
                        "Failed to connectedPackagePurge for contract $connectedContractAddressC",
                        throwable
                    )
                    continuation.resumeWithException(it)
                    return@whenComplete
                }

                continuation.resume(Unit)
            }
    }

    /**
     * Returns the ABI (Application Binary Interface) of a smart contract.
     *
     * @param contractName: The name of the smart contract.
     * @return The ABI data of the smart contract.
     * @throws IOException
     */
    @Throws(IOException::class)
    suspend fun getContractABI(
        contractName: String,
    ): String = contractUtils.getContractABI(contractName)

    /**
     * Initiates a new NFC adapter session for reading from the passport-enabled tag.
     */
    fun readNFC(activity: Activity) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
            ?: throw UnsupportedOperationException("Nfc is not supported for current device")

        nfcAdapter?.enableReaderMode(
            activity,
            { onTagDiscovered(activity, it) },
            FLAG_READER_NFC_A,
            Bundle()
        )
    }

    private fun onTagDiscovered(activity: Activity, tag: Tag) {
        val serialID: String = runBlocking(Dispatchers.IO) {
            val tagInfos = getTagInfos(tag)
            var tagInfosDetail = ""
            tagInfos.forEach { item ->
                tagInfosDetail += "${item.key}: ${item.value}\n"
                if (item.key == "Identifier") {
                    return@runBlocking ((item.value) as? String) ?: ""
                }
            }
            return@runBlocking ""
        }

        deviceScanType = DeviceScanType.NFC
        nfcAdapter?.disableReaderMode(activity)

        CoroutineScope(Dispatchers.IO).launch {
            scanCompleted(serialID)
        }
    }

    fun getTagInfos(tag: Tag): Map<String, String> {
        val infos = mutableMapOf<String, String>()

        infos["Identifier"] = byteArrayOf(*tag.id).toHex()

        when {
            // miFare
            tag.techList.contains("android.nfc.tech.MifareClassic") -> {

                val mifareClassicTag = MifareClassic.get(tag)
                infos["TagType"] = when (mifareClassicTag.type) {
                    MifareClassic.TYPE_CLASSIC -> "MiFare Classic"
                    MifareClassic.TYPE_PRO -> "MiFare Pro"
                    MifareClassic.TYPE_PLUS -> "MiFare Plus"
                    MifareClassic.TYPE_UNKNOWN -> "MiFare unknown"
                    else -> "MiFare unknown"
                }
            }

            tag.techList.contains("android.nfc.tech.MifareUltralight") -> {
                infos["TagType"] = "MiFare Ultralight"
            }

            // iso7816Compatible
            tag.techList.contains("android.nfc.tech.IsoDep") -> {
                val isoDep = IsoDep.get(tag)
                infos["TagType"] = "ISO 14443-4"
                infos["HistoricalBytes"] = isoDep.historicalBytes.toHex()
            }

            // ISO15693
            tag.techList.contains("android.nfc.tech.NfcV") -> {

                val nfcV = NfcV.get(tag)
                infos["TagType"] = "ISO15693"
                infos["Identifier"] = byteArrayOf(*tag.id).toHex()
                infos["DSF ID"] = nfcV.dsfId.toString()
            }

            // includes feliCa
            tag.techList.contains("android.nfc.tech.Ndef") -> {
                val ndef = Ndef.get(tag)
                infos["TagType"] = when (ndef.type) {
                    Ndef.NFC_FORUM_TYPE_1 -> "NFC Forum Type 1" // such as the Innovision Topaz
                    Ndef.NFC_FORUM_TYPE_2 -> "NFC Forum Type 2" // such as the the NXP MIFARE Ultralight
                    Ndef.NFC_FORUM_TYPE_3 -> "NFC Forum Type 3" // such as Sony Felica
                    Ndef.NFC_FORUM_TYPE_4 -> "NFC Forum Type 4" // such as NXP MIFARE Desfire
                    Ndef.MIFARE_CLASSIC -> "MiFare Classic"
                    else -> "NFC Forum Type unknown"
                }
            }
        }

        return infos
    }

    private suspend fun scanCompleted(serialID: String) {
        val result = connectedPackageQueryPass(serialID)
        passScanProtocolRouter(result.toPassScanProtocolParams())
    }

    /**
     * Processes the JSON string and performs actions based on the specified scan type.
     * @param passScanProtocolParams: params to be processed
     */
    private suspend fun passScanProtocolRouter(passScanProtocolParams: PassScanProtocolParams) =
        when (passScanProtocolParams) {
            is AirDropParams -> handleAirDrop(passScanProtocolParams)
            is LoyaltyPointsRequestParams -> handleRequestLoyaltyPoints(passScanProtocolParams)
        }

    /**
     * Handles the AirDrop functionality based on the provided JSON parameters.
     *
     * @param airDropParams: Parameters for the AirDrop
     */
    private suspend fun handleAirDrop(airDropParams: AirDropParams) {
        val response = discountsAPIService.requestToken(
            airDropParams.chainId, airDropParams.contractAddress, RequestTokenRequest(
                targetAddress = passportDataStore.getUserAccount()
                    ?: throw IllegalStateException("User is not authorized"),
                tokenId = airDropParams.tokenId,
                amount = airDropParams.amount
            )
        ).body() ?: throw Exception("Failed to request token")

        when (deviceScanType) {
            DeviceScanType.NFC -> passportListener.onNFCScanComplete(response.tx) //TODO pass the whole answer?
            DeviceScanType.QR -> passportListener.onPassScanComplete(response.tx)
        }
    }

    /**
     * Handles the request for loyalty points based on the provided JSON parameters.
     *
     * @param loyaltyPointsRequestParams: Parameters for the loyalty points request
     */
    private suspend fun handleRequestLoyaltyPoints(loyaltyPointsRequestParams: LoyaltyPointsRequestParams) {
        val response = discountsAPIService.requestLoyaltyPoints(
            chainId = loyaltyPointsRequestParams.chainId,
            eventId = loyaltyPointsRequestParams.eventId,
            contractAddress = loyaltyPointsRequestParams.contractAddress,
        ).body() ?: throw Exception("Failed to request loyalty points")

        when (deviceScanType) {
            DeviceScanType.NFC -> passportListener.onNFCScanComplete("") //TODO pass the whole answer?
            DeviceScanType.QR -> passportListener.onPassScanComplete("")
        }
    }

    /**
     * Returns a string containing information about the records in the given array of NdefMessage objects.
     *
     * @param messages: An array of NdefMessage objects.
     * @return A string containing information about the records in the given array of NdefMessage objects.
     */
    fun contentsForMessages(messages: List<NdefMessage>): String {
        return messages.map { message ->
            message.records.mapIndexed { i, record ->
                "Record(${i + 1}):\n" +
                        "Type name format: ${record.toMimeType()}\n" +
                        "Type: ${record.type}\n" +
                        "Identifier: ${record.id}\n" +
                        "Length: ${message.byteArrayLength}\n" +
                        "Payload content:${record.payload.decodeToString()}\n" +
                        "Payload raw data: ${record.payload}\n\n"
            }.joinToString(separator = "") { it }
        }.joinToString(separator = "") { it }
    }

    /**
     * Sends command to tag and prints response
     *
     * @param tag tag
     */
    fun sendCommandToTag(tag: Tag) {
        try {
            val isoDep = IsoDep.get(tag)
            isoDep.connect()

            val response =
                isoDep.transceive("00A4040C07F0010203040506".hexToByteArray())
            Log.d(TAG, "Response from tag $response")

            isoDep.close()
        } catch (e: Throwable) {
            Log.d("ABC1209", "error", e)
        }
    }

    /**
     * Queries the ruleset for a given passport and ruleset ID.
     *
     * @param passportId: The ID of the passport
     * @param ruleSetId: The ID of the ruleset
     *
     * @return A Data object containing information about the ruleset
     */
    suspend fun queryRuleset(passportId: String, ruleSetId: String): ValidateRulesetResponse? {
        val rulesetData =
            discountsAPIService.validateRuleset(ValidateRulesetRequest(passportId, ruleSetId))
        return rulesetData.body()
    }

    /**
     * Shows the QR code for the passport ID.
     * This method checks if the user is logged in and then generates and displays the QR code.
     */
    fun showPassportIDQRCode(): Bitmap? {
        if (authClient.getAccessToken() == null) {
            return null
        }

        val timestamp = SimpleDateFormat(DATE_FORMATE).format(Date())
        val sig = signTimestamp(timestamp)
        val qrCodeData = QRCodeData(
            scanType = SCAN_TYPE_PASSPORT_ID,
            date = timestamp,
            chainId = chainId.toString(),
            sig = sig,
        )

        return generateQRCode(Gson().toJson(qrCodeData))
    }

    /**
     * Signs the timestamp using OAuth code Web3.
     *
     * @return a string representing the signature
     */
    private fun signTimestamp(timestamp: String): String =
        Sign.signPrefixedMessage(
            timestamp.toByteArray(),
            Credentials.create(passportDataStore.getAdminAccount()).ecKeyPair
        ).toHexString()

    /**
     * Generates a QR code from a JSON string.
     *
     * @param qrCodeContent the JSON string to generate the QR code from
     * @return a bitmap containing the QR code
     */
    private fun generateQRCode(qrCodeContent: String): Bitmap {
        val size = 512 //pixels
        val hints = hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1
        } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }
}