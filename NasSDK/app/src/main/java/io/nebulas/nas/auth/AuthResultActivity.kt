package io.nebulas.nas.auth

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.nebulas.nas.R
import io.nebulas.response.AuthResponse
import kotlinx.android.synthetic.main.activity_auth_result.*

class AuthResultActivity : AppCompatActivity() {

    companion object {
        private const val key_response = "key_response"
        fun launch(context: Context, response:AuthResponse) {
            context.startActivity(Intent(context, AuthResultActivity::class.java).apply {
                putExtra(key_response, response)
            })
        }
    }

    private lateinit var response: AuthResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_result)
        title = "AuthResult/授权结果"
        handleIntent()
        initViews()
    }

    private fun initViews() {
        tv_result.text = response.toString()
        btn_copy_address.setOnClickListener {
            val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.primaryClip = ClipData.newPlainText("hash", response.address)
            Toast.makeText(this, "Copy Succeed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleIntent(){
        response = intent.getParcelableExtra(key_response)
    }
}
