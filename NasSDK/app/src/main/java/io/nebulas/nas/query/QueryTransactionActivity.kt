package io.nebulas.nas.query

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.nebulas.api.SmartContracts
import io.nebulas.configuration.Configuration
import io.nebulas.nas.R
import kotlinx.android.synthetic.main.activity_query_transaction.*

class QueryTransactionActivity : AppCompatActivity(), SmartContracts.StatusCallback {

    companion object {
        fun launch(context:Context){
            context.startActivity(Intent(context, QueryTransactionActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_transaction)
        title = "Query Transaction/交易查询"
        rg_type.setOnCheckedChangeListener { _, checkedId ->
            et_content.hint = when (checkedId) {
                R.id.rb_type_serial -> "Input the serial number"
                R.id.rb_type_hash -> "Input the transaction hash"
                else -> "Input something -_-!"
            }
        }
        btn_query_transaction.setOnClickListener {
            val type = rg_type.checkedRadioButtonId
            doQuery(type)
        }
    }

    override fun onSuccess(response: String) {
        progress_bar.visibility = View.GONE
        tv_result.text = response
    }

    override fun onFail(error: String) {
        progress_bar.visibility = View.GONE
        tv_result.text = error
    }

    private fun doQuery(type: Int) {
        progress_bar.visibility = View.VISIBLE
        when (type) {
            R.id.rb_type_serial -> doQueryBySerialNo()
            R.id.rb_type_hash -> doQueryByHash()
            else -> progress_bar.visibility = View.GONE
        }
    }

    private fun doQueryByHash() {
        val hash = et_content.text.toString()
        SmartContracts.queryTransferStatusByHash(Configuration.getCurrentReleaseType().netType,
                hash, this)
    }

    private fun doQueryBySerialNo() {
        val serialNo = et_content.text.toString()
        SmartContracts.queryTransferStatus(Configuration.getCurrentReleaseType().netType,
                serialNo, this)
    }
}
