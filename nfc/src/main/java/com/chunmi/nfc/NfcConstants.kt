package com.chunmi.nfc

/**
 * @author : Android-张康
 * created on: 2022/10/17 13:39
 * description: 常量
 */
object NfcConstants {

    /**
     * 错误码 成功
     */
    const val ERROR_CODE_SUCCESSFUL = 0

    /**
     * 错误码 不支持NFC
     */
    const val ERROR_CODE_NOT_SUPPORT = -101

    /**
     * 错误码 没有启用NFC
     */
    const val ERROR_CODE_NOT_ENABLED = -102

    /**
     * 错误码 标签不支持写入
     */
    const val ERROR_CODE_NOT_WRITABLE = -103

    /**
     * 错误码 超时
     */
    const val ERROR_CODE_OVERTIME = -104

    /**
     * 错误码 写入失败
     */
    const val ERROR_CODE_FAILURE = -105
}