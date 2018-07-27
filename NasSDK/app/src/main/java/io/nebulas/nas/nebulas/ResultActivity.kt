package io.nebulas.nas.nebulas

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.nebulas.api.ResponseHandler
import io.nebulas.api.ResultHelper
import io.nebulas.nas.R
import io.nebulas.nas.auth.AuthResultActivity
import io.nebulas.nas.transfer.TransferResultActivity
import io.nebulas.response.AuthResponse
import io.nebulas.response.Response
import io.nebulas.response.TransferResponse
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity(), ResponseHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        ResultHelper.handleIntent(intent, this) //用于将Intent中的返回值转为Response对象
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null) {
            return
        }
        ResultHelper.handleIntent(intent, this) //用于将Intent中的返回值转为Response对象
    }

    override fun onResponse(response: Response) {
        if (response.errorCode != 0) {
            textView.text = "Error\n\n$response"
            return
        }
        when (response) {
            is TransferResponse -> {
                TransferResultActivity.launch(this, response)
                finish()
            }
            is AuthResponse -> {
                AuthResultActivity.launch(this, response)
                finish()
            }
            else -> {
                textView.text = "Unknown Result\n\n$response"
            }
        }
    }
}
