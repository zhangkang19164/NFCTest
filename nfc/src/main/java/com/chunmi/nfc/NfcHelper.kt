package com.chunmi.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.provider.Settings

/**
 * @author : Android-张康
 * created on: 2022/9/29 16:17
 * description:
 */
class NfcHelper(private val activity: Activity) {

    companion object {
        private const val OVERTIME = 15000L

        /**
         * 操作标签默认
         */
        private const val OPERATION_FLAG_DEFAULT = 0

        /**
         * 操作标签读取
         */
        private const val OPERATION_FLAG_READER = 1

        /**
         * 操作标签写入
         */
        private const val OPERATION_FLAG_WRITER = 2

    }

    private val mNfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(activity)
    }

    private val mReaderNfcHelper: ReaderNfcHelper by lazy {
        ReaderNfcHelper(this, OVERTIME)
    }

    private val mWriterNfcHelper: WriterNfcHelper by lazy {
        WriterNfcHelper(this, OVERTIME)
    }

    /**
     * 用来判断是读取操作还是写入操作
     */
    private var mFlag = OPERATION_FLAG_DEFAULT

    /**
     * 是否支持NFC
     *
     * @return true 支持 false 不支持
     */
    fun isSupports(): Boolean = null != mNfcAdapter

    /**
     * 是否开启了NFC
     *
     * @return true 开启 false 未开启
     */
    fun isEnabled() = mNfcAdapter?.isEnabled == true

    /**
     * 跳转 Nfc 设置页面
     */
    fun startNfcSettings() {
        try {
            val intent = Intent(Settings.ACTION_NFC_SETTINGS)
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }

    /**
     * 在[Activity.onResume]方法中调用
     */
    fun onActivityResumed() {
        mNfcAdapter?.enableForegroundDispatch(activity, createPendingIntent(activity), null, null)
    }

    /**
     * 在[Activity.onPause]方法中调用
     */
    fun onActivityPaused() {
        mNfcAdapter?.disableForegroundDispatch(activity)
    }

    /**
     * 在[Activity.onNewIntent]方法中调用
     *
     * @param intent 数据
     */
    fun onActivityNewIntent(intent: Intent) {
        intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG).let { nfcTag ->
            if (mFlag == OPERATION_FLAG_READER) {
                mReaderNfcHelper.readerText(Ndef.get(nfcTag))
            } else if (mFlag == OPERATION_FLAG_WRITER) {
                mWriterNfcHelper.writerText(Ndef.get(nfcTag))
            }
            mFlag = OPERATION_FLAG_DEFAULT
        }
    }

    /**
     * 读取
     *
     * @param readNfcCallback 读取回调
     */
    fun readText(readNfcCallback: ReadNfcCallback) {
        mFlag = OPERATION_FLAG_READER
        mReaderNfcHelper.readerText(readNfcCallback)
    }

    /**
     * 写入
     *
     * @param text 需要写入的文字
     * @param writerNfcCallback 写入回调
     */
    fun writerText(text: String, writerNfcCallback: WriterNfcCallback) {
        mFlag = OPERATION_FLAG_DEFAULT
        mWriterNfcHelper.writerText(text, writerNfcCallback)
    }

    private fun createPendingIntent(activity: Activity): PendingIntent {
        return PendingIntent.getActivity(
            activity, 0,
            Intent(activity, activity.javaClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }, 0
        )
    }
}