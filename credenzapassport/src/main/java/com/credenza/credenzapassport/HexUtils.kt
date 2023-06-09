package com.credenza.credenzapassport


private val HEX_CHARS = "0123456789ABCDEF"
private val HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray()

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