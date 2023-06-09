package com.credenza.credenzapassport

import link.magic.android.modules.web3j.contract.MagicTxnManager
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthSendTransaction
import java.io.IOException
import java.math.BigInteger

class CustomMagicTxnManager constructor(private val web3j: Web3j, fromAddress: String) :
    MagicTxnManager(web3j, fromAddress) {

    @Throws(IOException::class)
    override fun sendTransaction(
        gasPrice: BigInteger,
        gasLimit: BigInteger,
        to: String?,
        data: String,
        value: BigInteger,
        constructor: Boolean
    ): EthSendTransaction {
        val transaction =
            Transaction.createFunctionCallTransaction(fromAddress, null, null, null, to, data)
        return web3j.ethSendTransaction(transaction).send()
    }
}