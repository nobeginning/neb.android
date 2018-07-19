package io.nebulas.model

import android.os.Bundle

class OpenAppModeWithPkg(private val originAppPackage:String, private val resultActivity:String): OpenAppMode() {
    override fun toBundle(): Bundle {
        val bundle = super.toBundle()
        bundle.putString("package", originAppPackage)
        bundle.putString("resultActivity", resultActivity)
        return bundle
    }
}