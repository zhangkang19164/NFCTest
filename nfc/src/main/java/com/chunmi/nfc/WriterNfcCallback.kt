package com.chunmi.nfc

/**
 * @author : Android-张康
 * created on: 2022/10/17 13:37
 * description: 写入 NFC 回调
 */
interface WriterNfcCallback {
    /**
     * 写入 NFC 回调
     *
     * @param errorCode 错误码
     */
    fun writerNfcCallback(errorCode: Int)
}