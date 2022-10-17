package com.chunmi.nfc

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.tech.Ndef
import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.nio.charset.Charset

/**
 * @author : Android-张康
 * created on: 2022/10/17 17:22
 * description:写入NFC协助类
 */
class WriterNfcHelper(
    private val nfcHelper: NfcHelper,
    private val overtime: Long
) {

    private val mHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private var mIsOvertime = false

    private var writerNfcCallback: WriterNfcCallback? = null

    private var mNeedWriterText: String? = null

    /**
     * 写入文字
     *
     * @param text 需要写入的文字
     * @param writerNfcCallback 写入回调
     */
    fun writerText(text: String, writerNfcCallback: WriterNfcCallback) {
        this.writerNfcCallback = writerNfcCallback
        if (!nfcHelper.isSupports()) {
            writerNfcCallback.writerNfcCallback(NfcConstants.ERROR_CODE_NOT_SUPPORT)
            return
        }
        if (!nfcHelper.isEnabled()) {
            writerNfcCallback.writerNfcCallback(NfcConstants.ERROR_CODE_NOT_ENABLED)
            return
        }
        mNeedWriterText = text
        mIsOvertime = false
        mHandler.postDelayed({
            mIsOvertime = true
            writerNfcCallback.writerNfcCallback(NfcConstants.ERROR_CODE_OVERTIME)
        }, overtime)
    }

    /**
     * 写入文字
     *
     * @param ndef ndef对象
     */
    internal fun writerText(ndef: Ndef?) {
        writerText(ndef, mNeedWriterText)
    }

    private fun writerText(ndef: Ndef?, text: String?) {
        ndef ?: return
        text ?: return
        if (mIsOvertime) {
            return
        }
        //判断标签是否可以写入
        val writable = ndef.isWritable
        if (!writable) {
            writerNfcCallback?.writerNfcCallback(NfcConstants.ERROR_CODE_NOT_WRITABLE)
            return
        }
        try {
            ndef.connect()
            ndef.writeNdefMessage(
                NdefMessage(
                    NdefRecord(
                        NdefRecord.TNF_WELL_KNOWN,
                        NdefRecord.RTD_TEXT,
                        null,
                        text.toByteArray(Charset.forName("UTF-8"))
                    )
                )
            )
            ndef.close()
        } catch (iOException: IOException) {
            writerNfcCallback?.writerNfcCallback(NfcConstants.ERROR_CODE_FAILURE)
            return
        }
        writerNfcCallback?.writerNfcCallback(NfcConstants.ERROR_CODE_SUCCESSFUL)
        mNeedWriterText = null
    }


}