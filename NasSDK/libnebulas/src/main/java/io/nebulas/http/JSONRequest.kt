package io.nebulas.http

import android.os.Handler
import android.os.Looper
import io.nebulas.logger.logD
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

class JSONRequest(private val url: String) {

    private val mainHandler = Handler(Looper.getMainLooper())

    interface RequestCallback {
        fun onSucceed(response: String)
        fun onFailed(errorMessage: String)
    }

    fun get(callback: RequestCallback?) {
        ThreadPoolHolder.executor.submit {
            doGet(callback)
        }
    }

    fun post(body: String?, callback: RequestCallback?) {
        ThreadPoolHolder.executor.submit {
            doPost(body, callback)
        }
    }

    private fun doGet(callback: RequestCallback?) {
        val realUrl = URL(url)
        var conn: HttpURLConnection? = null
        try {
            conn = realUrl.openConnection() as HttpURLConnection
            if (conn is HttpsURLConnection) {
                conn.sslSocketFactory = SSLSocketFactoryKeeper.getTrustAllSSLSocketFactory()
            }
            doCall(conn, callback)
        } catch (e: Exception) {
            callback?.apply {
                mainHandler.post {
                    onFailed(e.message ?: e.toString())
                }
            }
        } finally {
            conn?.disconnect()
        }
    }

    private fun doPost(body: String?, callback: RequestCallback?) {
        val realUrl = URL(url)
        var conn: HttpURLConnection? = null
        var writer: OutputStreamWriter? = null
        try {
            conn = realUrl.openConnection() as HttpURLConnection
            if (conn is HttpsURLConnection) {
                conn.sslSocketFactory = SSLSocketFactoryKeeper.getTrustAllSSLSocketFactory()
            }
            conn.requestMethod = "POST"
            conn.setRequestProperty("connection", "keep-alive")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.doInput = true
            conn.useCaches = false
            writer = OutputStreamWriter(conn.outputStream, "UTF-8")
            writer.write(body)
            writer.flush()
            doCall(conn, callback)
        } catch (e: Exception) {
            callback?.apply {
                mainHandler.post {
                    onFailed(e.message ?: e.toString())
                }
            }
        } finally {
            writer?.close()
            conn?.disconnect()
        }
    }

    private fun doCall(conn: HttpURLConnection, callback: RequestCallback?) {
        val code = conn.responseCode
        val message = conn.responseMessage
        var inputStream: InputStream? = null
        try {
            if (code >= 400) {
                callback?.apply {
                    mainHandler.post {
                        onFailed(message)
                    }
                }
                return
            }
            inputStream = BufferedInputStream(conn.inputStream)
            val result = readInStream(inputStream)
            logD(result)
            callback?.apply {
                mainHandler.post {
                    onSucceed(result)
                }
            }
        } catch (e: Exception) {
            callback?.apply {
                mainHandler.post {
                    onFailed(e.message ?: e.toString())
                }
            }
        } finally {
            conn.disconnect()
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun readInStream(inputStream: InputStream): String {
        val scanner = Scanner(inputStream).useDelimiter("\\A")
        return if (scanner.hasNext()) scanner.next() else ""
    }

}