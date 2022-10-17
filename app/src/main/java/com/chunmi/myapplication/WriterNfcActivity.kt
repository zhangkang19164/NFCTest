package com.chunmi.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chunmi.nfc.NfcConstants
import com.chunmi.nfc.NfcHelper
import com.chunmi.nfc.ReadNfcCallback
import com.chunmi.nfc.WriterNfcCallback


/**
 * @author : Android-张康
 * created on: 2022/9/27 17:37
 * description:
 */
class WriterNfcActivity : AppCompatActivity() {

    private val mNfcHelper: NfcHelper by lazy {
        NfcHelper(this)
    }

    private val mMessageTextView: TextView by lazy {
        findViewById(R.id.nfc_info)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_writer)
        title = "NFC写入"
        val editText = findViewById<EditText>(R.id.write_nfc_info)
        findViewById<View>(R.id.write_nfc).setOnClickListener {
            mMessageTextView.text = "开始写入"
            mNfcHelper.writerText(editText.text.toString(), createWriterNfcCallback())
        }
        findViewById<View>(R.id.reader_nfc).setOnClickListener {
            mNfcHelper.readText(object : ReadNfcCallback {
                override fun readNfcCallback(errorCode: Int, result: String?) {
                    mMessageTextView.append("\n读取结果：")
                    when (errorCode) {
                        NfcConstants.ERROR_CODE_SUCCESSFUL -> {
                            mMessageTextView.append("读取成功 result = $result")
                        }
                        NfcConstants.ERROR_CODE_NOT_SUPPORT -> {
                            mMessageTextView.append("读取失败，设备不支持NFC")
                        }
                        NfcConstants.ERROR_CODE_NOT_ENABLED -> {
                            mMessageTextView.append("读取失败，NFC功能没有开启")
                        }
                        NfcConstants.ERROR_CODE_OVERTIME -> {
                            mMessageTextView.append("读取失败，超时")
                        }
                        NfcConstants.ERROR_CODE_FAILURE -> {
                            mMessageTextView.append("读取失败，读取失败")
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        mNfcHelper.onActivityResumed()
    }

    override fun onPause() {
        super.onPause()
        mNfcHelper.onActivityPaused()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            mNfcHelper.onActivityNewIntent(it)
        }
    }

    private fun createWriterNfcCallback() = object : WriterNfcCallback {

        override fun writerNfcCallback(errorCode: Int) {
            when (errorCode) {
                NfcConstants.ERROR_CODE_SUCCESSFUL -> {
                    mMessageTextView.append("\n写入结果：")
                    mMessageTextView.append("写入成功")
                }
                NfcConstants.ERROR_CODE_NOT_SUPPORT -> {
                    mMessageTextView.append("\n写入结果：")
                    mMessageTextView.append("写入失败，设备不支持NFC")
                }
                NfcConstants.ERROR_CODE_NOT_ENABLED -> {
                    mMessageTextView.append("\n写入结果：")
                    mMessageTextView.append("写入失败，NFC功能没有开启")
                }
                NfcConstants.ERROR_CODE_NOT_WRITABLE -> {
                    mMessageTextView.append("\n写入结果：")
                    mMessageTextView.append("写入失败，标签不支持写入")
                }
                NfcConstants.ERROR_CODE_OVERTIME -> {
                    mMessageTextView.append("\n写入结果：")
                    mMessageTextView.append("写入失败，超时")
                }
                NfcConstants.ERROR_CODE_FAILURE -> {
                    mMessageTextView.append("\n写入结果：")
                    mMessageTextView.append("写入失败，写入失败")
                }
            }
        }
    }

}