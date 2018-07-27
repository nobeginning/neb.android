package io.nebulas.api

import android.content.Intent
import android.os.Bundle
import io.nebulas.Constants
import io.nebulas.logger.logD
import io.nebulas.response.AuthResponse
import io.nebulas.response.Response
import io.nebulas.response.TransferResponse

object ResultHelper {

    fun handleIntent(intent: Intent, handler: ResponseHandler) {
        val response = parseIntent(intent)
        logD(response.toString())
        handler.onResponse(response)
    }

    private fun parseIntent(intent: Intent): Response {
        val category = intent.getStringExtra("category")
        val errorCode = intent.getIntExtra("errorCode", 10000)
        val errorMessage = intent.getStringExtra("errorMessage")
        if (errorCode != 0) {
            if (errorCode == 10000) {
                return Response.unknownErrorResponse
            }
            return Response(errorCode, errorMessage)
        }
        val bizData: Bundle = intent.getBundleExtra("data") ?: return Response.unknownErrorResponse
        when (category) {
            Constants.CATEGORY_TRANSFER -> {
                val hash = bizData.getString("hash")
                return TransferResponse(errorCode, errorMessage, hash)
            }
            Constants.CATEGORY_AUTH -> {
                val address = bizData.getString("address")
                return AuthResponse(errorCode, errorMessage, address)
            }
        }
        return Response.unknownErrorResponse
    }
}