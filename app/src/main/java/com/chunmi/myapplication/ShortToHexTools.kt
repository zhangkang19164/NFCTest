package com.chunmi.myapplication

import java.util.*

/**
 * @author : Android-张康
 * created on: 2022/1/26 14:21
 * description: Short 转换 16进制工具类
 */
object ShortToHexTools {
    /**
     * Short 转为 16进制
     */
    @JvmStatic
    fun shortToHexString(short: Short): String {
        val i = short.toInt() and 0XFF
        val toHexString = Integer.toHexString(i).uppercase(Locale.getDefault())
        if (toHexString.length < 2) {
            return "0$toHexString"
        }
        return toHexString
    }

    /**
     * Short 转为 16进制 带前缀
     */
    @JvmStatic
    fun shortToHexStringAndPrefix(short: Short): String {
        return "0x${shortToHexString(short)}"
    }

    /**
     * ShortArray 转为 16进制
     */
    @JvmStatic
    fun shortArrayToHexString(shortArray: ShortArray): String {
        val hexString = StringBuilder()
        shortArray.forEach {
            hexString.append(shortToHexString(it))
        }
        return hexString.toString()
    }

    /**
     * ShortArray 转为 16进制 带前缀
     */
    @JvmStatic
    fun shortArrayToHexStringAndPrefix(shortArray: ShortArray): String {
        val hexString = StringBuilder("0x")
        shortArray.forEach {
            hexString.append(shortToHexString(it))
        }
        return hexString.toString()
    }
}