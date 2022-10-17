package com.chunmi.myapplication

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


/**
 * @author : Android-张康
 * created on: 2022/9/27 17:37
 * description:
 */
private const val TAG = "ReadNfcActivity"

class ReadNfcActivity : AppCompatActivity() {

    private val defaultAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }, 0
        )
    }

    private val mMessageTextView: TextView by lazy {
        findViewById(R.id.nfc_info)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_read)
        title = "NFC读取"
        if (null == defaultAdapter) {
            mMessageTextView.text = "设备不支持NFC功能"
        }
        if (defaultAdapter?.isEnabled != true) {
            mMessageTextView.append("请在系统设置中开启NFC功能")
        }
        onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        defaultAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        defaultAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent: $intent")
        intent?.let {
            resolveIntent(intent)
        }
    }

    private fun resolveIntent(intent: Intent) {
        val stringBuilder = StringBuilder()
        stringBuilder.append(intent.action.toString())
        stringBuilder.append("\n")
        intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.let { nfcTag ->
            stringBuilder.append("标签信息")
            stringBuilder.append("\n")
            stringBuilder.append("厂商:")
            stringBuilder.append("\n")
            stringBuilder.append("类型:")
            stringBuilder.append("\n")
            stringBuilder.append("描述:")
            nfcTag.techList.forEach {
                if (NfcA::class.java.name.equals(it)) {
                    stringBuilder.append("NfcA")
                    stringBuilder.append(",")
                }
                if (Ndef::class.java.name.equals(it)) {
                    stringBuilder.append("Ndef")
                }
            }
            stringBuilder.append("\n")
            stringBuilder.append("ID：${ByteToHexTools.bytesToHexString(nfcTag.id)}")

            val nfcA = NfcA.get(nfcTag)
            stringBuilder.append("\n")
            stringBuilder.append("ATQA:${ByteToHexTools.bytesToHexStringAndPrefix(nfcA.atqa)}")
            stringBuilder.append("\n")
            stringBuilder.append("SAK:${ShortToHexTools.shortToHexStringAndPrefix(nfcA.sak)}")

            val ndef = Ndef.get(nfcTag)
            stringBuilder.append("\n")
            stringBuilder.append("数据格式：${getNdefType(ndef.type)}")

            stringBuilder.append("\n")
            stringBuilder.append("大小：${ndef.cachedNdefMessage?.byteArrayLength ?: -1}/${ndef.maxSize}")

            stringBuilder.append("\n")
            stringBuilder.append("可写：${ndef.isWritable}")

            stringBuilder.append("\n")
            stringBuilder.append("可为只读：${ndef.canMakeReadOnly()}")
            stringBuilder.append("\n")
            stringBuilder.append("标签内容：")
            stringBuilder.append(
                ReadNdefHelper.readNdef(ndef.cachedNdefMessage)
            )
        }
//        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.let { rawMessages ->
//            val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
//            stringBuilder.append("\n")
//            stringBuilder.append("EXTRA_NDEF_MESSAGES")
//            stringBuilder.append("\n")
//            messages.forEach { ndefMessage ->
//                stringBuilder.append(ReadNdefHelper.readNdef(ndefMessage))
//                stringBuilder.append("\n")
//            }
//        }
        Log.i(TAG, "resolveIntent: $stringBuilder")
    }

    private fun getNdefType(ndefType: String): String {
        return when (ndefType) {
            Ndef.NFC_FORUM_TYPE_1 -> {
                "NFC FORUM TYPE 1"
            }
            Ndef.NFC_FORUM_TYPE_2 -> {
                "NFC FORUM TYPE 2"
            }
            Ndef.NFC_FORUM_TYPE_3 -> {
                "NFC FORUM TYPE 3"
            }
            Ndef.NFC_FORUM_TYPE_4 -> {
                "NFC FORUM TYPE 4"
            }
            Ndef.MIFARE_CLASSIC -> {
                "MIFARE CLASSIC"
            }
            "com.nxp.ndef.icodesli" -> {
                "ICODE SLI"
            }
            else -> {
                "UNKNOWN"
            }
        }
    }

}