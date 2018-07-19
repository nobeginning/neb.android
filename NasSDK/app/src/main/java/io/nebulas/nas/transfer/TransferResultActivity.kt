package io.nebulas.nas.transfer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.nebulas.nas.R
import io.nebulas.response.TransferResponse
import kotlinx.android.synthetic.main.activity_transfer_result.*

class TransferResultActivity : AppCompatActivity() {

    companion object {
        private const val key_response = "key_response"
        fun launch(context: Context, response:TransferResponse) {
            context.startActivity(Intent(context, TransferResultActivity::class.java).apply {
                putExtra(key_response, response)
            })
        }
    }

    private lateinit var response:TransferResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_result)
        title = "TransferResult/转账结果"
        handleIntent()
        initViews()
    }

    private fun handleIntent(){
        response = intent.getParcelableExtra(key_response)
    }

    private fun initViews() {
        tv_result.text = response.toString()

        btn_copy_hash.setOnClickListener {
            val clipboardManager:ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            clipboardManager.text = response.hash
            clipboardManager.primaryClip = ClipData.newPlainText("hash", response.hash)
            Toast.makeText(this, "Copy Succeed", Toast.LENGTH_SHORT).show()
        }
    }
}
