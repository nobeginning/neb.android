package io.nebulas.model

import android.os.Bundle

class GasInfo {
    var gasPrice: String? = null
    var gasLimit: String? = null

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("gasPrice", gasPrice)
        bundle.putString("gasLimit", gasLimit)
        return bundle
    }
}
