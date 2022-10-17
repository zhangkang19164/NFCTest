package com.chunmi.nfc

/**
 * @author : Android-张康
 * created on: 2022/10/17 13:37
 * description: 读取 NFC 回调
 */
interface ReadNfcCallback {
    /**
     * 读取 NFC 标签回调
     *
     * @param errorCode 错误码
     * @param result 当错误码为[NfcConstants.ERROR_CODE_SUCCESSFUL]时返回读取的结果
     */
    fun readNfcCallback(errorCode: Int, result: String?)
}