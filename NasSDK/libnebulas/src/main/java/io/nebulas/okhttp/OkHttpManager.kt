package io.nebulas.okhttp

import okhttp3.OkHttpClient

/**
 * Created by donald99 on 18/5/24.
 */

object OkHttpManager {

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient()
    }

    fun getInstance():OkHttpManager{
        return this
    }
}
