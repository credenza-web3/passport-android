package com.credenza3.credenzapassport

import android.util.Base64
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import java.security.MessageDigest


private const val HEX_CHARS = "0123456789ABCDEF"
private val HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray()

private const val ALGORITHM_SHA_256 = "SHA-256"

fun ByteArray.toHex(): String {
    val result = StringBuffer()

    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS_ARRAY[firstIndex])
        result.append(HEX_CHARS_ARRAY[secondIndex])
    }

    return result.toString()
}

fun String.hexToByteArray(): ByteArray {

    val result = ByteArray(length / 2)

    for (i in 0 until length step 2) {
        val firstIndex = HEX_CHARS.indexOf(get(i));
        val secondIndex = HEX_CHARS.indexOf(get(i + 1));

        val octet = firstIndex.shl(4).or(secondIndex)
        result.set(i.shr(1), octet.toByte())
    }

    return result
}

fun String.hashSha256(): ByteArray {
    val codeVerifierBytes = toByteArray(charset("US-ASCII"))
    val md = MessageDigest.getInstance("SHA-256")
    md.update(codeVerifierBytes)
    return md.digest()
}

fun ByteArray.base64URLEncode(): String =
    Base64.encodeToString(
        this,
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )

fun Sign.SignatureData.toHexString(): String {
    val value = ByteArray(65)
    System.arraycopy(r, 0, value, 0, 32)
    System.arraycopy(s, 0, value, 32, 32)
    System.arraycopy(v, 0, value, 64, 1)

    return Numeric.toHexString(value)
}