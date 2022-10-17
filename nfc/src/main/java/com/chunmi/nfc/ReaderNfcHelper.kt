package com.chunmi.nfc

import android.nfc.tech.Ndef
import android.os.Handler
import android.os.Looper
import java.nio.charset.Charset

/**
 * @author : Android-张康
 * created on: 2022/10/17 17:22
 * description:
 */
class ReaderNfcHelper(
    private val nfcHelper: NfcHelper,
    private val overtime: Long
) {

    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private var mIsOvertime = false

    private var readNfcCallback: ReadNfcCallback? = null

    /**
     * 读取文字
     *
     * @param readNfcCallback 回调
     */
    fun readerText(readNfcCallback: ReadNfcCallback) {
        this.readNfcCallback = readNfcCallback
        if (!nfcHelper.isSupports()) {
            readNfcCallback.readNfcCallback(NfcConstants.ERROR_CODE_NOT_SUPPORT, null)
            return
        }
        if (!nfcHelper.isEnabled()) {
            readNfcCallback.readNfcCallback(NfcConstants.ERROR_CODE_NOT_ENABLED, null)
            return
        }
        mIsOvertime = false
        mHandler.postDelayed({
            mIsOvertime = true
            readNfcCallback.readNfcCallback(NfcConstants.ERROR_CODE_OVERTIME, null)
        }, overtime)
    }

    /**
     * 读取文字
     *
     * @param ndef ndef对象
     */
    internal fun readerText(ndef: Ndef?) {
        if (mIsOvertime) {
            return
        }
        if (null == ndef) {
            readNfcCallback?.readNfcCallback(NfcConstants.ERROR_CODE_FAILURE, null)
            return
        }
        val cachedNdefMessage = ndef.cachedNdefMessage
        if (null == cachedNdefMessage) {
            readNfcCallback?.readNfcCallback(NfcConstants.ERROR_CODE_FAILURE, null)
            return
        }
        val records = cachedNdefMessage.records
        if (null == records || records.isEmpty()) {
            readNfcCallback?.readNfcCallback(NfcConstants.ERROR_CODE_FAILURE, null)
            return
        }
        val payload = records[0].payload
        if (null == payload || payload.isEmpty()) {
            readNfcCallback?.readNfcCallback(NfcConstants.ERROR_CODE_FAILURE, null)
            return
        }
        readNfcCallback?.readNfcCallback(
            NfcConstants.ERROR_CODE_SUCCESSFUL,
            String(payload, Charset.forName("UTF-8"))
        )
    }
}