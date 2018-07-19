package io.nebulas.nas.query

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import io.nebulas.api.SmartContracts
import io.nebulas.configuration.Configuration
import io.nebulas.nas.R
import kotlinx.android.synthetic.main.activity_query_account.*

class QueryAccountActivity : AppCompatActivity(), SmartContracts.StatusCallback {

    companion object {
        fun launch(context: Context){
            context.startActivity(Intent(context, QueryAccountActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_account)
        initAdapter()
        et_address.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                et_address.showDropDown()
            }
        }
        btn_query_account.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            val address = et_address.text.toString()
            cacheAddress(address)
            SmartContracts.queryAccountState(Configuration.getCurrentReleaseType().netType, address, this)
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

    private fun initAdapter() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        adapter.addAll(getCachedAddress())
        et_address.setAdapter(adapter)
    }

    private fun cacheAddress(address:String){
        val pref = getSharedPreferences("Address", Context.MODE_PRIVATE)
        pref.edit().putLong(address, System.currentTimeMillis()).apply()
    }

    private fun getCachedAddress():Set<String>{
        val pref = getSharedPreferences("Address", Context.MODE_PRIVATE)
        return pref.all.keys
    }
}
