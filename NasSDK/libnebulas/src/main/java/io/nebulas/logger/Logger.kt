package io.nebulas.logger

import android.util.Log
import io.nebulas.configuration.Configuration

fun <T:Any> T.logD(msg:String?){
    if (Configuration.logEnabled) {
        Log.d(getTag(this), msg?:"NULL")
    }
}
fun <T:Any> T.logE(msg:String?){
    if (Configuration.logEnabled) {
        Log.e(getTag(this), msg?:"NULL")
    }
}
fun <T:Any> T.logI(msg:String?){
    if (Configuration.logEnabled) {
        Log.i(getTag(this), msg?:"NULL")
    }
}
fun <T:Any> T.logV(msg:String?){
    if (Configuration.logEnabled) {
        Log.v(getTag(this), msg?:"NULL")
    }
}

private fun <T:Any> getTag(obj:T):String{
    var tag = obj::class.java.simpleName
    if (tag.isNullOrEmpty()) {
        tag = obj.toString()
    }
    return tag
}