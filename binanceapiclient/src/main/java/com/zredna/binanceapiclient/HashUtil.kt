package com.zredna.binanceapiclient

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HashUtil {
    fun hmacSha256(string: String, secret: String): String {
        val byteKey = secret.toByteArray()
        val hmacSha256 = "HmacSHA256"
        val mac = Mac.getInstance(hmacSha256)
        val keySpec = SecretKeySpec(byteKey, hmacSha256)
        mac.init(keySpec);
        val macData = mac.doFinal(string.toByteArray())

        return macData.toHex()
    }

    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    fun ByteArray.toHex(): String {
        val result = StringBuffer()

        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }

        return result.toString()
    }
}