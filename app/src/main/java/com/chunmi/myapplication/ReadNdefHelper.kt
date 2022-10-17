package com.chunmi.myapplication

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import java.nio.charset.Charset

/**
 * @author : Android-张康
 * created on: 2022/9/28 15:43
 * description:
 */
object ReadNdefHelper {

    fun readNdef(ndefMessage: NdefMessage): String {
        return buildString {
            ndefMessage.records.forEach { ndefRecord ->
                append(readNdefRecord(ndefRecord))
            }
        }
    }

    fun readNdefRecord(ndefRecord: NdefRecord): String {
        return buildString {


            val type = ndefRecord.type
            append("\n")
            append("type：${String(type, Charset.forName("UTF-8"))}")
            append("\n")
            append("payload：")
            append(String(ndefRecord.payload, Charset.forName("UTF-8")))
        }
    }

}