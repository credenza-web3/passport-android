package com.credenza.credenzapassport

import com.credenza.credenzapassport.contracts.ConnectedPackagingContract
import com.credenza.credenzapassport.contracts.ERC20TestContract
import com.credenza.credenzapassport.contracts.LoyaltyContract
import com.credenza.credenzapassport.contracts.MetadataMembershipContract
import com.credenza.credenzapassport.contracts.NFTOwnership
import com.credenza.credenzapassport.contracts.OzzieContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import link.magic.android.modules.web3j.contract.MagicTxnManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.tx.gas.DefaultGasProvider
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ContractUtils(
    private val okHttpClient: OkHttpClient,
    private val web3j: Web3j,
    private val passportDataStore: PassportDataStore
) {

    companion object {

        private const val CONTRACTS_ABI_URL =
            "https://unpkg.com/@credenza-web3/contracts/artifacts/%s.json"

        private val CONTRACT_CLASSES = mapOf(
            "OzzieContract" to OzzieContract::class.java,
            "MetadataMembershipContract" to MetadataMembershipContract::class.java,
            "LoyaltyContract" to LoyaltyContract::class.java,
            "ERC20TestContract" to ERC20TestContract::class.java,
            "ConnectedPackagingContract" to ConnectedPackagingContract::class.java,
            "NFTOwnership" to NFTOwnership::class.java,
        )
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
        contractName: String
    ): String = suspendCoroutine { continuation ->
        runBlocking(Dispatchers.IO) {
            try {
                val request: Request = Request.Builder()
                    .url(CONTRACTS_ABI_URL.format(contractName))
                    .build()
                okHttpClient.newCall(request).execute().use { response ->
                    response.body?.string()?.let { jsonStr ->
                        try {
                            val responseJSON = JSONObject(jsonStr)
                            val abi = responseJSON.getJSONArray("abi")
                            continuation.resume(abi.toString())
                        } catch (e: JSONException) {
                            continuation.resumeWithException(
                                IOException(
                                    "Failed to convert response string $jsonStr to json",
                                    e
                                )
                            )
                        }
                    } ?: run {
                        continuation.resumeWithException(IOException("Empty response body"))
                    }
                }
            } catch (e: IOException) {
                continuation.resumeWithException(e)
            }
        }
    }

    /**
     * Returns a smart contract object.
     *
     * @param contractClassName: The name of the smart contract class.
     * @param contractAddress: The address of the contract.
     * @return The object of the smart contract class
     */
    fun getContract(
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

    fun <T : Contract> getContract(
        contractClass: Class<T>,
        contractAddress: String
    ): T = runBlocking {
        val gasProvider = DefaultGasProvider()
        val accountAddress = passportDataStore.getAccount()
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
                CustomMagicTxnManager(web3j, accountAddress),
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
}