package com.chunmi.myapplication

import java.util.*

/**
 * @author : Android-张康
 * created on: 2022/1/26 14:21
 * description:
 */
object ByteToHexTools {
    /**
     * byte 转为 16进制
     */
    @JvmStatic
    fun byteToHexString(byte: Byte): String {
        val i = byte.toInt() and 0XFF
        val toHexString = Integer.toHexString(i).uppercase(Locale.getDefault())
        if (toHexString.length < 2) {
            return "0$toHexString"
        }
        return toHexString
    }

    /**
     * byte 转为 16进制 带前缀
     */
    @JvmStatic
    fun byteToHexStringAndPrefix(byte: Byte): String {
        return "0x${byteToHexString(byte)}"
    }

    /**
     * ByteArray 转为 16进制
     */
    @JvmStatic
    fun bytesToHexString(bytes: ByteArray): String {
        val hexString = StringBuilder()
        bytes.forEach {
            hexString.append(byteToHexString(it))
        }
        return hexString.toString()
    }

    /**
     * ByteArray 转为 16进制 带前缀
     */
    @JvmStatic
    fun bytesToHexStringAndPrefix(bytes: ByteArray): String {
        val hexString = StringBuilder("0x")
        bytes.forEach {
            hexString.append(byteToHexString(it))
        }
        return hexString.toString()
    }
}