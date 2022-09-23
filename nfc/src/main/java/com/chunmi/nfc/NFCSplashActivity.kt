package com.chunmi.nfc

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * @author : Android-张康
 * created on: 2022/9/23 17:08
 * description:
 */
class NFCSplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NFCSplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        var pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_MUTABLE)

        readNFC(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        readNFC(intent)
    }

    private fun readNFC(intent: Intent?) {
        intent ?: return
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        val ndefMessage: NdefMessage? = intent.getParcelableExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        val id: ByteArray? = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        findViewById<TextView>(R.id.nfc_info).text =
            "readNFC: tag = $tag ndefMessage = $ndefMessage id = $id"
    }
}