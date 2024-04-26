package com.credenza3.credenzapassport

import com.credenza3.credenzapassport.contracts.ConnectedPackagingContract
import com.credenza3.credenzapassport.contracts.CredenzaToken
import com.credenza3.credenzapassport.contracts.LedgerContract
import com.credenza3.credenzapassport.contracts.NFTOwnership
import com.credenza3.credenzapassport.contracts.OzzieContract
import com.credenza3.credenzapassport.contracts.SellableMetadataMembershipContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import link.magic.android.modules.web3j.contract.MagicTxnManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.Contract
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ContractUtils(
    private val okHttpClient: OkHttpClient,
    private val web3j: Web3j,
    private val chainId: Long,
    private val passportDataStore: PassportDataStore
) {

    companion object {

        private const val CONTRACTS_ABI_URL =
            "https://unpkg.com/@credenza-web3/contracts/artifacts/%s.json"

        private val CONTRACT_CLASSES = mapOf(
            "OzzieContract" to OzzieContract::class.java,
            "LedgerContract" to LedgerContract::class.java,
            "CredenzaToken" to CredenzaToken::class.java,
            "ConnectedPackagingContract" to ConnectedPackagingContract::class.java,
            "NFTOwnership" to NFTOwnership::class.java,
            "SellableMetadataMembershipContract" to SellableMetadataMembershipContract::class.java,
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

    fun getContractAsUser(
        contractClassName: String,
        contractAddress: String
    ): Contract = getContract(
        contractClassName = contractClassName,
        contractAddress = contractAddress,
        accountAddress = getAccountAddress()
    )

    fun getContractAsAdmin(
        contractClassName: String,
        contractAddress: String
    ): Contract = getContract(
        contractClassName = contractClassName,
        contractAddress = contractAddress,
        accountAddress = getAdminPrivateKey()
    )

    fun <T : Contract> getContractAsUser(
        contractClass: Class<T>,
        contractAddress: String
    ): T = getContract(
        contractClass = contractClass,
        contractAddress = contractAddress,
        accountAddress = getAccountAddress()
    )

    fun <T : Contract> getContractAsAdmin(
        contractClass: Class<T>,
        contractAddress: String
    ): T = getContract(
        contractClass = contractClass,
        contractAddress = contractAddress,
        accountAddress = getAdminPrivateKey(),
        asAdmin = true
    )

    /**
     * Returns a smart contract object.
     *
     * @param contractClassName: The name of the smart contract class.
     * @param contractAddress: The address of the contract.
     * @return The object of the smart contract class
     */
    private fun getContract(
        contractClassName: String,
        contractAddress: String,
        accountAddress: String
    ): Contract = runBlocking {
        val contractClass = CONTRACT_CLASSES[contractClassName]?.asSubclass(Contract::class.java)
            ?: throw IllegalArgumentException("Contract of type $contractClassName is not supported")

        return@runBlocking getContract(
            contractClass,
            contractAddress,
            accountAddress
        )
    }

    private fun <T : Contract> getContract(
        contractClass: Class<T>,
        contractAddress: String,
        accountAddress: String = "",
        asAdmin: Boolean = false
    ): T = runBlocking {
        val gasProvider = DefaultGasProvider()
        val transactionManager = getTransactionManager(accountAddress, getAdminPrivateKey(), asAdmin)

        return@runBlocking when (contractClass) {
            OzzieContract::class.java -> OzzieContract.load(
                contractAddress,
                web3j,
                transactionManager,
                gasProvider
            ) as T

            LedgerContract::class.java -> LedgerContract.load(
                contractAddress,
                web3j,
                transactionManager,
                gasProvider
            ) as T

            CredenzaToken::class.java -> CredenzaToken.load(
                contractAddress,
                web3j,
                transactionManager,
                gasProvider
            ) as T

            ConnectedPackagingContract::class.java -> ConnectedPackagingContract.load(
                contractAddress,
                web3j,
                transactionManager,
                gasProvider
            ) as T

            NFTOwnership::class.java -> NFTOwnership.load(
                contractAddress,
                web3j,
                transactionManager,
                gasProvider
            ) as T

            SellableMetadataMembershipContract::class.java -> SellableMetadataMembershipContract.load(
                contractAddress,
                web3j,
                transactionManager,
                gasProvider
            ) as T

            else -> throw IllegalArgumentException("Contract of type ${contractClass.name} is not supported")
        }
    }

    private fun getTransactionManager(
        accountAddress: String = "",
        privateKey: String = "",
        asAdmin: Boolean = false
    ) = if (asAdmin) RawTransactionManager(
        web3j,
        Credentials.create(privateKey),
        chainId
    ) else MagicTxnManager(web3j, accountAddress)

    private fun getAccountAddress() = passportDataStore.getUserAccount()
        ?: throw IllegalStateException("Current user is not signed in")

    private fun getAdminPrivateKey() = passportDataStore.getAdminAccount()
        ?: throw IllegalStateException("KRYPTKEY is missed in config files")
}