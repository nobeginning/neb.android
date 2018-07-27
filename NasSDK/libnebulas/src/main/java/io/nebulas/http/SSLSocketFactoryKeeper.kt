package io.nebulas.http

import android.util.Log
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSLSocketFactoryKeeper {
    private var trustAllSSlSocketFactory: SSLSocketFactory? = null

    fun getTrustAllSSLSocketFactory(): SSLSocketFactory? {
        if (trustAllSSlSocketFactory == null) {
            synchronized(SSLSocketFactoryKeeper::class.java) {
                if (trustAllSSlSocketFactory == null) {

                    // 信任所有证书
                    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<X509Certificate>?, authType: String?) {

                        }

                        override fun checkServerTrusted(chain: Array<X509Certificate>?, authType: String?) {

                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate>? {
                            return null
                        }

                    })
                    try {
                        val sslContext = SSLContext.getInstance("TLS")
                        sslContext.init(null, trustAllCerts, null)
                        trustAllSSlSocketFactory = sslContext.socketFactory
                    } catch (ex: Throwable) {
                        Log.e(ex.message, ex.toString())
                    }
                }
            }
        }
        return trustAllSSlSocketFactory
    }
}