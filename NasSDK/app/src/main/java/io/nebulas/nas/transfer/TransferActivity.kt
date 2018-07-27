package io.nebulas.nas.transfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import io.nebulas.Constants
import io.nebulas.api.SmartContracts
import io.nebulas.nas.R
import kotlinx.android.synthetic.main.activity_transfer.*

class TransferActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, TransferActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)
        title = "Transfer/转账"
        initAdapter()
        et_address.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                et_address.showDropDown()
            }
        }
        btn_transfer.setOnClickListener {
            val to = et_address.text.toString()
            cacheAddress(to)
            val amount = et_amount.text.toString()
            val gasPrice = et_gas_price.text.toString()
            val gasLimit = et_gas_limit.text.toString()
            val netType = when (rg_net_type.checkedRadioButtonId) {
                R.id.rb_net_type_main -> Constants.MAIN_NET
                else -> Constants.TEST_NET
            }
            SmartContracts.pay(
                    context = this,
                    netType = netType,
                    to = to,
                    value = amount,
                    gasPrice = gasPrice,
                    gasLimit = gasLimit
            )
        }
    }

    private fun initAdapter() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
        adapter.addAll(getCachedAddress())
        et_address.setAdapter(adapter)
    }

    private fun cacheAddress(address: String) {
        if (address.isBlank()){
            return
        }
        val pref = getSharedPreferences("Address", Context.MODE_PRIVATE)
        pref.edit().putLong(address, System.currentTimeMillis()).apply()
    }

    private fun getCachedAddress(): Set<String> {
        val pref = getSharedPreferences("Address", Context.MODE_PRIVATE)
        return pref.all.keys
    }
}
